package com.sih26132.controller;

import com.sih26132.dto.NotificationCreateRequest;
import com.sih26132.dto.NotificationResponse;
import com.sih26132.entity.User;
import com.sih26132.repository.UserRepository;
import com.sih26132.service.NotificationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // ============================================================
    // CREATE NOTIFICATION
    // ============================================================

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationCreateRequest request
    ) {

        NotificationResponse response =
                notificationService.createNotification(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET NOTIFICATION BY ID
    // ============================================================

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotificationById(
            @PathVariable UUID notificationId,
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getNotificationById(
                        notificationId,
                        userId
                )
        );
    }

    // ============================================================
    // GET MY NOTIFICATIONS
    // ============================================================

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getMyNotifications(userId)
        );
    }

    // ============================================================
    // GET UNREAD NOTIFICATIONS
    // ============================================================

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId)
        );
    }

    // ============================================================
    // COUNT UNREAD NOTIFICATIONS
    // ============================================================

    @GetMapping("/unread/count")
    public ResponseEntity<Long> countUnreadNotifications(
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.countUnreadNotifications(userId)
        );
    }

    // ============================================================
    // MARK ONE NOTIFICATION AS READ
    // ============================================================

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable UUID notificationId,
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.markAsRead(
                        notificationId,
                        userId
                )
        );
    }

    // ============================================================
    // MARK ALL NOTIFICATIONS AS READ
    // ============================================================

    @PatchMapping("/read-all")
    public ResponseEntity<Integer> markAllAsRead(
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.markAllAsRead(userId)
        );
    }

    // ============================================================
    // GET NOTIFICATIONS BY CASE
    // ============================================================

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByCase(
            @PathVariable UUID caseId,
            Authentication authentication
    ) {

        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                notificationService.getNotificationsByCase(
                        caseId,
                        userId
                )
        );
    }

    // ============================================================
    // RESOLVE AUTHENTICATED USER ID
    // ============================================================

    private UUID getAuthenticatedUserId(
            Authentication authentication
    ) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new org.springframework.web.server.ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Authenticated user not found"
                        )
                );

        return user.getId();
    }
}