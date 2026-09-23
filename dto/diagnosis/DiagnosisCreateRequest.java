package com.sih26132.dto.diagnosis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.util.Map;
import java.util.UUID;

public class DiagnosisCreateRequest {

    @NotNull
    private UUID caseId;

    private UUID modelVersionId;

    @NotBlank
    private String conditionName;

    private String conditionType;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double confidence;

    private Map<String, Object> alternatives;

    private Map<String, Object> explainability;

    @NotBlank
    private String source;

    private boolean isFinal;

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public UUID getModelVersionId() {
        return modelVersionId;
    }

    public void setModelVersionId(UUID modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Map<String, Object> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(Map<String, Object> alternatives) {
        this.alternatives = alternatives;
    }

    public Map<String, Object> getExplainability() {
        return explainability;
    }

    public void setExplainability(Map<String, Object> explainability) {
        this.explainability = explainability;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
}