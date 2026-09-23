package com.sih26132.service;

import com.sih26132.dto.risk.RiskPredictionCreateRequest;
import com.sih26132.dto.risk.RiskPredictionResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.ModelVersion;
import com.sih26132.entity.RiskPrediction;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.ModelVersionRepository;
import com.sih26132.repository.RiskPredictionRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RiskPredictionService {

    private final RiskPredictionRepository riskPredictionRepository;
    private final CaseRepository caseRepository;
    private final ModelVersionRepository modelVersionRepository;
    private final UserRepository userRepository;

    public RiskPredictionService(
            RiskPredictionRepository riskPredictionRepository,
            CaseRepository caseRepository,
            ModelVersionRepository modelVersionRepository,
            UserRepository userRepository) {

        this.riskPredictionRepository = riskPredictionRepository;
        this.caseRepository = caseRepository;
        this.modelVersionRepository = modelVersionRepository;
        this.userRepository = userRepository;
    }

    public RiskPredictionResponse createRiskPrediction(
            RiskPredictionCreateRequest request,
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

        RiskPrediction prediction = RiskPrediction.builder()
                .caseEntity(caseEntity)
                .modelVersion(modelVersion)
                .riskScore(request.getRiskScore())
                .severity(request.getSeverity())
                .forecastStart(request.getForecastStart())
                .forecastEnd(request.getForecastEnd())
                .contributingFactors(request.getContributingFactors())
                .createdAt(OffsetDateTime.now())
                .build();

        RiskPrediction savedPrediction =
                riskPredictionRepository.save(prediction);

        return mapToResponse(savedPrediction);
    }

    @Transactional(readOnly = true)
    public List<RiskPredictionResponse> getCaseRiskPredictions(
            UUID caseId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        return riskPredictionRepository
                .findByCaseEntity(caseEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RiskPredictionResponse getRiskPrediction(
            UUID caseId,
            UUID predictionId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        RiskPrediction prediction =
                riskPredictionRepository
                        .findByIdAndCaseEntity(
                                predictionId,
                                caseEntity
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risk prediction not found or access denied"
                                ));

        return mapToResponse(prediction);
    }

    public void deleteRiskPrediction(
            UUID caseId,
            UUID predictionId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        RiskPrediction prediction =
                riskPredictionRepository
                        .findByIdAndCaseEntity(
                                predictionId,
                                caseEntity
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risk prediction not found or access denied"
                                ));

        riskPredictionRepository.delete(prediction);
    }

    private User findUser(String identifier) {

        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + identifier
                        ));
    }

    private RiskPredictionResponse mapToResponse(
            RiskPrediction prediction) {

        RiskPredictionResponse response =
                new RiskPredictionResponse();

        response.setId(prediction.getId());

        response.setCaseId(
                prediction.getCaseEntity().getId()
        );

        if (prediction.getModelVersion() != null) {
            response.setModelVersionId(
                    prediction.getModelVersion().getId()
            );
        }

        response.setRiskScore(
                prediction.getRiskScore()
        );

        response.setSeverity(
                prediction.getSeverity()
        );

        response.setForecastStart(
                prediction.getForecastStart()
        );

        response.setForecastEnd(
                prediction.getForecastEnd()
        );

        response.setContributingFactors(
                prediction.getContributingFactors()
        );

        response.setCreatedAt(
                prediction.getCreatedAt()
        );

        return response;
    }
}