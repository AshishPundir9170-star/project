package com.sih26132.repository;

import com.sih26132.entity.KnowledgeChunk;
import com.sih26132.entity.KnowledgeDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KnowledgeChunkRepository
        extends JpaRepository<KnowledgeChunk, UUID> {

    List<KnowledgeChunk> findByDocument(
            KnowledgeDocument document
    );

    Optional<KnowledgeChunk> findByIdAndDocument(
            UUID id,
            KnowledgeDocument document
    );

    List<KnowledgeChunk> findByDocumentOrderByChunkIndexAsc(
            KnowledgeDocument document
    );

    Optional<KnowledgeChunk> findByDocumentAndChunkIndex(
            KnowledgeDocument document,
            Integer chunkIndex
    );

    void deleteByDocument(
            KnowledgeDocument document
    );

    /**
     * Finds the most similar knowledge chunks using
     * PostgreSQL pgvector cosine distance.
     *
     * The embedding parameter must be in pgvector format:
     * [0.1,0.2,0.3,...]
     *
     * The returned Object[] contains:
     *
     * [0] chunk id
     * [1] document id
     * [2] chunk index
     * [3] content
     * [4] metadata
     * [5] similarity score
     */
    @Query(value = """
            SELECT
                kc.id,
                kc.document_id,
                kc.chunk_index,
                kc.content,
                kc.metadata,
                1 - (kc.embedding <=> CAST(:embedding AS vector))
                    AS similarity_score
            FROM knowledge_chunks kc
            JOIN knowledge_documents kd
                ON kd.id = kc.document_id
            WHERE kc.embedding IS NOT NULL
              AND kd.is_active = true
            ORDER BY kc.embedding <=> CAST(:embedding AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findSimilarChunks(
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}