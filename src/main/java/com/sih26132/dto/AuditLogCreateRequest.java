package com.sih26132.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogCreateRequest {

    private UUID actorUserId;

    @NotBlank(message = "Action is required")
    private String action;

    private String entityType;

    private UUID entityId;

    private String details;

    private String ipAddress;

    private String userAgent;

    private UUID userId;
}