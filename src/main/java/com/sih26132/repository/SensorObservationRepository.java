package com.sih26132.repository;

import com.sih26132.entity.Farm;
import com.sih26132.entity.SensorObservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorObservationRepository
        extends JpaRepository<SensorObservation, UUID> {

    List<SensorObservation> findByFarm(Farm farm);

    Optional<SensorObservation> findByIdAndFarm(
            UUID id,
            Farm farm
    );

    List<SensorObservation> findByFarmAndObservedAtBetween(
            Farm farm,
            OffsetDateTime start,
            OffsetDateTime end
    );

    List<SensorObservation> findByFarmAndSensorType(
            Farm farm,
            String sensorType
    );

    List<SensorObservation> findByFarmAndMetric(
            Farm farm,
            String metric
    );
}