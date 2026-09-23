package com.sih26132.dto.cases;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CaseResponse {

    private UUID id;

    private String caseNumber;

    private UUID cropCycleId;

    private UUID createdByUserId;

    private UUID assignedToUserId;

    private String status;

    private String description;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}