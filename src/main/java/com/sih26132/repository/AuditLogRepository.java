package com.sih26132.repository;

import com.sih26132.entity.AuditLog;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByActorUser(
            User actorUser
    );

    List<AuditLog> findByUser(
            User user
    );

    List<AuditLog> findByActorUserOrderByCreatedAtDesc(
            User actorUser
    );

    List<AuditLog> findByUserOrderByCreatedAtDesc(
            User user
    );

    List<AuditLog> findByAction(
            String action
    );

    List<AuditLog> findByEntityType(
            String entityType
    );

    List<AuditLog> findByEntityId(
            UUID entityId
    );

    List<AuditLog> findByEntityTypeAndEntityId(
            String entityType,
            UUID entityId
    );

    List<AuditLog> findByCreatedAtAfter(
            OffsetDateTime time
    );

    List<AuditLog> findByActionAndCreatedAtAfter(
            String action,
            OffsetDateTime time
    );
}