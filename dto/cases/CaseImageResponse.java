
package com.sih26132.dto.cases;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class CaseImageResponse {

    private UUID id;

    private UUID caseId;

    private String objectKey;

    private String storageUrl;

    private String originalFilename;

    private String contentType;

    private Long fileSizeBytes;

    private String checksum;

    private OffsetDateTime capturedAt;

    private Double imageQualityScore;

    private String imageQualityStatus;

    private Map<String, Object> metadata;

    private OffsetDateTime createdAt;
}

