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
public class NotificationResponse {

    private UUID id;

    private UUID userId;

    private UUID relatedCaseId;

    private UUID caseId;

    private String type;

    private String title;

    private String body;

    private String channel;

    private String status;

    private OffsetDateTime scheduledAt;

    private OffsetDateTime sentAt;

    private OffsetDateTime readAt;

    private OffsetDateTime createdAt;

    private String message;

    private boolean read;
}