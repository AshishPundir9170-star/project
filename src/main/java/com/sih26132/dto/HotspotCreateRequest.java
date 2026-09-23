package com.sih26132.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotspotCreateRequest {

    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    @NotNull(message = "Detected time is required")
    private OffsetDateTime detectedAt;

    @NotBlank(message = "Condition name is required")
    private String conditionName;

    private BigDecimal hotspotScore;

    private String severity;

    private Integer caseCount;

    private String state;

    private String district;

    private String block;

    @NotBlank(message = "Status is required")
    private String status;

    private Map<String, Object> metadata;

    private OffsetDateTime expiresAt;

    private Double radiusKm;

    private Double riskScore;
}