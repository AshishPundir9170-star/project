package com.sih26132.repository;

import com.sih26132.entity.Case;
import com.sih26132.entity.FollowUp;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowUpRepository extends JpaRepository<FollowUp, UUID> {

    List<FollowUp> findByCaseEntity(Case caseEntity);

    List<FollowUp> findByFarmerUser(User farmerUser);

    Optional<FollowUp> findByIdAndFarmerUser(
            UUID id,
            User farmerUser
    );

    List<FollowUp> findByCaseEntityOrderByScheduledAtAsc(
            Case caseEntity
    );

    List<FollowUp> findByFarmerUserOrderByScheduledAtAsc(
            User farmerUser
    );

    List<FollowUp> findByStatus(
            String status
    );

    List<FollowUp> findByStatusAndScheduledAtBefore(
            String status,
            OffsetDateTime time
    );

    List<FollowUp> findByFarmerUserAndStatus(
            User farmerUser,
            String status
    );
}