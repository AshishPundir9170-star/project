package com.sih26132.service;

import com.sih26132.dto.FollowUpCreateRequest;
import com.sih26132.dto.FollowUpResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.FollowUp;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.FollowUpRepository;
import com.sih26132.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    public FollowUpResponse createFollowUp(
            FollowUpCreateRequest request,
            UUID farmerUserId
    ) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Follow-up request is required"
            );
        }

        User farmerUser = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmer user not found"
                ));

        Case caseEntity = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Case not found"
                ));

        if (caseEntity.getCreatedByUser() == null
                || !caseEntity.getCreatedByUser()
                        .getId()
                        .equals(farmerUser.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to create a follow-up for this case"
            );
        }

        OffsetDateTime now = OffsetDateTime.now();

        FollowUp followUp = FollowUp.builder()
                .caseEntity(caseEntity)
                .farmerUser(farmerUser)
                .scheduledAt(request.getScheduledAt())
                .status("SCHEDULED")
                .notes(request.getNotes())
                .nextAction(request.getNextAction())
                .createdAt(now)
                .build();

        FollowUp savedFollowUp =
                followUpRepository.save(followUp);

        return mapToResponse(savedFollowUp);
    }

    @Transactional(readOnly = true)
    public FollowUpResponse getFollowUpById(
            UUID followUpId,
            UUID farmerUserId
    ) {

        User farmerUser = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmer user not found"
                ));

        FollowUp followUp = followUpRepository
                .findByIdAndFarmerUser(followUpId, farmerUser)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Follow-up not found"
                ));

        return mapToResponse(followUp);
    }

    @Transactional(readOnly = true)
    public List<FollowUpResponse> getMyFollowUps(
            UUID farmerUserId
    ) {

        User farmerUser = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmer user not found"
                ));

        return followUpRepository
                .findByFarmerUserOrderByScheduledAtAsc(farmerUser)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowUpResponse> getFollowUpsByCase(
            UUID caseId,
            UUID farmerUserId
    ) {

        User farmerUser = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmer user not found"
                ));

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Case not found"
                ));

        if (caseEntity.getCreatedByUser() == null
                || !caseEntity.getCreatedByUser()
                        .getId()
                        .equals(farmerUser.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to access follow-ups for this case"
            );
        }

        return followUpRepository
                .findByCaseEntityOrderByScheduledAtAsc(caseEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FollowUpResponse completeFollowUp(
            UUID followUpId,
            UUID farmerUserId,
            String outcome
    ) {

        User farmerUser = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmer user not found"
                ));

        FollowUp followUp = followUpRepository
                .findByIdAndFarmerUser(followUpId, farmerUser)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Follow-up not found"
                ));

        followUp.setStatus("COMPLETED");
        followUp.setCompletedAt(OffsetDateTime.now());
        followUp.setOutcome(outcome);

        FollowUp savedFollowUp =
                followUpRepository.save(followUp);

        return mapToResponse(savedFollowUp);
    }

    private FollowUpResponse mapToResponse(
            FollowUp followUp
    ) {

        UUID caseId = followUp.getCaseEntity() != null
                ? followUp.getCaseEntity().getId()
                : null;

        UUID farmerUserId = followUp.getFarmerUser() != null
                ? followUp.getFarmerUser().getId()
                : null;

        return FollowUpResponse.builder()
                .id(followUp.getId())
                .caseId(caseId)
                .farmerUserId(farmerUserId)
                .scheduledAt(followUp.getScheduledAt())
                .completedAt(followUp.getCompletedAt())
                .status(followUp.getStatus())
                .notes(followUp.getNotes())
                .outcome(followUp.getOutcome())
                .nextAction(followUp.getNextAction())
                .createdAt(followUp.getCreatedAt())
                .build();
    }
}