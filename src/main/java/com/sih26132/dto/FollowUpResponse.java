package com.sih26132.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    private String nextAction;

    private OffsetDateTime createdAt;
}