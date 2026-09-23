package com.sih26132.dto.advisory;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AdvisoryGenerationResponse {

    private UUID advisoryId;
    private UUID caseId;
    private String language;
    private String title;
    private String content;
    private String immediateActions;
    private String preventiveActions;
    private String ipdmActions;
    private String safeUseInstructions;
    private String whenToContactExpert;
    private String whenToRecheck;
    private String generatedBy;
    private String status;
    private OffsetDateTime createdAt;

    public UUID getAdvisoryId() {
        return advisoryId;
    }

    public void setAdvisoryId(UUID advisoryId) {
        this.advisoryId = advisoryId;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImmediateActions() {
        return immediateActions;
    }

    public void setImmediateActions(String immediateActions) {
        this.immediateActions = immediateActions;
    }

    public String getPreventiveActions() {
        return preventiveActions;
    }

    public void setPreventiveActions(String preventiveActions) {
        this.preventiveActions = preventiveActions;
    }

    public String getIpdmActions() {
        return ipdmActions;
    }

    public void setIpdmActions(String ipdmActions) {
        this.ipdmActions = ipdmActions;
    }

    public String getSafeUseInstructions() {
        return safeUseInstructions;
    }

    public void setSafeUseInstructions(String safeUseInstructions) {
        this.safeUseInstructions = safeUseInstructions;
    }

    public String getWhenToContactExpert() {
        return whenToContactExpert;
    }

    public void setWhenToContactExpert(String whenToContactExpert) {
        this.whenToContactExpert = whenToContactExpert;
    }

    public String getWhenToRecheck() {
        return whenToRecheck;
    }

    public void setWhenToRecheck(String whenToRecheck) {
        this.whenToRecheck = whenToRecheck;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}