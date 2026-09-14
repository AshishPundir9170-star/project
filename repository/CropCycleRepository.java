package com.sih26132.repository;

import com.sih26132.entity.Crop;
import com.sih26132.entity.CropCycle;
import com.sih26132.entity.Farm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CropCycleRepository
        extends JpaRepository<CropCycle, UUID> {

    List<CropCycle> findByFarm(Farm farm);

    Optional<CropCycle> findByIdAndFarm(
            UUID id,
            Farm farm
    );

    boolean existsByIdAndFarm(
            UUID id,
            Farm farm
    );

    List<CropCycle> findByFarmAndStatus(
            Farm farm,
            String status
    );

    List<CropCycle> findByCrop(Crop crop);
}