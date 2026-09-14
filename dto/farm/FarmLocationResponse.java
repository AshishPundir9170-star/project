package com.sih26132.dto.farm;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class FarmLocationResponse {

    private UUID id;

    private UUID farmId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private String village;

    private String block;

    private String district;

    private String state;

    private String country;

    private OffsetDateTime createdAt;
}