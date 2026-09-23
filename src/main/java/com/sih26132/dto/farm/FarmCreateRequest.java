package com.sih26132.dto.farm;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FarmCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String farmName;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal areaHectares;

    @Size(max = 100)
    private String soilType;

    @Size(max = 100)
    private String irrigationType;

    private String description;
}