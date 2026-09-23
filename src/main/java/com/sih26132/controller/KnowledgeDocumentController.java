package com.sih26132.controller;

import com.sih26132.dto.knowledge.KnowledgeDocumentCreateRequest;
import com.sih26132.dto.knowledge.KnowledgeDocumentResponse;
import com.sih26132.service.KnowledgeDocumentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/knowledge-documents")
@RequiredArgsConstructor
public class KnowledgeDocumentController {

    private final KnowledgeDocumentService knowledgeDocumentService;

    // CREATE
    @PostMapping
    public ResponseEntity<KnowledgeDocumentResponse> createDocument(
            @Valid @RequestBody KnowledgeDocumentCreateRequest request) {

        KnowledgeDocumentResponse response =
                knowledgeDocumentService.createDocument(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<KnowledgeDocumentResponse>>
            getAllDocuments() {

        return ResponseEntity.ok(
                knowledgeDocumentService.getAllDocuments()
        );
    }

    // GET ACTIVE
    @GetMapping("/active")
    public ResponseEntity<List<KnowledgeDocumentResponse>>
            getActiveDocuments() {

        return ResponseEntity.ok(
                knowledgeDocumentService.getActiveDocuments()
        );
    }

    // GET BY LANGUAGE
    @GetMapping("/language/{language}")
    public ResponseEntity<List<KnowledgeDocumentResponse>>
            getByLanguage(
                    @PathVariable String language) {

        return ResponseEntity.ok(
                knowledgeDocumentService.getByLanguage(language)
        );
    }

    // GET ACTIVE BY LANGUAGE
    @GetMapping("/active/language/{language}")
    public ResponseEntity<List<KnowledgeDocumentResponse>>
            getActiveByLanguage(
                    @PathVariable String language) {

        return ResponseEntity.ok(
                knowledgeDocumentService.getActiveByLanguage(language)
        );
    }

    // GET BY DOCUMENT TYPE
    @GetMapping("/type/{documentType}")
    public ResponseEntity<List<KnowledgeDocumentResponse>>
            getByDocumentType(
                    @PathVariable String documentType) {

        return ResponseEntity.ok(
                knowledgeDocumentService
                        .getByDocumentType(documentType)
        );
    }

    // GET BY SOURCE
    @GetMapping("/source/{source}")
    public ResponseEntity<List<KnowledgeDocumentResponse>>
            getBySource(
                    @PathVariable String source) {

        return ResponseEntity.ok(
                knowledgeDocumentService.getBySource(source)
        );
    }

    // GET ONE
    @GetMapping("/{documentId}")
    public ResponseEntity<KnowledgeDocumentResponse> getDocument(
            @PathVariable UUID documentId) {

        return ResponseEntity.ok(
                knowledgeDocumentService.getDocument(documentId)
        );
    }

    // UPDATE
    @PutMapping("/{documentId}")
    public ResponseEntity<KnowledgeDocumentResponse> updateDocument(
            @PathVariable UUID documentId,
            @Valid @RequestBody KnowledgeDocumentCreateRequest request) {

        return ResponseEntity.ok(
                knowledgeDocumentService.updateDocument(
                        documentId,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable UUID documentId) {

        knowledgeDocumentService.deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}