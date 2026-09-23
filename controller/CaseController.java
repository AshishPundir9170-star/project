package com.sih26132.controller;

import com.sih26132.dto.cases.CaseCreateRequest;
import com.sih26132.dto.cases.CaseResponse;
import com.sih26132.service.CaseService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @PostMapping
    public ResponseEntity<CaseResponse> createCase(
            @Valid @RequestBody CaseCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                caseService.createCase(
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<CaseResponse>> getMyCases(
            Authentication authentication) {

        return ResponseEntity.ok(
                caseService.getMyCases(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{caseId}")
    public ResponseEntity<CaseResponse> getMyCase(
            @PathVariable UUID caseId,
            Authentication authentication) {

        return ResponseEntity.ok(
                caseService.getMyCase(
                        caseId,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CaseResponse>> getCasesByStatus(
            @PathVariable String status,
            Authentication authentication) {

        return ResponseEntity.ok(
                caseService.getCasesByStatus(
                        status,
                        authentication.getName()
                )
        );
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<CaseResponse> updateCase(
            @PathVariable UUID caseId,
            @Valid @RequestBody CaseCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                caseService.updateCase(
                        caseId,
                        request,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<Void> deleteCase(
            @PathVariable UUID caseId,
            Authentication authentication) {

        caseService.deleteCase(
                caseId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}