package com.sih26132.controller;

import com.sih26132.dto.farm.FarmCreateRequest;
import com.sih26132.dto.farm.FarmResponse;
import com.sih26132.service.FarmService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/farms")
@SecurityRequirement(name = "bearerAuth")

public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping
    public ResponseEntity<FarmResponse> createFarm(
            @Valid @RequestBody FarmCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                farmService.createFarm(
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<FarmResponse>> getMyFarms(
            Authentication authentication) {

        return ResponseEntity.ok(
                farmService.getMyFarms(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{farmId}")
    public ResponseEntity<FarmResponse> getMyFarm(
            @PathVariable UUID farmId,
            Authentication authentication) {

        return ResponseEntity.ok(
                farmService.getMyFarm(
                        farmId,
                        authentication.getName()
                )
        );
    }

    @PutMapping("/{farmId}")
    public ResponseEntity<FarmResponse> updateFarm(
            @PathVariable UUID farmId,
            @Valid @RequestBody FarmCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                farmService.updateFarm(
                        farmId,
                        request,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{farmId}")
    public ResponseEntity<Void> deleteFarm(
            @PathVariable UUID farmId,
            Authentication authentication) {

        farmService.deleteFarm(
                farmId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}