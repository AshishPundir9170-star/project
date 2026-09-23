package com.sih26132.repository;

import com.sih26132.entity.KnowledgeDocument;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KnowledgeDocumentRepository
        extends JpaRepository<KnowledgeDocument, UUID> {

    List<KnowledgeDocument> findByActiveTrue();

    List<KnowledgeDocument> findByLanguage(String language);

    List<KnowledgeDocument> findByDocumentType(String documentType);

    List<KnowledgeDocument> findBySource(String source);

    List<KnowledgeDocument> findByActiveTrueAndLanguage(
            String language
    );
}