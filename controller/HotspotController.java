package com.sih26132.controller;

import com.sih26132.dto.HotspotCreateRequest;
import com.sih26132.dto.HotspotResponse;
import com.sih26132.service.HotspotService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hotspots")
@RequiredArgsConstructor
public class HotspotController {

    private final HotspotService hotspotService;

    @PostMapping
    public ResponseEntity<HotspotResponse> createHotspot(
            @Valid @RequestBody HotspotCreateRequest request
    ) {

        HotspotResponse response =
                hotspotService.createHotspot(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<HotspotResponse>> getAllHotspots() {

        return ResponseEntity.ok(
                hotspotService.getAllHotspots()
        );
    }

    @GetMapping("/{hotspotId}")
    public ResponseEntity<HotspotResponse> getHotspotById(
            @PathVariable UUID hotspotId
    ) {

        return ResponseEntity.ok(
                hotspotService.getHotspotById(hotspotId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HotspotResponse>> getHotspotsByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                hotspotService.getHotspotsByStatus(status)
        );
    }

    @GetMapping("/condition/{conditionName}")
    public ResponseEntity<List<HotspotResponse>> getHotspotsByCondition(
            @PathVariable String conditionName
    ) {

        return ResponseEntity.ok(
                hotspotService.getHotspotsByCondition(conditionName)
        );
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<HotspotResponse>> getHotspotsBySeverity(
            @PathVariable String severity
    ) {

        return ResponseEntity.ok(
                hotspotService.getHotspotsBySeverity(severity)
        );
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<HotspotResponse>> getHotspotsByState(
            @PathVariable String state
    ) {

        return ResponseEntity.ok(
                hotspotService.getHotspotsByState(state)
        );
    }

    @GetMapping("/district/{district}")
    public ResponseEntity<List<HotspotResponse>> getHotspotsByDistrict(
            @PathVariable String district
    ) {

        return ResponseEntity.ok(
                hotspotService.getHotspotsByDistrict(district)
        );
    }

    @PatchMapping("/{hotspotId}/status")
    public ResponseEntity<HotspotResponse> updateHotspotStatus(
            @PathVariable UUID hotspotId,
            @RequestParam String status
    ) {

        return ResponseEntity.ok(
                hotspotService.updateHotspotStatus(
                        hotspotId,
                        status
                )
        );
    }

    @PostMapping("/expire")
    public ResponseEntity<Integer> expireHotspots() {

        return ResponseEntity.ok(
                hotspotService.expireHotspots()
        );
    }
}