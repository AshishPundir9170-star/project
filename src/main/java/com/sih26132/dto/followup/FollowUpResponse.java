package com.sih26132.dto.followup;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class FollowUpResponse {

    private UUID id;

    private UUID caseId;

    private UUID farmerUserId;

    private OffsetDateTime scheduledAt;

    private OffsetDateTime completedAt;

    private String status;

    private String notes;

    private String outcome;

    private OffsetDateTime createdAt;
}