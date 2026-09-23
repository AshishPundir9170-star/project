package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.CropCycle;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaseRepository extends JpaRepository<Case, UUID> {

    List<Case> findByCreatedByUser(User user);

    Optional<Case> findByIdAndCreatedByUser(
            UUID id,
            User user
    );

    List<Case> findByCropCycle(CropCycle cropCycle);

    List<Case> findByStatus(String status);

    Optional<Case> findByCaseNumber(String caseNumber);

    boolean existsByCaseNumber(String caseNumber);
}