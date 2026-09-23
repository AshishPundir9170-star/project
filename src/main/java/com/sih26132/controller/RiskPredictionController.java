package com.sih26132.controller;

import com.sih26132.dto.risk.RiskPredictionCreateRequest;
import com.sih26132.dto.risk.RiskPredictionResponse;
import com.sih26132.service.RiskPredictionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases/{caseId}/risk-predictions")
@RequiredArgsConstructor
public class RiskPredictionController {

    private final RiskPredictionService riskPredictionService;

    @PostMapping
    public ResponseEntity<RiskPredictionResponse> createRiskPrediction(
            @PathVariable UUID caseId,
            @Valid @RequestBody RiskPredictionCreateRequest request,
            Authentication authentication) {

        request.setCaseId(caseId);

        RiskPredictionResponse response =
                riskPredictionService.createRiskPrediction(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RiskPredictionResponse>> getCaseRiskPredictions(
            @PathVariable UUID caseId,
            Authentication authentication) {

        List<RiskPredictionResponse> predictions =
                riskPredictionService.getCaseRiskPredictions(
                        caseId,
                        authentication.getName()
                );

        return ResponseEntity.ok(predictions);
    }

    @GetMapping("/{predictionId}")
    public ResponseEntity<RiskPredictionResponse> getRiskPrediction(
            @PathVariable UUID caseId,
            @PathVariable UUID predictionId,
            Authentication authentication) {

        RiskPredictionResponse response =
                riskPredictionService.getRiskPrediction(
                        caseId,
                        predictionId,
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{predictionId}")
    public ResponseEntity<Void> deleteRiskPrediction(
            @PathVariable UUID caseId,
            @PathVariable UUID predictionId,
            Authentication authentication) {

        riskPredictionService.deleteRiskPrediction(
                caseId,
                predictionId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}