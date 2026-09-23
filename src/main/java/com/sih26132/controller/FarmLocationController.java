package com.sih26132.controller;

import com.sih26132.dto.farm.FarmLocationCreateRequest;
import com.sih26132.dto.farm.FarmLocationResponse;
import com.sih26132.service.FarmLocationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/farms/{farmId}/location")
@SecurityRequirement(name = "bearerAuth")

public class FarmLocationController {

    private final FarmLocationService farmLocationService;

    public FarmLocationController(
            FarmLocationService farmLocationService) {

        this.farmLocationService = farmLocationService;
    }

    @PostMapping
    public ResponseEntity<FarmLocationResponse> createLocation(
            @PathVariable UUID farmId,
            @Valid @RequestBody FarmLocationCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                farmLocationService.createLocation(
                        farmId,
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<FarmLocationResponse> getLocation(
            @PathVariable UUID farmId,
            Authentication authentication) {

        return ResponseEntity.ok(
                farmLocationService.getLocation(
                        farmId,
                        authentication.getName()
                )
        );
    }

    @PutMapping
    public ResponseEntity<FarmLocationResponse> updateLocation(
            @PathVariable UUID farmId,
            @Valid @RequestBody FarmLocationCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                farmLocationService.updateLocation(
                        farmId,
                        request,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLocation(
            @PathVariable UUID farmId,
            Authentication authentication) {

        farmLocationService.deleteLocation(
                farmId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}