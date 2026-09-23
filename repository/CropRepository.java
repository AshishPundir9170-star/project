package com.sih26132.repository;

import com.sih26132.entity.Crop;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CropRepository extends JpaRepository<Crop, UUID> {

    Optional<Crop> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}