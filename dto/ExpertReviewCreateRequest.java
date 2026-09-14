package com.sih26132.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpertReviewCreateRequest {

    @NotNull(message = "Case ID is required")
    private UUID caseId;

    @NotBlank(message = "Decision is required")
    private String decision;

    private String diagnosis;

    private String comments;

    private String recommendedAction;
}