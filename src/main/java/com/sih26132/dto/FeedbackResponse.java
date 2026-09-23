package com.sih26132.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private UUID id;

    private UUID caseId;

    private UUID userId;

    private String feedbackType;

    private Integer rating;

    private String comment;

    private String verifiedLabel;

    private String actualCondition;

    private Boolean modelWasCorrect;

    private OffsetDateTime createdAt;
}