package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.ExpertReview;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpertReviewRepository extends JpaRepository<ExpertReview, UUID> {

    List<ExpertReview> findByCaseEntity(Case caseEntity);

    List<ExpertReview> findByExpertUser(User expertUser);

    Optional<ExpertReview> findByIdAndExpertUser(
            UUID id,
            User expertUser
    );

    List<ExpertReview> findByCaseEntityOrderByReviewedAtDesc(
            Case caseEntity
    );

    List<ExpertReview> findByExpertUserOrderByReviewedAtDesc(
            User expertUser
    );

    List<ExpertReview> findByDecision(
            String decision
    );
}