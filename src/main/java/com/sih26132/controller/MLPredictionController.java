package com.sih26132.controller;

import com.sih26132.dto.ml.MLPredictionRequest;
import com.sih26132.service.ml.MLPredictionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/ml")
public class MLPredictionController {

    private final MLPredictionService mlPredictionService;

    public MLPredictionController(
            MLPredictionService mlPredictionService) {

        this.mlPredictionService =
                mlPredictionService;
    }


    @PostMapping("/price")
    public ResponseEntity<?> predictPrice(
            @RequestBody MLPredictionRequest request) {

        return ResponseEntity.ok(
                mlPredictionService.predictPrice(request)
        );
    }


    @PostMapping("/sale-window")
    public ResponseEntity<?> predictSaleWindow(
            @RequestBody MLPredictionRequest request) {

        return ResponseEntity.ok(
                mlPredictionService.predictSaleWindow(request)
        );
    }


    @PostMapping("/buyer-match")
    public ResponseEntity<?> predictBuyerMatch(
            @RequestBody MLPredictionRequest request) {

        return ResponseEntity.ok(
                mlPredictionService.predictBuyerMatch(request)
        );
    }
}