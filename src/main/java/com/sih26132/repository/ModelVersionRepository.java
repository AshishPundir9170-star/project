package com.sih26132.repository;

import com.sih26132.entity.ModelVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModelVersionRepository extends JpaRepository<ModelVersion, UUID> {

    Optional<ModelVersion> findByModelNameAndVersion(
            String modelName,
            String version
    );

    List<ModelVersion> findByModelName(String modelName);

    List<ModelVersion> findByActiveTrue();
}