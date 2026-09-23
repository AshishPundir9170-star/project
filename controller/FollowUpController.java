package com.sih26132.controller;

import com.sih26132.dto.FollowUpCreateRequest;
import com.sih26132.dto.FollowUpResponse;
import com.sih26132.service.FollowUpService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/follow-ups")
@RequiredArgsConstructor
public class FollowUpController {

    private final FollowUpService followUpService;

    @PostMapping
    public ResponseEntity<FollowUpResponse> createFollowUp(
            @Valid @RequestBody FollowUpCreateRequest request,
            Authentication authentication
    ) {

        UUID farmerUserId =
                getAuthenticatedUserId(authentication);

        FollowUpResponse response =
                followUpService.createFollowUp(
                        request,
                        farmerUserId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{followUpId}")
    public ResponseEntity<FollowUpResponse> getFollowUpById(
            @PathVariable UUID followUpId,
            Authentication authentication
    ) {

        UUID farmerUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                followUpService.getFollowUpById(
                        followUpId,
                        farmerUserId
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<FollowUpResponse>> getMyFollowUps(
            Authentication authentication
    ) {

        UUID farmerUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                followUpService.getMyFollowUps(
                        farmerUserId
                )
        );
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<FollowUpResponse>> getFollowUpsByCase(
            @PathVariable UUID caseId,
            Authentication authentication
    ) {

        UUID farmerUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                followUpService.getFollowUpsByCase(
                        caseId,
                        farmerUserId
                )
        );
    }

    @PatchMapping("/{followUpId}/complete")
    public ResponseEntity<FollowUpResponse> completeFollowUp(
            @PathVariable UUID followUpId,
            @RequestParam(required = false) String outcome,
            Authentication authentication
    ) {

        UUID farmerUserId =
                getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                followUpService.completeFollowUp(
                        followUpId,
                        farmerUserId,
                        outcome
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