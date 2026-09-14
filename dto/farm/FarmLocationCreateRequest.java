package com.sih26132.dto.farm;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FarmLocationCreateRequest {

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private BigDecimal latitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private BigDecimal longitude;

    private String address;

    @Size(max = 150)
    private String village;

    @Size(max = 150)
    private String block;

    @Size(max = 150)
    private String district;

    @Size(max = 150)
    private String state;

    @Size(max = 100)
    private String country;
}