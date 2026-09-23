package com.sih26132.dto.feedback;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class FeedbackResponse {

    private UUID id;

    private UUID caseId;

    private UUID userId;

    private String type;

    private String comments;

    private Integer rating;

    private String correctDiagnosis;

    private OffsetDateTime createdAt;
}