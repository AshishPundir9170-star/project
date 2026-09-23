package com.sih26132.service;

import com.sih26132.dto.crop.CropCycleCreateRequest;
import com.sih26132.dto.crop.CropCycleResponse;
import com.sih26132.entity.Crop;
import com.sih26132.entity.CropCycle;
import com.sih26132.entity.Farm;
import com.sih26132.entity.User;
import com.sih26132.repository.CropCycleRepository;
import com.sih26132.repository.CropRepository;
import com.sih26132.repository.FarmRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CropCycleService {

    private final CropCycleRepository cropCycleRepository;
    private final CropRepository cropRepository;
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    public CropCycleService(
            CropCycleRepository cropCycleRepository,
            CropRepository cropRepository,
            FarmRepository farmRepository,
            UserRepository userRepository) {

        this.cropCycleRepository = cropCycleRepository;
        this.cropRepository = cropRepository;
        this.farmRepository = farmRepository;
        this.userRepository = userRepository;
    }

    public CropCycleResponse createCropCycle(
            UUID farmId,
            CropCycleCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        Crop crop = cropRepository.findById(request.getCropId())
                .orElseThrow(() ->
                        new RuntimeException("Crop not found")
                );

        CropCycle cropCycle = CropCycle.builder()
                .farm(farm)
                .crop(crop)
                .variety(request.getVariety())
                .growthStage(request.getGrowthStage())
                .sowingDate(request.getSowingDate())
                .transplantDate(request.getTransplantDate())
                .expectedHarvestDate(
                        request.getExpectedHarvestDate()
                )
                .areaHectares(request.getAreaHectares())
                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : "ACTIVE"
                )
                .createdAt(java.time.OffsetDateTime.now())
                .updatedAt(java.time.OffsetDateTime.now())
                .build();

        CropCycle savedCropCycle =
                cropCycleRepository.save(cropCycle);

        return mapToResponse(savedCropCycle);
    }

    @Transactional(readOnly = true)
    public List<CropCycleResponse> getMyCropCycles(
            UUID farmId,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        return cropCycleRepository.findByFarm(farm)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CropCycleResponse getMyCropCycle(
            UUID farmId,
            UUID cropCycleId,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        CropCycle cropCycle = cropCycleRepository
                .findByIdAndFarm(cropCycleId, farm)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Crop cycle not found or access denied"
                        ));

        return mapToResponse(cropCycle);
    }

    public CropCycleResponse updateCropCycle(
            UUID farmId,
            UUID cropCycleId,
            CropCycleCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        CropCycle cropCycle = cropCycleRepository
                .findByIdAndFarm(cropCycleId, farm)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Crop cycle not found or access denied"
                        ));

        Crop crop = cropRepository.findById(request.getCropId())
                .orElseThrow(() ->
                        new RuntimeException("Crop not found")
                );

        cropCycle.setCrop(crop);
        cropCycle.setVariety(request.getVariety());
        cropCycle.setGrowthStage(request.getGrowthStage());
        cropCycle.setSowingDate(request.getSowingDate());
        cropCycle.setTransplantDate(request.getTransplantDate());
        cropCycle.setExpectedHarvestDate(
                request.getExpectedHarvestDate()
        );
        cropCycle.setAreaHectares(request.getAreaHectares());

        if (request.getStatus() != null) {
            cropCycle.setStatus(request.getStatus());
        }

        cropCycle.setUpdatedAt(
                java.time.OffsetDateTime.now()
        );

        CropCycle updatedCropCycle =
                cropCycleRepository.save(cropCycle);

        return mapToResponse(updatedCropCycle);
    }

    public void deleteCropCycle(
            UUID farmId,
            UUID cropCycleId,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        CropCycle cropCycle = cropCycleRepository
                .findByIdAndFarm(cropCycleId, farm)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Crop cycle not found or access denied"
                        ));

        cropCycleRepository.delete(cropCycle);
    }

    @Transactional(readOnly = true)
    public List<CropCycleResponse> getActiveCropCycles(
            UUID farmId,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        return cropCycleRepository
                .findByFarmAndStatus(farm, "ACTIVE")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private User findUser(String identifier) {

        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + identifier
                        ));
    }

    private Farm findOwnedFarm(
            UUID farmId,
            User user) {

        return farmRepository
                .findByIdAndUser(farmId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm not found or access denied"
                        ));
    }

    private CropCycleResponse mapToResponse(
            CropCycle cropCycle) {

        return CropCycleResponse.builder()
                .id(cropCycle.getId())
                .farmId(cropCycle.getFarm().getId())
                .cropId(cropCycle.getCrop().getId())
                .cropName(cropCycle.getCrop().getName())
                .variety(cropCycle.getVariety())
                .growthStage(cropCycle.getGrowthStage())
                .sowingDate(cropCycle.getSowingDate())
                .transplantDate(cropCycle.getTransplantDate())
                .expectedHarvestDate(
                        cropCycle.getExpectedHarvestDate()
                )
                .areaHectares(cropCycle.getAreaHectares())
                .status(cropCycle.getStatus())
                .createdAt(cropCycle.getCreatedAt())
                .updatedAt(cropCycle.getUpdatedAt())
                .build();
    }
}