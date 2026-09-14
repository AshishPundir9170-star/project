package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.CaseImage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaseImageRepository
        extends JpaRepository<CaseImage, UUID> {

    List<CaseImage> findByCaseEntity(Case caseEntity);

    Optional<CaseImage> findByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );

    boolean existsByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );
}