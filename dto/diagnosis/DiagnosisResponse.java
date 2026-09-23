package com.sih26132.dto.diagnosis;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class DiagnosisResponse {

    private UUID id;
    private UUID caseId;
    private UUID modelVersionId;
    private String conditionName;
    private String conditionType;
    private Double confidence;
    private Map<String, Object> alternatives;
    private Map<String, Object> explainability;
    private String source;
    private Boolean isFinal;
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}