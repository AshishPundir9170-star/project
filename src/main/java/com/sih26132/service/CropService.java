package com.sih26132.service;

import com.sih26132.dto.crop.CropCreateRequest;
import com.sih26132.dto.crop.CropResponse;
import com.sih26132.entity.Crop;
import com.sih26132.repository.CropRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public CropResponse createCrop(CropCreateRequest request) {

        if (cropRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException(
                    "Crop with this name already exists"
            );
        }

        Crop crop = Crop.builder()
                .name(request.getName())
                .scientificName(request.getScientificName())
                .description(request.getDescription())
                .createdAt(java.time.OffsetDateTime.now())
                .build();

        Crop savedCrop = cropRepository.save(crop);

        return mapToResponse(savedCrop);
    }

    @Transactional(readOnly = true)
    public List<CropResponse> getAllCrops() {

        return cropRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CropResponse getCrop(UUID cropId) {

        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() ->
                        new RuntimeException("Crop not found")
                );

        return mapToResponse(crop);
    }

    public CropResponse updateCrop(
            UUID cropId,
            CropCreateRequest request) {

        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() ->
                        new RuntimeException("Crop not found")
                );

        if (!crop.getName().equalsIgnoreCase(request.getName())
                && cropRepository.existsByNameIgnoreCase(
                        request.getName())) {

            throw new RuntimeException(
                    "Crop with this name already exists"
            );
        }

        crop.setName(request.getName());
        crop.setScientificName(request.getScientificName());
        crop.setDescription(request.getDescription());

        Crop updatedCrop = cropRepository.save(crop);

        return mapToResponse(updatedCrop);
    }

    public void deleteCrop(UUID cropId) {

        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() ->
                        new RuntimeException("Crop not found")
                );

        cropRepository.delete(crop);
    }

    private CropResponse mapToResponse(Crop crop) {

        return CropResponse.builder()
                .id(crop.getId())
                .name(crop.getName())
                .scientificName(crop.getScientificName())
                .description(crop.getDescription())
                .createdAt(crop.getCreatedAt())
                .build();
    }
}