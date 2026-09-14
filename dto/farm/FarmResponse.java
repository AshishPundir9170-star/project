package com.sih26132.dto.farm;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class FarmResponse {

    private UUID id;

    private UUID userId;

    private String farmName;

    private BigDecimal areaHectares;

    private String soilType;

    private String irrigationType;

    private String description;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}