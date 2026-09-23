package com.sih26132.controller;

import com.sih26132.dto.crop.CropCreateRequest;
import com.sih26132.dto.crop.CropResponse;
import com.sih26132.service.CropService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/crops")
@SecurityRequirement(name = "bearerAuth")

public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<CropResponse> createCrop(
            @Valid @RequestBody CropCreateRequest request) {

        return ResponseEntity.ok(
                cropService.createCrop(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<CropResponse>> getAllCrops() {

        return ResponseEntity.ok(
                cropService.getAllCrops()
        );
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<CropResponse> getCrop(
            @PathVariable UUID cropId) {

        return ResponseEntity.ok(
                cropService.getCrop(cropId)
        );
    }

    @PutMapping("/{cropId}")
    public ResponseEntity<CropResponse> updateCrop(
            @PathVariable UUID cropId,
            @Valid @RequestBody CropCreateRequest request) {

        return ResponseEntity.ok(
                cropService.updateCrop(
                        cropId,
                        request
                )
        );
    }

    @DeleteMapping("/{cropId}")
    public ResponseEntity<Void> deleteCrop(
            @PathVariable UUID cropId) {

        cropService.deleteCrop(cropId);

        return ResponseEntity.noContent().build();
    }
}