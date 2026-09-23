package com.sih26132.dto.crop;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CropCycleResponse {

    private UUID id;

    private UUID farmId;

    private UUID cropId;

    private String cropName;

    private String variety;

    private String growthStage;

    private LocalDate sowingDate;

    private LocalDate transplantDate;

    private LocalDate expectedHarvestDate;

    private BigDecimal areaHectares;

    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}