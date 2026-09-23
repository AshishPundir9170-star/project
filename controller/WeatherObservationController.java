package com.sih26132.controller;

import com.sih26132.dto.weather.WeatherObservationCreateRequest;
import com.sih26132.dto.weather.WeatherObservationResponse;
import com.sih26132.service.WeatherObservationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherObservationController {

    private final WeatherObservationService weatherObservationService;

    @PostMapping
    public ResponseEntity<WeatherObservationResponse> createObservation(
            @Valid @RequestBody WeatherObservationCreateRequest request,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        WeatherObservationResponse response =
                weatherObservationService.createObservation(
                        request,
                        latitude,
                        longitude
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<WeatherObservationResponse>>
            getAllObservations() {

        return ResponseEntity.ok(
                weatherObservationService.getAllObservations()
        );
    }

    @GetMapping("/range")
    public ResponseEntity<List<WeatherObservationResponse>>
            getObservationsBetween(
                    @RequestParam
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime start,

                    @RequestParam
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime end) {

        return ResponseEntity.ok(
                weatherObservationService
                        .getObservationsBetween(start, end)
        );
    }

    @GetMapping("/source/{source}")
    public ResponseEntity<List<WeatherObservationResponse>>
            getObservationsBySource(
                    @PathVariable String source) {

        return ResponseEntity.ok(
                weatherObservationService
                        .getObservationsBySource(source)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherObservationResponse>
            getObservation(@PathVariable UUID id) {

        return ResponseEntity.ok(
                weatherObservationService.getObservation(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObservation(
            @PathVariable UUID id) {

        weatherObservationService.deleteObservation(id);

        return ResponseEntity.noContent().build();
    }
}