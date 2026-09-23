package com.sih26132.service;

import com.sih26132.dto.HotspotCreateRequest;
import com.sih26132.dto.HotspotResponse;
import com.sih26132.entity.Hotspot;
import com.sih26132.repository.HotspotRepository;

import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HotspotService {

    private final HotspotRepository hotspotRepository;

    private final GeometryFactory geometryFactory =
            new GeometryFactory(
                    new PrecisionModel(),
                    4326
            );

    public HotspotResponse createHotspot(
            HotspotCreateRequest request
    ) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Hotspot request is required"
            );
        }

        validateCoordinates(
                request.getLatitude().doubleValue(),
                request.getLongitude().doubleValue()
        );

        Point location = createPoint(
                request.getLatitude().doubleValue(),
                request.getLongitude().doubleValue()
        );

        OffsetDateTime now = OffsetDateTime.now();

        Integer caseCount = request.getCaseCount();

        if (caseCount == null) {
            caseCount = 0;
        }

        Hotspot hotspot = Hotspot.builder()
                .location(location)
                .detectedAt(request.getDetectedAt())
                .conditionName(request.getConditionName())
                .hotspotScore(request.getHotspotScore())
                .severity(request.getSeverity())
                .caseCount(caseCount)
                .state(request.getState())
                .district(request.getDistrict())
                .block(request.getBlock())
                .status(request.getStatus())
                .metadata(request.getMetadata())
                .createdAt(now)
                .expiresAt(request.getExpiresAt())
                .radiusKm(request.getRadiusKm())
                .riskScore(request.getRiskScore())
                .build();

        Hotspot savedHotspot =
                hotspotRepository.save(hotspot);

        return mapToResponse(savedHotspot);
    }

    @Transactional(readOnly = true)
    public HotspotResponse getHotspotById(
            UUID hotspotId
    ) {

        Hotspot hotspot =
                hotspotRepository.findById(hotspotId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Hotspot not found"
                        ));

        return mapToResponse(hotspot);
    }

    @Transactional(readOnly = true)
    public List<HotspotResponse> getAllHotspots() {

        return hotspotRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotspotResponse> getHotspotsByStatus(
            String status
    ) {

        return hotspotRepository
                .findByStatusOrderByDetectedAtDesc(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotspotResponse> getHotspotsByCondition(
            String conditionName
    ) {

        return hotspotRepository
                .findByConditionName(conditionName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotspotResponse> getHotspotsBySeverity(
            String severity
    ) {

        return hotspotRepository
                .findBySeverity(severity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotspotResponse> getHotspotsByDistrict(
            String district
    ) {

        return hotspotRepository
                .findByDistrict(district)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotspotResponse> getHotspotsByState(
            String state
    ) {

        return hotspotRepository
                .findByState(state)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public HotspotResponse updateHotspotStatus(
            UUID hotspotId,
            String status
    ) {

        if (status == null || status.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required"
            );
        }

        Hotspot hotspot =
                hotspotRepository.findById(hotspotId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Hotspot not found"
                        ));

        hotspot.setStatus(status);

        Hotspot savedHotspot =
                hotspotRepository.save(hotspot);

        return mapToResponse(savedHotspot);
    }

    public int expireHotspots() {

        OffsetDateTime now = OffsetDateTime.now();

        List<Hotspot> hotspots =
                hotspotRepository
                        .findByStatusAndExpiresAtBefore(
                                "ACTIVE",
                                now
                        );

        for (Hotspot hotspot : hotspots) {
            hotspot.setStatus("EXPIRED");
        }

        hotspotRepository.saveAll(hotspots);

        return hotspots.size();
    }

    private Point createPoint(
            double latitude,
            double longitude
    ) {

        Point point = geometryFactory.createPoint(
                new Coordinate(
                        longitude,
                        latitude
                )
        );

        point.setSRID(4326);

        return point;
    }

    private void validateCoordinates(
            double latitude,
            double longitude
    ) {

        if (latitude < -90.0 || latitude > 90.0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Latitude must be between -90 and 90"
            );
        }

        if (longitude < -180.0 || longitude > 180.0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Longitude must be between -180 and 180"
            );
        }
    }

    private HotspotResponse mapToResponse(
            Hotspot hotspot
    ) {

        double latitude = 0.0;
        double longitude = 0.0;

        if (hotspot.getLocation() instanceof Point point) {
            latitude = point.getY();
            longitude = point.getX();
        }

        return HotspotResponse.builder()
                .id(hotspot.getId())
                .latitude(
                        java.math.BigDecimal.valueOf(latitude)
                )
                .longitude(
                        java.math.BigDecimal.valueOf(longitude)
                )
                .detectedAt(hotspot.getDetectedAt())
                .conditionName(hotspot.getConditionName())
                .hotspotScore(hotspot.getHotspotScore())
                .severity(hotspot.getSeverity())
                .caseCount(hotspot.getCaseCount())
                .state(hotspot.getState())
                .district(hotspot.getDistrict())
                .block(hotspot.getBlock())
                .status(hotspot.getStatus())
                .metadata(hotspot.getMetadata())
                .createdAt(hotspot.getCreatedAt())
                .expiresAt(hotspot.getExpiresAt())
                .radiusKm(hotspot.getRadiusKm())
                .riskScore(hotspot.getRiskScore())
                .build();
    }
}