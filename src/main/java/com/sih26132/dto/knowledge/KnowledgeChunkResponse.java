package com.sih26132.dto.knowledge;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class KnowledgeChunkResponse {

    private UUID id;

    private UUID documentId;

    private Integer chunkIndex;

    private String content;

    private String embedding;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;


    public UUID getId() {
        return id;
    }

    public void setId(
            UUID id) {

        this.id = id;
    }


    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(
            UUID documentId) {

        this.documentId = documentId;
    }


    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(
            Integer chunkIndex) {

        this.chunkIndex = chunkIndex;
    }


    public String getContent() {
        return content;
    }

    public void setContent(
            String content) {

        this.content = content;
    }


    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(
            String embedding) {

        this.embedding = embedding;
    }


    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(
            Map<String, Object> metadata) {

        this.metadata = metadata;
    }


    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            OffsetDateTime createdAt) {

        this.createdAt = createdAt;
    }
}