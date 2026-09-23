package com.sih26132.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
public class NotificationCreateRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    private UUID relatedCaseId;

    private UUID caseId;

    @NotBlank(message = "Notification type is required")
    private String type;

    @NotBlank(message = "Notification title is required")
    private String title;

    @NotBlank(message = "Notification body is required")
    private String body;

    @NotBlank(message = "Notification channel is required")
    private String channel;

    private String status;

    private OffsetDateTime scheduledAt;

    private String message;
}