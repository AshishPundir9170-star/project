package com.sih26132.controller;

import com.sih26132.dto.diagnosis.DiagnosisCreateRequest;
import com.sih26132.dto.diagnosis.DiagnosisResponse;
import com.sih26132.service.DiagnosisService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases/{caseId}/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @PostMapping
    public ResponseEntity<DiagnosisResponse> createDiagnosis(
            @PathVariable UUID caseId,
            @Valid @RequestBody DiagnosisCreateRequest request,
            Authentication authentication) {

        /*
         * The caseId comes from the URL.
         * We explicitly use it in the request object so that
         * the service always works with the URL case.
         */
        request.setCaseId(caseId);

        DiagnosisResponse response =
                diagnosisService.createDiagnosis(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<DiagnosisResponse>> getCaseDiagnoses(
            @PathVariable UUID caseId,
            Authentication authentication) {

        List<DiagnosisResponse> diagnoses =
                diagnosisService.getCaseDiagnoses(
                        caseId,
                        authentication.getName()
                );

        return ResponseEntity.ok(diagnoses);
    }

    @GetMapping("/{diagnosisId}")
    public ResponseEntity<DiagnosisResponse> getDiagnosis(
            @PathVariable UUID caseId,
            @PathVariable UUID diagnosisId,
            Authentication authentication) {

        DiagnosisResponse response =
                diagnosisService.getDiagnosis(
                        caseId,
                        diagnosisId,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{diagnosisId}")
    public ResponseEntity<Void> deleteDiagnosis(
            @PathVariable UUID caseId,
            @PathVariable UUID diagnosisId,
            Authentication authentication) {

        diagnosisService.deleteDiagnosis(
                caseId,
                diagnosisId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}