package com.sih26132.dto.knowledge;

import jakarta.validation.constraints.NotBlank;

public class KnowledgeIngestionRequest {

    @NotBlank(message = "Knowledge text must not be blank")
    private String text;

    public KnowledgeIngestionRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}