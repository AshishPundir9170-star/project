package com.sih26132.controller;

import com.sih26132.dto.knowledge.RagRetrievalRequest;
import com.sih26132.dto.knowledge.RagRetrievalResponse;
import com.sih26132.service.RagContext;
import com.sih26132.service.RagRetrievalService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagRetrievalController {

    private final RagRetrievalService ragRetrievalService;

    @PostMapping("/retrieve")
    public ResponseEntity<RagRetrievalResponse> retrieve(
            @Valid @RequestBody RagRetrievalRequest request) {

        RagContext context =
                ragRetrievalService.retrieve(
                        request.getQuery(),
                        request.getLimit()
                );

        RagRetrievalResponse response =
                new RagRetrievalResponse(
                        context.getQuery(),
                        context.getResults().size(),
                        context.getResults(),
                        context.getCombinedContext()
                );

        return ResponseEntity.ok(response);
    }
}