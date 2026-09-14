package com.sih26132.repository;

import com.sih26132.entity.CropLot;
import com.sih26132.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CropLotRepository extends JpaRepository<CropLot, UUID> {

    List<CropLot> findByFarmer(User farmer);

    List<CropLot> findByCropId(UUID cropId);

    List<CropLot> findByDistrictIgnoreCase(String district);

    List<CropLot> findByFarmerId(UUID farmerId);
}