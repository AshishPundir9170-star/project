package com.sih26132.controller;

import com.sih26132.dto.AuditLogCreateRequest;
import com.sih26132.dto.AuditLogResponse;
import com.sih26132.service.AuditLogService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<AuditLogResponse> createAuditLog(
            @Valid @RequestBody AuditLogCreateRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auditLogService.createAuditLog(request));
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs()
        );
    }

    @GetMapping("/{auditLogId}")
    public ResponseEntity<AuditLogResponse> getAuditLogById(
            @PathVariable UUID auditLogId
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogById(auditLogId)
        );
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByAction(
            @PathVariable String action
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByAction(action)
        );
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable UUID entityId
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByEntity(
                        entityType,
                        entityId
                )
        );
    }

    @GetMapping("/actor/{actorUserId}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByActor(
            @PathVariable UUID actorUserId
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByActor(actorUserId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByUser(
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByUser(userId)
        );
    }
}