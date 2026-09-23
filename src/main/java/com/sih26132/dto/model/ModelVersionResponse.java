package com.sih26132.dto.model;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class ModelVersionResponse {

    private UUID id;

    private String modelName;

    private String modelType;

    private String version;

    private String artifactUri;

    private String framework;

    private String metrics;

    private Boolean active;

    private OffsetDateTime deployedAt;

    private OffsetDateTime createdAt;
}