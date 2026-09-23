package com.sih26132.service;

import com.sih26132.dto.AuditLogCreateRequest;
import com.sih26132.dto.AuditLogResponse;
import com.sih26132.entity.AuditLog;
import com.sih26132.entity.User;
import com.sih26132.repository.AuditLogRepository;
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
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogResponse createAuditLog(
            AuditLogCreateRequest request
    ) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Audit log request is required"
            );
        }

        User actorUser = null;
        User user = null;

        if (request.getActorUserId() != null) {
            actorUser = userRepository.findById(
                    request.getActorUserId()
            ).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Actor user not found"
            ));
        }

        if (request.getUserId() != null) {
            user = userRepository.findById(
                    request.getUserId()
            ).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            ));
        }

        AuditLog auditLog = AuditLog.builder()
                .actorUser(actorUser)
                .action(request.getAction())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .details(request.getDetails())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .createdAt(OffsetDateTime.now())
                .user(user)
                .build();

        AuditLog savedAuditLog =
                auditLogRepository.save(auditLog);

        return mapToResponse(savedAuditLog);
    }

    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLogById(
            UUID auditLogId
    ) {

        AuditLog auditLog =
                auditLogRepository.findById(auditLogId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Audit log not found"
                        ));

        return mapToResponse(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAllAuditLogs() {

        return auditLogRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByAction(
            String action
    ) {

        return auditLogRepository
                .findByAction(action)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByEntity(
            String entityType,
            UUID entityId
    ) {

        return auditLogRepository
                .findByEntityTypeAndEntityId(
                        entityType,
                        entityId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByActor(
            UUID actorUserId
    ) {

        User actorUser = userRepository.findById(actorUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actor user not found"
                ));

        return auditLogRepository
                .findByActorUserOrderByCreatedAtDesc(actorUser)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByUser(
            UUID userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return auditLogRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AuditLogResponse mapToResponse(
            AuditLog auditLog
    ) {

        UUID actorUserId =
                auditLog.getActorUser() != null
                        ? auditLog.getActorUser().getId()
                        : null;

        UUID userId =
                auditLog.getUser() != null
                        ? auditLog.getUser().getId()
                        : null;

        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .actorUserId(actorUserId)
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .details(auditLog.getDetails())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .createdAt(auditLog.getCreatedAt())
                .userId(userId)
                .build();
    }
}