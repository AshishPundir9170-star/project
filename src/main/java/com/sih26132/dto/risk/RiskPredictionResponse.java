package com.sih26132.dto.risk;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class RiskPredictionResponse {

    private UUID id;
    private UUID caseId;
    private UUID modelVersionId;
    private Double riskScore;
    private String severity;
    private OffsetDateTime forecastStart;
    private OffsetDateTime forecastEnd;
    private Map<String, Object> contributingFactors;
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

    public Double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public OffsetDateTime getForecastStart() {
        return forecastStart;
    }

    public void setForecastStart(OffsetDateTime forecastStart) {
        this.forecastStart = forecastStart;
    }

    public OffsetDateTime getForecastEnd() {
        return forecastEnd;
    }

    public void setForecastEnd(OffsetDateTime forecastEnd) {
        this.forecastEnd = forecastEnd;
    }

    public Map<String, Object> getContributingFactors() {
        return contributingFactors;
    }

    public void setContributingFactors(
            Map<String, Object> contributingFactors) {
        this.contributingFactors = contributingFactors;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}