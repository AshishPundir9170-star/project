package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.RiskPrediction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiskPredictionRepository
        extends JpaRepository<RiskPrediction, UUID> {

    List<RiskPrediction> findByCaseEntity(Case caseEntity);

    Optional<RiskPrediction> findByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );

    boolean existsByIdAndCaseEntity(
            UUID id,
            Case caseEntity
    );
}