package com.sih26132.controller;

import com.sih26132.dto.cases.CaseImageResponse;
import com.sih26132.service.CaseImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cases/{caseId}/images")
@RequiredArgsConstructor
public class CaseImageController {

    private final CaseImageService caseImageService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CaseImageResponse> uploadImage(
            @PathVariable UUID caseId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        CaseImageResponse response =
                caseImageService.uploadImage(
                        caseId,
                        file,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CaseImageResponse>> getCaseImages(
            @PathVariable UUID caseId,
            Authentication authentication) {

        List<CaseImageResponse> images =
                caseImageService.getCaseImages(
                        caseId,
                        authentication.getName()
                );

        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID caseId,
            @PathVariable UUID imageId,
            Authentication authentication) throws IOException {

        caseImageService.deleteImage(
                caseId,
                imageId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}