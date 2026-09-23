package com.sih26132.service;

import com.sih26132.dto.ExpertReviewCreateRequest;
import com.sih26132.dto.ExpertReviewResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.ExpertReview;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.ExpertReviewRepository;
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
public class ExpertReviewService {

    private final ExpertReviewRepository expertReviewRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    public ExpertReviewResponse createReview(
            ExpertReviewCreateRequest request,
            UUID expertUserId
    ) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expert review request is required"
            );
        }

        User expertUser = userRepository.findById(expertUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expert user not found"
                ));

        Case caseEntity = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Case not found"
                ));

        OffsetDateTime now = OffsetDateTime.now();

        ExpertReview review = ExpertReview.builder()
                .caseEntity(caseEntity)
                .expertUser(expertUser)
                .decision(request.getDecision())
                .diagnosis(request.getDiagnosis())
                .comments(request.getComments())
                .recommendedAction(request.getRecommendedAction())
                .reviewedAt(now)
                .createdAt(now)
                .build();

        ExpertReview savedReview =
                expertReviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public ExpertReviewResponse getReviewById(
            UUID reviewId,
            UUID expertUserId
    ) {

        User expertUser = userRepository.findById(expertUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expert user not found"
                ));

        ExpertReview review = expertReviewRepository
                .findByIdAndExpertUser(reviewId, expertUser)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expert review not found"
                ));

        return mapToResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ExpertReviewResponse> getMyReviews(
            UUID expertUserId
    ) {

        User expertUser = userRepository.findById(expertUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expert user not found"
                ));

        return expertReviewRepository
                .findByExpertUserOrderByReviewedAtDesc(expertUser)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExpertReviewResponse> getReviewsByCase(
            UUID caseId,
            UUID expertUserId
    ) {

        User expertUser = userRepository.findById(expertUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expert user not found"
                ));

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Case not found"
                ));

        return expertReviewRepository
                .findByCaseEntityOrderByReviewedAtDesc(caseEntity)
                .stream()
                .filter(review ->
                        review.getExpertUser() != null
                                && review.getExpertUser()
                                .getId()
                                .equals(expertUser.getId()))
                .map(this::mapToResponse)
                .toList();
    }

    private ExpertReviewResponse mapToResponse(
            ExpertReview review
    ) {

        UUID caseId = review.getCaseEntity() != null
                ? review.getCaseEntity().getId()
                : null;

        UUID expertUserId = review.getExpertUser() != null
                ? review.getExpertUser().getId()
                : null;

        return ExpertReviewResponse.builder()
                .id(review.getId())
                .caseId(caseId)
                .expertUserId(expertUserId)
                .decision(review.getDecision())
                .diagnosis(review.getDiagnosis())
                .comments(review.getComments())
                .recommendedAction(review.getRecommendedAction())
                .reviewedAt(review.getReviewedAt())
                .createdAt(review.getCreatedAt())
                .build();
    }
}