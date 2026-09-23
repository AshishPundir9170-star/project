package com.sih26132.dto.crop;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CropCycleCreateRequest {

    @NotNull
    private UUID cropId;

    @Size(max = 150)
    private String variety;

    @Size(max = 100)
    private String growthStage;

    private LocalDate sowingDate;

    private LocalDate transplantDate;

    private LocalDate expectedHarvestDate;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal areaHectares;

    @Size(max = 30)
    private String status;
}