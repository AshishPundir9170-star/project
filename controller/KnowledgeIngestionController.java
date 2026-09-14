package com.sih26132.controller;

import com.sih26132.dto.knowledge.KnowledgeIngestionRequest;
import com.sih26132.dto.knowledge.KnowledgeIngestionResponse;
import com.sih26132.service.KnowledgeIngestionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/knowledge-documents")
@RequiredArgsConstructor
public class KnowledgeIngestionController {

    private final KnowledgeIngestionService knowledgeIngestionService;

    @PostMapping("/{documentId}/ingest")
    public ResponseEntity<KnowledgeIngestionResponse> ingestDocument(
            @PathVariable UUID documentId,
            @Valid @RequestBody KnowledgeIngestionRequest request) {

        int chunksCreated =
                knowledgeIngestionService.ingestDocument(
                        documentId,
                        request.getText()
                );

        KnowledgeIngestionResponse response =
                new KnowledgeIngestionResponse(
                        documentId,
                        chunksCreated,
                        "Knowledge document ingested successfully"
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}