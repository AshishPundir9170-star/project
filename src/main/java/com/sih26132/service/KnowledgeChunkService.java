package com.sih26132.service;

import com.sih26132.dto.knowledge.KnowledgeChunkCreateRequest;
import com.sih26132.dto.knowledge.KnowledgeChunkResponse;
import com.sih26132.entity.KnowledgeChunk;
import com.sih26132.entity.KnowledgeDocument;
import com.sih26132.repository.KnowledgeChunkRepository;
import com.sih26132.repository.KnowledgeDocumentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KnowledgeChunkService {

    private final KnowledgeChunkRepository knowledgeChunkRepository;

    private final KnowledgeDocumentRepository knowledgeDocumentRepository;

    private final EmbeddingService embeddingService;


    /*
     * =========================================================
     * CREATE CHUNK
     * =========================================================
     */

    public KnowledgeChunkResponse createChunk(
            UUID documentId,
            KnowledgeChunkCreateRequest request) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );


        /*
         * Generate embedding from chunk content.
         *
         * The embedding service currently returns:
         *
         * List<Double>
         *
         * Hibernate vector mapping requires:
         *
         * float[]
         */
        List<Double> embeddingValues =
                embeddingService.generateEmbedding(
                        request.getContent()
                );


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

                        .chunkIndex(
                                request.getChunkIndex()
                        )

                        .content(
                                request.getContent()
                        )

                        .embedding(
                                embedding
                        )

                        .metadata(
                                request.getMetadata()
                        )

                        .createdAt(
                                OffsetDateTime.now()
                        )

                        .build();


        /*
         * Save chunk.
         */
        KnowledgeChunk savedChunk =
                knowledgeChunkRepository.save(
                        chunk
                );


        /*
         * Convert entity to API response.
         */
        return mapToResponse(
                savedChunk
        );
    }


    /*
     * =========================================================
     * GET ALL CHUNKS FOR DOCUMENT
     * =========================================================
     */

    @Transactional(readOnly = true)
    public List<KnowledgeChunkResponse> getDocumentChunks(
            UUID documentId) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );


        return knowledgeChunkRepository
                .findByDocumentOrderByChunkIndexAsc(
                        document
                )

                .stream()

                .map(
                        this::mapToResponse
                )

                .collect(
                        Collectors.toList()
                );
    }


    /*
     * =========================================================
     * GET CHUNK BY ID
     * =========================================================
     */

    @Transactional(readOnly = true)
    public KnowledgeChunkResponse getChunk(
            UUID documentId,
            UUID chunkId) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );


        KnowledgeChunk chunk =
                knowledgeChunkRepository
                        .findByIdAndDocument(
                                chunkId,
                                document
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge chunk not found: "
                                                + chunkId
                                )
                        );


        return mapToResponse(
                chunk
        );
    }


    /*
     * =========================================================
     * GET CHUNK BY INDEX
     * =========================================================
     */

    
    @Transactional(readOnly = true)
    public KnowledgeChunkResponse getChunkByIndex(
            UUID documentId,
            Integer chunkIndex) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );

        KnowledgeChunk chunk =
                knowledgeChunkRepository
                        .findByDocumentAndChunkIndex(
                                document,
                                chunkIndex
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge chunk not found for index: "
                                                + chunkIndex
                                )
                        );

        return mapToResponse(chunk);
    }


    /*
     * =========================================================
     * UPDATE CHUNK
     * =========================================================
     */

    public KnowledgeChunkResponse updateChunk(
            UUID documentId,
            UUID chunkId,
            KnowledgeChunkCreateRequest request) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );


        KnowledgeChunk chunk =
                knowledgeChunkRepository
                        .findByIdAndDocument(
                                chunkId,
                                document
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge chunk not found: "
                                                + chunkId
                                )
                        );


        /*
         * Update chunk index.
         */
        chunk.setChunkIndex(
                request.getChunkIndex()
        );


        /*
         * Update content.
         */
        chunk.setContent(
                request.getContent()
        );


        /*
         * Generate a NEW embedding because
         * the content has changed.
         */
        List<Double> embeddingValues =
                embeddingService.generateEmbedding(
                        request.getContent()
                );


        float[] embedding =
                convertToFloatArray(
                        embeddingValues
                );


        chunk.setEmbedding(
                embedding
        );


        /*
         * Update metadata.
         */
        chunk.setMetadata(
                request.getMetadata()
        );


        /*
         * Save updated entity.
         */
        KnowledgeChunk updatedChunk =
                knowledgeChunkRepository.save(
                        chunk
                );


        return mapToResponse(
                updatedChunk
        );
    }


    /*
     * =========================================================
     * DELETE CHUNK
     * =========================================================
     */

    public void deleteChunk(
            UUID documentId,
            UUID chunkId) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found: "
                                                + documentId
                                )
                        );


        KnowledgeChunk chunk =
                knowledgeChunkRepository
                        .findByIdAndDocument(
                                chunkId,
                                document
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge chunk not found: "
                                                + chunkId
                                )
                        );


        knowledgeChunkRepository.delete(
                chunk
        );
    }


    /*
     * =========================================================
     * CONVERT List<Double> → float[]
     * =========================================================
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
                new float[
                        embeddingValues.size()
                ];


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


    /*
     * =========================================================
     * ENTITY → RESPONSE
     * =========================================================
     */

    private KnowledgeChunkResponse mapToResponse(
            KnowledgeChunk chunk) {

        KnowledgeChunkResponse response =
                new KnowledgeChunkResponse();


        /*
         * Chunk ID.
         */
        response.setId(
                chunk.getId()
        );


        /*
         * Document ID.
         */
        if (chunk.getDocument() != null) {

            response.setDocumentId(
                    chunk.getDocument().getId()
            );
        }


        /*
         * Chunk index.
         */
        response.setChunkIndex(
                chunk.getChunkIndex()
        );


        /*
         * Content.
         */
        response.setContent(
                chunk.getContent()
        );


        /*
         * Convert float[] to String for API response.
         *
         * Example:
         *
         * [0.12, -0.35, 0.76, ...]
         */
        response.setEmbedding(
                chunk.getEmbedding() != null
                        ? Arrays.toString(
                                chunk.getEmbedding()
                        )
                        : null
        );


        /*
         * Metadata.
         */
        response.setMetadata(
                chunk.getMetadata()
        );


        /*
         * Created timestamp.
         */
        response.setCreatedAt(
                chunk.getCreatedAt()
        );


        return response;
    }
}