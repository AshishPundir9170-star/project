package com.sih26132.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    /**
     * text-embedding-3-small default embedding dimension.
     */
    private static final int EXPECTED_EMBEDDING_DIMENSIONS = 1536;

    private final ObjectMapper objectMapper;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.embedding-url}")
    private String embeddingUrl;

    @Value("${openai.embedding-model}")
    private String embeddingModel;

    private final RestClient restClient =
            RestClient.builder().build();

    @Override
    public List<Double> generateEmbedding(String text) {

        /*
         * ---------------------------------------------------------
         * 1. Validate input
         * ---------------------------------------------------------
         */

        if (text == null || text.isBlank()) {

            throw new IllegalArgumentException(
                    "Text must not be null or blank"
            );
        }

        /*
         * ---------------------------------------------------------
         * 2. Validate API configuration
         * ---------------------------------------------------------
         */

        if (apiKey == null || apiKey.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI API key is not configured"
            );
        }

        if (embeddingUrl == null || embeddingUrl.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI embedding URL is not configured"
            );
        }

        if (embeddingModel == null || embeddingModel.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI embedding model is not configured"
            );
        }

        /*
         * ---------------------------------------------------------
         * 3. Build OpenAI embedding request
         * ---------------------------------------------------------
         */

        Map<String, Object> requestBody =
                Map.of(
                        "model",
                        embeddingModel,

                        "input",
                        text,

                        "encoding_format",
                        "float"
                );

        /*
         * ---------------------------------------------------------
         * 4. Call OpenAI Embeddings API
         * ---------------------------------------------------------
         */

        String responseBody =
                restClient
                        .post()
                        .uri(embeddingUrl)
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + apiKey
                        )
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .body(requestBody)
                        .retrieve()
                        .body(String.class);

        /*
         * ---------------------------------------------------------
         * 5. Validate response
         * ---------------------------------------------------------
         */

        if (responseBody == null ||
                responseBody.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI returned an empty response"
            );
        }

        /*
         * ---------------------------------------------------------
         * 6. Parse embedding response
         * ---------------------------------------------------------
         */

        try {

            JsonNode root =
                    objectMapper.readTree(responseBody);

            JsonNode dataNode =
                    root.path("data");

            if (!dataNode.isArray() ||
                    dataNode.isEmpty()) {

                throw new IllegalStateException(
                        "OpenAI response does not contain "
                        + "embedding data"
                );
            }

            JsonNode embeddingNode =
                    dataNode
                            .path(0)
                            .path("embedding");

            if (!embeddingNode.isArray()) {

                throw new IllegalStateException(
                        "OpenAI response does not contain "
                        + "a valid embedding vector"
                );
            }

            /*
             * -----------------------------------------------------
             * 7. Convert JSON array to List<Double>
             * -----------------------------------------------------
             */

            List<Double> embedding =
                    new ArrayList<>(
                            embeddingNode.size()
                    );

            for (JsonNode value : embeddingNode) {

                if (!value.isNumber()) {

                    throw new IllegalStateException(
                            "Embedding contains a "
                            + "non-numeric value"
                    );
                }

                embedding.add(
                        value.asDouble()
                );
            }

            /*
             * -----------------------------------------------------
             * 8. Validate that embedding is not empty
             * -----------------------------------------------------
             */

            if (embedding.isEmpty()) {

                throw new IllegalStateException(
                        "OpenAI returned an empty embedding"
                );
            }

            /*
             * -----------------------------------------------------
             * 9. Validate embedding dimension
             * -----------------------------------------------------
             */

            if (embedding.size()
                    != EXPECTED_EMBEDDING_DIMENSIONS) {

                throw new IllegalStateException(
                        "Unexpected embedding dimension. "
                        + "Expected "
                        + EXPECTED_EMBEDDING_DIMENSIONS
                        + " but received "
                        + embedding.size()
                );
            }

            /*
             * -----------------------------------------------------
             * 10. Return embedding
             * -----------------------------------------------------
             */

            return embedding;

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to parse OpenAI embedding response",
                    e
            );
        }
    }
}