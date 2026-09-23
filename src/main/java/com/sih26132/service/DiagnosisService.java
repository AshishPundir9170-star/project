package com.sih26132.service;

import com.sih26132.dto.diagnosis.DiagnosisCreateRequest;
import com.sih26132.dto.diagnosis.DiagnosisResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.Diagnosis;
import com.sih26132.entity.ModelVersion;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.DiagnosisRepository;
import com.sih26132.repository.ModelVersionRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final CaseRepository caseRepository;
    private final ModelVersionRepository modelVersionRepository;
    private final UserRepository userRepository;

    public DiagnosisService(
            DiagnosisRepository diagnosisRepository,
            CaseRepository caseRepository,
            ModelVersionRepository modelVersionRepository,
            UserRepository userRepository) {

        this.diagnosisRepository = diagnosisRepository;
        this.caseRepository = caseRepository;
        this.modelVersionRepository = modelVersionRepository;
        this.userRepository = userRepository;
    }

    public DiagnosisResponse createDiagnosis(
            DiagnosisCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(request.getCaseId(), user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        ModelVersion modelVersion = null;

        if (request.getModelVersionId() != null) {

            modelVersion = modelVersionRepository
                    .findById(request.getModelVersionId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Model version not found"
                            ));
        }

        Diagnosis diagnosis = Diagnosis.builder()
                .caseEntity(caseEntity)
                .modelVersion(modelVersion)
                .conditionName(request.getConditionName())
                .conditionType(request.getConditionType())
                .confidence(request.getConfidence())
                .alternatives(request.getAlternatives())
                .explainability(request.getExplainability())
                .source(request.getSource())
                .isFinal(request.isFinal())
                .createdAt(OffsetDateTime.now())
                .build();

        Diagnosis savedDiagnosis =
                diagnosisRepository.save(diagnosis);

        return mapToResponse(savedDiagnosis);
    }

    @Transactional(readOnly = true)
    public List<DiagnosisResponse> getCaseDiagnoses(
            UUID caseId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        return diagnosisRepository
                .findByCaseEntity(caseEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DiagnosisResponse getDiagnosis(
            UUID caseId,
            UUID diagnosisId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        Diagnosis diagnosis = diagnosisRepository
                .findByIdAndCaseEntity(diagnosisId, caseEntity)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Diagnosis not found or access denied"
                        ));

        return mapToResponse(diagnosis);
    }

    public void deleteDiagnosis(
            UUID caseId,
            UUID diagnosisId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        Diagnosis diagnosis = diagnosisRepository
                .findByIdAndCaseEntity(diagnosisId, caseEntity)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Diagnosis not found or access denied"
                        ));

        diagnosisRepository.delete(diagnosis);
    }

    private User findUser(String identifier) {

        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + identifier
                        ));
    }

    private DiagnosisResponse mapToResponse(
            Diagnosis diagnosis) {

        DiagnosisResponse response =
                new DiagnosisResponse();

        response.setId(diagnosis.getId());

        response.setCaseId(
                diagnosis.getCaseEntity().getId()
        );

        if (diagnosis.getModelVersion() != null) {

            response.setModelVersionId(
                    diagnosis.getModelVersion().getId()
            );
        }

        response.setConditionName(
                diagnosis.getConditionName()
        );

        response.setConditionType(
                diagnosis.getConditionType()
        );

        response.setConfidence(
                diagnosis.getConfidence()
        );

        response.setAlternatives(
                diagnosis.getAlternatives()
        );

        response.setExplainability(
                diagnosis.getExplainability()
        );

        response.setSource(
                diagnosis.getSource()
        );

        response.setIsFinal(
                diagnosis.isFinal()
        );

        response.setCreatedAt(
                diagnosis.getCreatedAt()
        );

        return response;
    }
}