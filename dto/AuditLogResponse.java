package com.sih26132.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {

    private UUID id;

    private UUID actorUserId;

    private String action;

    private String entityType;

    private UUID entityId;

    private String details;

    private String ipAddress;

    private String userAgent;

    private OffsetDateTime createdAt;

    private UUID userId;
}