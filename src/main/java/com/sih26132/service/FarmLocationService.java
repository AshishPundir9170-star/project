package com.sih26132.service;

import com.sih26132.dto.farm.FarmLocationCreateRequest;
import com.sih26132.dto.farm.FarmLocationResponse;
import com.sih26132.entity.Farm;
import com.sih26132.entity.FarmLocation;
import com.sih26132.entity.User;
import com.sih26132.repository.FarmLocationRepository;
import com.sih26132.repository.FarmRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class FarmLocationService {

    private final FarmLocationRepository farmLocationRepository;
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    public FarmLocationService(
            FarmLocationRepository farmLocationRepository,
            FarmRepository farmRepository,
            UserRepository userRepository) {

        this.farmLocationRepository = farmLocationRepository;
        this.farmRepository = farmRepository;
        this.userRepository = userRepository;
    }

    public FarmLocationResponse createLocation(
            UUID farmId,
            FarmLocationCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Farm farm = findOwnedFarm(farmId, user);

        if (farmLocationRepository.existsByFarmId(farmId)) {
            throw new RuntimeException(
                    "Location already exists for this farm"
            );
        }

        FarmLocation location = FarmLocation.builder()
                .farm(farm)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .address(request.getAddress())
                .village(request.getVillage())
                .block(request.getBlock())
                .district(request.getDistrict())
                .state(request.getState())
                .country(
                        request.getCountry() != null
                                ? request.getCountry()
                                : "India"
                )
                .build();

        FarmLocation savedLocation =
                farmLocationRepository.save(location);

        return mapToResponse(savedLocation);
    }

    @Transactional(readOnly = true)
    public FarmLocationResponse getLocation(
            UUID farmId,
            String identifier) {

        User user = findUser(identifier);

        findOwnedFarm(farmId, user);

        FarmLocation location = farmLocationRepository
                .findByFarmId(farmId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm location not found"
                        ));

        return mapToResponse(location);
    }

    public FarmLocationResponse updateLocation(
            UUID farmId,
            FarmLocationCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        findOwnedFarm(farmId, user);

        FarmLocation location = farmLocationRepository
                .findByFarmId(farmId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm location not found"
                        ));

        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setAddress(request.getAddress());
        location.setVillage(request.getVillage());
        location.setBlock(request.getBlock());
        location.setDistrict(request.getDistrict());
        location.setState(request.getState());

        if (request.getCountry() != null) {
            location.setCountry(request.getCountry());
        }

        FarmLocation updatedLocation =
                farmLocationRepository.save(location);

        return mapToResponse(updatedLocation);
    }

    public void deleteLocation(
            UUID farmId,
            String identifier) {

        User user = findUser(identifier);

        findOwnedFarm(farmId, user);

        FarmLocation location = farmLocationRepository
                .findByFarmId(farmId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farm location not found"
                        ));

        farmLocationRepository.delete(location);
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

    private FarmLocationResponse mapToResponse(
            FarmLocation location) {

        return FarmLocationResponse.builder()
                .id(location.getId())
                .farmId(location.getFarm().getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .village(location.getVillage())
                .block(location.getBlock())
                .district(location.getDistrict())
                .state(location.getState())
                .country(location.getCountry())
                .createdAt(location.getCreatedAt())
                .build();
    }
}