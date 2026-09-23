package com.sih26132.service;

import com.sih26132.dto.farm.FarmCreateRequest;
import com.sih26132.dto.farm.FarmResponse;
import com.sih26132.entity.Farm;
import com.sih26132.entity.User;
import com.sih26132.repository.FarmRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FarmService {

    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    public FarmService(
            FarmRepository farmRepository,
            UserRepository userRepository) {

        this.farmRepository = farmRepository;
        this.userRepository = userRepository;
    }

    public FarmResponse createFarm(
            FarmCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = Farm.builder()
                .user(user)
                .farmName(request.getFarmName())
                .areaHectares(request.getAreaHectares())
                .soilType(request.getSoilType())
                .irrigationType(request.getIrrigationType())
                .description(request.getDescription())
                .build();

        Farm savedFarm = farmRepository.save(farm);

        return mapToResponse(savedFarm);
    }

    @Transactional(readOnly = true)
    public List<FarmResponse> getMyFarms(String identifier) {

        User user = findUser(identifier);

        return farmRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FarmResponse getMyFarm(
            UUID farmId,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = farmRepository
                .findByIdAndUser(farmId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm not found or access denied"
                        ));

        return mapToResponse(farm);
    }

    public FarmResponse updateFarm(
            UUID farmId,
            FarmCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = farmRepository
                .findByIdAndUser(farmId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm not found or access denied"
                        ));

        farm.setFarmName(request.getFarmName());
        farm.setAreaHectares(request.getAreaHectares());
        farm.setSoilType(request.getSoilType());
        farm.setIrrigationType(request.getIrrigationType());
        farm.setDescription(request.getDescription());

        Farm updatedFarm = farmRepository.save(farm);

        return mapToResponse(updatedFarm);
    }

    public void deleteFarm(
            UUID farmId,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = farmRepository
                .findByIdAndUser(farmId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm not found or access denied"
                        ));

        farmRepository.delete(farm);
    }

    private User findUser(String identifier) {

        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + identifier
                        ));
    }

    private FarmResponse mapToResponse(Farm farm) {

        return FarmResponse.builder()
                .id(farm.getId())
                .userId(farm.getUser().getId())
                .farmName(farm.getFarmName())
                .areaHectares(farm.getAreaHectares())
                .soilType(farm.getSoilType())
                .irrigationType(farm.getIrrigationType())
                .description(farm.getDescription())
                .createdAt(farm.getCreatedAt())
                .updatedAt(farm.getUpdatedAt())
                .build();
    }
}