package com.sih26132.service;

import com.sih26132.dto.knowledge.KnowledgeDocumentCreateRequest;
import com.sih26132.dto.knowledge.KnowledgeDocumentResponse;
import com.sih26132.entity.KnowledgeDocument;
import com.sih26132.repository.KnowledgeDocumentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class KnowledgeDocumentService {

    private final KnowledgeDocumentRepository
            knowledgeDocumentRepository;

    public KnowledgeDocumentService(
            KnowledgeDocumentRepository knowledgeDocumentRepository) {

        this.knowledgeDocumentRepository =
                knowledgeDocumentRepository;
    }

    // CREATE
    public KnowledgeDocumentResponse createDocument(
            KnowledgeDocumentCreateRequest request) {

        OffsetDateTime now = OffsetDateTime.now();

        boolean active = request.getActive() == null
                || request.getActive();

        KnowledgeDocument document =
                KnowledgeDocument.builder()
                        .title(request.getTitle())
                        .source(request.getSource())
                        .sourceUrl(request.getSourceUrl())
                        .publisher(request.getPublisher())
                        .documentType(request.getDocumentType())
                        .language(request.getLanguage())
                        .version(request.getVersion())
                        .checksum(request.getChecksum())
                        .active(active)
                        .metadata(request.getMetadata())
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

        KnowledgeDocument saved =
                knowledgeDocumentRepository.save(document);

        return mapToResponse(saved);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> getAllDocuments() {

        return knowledgeDocumentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET ACTIVE
    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> getActiveDocuments() {

        return knowledgeDocumentRepository
                .findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET ONE
    @Transactional(readOnly = true)
    public KnowledgeDocumentResponse getDocument(
            UUID documentId) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found"
                                ));

        return mapToResponse(document);
    }

    // GET BY LANGUAGE
    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> getByLanguage(
            String language) {

        return knowledgeDocumentRepository
                .findByLanguage(language)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET ACTIVE BY LANGUAGE
    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse>
            getActiveByLanguage(String language) {

        return knowledgeDocumentRepository
                .findByActiveTrueAndLanguage(language)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY DOCUMENT TYPE
    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> getByDocumentType(
            String documentType) {

        return knowledgeDocumentRepository
                .findByDocumentType(documentType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY SOURCE
    @Transactional(readOnly = true)
    public List<KnowledgeDocumentResponse> getBySource(
            String source) {

        return knowledgeDocumentRepository
                .findBySource(source)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // UPDATE
    public KnowledgeDocumentResponse updateDocument(
            UUID documentId,
            KnowledgeDocumentCreateRequest request) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found"
                                ));

        document.setTitle(request.getTitle());
        document.setSource(request.getSource());
        document.setSourceUrl(request.getSourceUrl());
        document.setPublisher(request.getPublisher());
        document.setDocumentType(request.getDocumentType());
        document.setLanguage(request.getLanguage());
        document.setVersion(request.getVersion());
        document.setChecksum(request.getChecksum());

        if (request.getActive() != null) {
            document.setActive(request.getActive());
        }

        document.setMetadata(request.getMetadata());
        document.setUpdatedAt(OffsetDateTime.now());

        KnowledgeDocument updated =
                knowledgeDocumentRepository.save(document);

        return mapToResponse(updated);
    }

    // DELETE
    public void deleteDocument(UUID documentId) {

        KnowledgeDocument document =
                knowledgeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Knowledge document not found"
                                ));

        knowledgeDocumentRepository.delete(document);
    }

    // ENTITY → RESPONSE
    private KnowledgeDocumentResponse mapToResponse(
            KnowledgeDocument document) {

        KnowledgeDocumentResponse response =
                new KnowledgeDocumentResponse();

        response.setId(document.getId());
        response.setTitle(document.getTitle());
        response.setSource(document.getSource());
        response.setSourceUrl(document.getSourceUrl());
        response.setPublisher(document.getPublisher());
        response.setDocumentType(document.getDocumentType());
        response.setLanguage(document.getLanguage());
        response.setVersion(document.getVersion());
        response.setChecksum(document.getChecksum());
        response.setActive(document.isActive());
        response.setMetadata(document.getMetadata());
        response.setCreatedAt(document.getCreatedAt());
        response.setUpdatedAt(document.getUpdatedAt());

        return response;
    }
}