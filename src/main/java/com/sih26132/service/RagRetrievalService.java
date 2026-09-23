package com.sih26132.service;

import com.sih26132.service.KnowledgeSearchService.KnowledgeSearchResult;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RagRetrievalService {

    private final EmbeddingService embeddingService;

    private final KnowledgeSearchService knowledgeSearchService;

    /**
     * Retrieves relevant knowledge for a user query.
     *
     * @param query farmer/user question
     * @param limit maximum number of knowledge chunks
     * @return RAG context
     */
    public RagContext retrieve(
            String query,
            int limit) {

        /*
         * ---------------------------------------------------------
         * 1. Validate query
         * ---------------------------------------------------------
         */

        if (query == null || query.isBlank()) {

            throw new IllegalArgumentException(
                    "Query must not be null or blank"
            );
        }

        /*
         * ---------------------------------------------------------
         * 2. Validate limit
         * ---------------------------------------------------------
         */

        if (limit < 1) {

            throw new IllegalArgumentException(
                    "Retrieval limit must be at least 1"
            );
        }

        if (limit > 100) {

            throw new IllegalArgumentException(
                    "Retrieval limit must not exceed 100"
            );
        }

        /*
         * ---------------------------------------------------------
         * 3. Generate query embedding
         * ---------------------------------------------------------
         */

        List<Double> embeddingValues =
                embeddingService.generateEmbedding(
                        query
                );

        /*
         * ---------------------------------------------------------
         * 4. Convert embedding to pgvector format
         * ---------------------------------------------------------
         */

        String embedding =
                convertToVectorString(
                        embeddingValues
                );

        /*
         * ---------------------------------------------------------
         * 5. Perform similarity search
         * ---------------------------------------------------------
         */

        List<KnowledgeSearchResult> results =
                knowledgeSearchService.search(
                        embedding,
                        limit
                );

        /*
         * ---------------------------------------------------------
         * 6. Build combined RAG context
         * ---------------------------------------------------------
         */

        String combinedContext =
                buildCombinedContext(
                        results
                );

        /*
         * ---------------------------------------------------------
         * 7. Return context
         * ---------------------------------------------------------
         */

        return new RagContext(
                query,
                results,
                combinedContext
        );
    }

    /**
     * Uses the default retrieval limit.
     */
    public RagContext retrieve(
            String query) {

        return retrieve(
                query,
                5
        );
    }

    /**
     * Converts List<Double> into pgvector string format.
     *
     * Example:
     *
     * [0.1, 0.2, 0.3]
     *
     * becomes:
     *
     * [0.1,0.2,0.3]
     */
    private String convertToVectorString(
            List<Double> embeddingValues) {

        if (embeddingValues == null ||
                embeddingValues.isEmpty()) {

            throw new IllegalArgumentException(
                    "Embedding must not be null or empty"
            );
        }

        StringBuilder vector =
                new StringBuilder("[");

        for (int i = 0;
             i < embeddingValues.size();
             i++) {

            if (i > 0) {
                vector.append(",");
            }

            vector.append(
                    embeddingValues.get(i)
            );
        }

        vector.append("]");

        return vector.toString();
    }

    /**
     * Combines retrieved chunks into a context string.
     *
     * The context will later be supplied to the LLM/advisory
     * generation layer.
     */
    private String buildCombinedContext(
            List<KnowledgeSearchResult> results) {

        if (results == null ||
                results.isEmpty()) {

            return "";
        }

        return results.stream()
                .filter(result ->
                        result.getContent() != null &&
                        !result.getContent().isBlank()
                )
                .map(this::formatResult)
                .collect(
                        Collectors.joining(
                                "\n\n"
                        )
                );
    }

    /**
     * Formats an individual retrieved chunk.
     */
    private String formatResult(
            KnowledgeSearchResult result) {

        StringBuilder context =
                new StringBuilder();

        context.append(
                "[Knowledge Chunk]\n"
        );

        context.append(
                "Document ID: "
        );

        context.append(
                result.getDocumentId()
        );

        context.append("\n");

        context.append(
                "Chunk Index: "
        );

        context.append(
                result.getChunkIndex()
        );

        context.append("\n");

        context.append(
                "Similarity Score: "
        );

        context.append(
                result.getSimilarityScore()
        );

        context.append("\n\n");

        context.append(
                result.getContent()
        );

        return context.toString();
    }
}