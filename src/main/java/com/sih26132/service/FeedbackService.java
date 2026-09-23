package com.sih26132.service;

import com.sih26132.dto.FeedbackCreateRequest;
import com.sih26132.dto.FeedbackResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.Feedback;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.FeedbackRepository;
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
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    /**
     * Create feedback for the authenticated user.
     */
    public FeedbackResponse createFeedback(
            FeedbackCreateRequest request,
            UUID userId
    ) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Feedback request is required"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Case caseEntity = null;

        if (request.getCaseId() != null) {

            caseEntity = caseRepository.findById(request.getCaseId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Case not found"
                    ));

            /*
             * A user can submit feedback only for their own case.
             */
            if (caseEntity.getCreatedByUser() == null
                    || !caseEntity.getCreatedByUser()
                            .getId()
                            .equals(user.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You are not allowed to submit feedback for this case"
                );
            }
        }

        if (request.getRating() != null
                && (request.getRating() < 1
                || request.getRating() > 5)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rating must be between 1 and 5"
            );
        }

        Feedback feedback = Feedback.builder()
                .caseEntity(caseEntity)
                .user(user)
                .feedbackType(request.getFeedbackType())
                .rating(request.getRating())
                .comment(request.getComment())
                .verifiedLabel(request.getVerifiedLabel())
                .actualCondition(request.getActualCondition())
                .modelWasCorrect(request.getModelWasCorrect())
                .createdAt(OffsetDateTime.now())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return mapToResponse(savedFeedback);
    }

    /**
     * Get feedback by ID for the authenticated user.
     */
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(
            UUID feedbackId,
            UUID userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Feedback feedback = feedbackRepository
                .findByIdAndUser(feedbackId, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feedback not found"
                ));

        return mapToResponse(feedback);
    }

    /**
     * Get all feedback submitted by the authenticated user.
     */
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getMyFeedback(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return feedbackRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get feedback associated with a case.
     */
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbackByCase(
            UUID caseId,
            UUID userId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Case not found"
                ));

        /*
         * Only the creator of the case can view its feedback.
         */
        if (caseEntity.getCreatedByUser() == null
                || !caseEntity.getCreatedByUser()
                        .getId()
                        .equals(user.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to access feedback for this case"
            );
        }

        return feedbackRepository
                .findByCaseEntityOrderByCreatedAtDesc(caseEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Convert Feedback entity to FeedbackResponse DTO.
     */
    private FeedbackResponse mapToResponse(Feedback feedback) {

        UUID caseId = feedback.getCaseEntity() != null
                ? feedback.getCaseEntity().getId()
                : null;

        UUID userId = feedback.getUser() != null
                ? feedback.getUser().getId()
                : null;

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .caseId(caseId)
                .userId(userId)
                .feedbackType(feedback.getFeedbackType())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .verifiedLabel(feedback.getVerifiedLabel())
                .actualCondition(feedback.getActualCondition())
                .modelWasCorrect(feedback.getModelWasCorrect())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}