package com.sih26132.controller;

import com.sih26132.dto.ExpertReviewCreateRequest;
import com.sih26132.dto.ExpertReviewResponse;
import com.sih26132.service.ExpertReviewService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expert-reviews")
@RequiredArgsConstructor
public class ExpertReviewController {

    private final ExpertReviewService expertReviewService;

    @PostMapping
    public ResponseEntity<ExpertReviewResponse> createReview(
            @Valid @RequestBody ExpertReviewCreateRequest request,
            Authentication authentication
    ) {

        UUID expertUserId =
                getAuthenticatedUserId(authentication);

        ExpertReviewResponse response =
                expertReviewService.createReview(
                        request,
                        expertUserId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ExpertReviewResponse> getReviewById(
            @PathVariable UUID reviewId,
            Authentication authentication
    ) {

        UUID expertUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expertReviewService.getReviewById(
                        reviewId,
                        expertUserId
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<ExpertReviewResponse>> getMyReviews(
            Authentication authentication
    ) {

        UUID expertUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expertReviewService.getMyReviews(
                        expertUserId
                )
        );
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<ExpertReviewResponse>> getReviewsByCase(
            @PathVariable UUID caseId,
            Authentication authentication
    ) {

        UUID expertUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expertReviewService.getReviewsByCase(
                        caseId,
                        expertUserId
                )
        );
    }

    private UUID getAuthenticatedUserId(
            Authentication authentication
    ) {

        if (authentication == null
                || authentication.getPrincipal() == null) {

            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        try {
            return UUID.fromString(
                    authentication.getPrincipal().toString()
            );

        } catch (IllegalArgumentException exception) {

            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid authenticated user"
            );
        }
    }
}