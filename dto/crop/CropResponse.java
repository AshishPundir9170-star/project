package com.sih26132.dto.crop;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CropResponse {

    private UUID id;
    private String name;
    private String scientificName;
    private String description;
    private OffsetDateTime createdAt;
}