package com.sih26132.service;

import com.sih26132.repository.KnowledgeChunkRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KnowledgeSearchService {

    private final KnowledgeChunkRepository knowledgeChunkRepository;

    /**
     * Finds the most similar knowledge chunks for the supplied
     * query embedding.
     *
     * @param embedding query embedding in pgvector format
     *                  example: [0.1,0.2,0.3]
     * @param limit     maximum number of results
     * @return list of similar knowledge chunks
     */
    public List<KnowledgeSearchResult> search(
            String embedding,
            int limit) {

        validateEmbedding(embedding);
        validateLimit(limit);

        List<Object[]> rows =
                knowledgeChunkRepository.findSimilarChunks(
                        embedding,
                        limit
                );

        List<KnowledgeSearchResult> results =
                new ArrayList<>();

        for (Object[] row : rows) {

            KnowledgeSearchResult result =
                    new KnowledgeSearchResult();

            result.setId(
                    toUUID(row[0])
            );

            result.setDocumentId(
                    toUUID(row[1])
            );

            result.setChunkIndex(
                    toInteger(row[2])
            );

            result.setContent(
                    row[3] != null
                            ? row[3].toString()
                            : null
            );

            result.setMetadata(
                    row[4] != null
                            ? row[4].toString()
                            : null
            );

            result.setSimilarityScore(
                    toDouble(row[5])
            );

            results.add(result);
        }

        return results;
    }

    /**
     * Searches using the default number of results.
     */
    public List<KnowledgeSearchResult> search(
            String embedding) {

        return search(embedding, 5);
    }

    /**
     * Validates the pgvector embedding string.
     */
    private void validateEmbedding(
            String embedding) {

        if (embedding == null ||
                embedding.isBlank()) {

            throw new IllegalArgumentException(
                    "Embedding must not be null or blank"
            );
        }

        String trimmed =
                embedding.trim();

        if (!trimmed.startsWith("[") ||
                !trimmed.endsWith("]")) {

            throw new IllegalArgumentException(
                    "Invalid embedding format. "
                    + "Expected pgvector format: "
                    + "[0.1,0.2,0.3]"
            );
        }

        String values =
                trimmed.substring(
                        1,
                        trimmed.length() - 1
                ).trim();

        if (values.isBlank()) {

            throw new IllegalArgumentException(
                    "Embedding must contain values"
            );
        }

        String[] dimensions =
                values.split(",");

        for (String dimension : dimensions) {

            String value =
                    dimension.trim();

            if (value.isBlank()) {

                throw new IllegalArgumentException(
                        "Embedding contains an empty value"
                );
            }

            try {

                Double.parseDouble(value);

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "Embedding contains an invalid "
                        + "numeric value: "
                        + value,
                        e
                );
            }
        }
    }

    /**
     * Prevents invalid or excessively large result requests.
     */
    private void validateLimit(int limit) {

        if (limit < 1) {

            throw new IllegalArgumentException(
                    "Search limit must be at least 1"
            );
        }

        if (limit > 100) {

            throw new IllegalArgumentException(
                    "Search limit must not exceed 100"
            );
        }
    }

    private UUID toUUID(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof UUID uuid) {
            return uuid;
        }

        return UUID.fromString(
                value.toString()
        );
    }

    private Integer toInteger(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.valueOf(
                value.toString()
        );
    }

    private Double toDouble(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        return Double.valueOf(
                value.toString()
        );
    }

    /**
     * Result returned by vector similarity search.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class KnowledgeSearchResult {

        private UUID id;

        private UUID documentId;

        private Integer chunkIndex;

        private String content;

        private String metadata;

        private Double similarityScore;

        public KnowledgeSearchResult() {
        }
    }
}