package com.sih26132.service;

import java.util.List;

public interface EmbeddingService {

    /**
     * Generates an embedding vector for the supplied text.
     *
     * @param text text to convert into an embedding
     * @return embedding vector
     */
    List<Double> generateEmbedding(
            String text
    );
}