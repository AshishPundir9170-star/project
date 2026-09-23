package com.sih26132.dto.hotspot;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class HotspotResponse {

    private UUID id;

    private String conditionName;

    private String severity;

    private Integer caseCount;

    private Double riskScore;

    private Double latitude;

    private Double longitude;

    private Double radiusKm;

    private OffsetDateTime detectedAt;

    private OffsetDateTime expiresAt;

    private OffsetDateTime createdAt;
}