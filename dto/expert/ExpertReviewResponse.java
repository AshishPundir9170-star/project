package com.sih26132.dto.expert;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class ExpertReviewResponse {

    private UUID id;

    private UUID caseId;

    private UUID expertUserId;

    private String decision;

    private String diagnosis;

    private String comments;

    private String recommendedAction;

    private OffsetDateTime reviewedAt;

    private OffsetDateTime createdAt;
}