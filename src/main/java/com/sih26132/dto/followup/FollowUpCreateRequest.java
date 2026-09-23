package com.sih26132.dto.followup;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class FollowUpCreateRequest {

    @NotNull
    private UUID caseId;

    private OffsetDateTime scheduledAt;

    private String notes;
}