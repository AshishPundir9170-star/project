
package com.sih26132.controller;

import com.sih26132.dto.crop.CropCycleCreateRequest;
import com.sih26132.dto.crop.CropCycleResponse;
import com.sih26132.service.CropCycleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/farms/{farmId}/crop-cycles")
@SecurityRequirement(name = "bearerAuth")
public class CropCycleController {

    private final CropCycleService cropCycleService;

    public CropCycleController(CropCycleService cropCycleService) {
        this.cropCycleService = cropCycleService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<CropCycleResponse> createCropCycle(
            @PathVariable UUID farmId,
            @Valid @RequestBody CropCycleCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                cropCycleService.createCropCycle(
                        farmId,
                        request,
                        authentication.getName()
                )
        );
    }

    // GET ALL CROP CYCLES FOR MY FARM
    @GetMapping
    public ResponseEntity<List<CropCycleResponse>> getMyCropCycles(
            @PathVariable UUID farmId,
            Authentication authentication) {

        return ResponseEntity.ok(
                cropCycleService.getMyCropCycles(
                        farmId,
                        authentication.getName()
                )
        );
    }

    // GET ONE CROP CYCLE
    @GetMapping("/{cropCycleId}")
    public ResponseEntity<CropCycleResponse> getMyCropCycle(
            @PathVariable UUID farmId,
            @PathVariable UUID cropCycleId,
            Authentication authentication) {

        return ResponseEntity.ok(
                cropCycleService.getMyCropCycle(
                        farmId,
                        cropCycleId,
                        authentication.getName()
                )
        );
    }

    // GET ACTIVE CROP CYCLES
    @GetMapping("/active")
    public ResponseEntity<List<CropCycleResponse>> getActiveCropCycles(
            @PathVariable UUID farmId,
            Authentication authentication) {

        return ResponseEntity.ok(
                cropCycleService.getActiveCropCycles(
                        farmId,
                        authentication.getName()
                )
        );
    }

    // UPDATE
    @PutMapping("/{cropCycleId}")
    public ResponseEntity<CropCycleResponse> updateCropCycle(
            @PathVariable UUID farmId,
            @PathVariable UUID cropCycleId,
            @Valid @RequestBody CropCycleCreateRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                cropCycleService.updateCropCycle(
                        farmId,
                        cropCycleId,
                        request,
                        authentication.getName()
                )
        );
    }

    // DELETE
    @DeleteMapping("/{cropCycleId}")
    public ResponseEntity<Void> deleteCropCycle(
            @PathVariable UUID farmId,
            @PathVariable UUID cropCycleId,
            Authentication authentication) {

        cropCycleService.deleteCropCycle(
                farmId,
                cropCycleId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
