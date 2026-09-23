package com.sih26132.service;

import com.sih26132.dto.NotificationCreateRequest;
import com.sih26132.dto.NotificationResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.Notification;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.NotificationRepository;
import com.sih26132.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CaseRepository caseRepository;

    public NotificationResponse createNotification(
            NotificationCreateRequest request
    ) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Notification request is required"
            );
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Case relatedCase = null;
        Case caseEntity = null;

        if (request.getRelatedCaseId() != null) {
            relatedCase = caseRepository.findById(
                    request.getRelatedCaseId()
            ).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Related case not found"
            ));
        }

        if (request.getCaseId() != null) {
            caseEntity = caseRepository.findById(
                    request.getCaseId()
            ).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Case not found"
            ));
        }

        OffsetDateTime now = OffsetDateTime.now();

        String status = request.getStatus();

        if (status == null || status.isBlank()) {
            status = "PENDING";
        }

        Notification notification = Notification.builder()
                .user(user)
                .relatedCase(relatedCase)
                .caseEntity(caseEntity)
                .type(request.getType())
                .title(request.getTitle())
                .body(request.getBody())
                .channel(request.getChannel())
                .status(status)
                .scheduledAt(request.getScheduledAt())
                .createdAt(now)
                .message(request.getMessage())
                .read(false)
                .build();

        Notification savedNotification =
                notificationRepository.save(notification);

        return mapToResponse(savedNotification);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(
            UUID notificationId,
            UUID userId
    ) {

        User user = getUser(userId);

        Notification notification =
                notificationRepository
                        .findByIdAndUser(notificationId, user)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Notification not found"
                        ));

        return mapToResponse(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(
            UUID userId
    ) {

        User user = getUser(userId);

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(
            UUID userId
    ) {

        User user = getUser(userId);

        return notificationRepository
                .findByUserAndReadOrderByCreatedAtDesc(
                        user,
                        false
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countUnreadNotifications(
            UUID userId
    ) {

        User user = getUser(userId);

        return notificationRepository.countByUserAndRead(
                user,
                false
        );
    }

    public NotificationResponse markAsRead(
            UUID notificationId,
            UUID userId
    ) {

        User user = getUser(userId);

        Notification notification =
                notificationRepository
                        .findByIdAndUser(notificationId, user)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Notification not found"
                        ));

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(OffsetDateTime.now());

            if ("PENDING".equalsIgnoreCase(notification.getStatus())
                    || "SENT".equalsIgnoreCase(notification.getStatus())) {
                notification.setStatus("READ");
            }
        }

        Notification savedNotification =
                notificationRepository.save(notification);

        return mapToResponse(savedNotification);
    }

    public int markAllAsRead(
            UUID userId
    ) {

        User user = getUser(userId);

        List<Notification> notifications =
                notificationRepository
                        .findByUserAndRead(
                                user,
                                false
                        );

        OffsetDateTime now = OffsetDateTime.now();

        for (Notification notification : notifications) {

            notification.setRead(true);
            notification.setReadAt(now);

            if ("PENDING".equalsIgnoreCase(notification.getStatus())
                    || "SENT".equalsIgnoreCase(notification.getStatus())) {
                notification.setStatus("READ");
            }
        }

        notificationRepository.saveAll(notifications);

        return notifications.size();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByCase(
            UUID caseId,
            UUID userId
    ) {

        User user = getUser(userId);

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Case not found"
                ));

        return notificationRepository
                .findByRelatedCase(caseEntity)
                .stream()
                .filter(notification ->
                        notification.getUser() != null
                                && notification.getUser()
                                .getId()
                                .equals(user.getId()))
                .map(this::mapToResponse)
                .toList();
    }

    private User getUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    private NotificationResponse mapToResponse(
            Notification notification
    ) {

        UUID userId = notification.getUser() != null
                ? notification.getUser().getId()
                : null;

        UUID relatedCaseId =
                notification.getRelatedCase() != null
                        ? notification.getRelatedCase().getId()
                        : null;

        UUID caseId =
                notification.getCaseEntity() != null
                        ? notification.getCaseEntity().getId()
                        : null;

        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(userId)
                .relatedCaseId(relatedCaseId)
                .caseId(caseId)
                .type(notification.getType())
                .title(notification.getTitle())
                .body(notification.getBody())
                .channel(notification.getChannel())
                .status(notification.getStatus())
                .scheduledAt(notification.getScheduledAt())
                .sentAt(notification.getSentAt())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .message(notification.getMessage())
                .read(notification.isRead())
                .build();
    }
}