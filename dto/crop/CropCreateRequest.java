package com.sih26132.dto.crop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CropCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 200)
    private String scientificName;

    private String description;
}