package com.sih26132.controller;

import com.sih26132.dto.advisory.AdvisoryGenerationRequest;
import com.sih26132.dto.advisory.AdvisoryGenerationResponse;
import com.sih26132.entity.Advisory;
import com.sih26132.service.AdvisoryGenerationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advisories")
@RequiredArgsConstructor
public class AdvisoryGenerationController {

    private final AdvisoryGenerationService advisoryGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<AdvisoryGenerationResponse> generateAdvisory(
            @Valid @RequestBody AdvisoryGenerationRequest request) {

        Advisory advisory =
                advisoryGenerationService.generateAndSaveAdvisory(
                        request.getCaseId(),
                        request.getFarmerQuestion(),
                        request.getLanguage(),
                        request.getRetrievalLimit()
                );

        AdvisoryGenerationResponse response =
                mapToResponse(advisory);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    private AdvisoryGenerationResponse mapToResponse(
            Advisory advisory) {

        AdvisoryGenerationResponse response =
                new AdvisoryGenerationResponse();

        response.setAdvisoryId(advisory.getId());

        response.setCaseId(
                advisory.getCaseEntity().getId()
        );

        response.setLanguage(
                advisory.getLanguage()
        );

        response.setTitle(
                advisory.getTitle()
        );

        response.setContent(
                advisory.getContent()
        );

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

        response.setGeneratedBy(
                advisory.getGeneratedBy()
        );

        response.setStatus(
                advisory.getStatus()
        );

        response.setCreatedAt(
                advisory.getCreatedAt()
        );

        return response;
    }
}