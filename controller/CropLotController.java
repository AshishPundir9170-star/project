package com.sih26132.controller;

import com.sih26132.dto.ml.CropLotMLPredictionResponse;
import com.sih26132.entity.CropLot;
import com.sih26132.service.CropLotService;
import com.sih26132.service.ml.CropLotMLService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/crop-lots")
public class CropLotController {

    private final CropLotService cropLotService;
    private final CropLotMLService cropLotMLService;

    public CropLotController(
            CropLotService cropLotService,
            CropLotMLService cropLotMLService
    ) {
        this.cropLotService = cropLotService;
        this.cropLotMLService = cropLotMLService;
    }

    @PostMapping
    public ResponseEntity<CropLot> createCropLot(
            Authentication authentication,
            @RequestParam UUID cropId,
            @RequestBody CropLot cropLot
    ) {

        String username = authentication.getName();

        CropLot savedLot =
                cropLotService.createCropLot(
                        username,
                        cropId,
                        cropLot
                );

        return ResponseEntity.ok(savedLot);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyCropLots(
            Authentication authentication
    ) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                cropLotService.getFarmerCropLots(username)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCropLot(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                cropLotService.getCropLot(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCropLot(
            @PathVariable UUID id
    ) {

        cropLotService.deleteCropLot(id);

        return ResponseEntity.ok(
                "Crop lot deleted successfully"
        );
    }

    // ================================
    // ML PREDICTION ENDPOINT
    // ================================

    @GetMapping("/{id}/predictions")
    public ResponseEntity<CropLotMLPredictionResponse> predictCropLot(
            @PathVariable UUID id
    ) {

        CropLotMLPredictionResponse response =
                cropLotMLService.predictCropLot(id);

        return ResponseEntity.ok(response);
    }
}