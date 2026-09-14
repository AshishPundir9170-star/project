package com.sih26132.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "model_versions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_model_version",
            columnNames = {"model_name", "version"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "model_name", nullable = false, length = 150)
    private String modelName;

    @Column(name = "model_type", nullable = false, length = 100)
    private String modelType;

    @Column(nullable = false, length = 100)
    private String version;

    @Column(name = "artifact_uri", length = 1000)
    private String artifactUri;

    @Column(length = 100)
    private String framework;

    @Column(columnDefinition = "JSONB")
    private String metrics;

    @Column(name = "is_active", nullable = false)
    private boolean active = false;

    @Column(name = "deployed_at")
    private OffsetDateTime deployedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}