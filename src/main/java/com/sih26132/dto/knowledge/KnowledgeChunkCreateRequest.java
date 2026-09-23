package com.sih26132.dto.knowledge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Map;

public class KnowledgeChunkCreateRequest {

    @NotNull
    @PositiveOrZero
    private Integer chunkIndex;

    @NotBlank
    private String content;

    private String embedding;

    private Map<String, Object> metadata;


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
}