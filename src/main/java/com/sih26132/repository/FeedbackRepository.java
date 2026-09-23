package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.Feedback;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    List<Feedback> findByCaseEntity(Case caseEntity);

    List<Feedback> findByUser(User user);

    Optional<Feedback> findByIdAndUser(
            UUID id,
            User user
    );

    List<Feedback> findByCaseEntityOrderByCreatedAtDesc(
            Case caseEntity
    );

    List<Feedback> findByUserOrderByCreatedAtDesc(
            User user
    );

    List<Feedback> findByFeedbackType(
            String feedbackType
    );

    List<Feedback> findByModelWasCorrect(
            Boolean modelWasCorrect
    );
}