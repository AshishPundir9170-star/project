package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, UUID> {

    List<Diagnosis> findByCaseEntity(Case caseEntity);

    Optional<Diagnosis> findByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );

    boolean existsByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );
}