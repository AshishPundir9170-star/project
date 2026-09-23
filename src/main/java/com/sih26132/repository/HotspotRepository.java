package com.sih26132.repository;

import com.sih26132.entity.Hotspot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface HotspotRepository
        extends JpaRepository<Hotspot, UUID> {

    List<Hotspot> findByStatus(
            String status
    );

    List<Hotspot> findByConditionName(
            String conditionName
    );

    List<Hotspot> findBySeverity(
            String severity
    );

    List<Hotspot> findByState(
            String state
    );

    List<Hotspot> findByDistrict(
            String district
    );

    List<Hotspot> findByBlock(
            String block
    );

    List<Hotspot> findByStatusOrderByDetectedAtDesc(
            String status
    );

    List<Hotspot> findByDetectedAtAfter(
            OffsetDateTime time
    );

    List<Hotspot> findByExpiresAtBefore(
            OffsetDateTime time
    );

    List<Hotspot> findByStatusAndExpiresAtBefore(
            String status,
            OffsetDateTime time
    );

    List<Hotspot> findByConditionNameAndStatus(
            String conditionName,
            String status
    );
}