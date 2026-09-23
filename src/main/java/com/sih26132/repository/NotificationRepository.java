package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.Notification;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findByUser(User user);

    Optional<Notification> findByIdAndUser(
            UUID id,
            User user
    );

    List<Notification> findByUserOrderByCreatedAtDesc(
            User user
    );

    List<Notification> findByUserAndRead(
            User user,
            boolean read
    );

    List<Notification> findByUserAndReadOrderByCreatedAtDesc(
            User user,
            boolean read
    );

    List<Notification> findByRelatedCase(
            Case relatedCase
    );

    List<Notification> findByCaseEntity(
            Case caseEntity
    );

    List<Notification> findByStatus(
            String status
    );

    List<Notification> findByStatusAndScheduledAtBefore(
            String status,
            OffsetDateTime time
    );

    long countByUserAndRead(
            User user,
            boolean read
    );
}