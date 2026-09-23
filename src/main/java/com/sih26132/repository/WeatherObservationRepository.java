package com.sih26132.repository;

import com.sih26132.entity.WeatherObservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface WeatherObservationRepository
        extends JpaRepository<WeatherObservation, UUID> {

    List<WeatherObservation> findByObservedAtBetween(
            OffsetDateTime start,
            OffsetDateTime end
    );

    List<WeatherObservation> findBySource(String source);
}