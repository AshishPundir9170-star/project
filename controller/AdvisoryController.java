package com.sih26132.controller;

import com.sih26132.dto.advisory.AdvisoryCreateRequest;
import com.sih26132.dto.advisory.AdvisoryResponse;
import com.sih26132.service.AdvisoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases/{caseId}/advisories")
@RequiredArgsConstructor
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    // CREATE
    @PostMapping
    public ResponseEntity<AdvisoryResponse> createAdvisory(
            @PathVariable UUID caseId,
            @Valid @RequestBody AdvisoryCreateRequest request,
            Authentication authentication) {

        request.setCaseId(caseId);

        AdvisoryResponse response =
                advisoryService.createAdvisory(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL ADVISORIES FOR A CASE
    @GetMapping
    public ResponseEntity<List<AdvisoryResponse>> getCaseAdvisories(
            @PathVariable UUID caseId,
            Authentication authentication) {

        return ResponseEntity.ok(
                advisoryService.getCaseAdvisories(
                        caseId,
                        authentication.getName()
                )
        );
    }

    // GET BY LANGUAGE
    @GetMapping("/language/{language}")
    public ResponseEntity<List<AdvisoryResponse>> getAdvisoriesByLanguage(
            @PathVariable UUID caseId,
            @PathVariable String language,
            Authentication authentication) {

        return ResponseEntity.ok(
                advisoryService.getAdvisoriesByLanguage(
                        caseId,
                        language,
                        authentication.getName()
                )
        );
    }

    // GET BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AdvisoryResponse>> getAdvisoriesByStatus(
            @PathVariable UUID caseId,
            @PathVariable String status,
            Authentication authentication) {

        return ResponseEntity.ok(
                advisoryService.getAdvisoriesByStatus(
                        caseId,
                        status,
                        authentication.getName()
                )
        );
    }

    // GET ONE ADVISORY
    @GetMapping("/{advisoryId}")
    public ResponseEntity<AdvisoryResponse> getAdvisory(
            @PathVariable UUID caseId,
            @PathVariable UUID advisoryId,
            Authentication authentication) {

        return ResponseEntity.ok(
                advisoryService.getAdvisory(
                        caseId,
                        advisoryId,
                        authentication.getName()
                )
        );
    }

    // UPDATE
    @PutMapping("/{advisoryId}")
    public ResponseEntity<AdvisoryResponse> updateAdvisory(
            @PathVariable UUID caseId,
            @PathVariable UUID advisoryId,
            @Valid @RequestBody AdvisoryCreateRequest request,
            Authentication authentication) {

        request.setCaseId(caseId);

        AdvisoryResponse response =
                advisoryService.updateAdvisory(
                        caseId,
                        advisoryId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{advisoryId}")
    public ResponseEntity<Void> deleteAdvisory(
            @PathVariable UUID caseId,
            @PathVariable UUID advisoryId,
            Authentication authentication) {

        advisoryService.deleteAdvisory(
                caseId,
                advisoryId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}