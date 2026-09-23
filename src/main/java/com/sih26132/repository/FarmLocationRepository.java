package com.sih26132.repository;

import com.sih26132.entity.Farm;
import com.sih26132.entity.FarmLocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FarmLocationRepository
        extends JpaRepository<FarmLocation, UUID> {

    Optional<FarmLocation> findByFarm(Farm farm);

    Optional<FarmLocation> findByFarmId(UUID farmId);

    boolean existsByFarmId(UUID farmId);
}