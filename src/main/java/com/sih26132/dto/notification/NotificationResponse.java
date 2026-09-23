package com.sih26132.dto.notification;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class NotificationResponse {

    private UUID id;

    private UUID userId;

    private UUID caseId;

    private String type;

    private String title;

    private String message;

    private Boolean read;

    private OffsetDateTime sentAt;

    private OffsetDateTime createdAt;
}