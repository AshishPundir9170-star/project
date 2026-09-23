package com.sih26132.service;

import com.sih26132.entity.KnowledgeChunk;
import com.sih26132.entity.KnowledgeDocument;
import com.sih26132.repository.KnowledgeChunkRepository;
import com.sih26132.repository.KnowledgeDocumentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KnowledgeIngestionService {

    private final KnowledgeDocumentRepository knowledgeDocumentRepository;

    private final KnowledgeChunkRepository knowledgeChunkRepository;

    private final KnowledgeChunkingService knowledgeChunkingService;

    private final EmbeddingService embeddingService;

    /**
     * Ingests complete knowledge text into the selected
     * knowledge document.
     *
     * Existing chunks belonging to the document are removed
     * before the new chunks are created.
     */
    @Transactional
    public int ingestDocument(
            UUID documentId,
            String text) {

        /*
         * ---------------------------------------------------------
         * 1. Validate document
         * ---------------------------------------------------------
         */

        KnowledgeDocument document =
                knowledgeDocumentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );

        /*
         * ---------------------------------------------------------
         * 2. Validate text
         * ---------------------------------------------------------
         */

        if (text == null || text.isBlank()) {

            throw new IllegalArgumentException(
                    "Knowledge document text must not be null or blank"
            );
        }

        /*
         * ---------------------------------------------------------
         * 3. Split document into chunks
         * ---------------------------------------------------------
         */

        List<String> chunks =
                knowledgeChunkingService.splitIntoChunks(
                        text
                );

        if (chunks.isEmpty()) {

            throw new IllegalArgumentException(
                    "No knowledge chunks were generated"
            );
        }

        /*
         * ---------------------------------------------------------
         * 4. Remove existing chunks
         * ---------------------------------------------------------
         *
         * This makes ingestion idempotent.
         *
         * If the same document is ingested again,
         * its previous chunks are replaced.
         */

        knowledgeChunkRepository.deleteByDocument(
                document
        );

        /*
         * ---------------------------------------------------------
         * 5. Generate embeddings and create chunks
         * ---------------------------------------------------------
         */

        List<KnowledgeChunk> entities =
                new ArrayList<>();

        for (int index = 0;
             index < chunks.size();
             index++) {

            String content =
                    chunks.get(index);

            /*
             * Generate embedding from chunk text.
             */

            List<Double> embeddingValues =
                    embeddingService.generateEmbedding(
                            content
                    );

            /*
             * Convert List<Double> to float[].
             *
             * KnowledgeChunk now uses float[] for the
             * Hibernate VECTOR mapping.
             */

            float[] embedding =
                    convertToFloatArray(
                            embeddingValues
                    );

            /*
             * Create KnowledgeChunk entity.
             */

            KnowledgeChunk chunk =
                    KnowledgeChunk.builder()
                            .document(document)
                            .chunkIndex(index)
                            .content(content)
                            .embedding(embedding)
                            .metadata(null)
                            .createdAt(
                                    OffsetDateTime.now()
                            )
                            .build();

            entities.add(chunk);
        }

        /*
         * ---------------------------------------------------------
         * 6. Save all chunks
         * ---------------------------------------------------------
         */

        knowledgeChunkRepository.saveAll(
                entities
        );

        /*
         * ---------------------------------------------------------
         * 7. Return number of generated chunks
         * ---------------------------------------------------------
         */

        return entities.size();
    }

    /**
     * Converts List<Double> into float[].
     *
     * Hibernate's native VECTOR mapping expects
     * the entity field to be a Java array.
     */
    private float[] convertToFloatArray(
            List<Double> embeddingValues) {

        if (embeddingValues == null ||
                embeddingValues.isEmpty()) {

            throw new IllegalArgumentException(
                    "Embedding must not be null or empty"
            );
        }

        float[] embedding =
                new float[embeddingValues.size()];

        for (int i = 0;
             i < embeddingValues.size();
             i++) {

            Double value =
                    embeddingValues.get(i);

            if (value == null) {

                throw new IllegalArgumentException(
                        "Embedding contains null value at index "
                                + i
                );
            }

            embedding[i] =
                    value.floatValue();
        }

        return embedding;
    }
}