package com.sih26132.controller;

import com.sih26132.dto.knowledge.KnowledgeSearchRequest;
import com.sih26132.service.KnowledgeSearchService;
import com.sih26132.service.KnowledgeSearchService.KnowledgeSearchResult;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeSearchController {

    private final KnowledgeSearchService knowledgeSearchService;

    /**
     * Performs vector similarity search over active
     * knowledge chunks.
     *
     * POST /api/knowledge/search
     */
    @PostMapping("/search")
    public ResponseEntity<List<KnowledgeSearchResult>> search(
            @Valid @RequestBody KnowledgeSearchRequest request) {

        List<KnowledgeSearchResult> results =
                knowledgeSearchService.search(
                        request.getEmbedding(),
                        request.getLimit()
                );

        return ResponseEntity.ok(results);
    }
}