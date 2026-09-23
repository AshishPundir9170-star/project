package com.sih26132.service;

import com.sih26132.dto.advisory.AdvisoryCreateRequest;
import com.sih26132.dto.advisory.AdvisoryResponse;
import com.sih26132.entity.Advisory;
import com.sih26132.entity.Case;
import com.sih26132.entity.User;
import com.sih26132.repository.AdvisoryRepository;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    public AdvisoryService(
            AdvisoryRepository advisoryRepository,
            CaseRepository caseRepository,
            UserRepository userRepository) {

        this.advisoryRepository = advisoryRepository;
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
    }

    public AdvisoryResponse createAdvisory(
            AdvisoryCreateRequest request,
            String username) {

        Case caseEntity = getOwnedCase(
                request.getCaseId(),
                username
        );

        OffsetDateTime now = OffsetDateTime.now();

        Advisory advisory = Advisory.builder()
                .caseEntity(caseEntity)
                .language(request.getLanguage())
                .title(request.getTitle())
                .content(request.getContent())
                .immediateActions(request.getImmediateActions())
                .preventiveActions(request.getPreventiveActions())
                .ipdmActions(request.getIpdmActions())
                .safeUseInstructions(request.getSafeUseInstructions())
                .whenToContactExpert(request.getWhenToContactExpert())
                .whenToRecheck(request.getWhenToRecheck())
                .knowledgeSources(request.getKnowledgeSources())
                .generatedBy(request.getGeneratedBy())
                .status(request.getStatus())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Advisory saved = advisoryRepository.save(advisory);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AdvisoryResponse> getCaseAdvisories(
            UUID caseId,
            String username) {

        Case caseEntity = getOwnedCase(caseId, username);

        return advisoryRepository
                .findByCaseEntity(caseEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdvisoryResponse getAdvisory(
            UUID caseId,
            UUID advisoryId,
            String username) {

        Case caseEntity = getOwnedCase(caseId, username);

        Advisory advisory = advisoryRepository
                .findByIdAndCaseEntity(advisoryId, caseEntity)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Advisory not found"
                        ));

        return mapToResponse(advisory);
    }

    @Transactional(readOnly = true)
    public List<AdvisoryResponse> getAdvisoriesByLanguage(
            UUID caseId,
            String language,
            String username) {

        Case caseEntity = getOwnedCase(caseId, username);

        return advisoryRepository
                .findByCaseEntityAndLanguage(
                        caseEntity,
                        language
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdvisoryResponse> getAdvisoriesByStatus(
            UUID caseId,
            String status,
            String username) {

        Case caseEntity = getOwnedCase(caseId, username);

        return advisoryRepository
                .findByCaseEntityAndStatus(
                        caseEntity,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AdvisoryResponse updateAdvisory(
            UUID caseId,
            UUID advisoryId,
            AdvisoryCreateRequest request,
            String username) {

        Case caseEntity = getOwnedCase(caseId, username);

        Advisory advisory = advisoryRepository
                .findByIdAndCaseEntity(advisoryId, caseEntity)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Advisory not found"
                        ));

        advisory.setLanguage(request.getLanguage());
        advisory.setTitle(request.getTitle());
        advisory.setContent(request.getContent());
        advisory.setImmediateActions(
                request.getImmediateActions()
        );
        advisory.setPreventiveActions(
                request.getPreventiveActions()
        );
        advisory.setIpdmActions(
                request.getIpdmActions()
        );
        advisory.setSafeUseInstructions(
                request.getSafeUseInstructions()
        );
        advisory.setWhenToContactExpert(
                request.getWhenToContactExpert()
        );
        advisory.setWhenToRecheck(
                request.getWhenToRecheck()
        );
        advisory.setKnowledgeSources(
                request.getKnowledgeSources()
        );
        advisory.setGeneratedBy(
                request.getGeneratedBy()
        );
        advisory.setStatus(request.getStatus());
        advisory.setUpdatedAt(OffsetDateTime.now());

        Advisory updated = advisoryRepository.save(advisory);

        return mapToResponse(updated);
    }

    public void deleteAdvisory(
            UUID caseId,
            UUID advisoryId,
            String username) {

        Case caseEntity = getOwnedCase(caseId, username);

        Advisory advisory = advisoryRepository
                .findByIdAndCaseEntity(advisoryId, caseEntity)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Advisory not found"
                        ));

        advisoryRepository.delete(advisory);
    }

    private Case getOwnedCase(
            UUID caseId,
            String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));

        return caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));
    }

    private AdvisoryResponse mapToResponse(
            Advisory advisory) {

        AdvisoryResponse response =
                new AdvisoryResponse();

        response.setId(advisory.getId());

        if (advisory.getCaseEntity() != null) {
            response.setCaseId(
                    advisory.getCaseEntity().getId()
            );
        }

        response.setLanguage(advisory.getLanguage());
        response.setTitle(advisory.getTitle());
        response.setContent(advisory.getContent());
        response.setImmediateActions(
                advisory.getImmediateActions()
        );
        response.setPreventiveActions(
                advisory.getPreventiveActions()
        );
        response.setIpdmActions(
                advisory.getIpdmActions()
        );
        response.setSafeUseInstructions(
                advisory.getSafeUseInstructions()
        );
        response.setWhenToContactExpert(
                advisory.getWhenToContactExpert()
        );
        response.setWhenToRecheck(
                advisory.getWhenToRecheck()
        );
        response.setKnowledgeSources(
                advisory.getKnowledgeSources()
        );
        response.setGeneratedBy(advisory.getGeneratedBy());
        response.setStatus(advisory.getStatus());
        response.setCreatedAt(advisory.getCreatedAt());
        response.setUpdatedAt(advisory.getUpdatedAt());

        return response;
    }
}