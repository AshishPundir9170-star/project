package com.sih26132.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
    name = "knowledge_chunks",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_document_chunk",
            columnNames = {
                "document_id",
                "chunk_index"
            }
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "document_id",
        nullable = false
    )
    private KnowledgeDocument document;

    @Column(
        name = "chunk_index",
        nullable = false
    )
    private Integer chunkIndex;

    @Column(
        nullable = false,
        columnDefinition = "TEXT"
    )
    private String content;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    @Column(
        name = "embedding",
        columnDefinition = "vector(1536)"
    )
    private float[] embedding;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
        columnDefinition = "jsonb"
    )
    private Map<String, Object> metadata;

    @Column(
        name = "created_at",
        nullable = false
    )
    private OffsetDateTime createdAt;
}