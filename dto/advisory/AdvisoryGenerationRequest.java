package com.sih26132.dto.advisory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AdvisoryGenerationRequest {

    @NotNull(message = "Case ID is required")
    private UUID caseId;

    @NotBlank(message = "Farmer question is required")
    private String farmerQuestion;

    private String language;

    @NotNull(message = "Retrieval limit is required")
    @Min(value = 1, message = "Retrieval limit must be at least 1")
    @Max(value = 100, message = "Retrieval limit must not exceed 100")
    private Integer retrievalLimit = 5;

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public String getFarmerQuestion() {
        return farmerQuestion;
    }

    public void setFarmerQuestion(String farmerQuestion) {
        this.farmerQuestion = farmerQuestion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getRetrievalLimit() {
        return retrievalLimit;
    }

    public void setRetrievalLimit(Integer retrievalLimit) {
        this.retrievalLimit = retrievalLimit;
    }
}