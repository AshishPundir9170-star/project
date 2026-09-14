package com.sih26132.controller;

import com.sih26132.dto.knowledge.KnowledgeChunkCreateRequest;
import com.sih26132.dto.knowledge.KnowledgeChunkResponse;
import com.sih26132.service.KnowledgeChunkService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        "/api/knowledge-documents/{documentId}/chunks"
)
@RequiredArgsConstructor
public class KnowledgeChunkController {


    private final KnowledgeChunkService knowledgeChunkService;


    /*
     * =========================================================
     * CREATE CHUNK
     * =========================================================
     */

    @PostMapping
    public ResponseEntity<KnowledgeChunkResponse> createChunk(
            @PathVariable UUID documentId,
            @Valid @RequestBody
            KnowledgeChunkCreateRequest request) {

        KnowledgeChunkResponse response =
                knowledgeChunkService.createChunk(
                        documentId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /*
     * =========================================================
     * GET ALL CHUNKS
     * =========================================================
     */

    @GetMapping
    public ResponseEntity<List<KnowledgeChunkResponse>>
            getDocumentChunks(
                    @PathVariable UUID documentId) {

        return ResponseEntity.ok(
                knowledgeChunkService
                        .getDocumentChunks(
                                documentId
                        )
        );
    }


    /*
     * =========================================================
     * GET CHUNK BY INDEX
     * =========================================================
     */

    @GetMapping("/index/{chunkIndex}")
    public ResponseEntity<KnowledgeChunkResponse>
            getChunkByIndex(
                    @PathVariable UUID documentId,
                    @PathVariable Integer chunkIndex) {

        return ResponseEntity.ok(
                knowledgeChunkService
                        .getChunkByIndex(
                                documentId,
                                chunkIndex
                        )
        );
    }


    /*
     * =========================================================
     * GET CHUNK BY ID
     * =========================================================
     */

    @GetMapping("/{chunkId}")
    public ResponseEntity<KnowledgeChunkResponse>
            getChunk(
                    @PathVariable UUID documentId,
                    @PathVariable UUID chunkId) {

        return ResponseEntity.ok(
                knowledgeChunkService.getChunk(
                        documentId,
                        chunkId
                )
        );
    }


    /*
     * =========================================================
     * UPDATE CHUNK
     * =========================================================
     */

    @PutMapping("/{chunkId}")
    public ResponseEntity<KnowledgeChunkResponse>
            updateChunk(
                    @PathVariable UUID documentId,
                    @PathVariable UUID chunkId,
                    @Valid @RequestBody
                    KnowledgeChunkCreateRequest request) {

        return ResponseEntity.ok(
                knowledgeChunkService.updateChunk(
                        documentId,
                        chunkId,
                        request
                )
        );
    }


    /*
     * =========================================================
     * DELETE CHUNK
     * =========================================================
     */

    @DeleteMapping("/{chunkId}")
    public ResponseEntity<Void> deleteChunk(
            @PathVariable UUID documentId,
            @PathVariable UUID chunkId) {

        knowledgeChunkService.deleteChunk(
                documentId,
                chunkId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}