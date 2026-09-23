package com.sih26132.controller;

import com.sih26132.dto.sensor.SensorObservationCreateRequest;
import com.sih26132.dto.sensor.SensorObservationResponse;
import com.sih26132.service.SensorObservationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/farms/{farmId}/sensor-observations")
@RequiredArgsConstructor
public class SensorObservationController {

    private final SensorObservationService sensorObservationService;

    @PostMapping
    public ResponseEntity<SensorObservationResponse> createObservation(
            @PathVariable UUID farmId,
            @Valid @RequestBody SensorObservationCreateRequest request,
            Authentication authentication) {

        request.setFarmId(farmId);

        SensorObservationResponse response =
                sensorObservationService.createObservation(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SensorObservationResponse>>
            getFarmObservations(
                    @PathVariable UUID farmId,
                    Authentication authentication) {

        return ResponseEntity.ok(
                sensorObservationService.getFarmObservations(
                        farmId,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/range")
    public ResponseEntity<List<SensorObservationResponse>>
            getObservationsBetween(
                    @PathVariable UUID farmId,

                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE_TIME
                    )
                    OffsetDateTime start,

                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE_TIME
                    )
                    OffsetDateTime end,

                    Authentication authentication) {

        return ResponseEntity.ok(
                sensorObservationService.getObservationsBetween(
                        farmId,
                        start,
                        end,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/sensor-type/{sensorType}")
    public ResponseEntity<List<SensorObservationResponse>>
            getObservationsBySensorType(
                    @PathVariable UUID farmId,
                    @PathVariable String sensorType,
                    Authentication authentication) {

        return ResponseEntity.ok(
                sensorObservationService
                        .getObservationsBySensorType(
                                farmId,
                                sensorType,
                                authentication.getName()
                        )
        );
    }

    @GetMapping("/metric/{metric}")
    public ResponseEntity<List<SensorObservationResponse>>
            getObservationsByMetric(
                    @PathVariable UUID farmId,
                    @PathVariable String metric,
                    Authentication authentication) {

        return ResponseEntity.ok(
                sensorObservationService
                        .getObservationsByMetric(
                                farmId,
                                metric,
                                authentication.getName()
                        )
        );
    }

    @GetMapping("/{observationId}")
    public ResponseEntity<SensorObservationResponse>
            getObservation(
                    @PathVariable UUID farmId,
                    @PathVariable UUID observationId,
                    Authentication authentication) {

        return ResponseEntity.ok(
                sensorObservationService.getObservation(
                        farmId,
                        observationId,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{observationId}")
    public ResponseEntity<Void> deleteObservation(
            @PathVariable UUID farmId,
            @PathVariable UUID observationId,
            Authentication authentication) {

        sensorObservationService.deleteObservation(
                farmId,
                observationId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}