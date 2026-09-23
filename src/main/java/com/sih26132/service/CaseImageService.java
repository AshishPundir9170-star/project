package com.sih26132.service;

import com.sih26132.dto.cases.CaseImageResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.CaseImage;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseImageRepository;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CaseImageService {

    private final CaseImageRepository caseImageRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public CaseImageService(
            CaseImageRepository caseImageRepository,
            CaseRepository caseRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService) {

        this.caseImageRepository = caseImageRepository;
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public CaseImageResponse uploadImage(
            UUID caseId,
            MultipartFile file,
            String identifier) throws IOException {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        validateImage(file);

        String objectKey = fileStorageService.store(file);

        try {

            CaseImage image = CaseImage.builder()
                    .caseEntity(caseEntity)
                    .objectKey(objectKey)
                    .originalFilename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSizeBytes(file.getSize())
                    .capturedAt(OffsetDateTime.now())
                    .createdAt(OffsetDateTime.now())
                    .build();

            CaseImage savedImage =
                    caseImageRepository.save(image);

            return mapToResponse(savedImage);

        } catch (Exception exception) {

            fileStorageService.delete(objectKey);

            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<CaseImageResponse> getCaseImages(
            UUID caseId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        return caseImageRepository
                .findByCaseEntity(caseEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteImage(
            UUID caseId,
            UUID imageId,
            String identifier) throws IOException {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        CaseImage image = caseImageRepository
                .findByIdAndCaseEntity(imageId, caseEntity)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Image not found or access denied"
                        ));

        fileStorageService.delete(image.getObjectKey());

        caseImageRepository.delete(image);
    }

    private void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Image file is required"
            );
        }

        long maxSize = 10 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new RuntimeException(
                    "Image size must not exceed 10 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/webp"))) {

            throw new RuntimeException(
                    "Only JPEG, PNG and WebP images are allowed"
            );
        }
    }

    private User findUser(String identifier) {

        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + identifier
                        ));
    }

    private CaseImageResponse mapToResponse(
            CaseImage image) {

        return CaseImageResponse.builder()
                .id(image.getId())
                .caseId(image.getCaseEntity().getId())
                .objectKey(image.getObjectKey())
                .storageUrl(image.getStorageUrl())
                .originalFilename(image.getOriginalFilename())
                .contentType(image.getContentType())
                .fileSizeBytes(image.getFileSizeBytes())
                .checksum(image.getChecksum())
                .capturedAt(image.getCapturedAt())
                .imageQualityScore(image.getImageQualityScore())
                .imageQualityStatus(image.getImageQualityStatus())
                .metadata(image.getMetadata())
                .createdAt(image.getCreatedAt())
                .build();
    }
}