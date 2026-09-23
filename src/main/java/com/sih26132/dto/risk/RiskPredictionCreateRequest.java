package com.sih26132.dto.risk;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class RiskPredictionCreateRequest {

    @NotNull
    private UUID caseId;

    private UUID modelVersionId;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double riskScore;

    @Size(max = 30)
    private String severity;

    private OffsetDateTime forecastStart;

    private OffsetDateTime forecastEnd;

    private Map<String, Object> contributingFactors;

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

    public void setContributingFactors(Map<String, Object> contributingFactors) {
        this.contributingFactors = contributingFactors;
    }
}