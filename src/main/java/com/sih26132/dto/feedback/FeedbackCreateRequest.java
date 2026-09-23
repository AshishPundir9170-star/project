package com.sih26132.dto.feedback;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FeedbackCreateRequest {

    @NotNull
    private UUID caseId;

    private String type;

    private String comments;

    private Integer rating;

    private String correctDiagnosis;
}