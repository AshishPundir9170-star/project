package com.sih26132.dto.knowledge;

import java.util.UUID;

public class KnowledgeIngestionResponse {

    private UUID documentId;

    private int chunksCreated;

    private String message;

    public KnowledgeIngestionResponse() {
    }

    public KnowledgeIngestionResponse(
            UUID documentId,
            int chunksCreated,
            String message) {

        this.documentId = documentId;
        this.chunksCreated = chunksCreated;
        this.message = message;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public int getChunksCreated() {
        return chunksCreated;
    }

    public void setChunksCreated(int chunksCreated) {
        this.chunksCreated = chunksCreated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}