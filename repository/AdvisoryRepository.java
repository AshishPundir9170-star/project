package com.sih26132.repository;

import com.sih26132.entity.Advisory;
import com.sih26132.entity.Case;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdvisoryRepository
        extends JpaRepository<Advisory, UUID> {

    List<Advisory> findByCaseEntity(
            Case caseEntity
    );

    List<Advisory> findByCaseEntityAndLanguage(
            Case caseEntity,
            String language
    );

    List<Advisory> findByCaseEntityAndStatus(
            Case caseEntity,
            String status
    );

    Optional<Advisory> findByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );

    List<Advisory> findByCaseEntityOrderByCreatedAtDesc(
            Case caseEntity
    );

    List<Advisory> findByStatus(
            String status
    );
}