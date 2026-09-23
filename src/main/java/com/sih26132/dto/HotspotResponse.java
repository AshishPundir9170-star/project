package com.sih26132.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotspotResponse {

    private UUID id;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private OffsetDateTime detectedAt;

    private String conditionName;

    private BigDecimal hotspotScore;

    private String severity;

    private Integer caseCount;

    private String state;

    private String district;

    private String block;

    private String status;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;

    private OffsetDateTime expiresAt;

    private Double radiusKm;

    private Double riskScore;
}