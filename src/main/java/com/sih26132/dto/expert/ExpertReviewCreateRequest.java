package com.sih26132.dto.expert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ExpertReviewCreateRequest {

    @NotNull
    private UUID caseId;

    @NotBlank
    private String decision;

    private String diagnosis;

    private String comments;

    private String recommendedAction;
}