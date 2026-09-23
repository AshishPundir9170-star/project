package com.sih26132.service;

import com.sih26132.dto.sensor.SensorObservationCreateRequest;
import com.sih26132.dto.sensor.SensorObservationResponse;
import com.sih26132.entity.Farm;
import com.sih26132.entity.SensorObservation;
import com.sih26132.repository.FarmRepository;
import com.sih26132.repository.SensorObservationRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SensorObservationService {

    private final SensorObservationRepository sensorObservationRepository;
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    public SensorObservationService(
            SensorObservationRepository sensorObservationRepository,
            FarmRepository farmRepository,
            UserRepository userRepository) {

        this.sensorObservationRepository = sensorObservationRepository;
        this.farmRepository = farmRepository;
        this.userRepository = userRepository;
    }

    public SensorObservationResponse createObservation(
            SensorObservationCreateRequest request,
            String username) {

        Farm farm = getOwnedFarm(request.getFarmId(), username);

        SensorObservation observation =
                SensorObservation.builder()
                        .farm(farm)
                        .observedAt(request.getObservedAt())
                        .sensorType(request.getSensorType())
                        .metric(request.getMetric())
                        .value(request.getValue())
                        .unit(request.getUnit())
                        .rawData(request.getRawData())
                        .createdAt(OffsetDateTime.now())
                        .build();

        SensorObservation saved =
                sensorObservationRepository.save(observation);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SensorObservationResponse> getFarmObservations(
            UUID farmId,
            String username) {

        Farm farm = getOwnedFarm(farmId, username);

        return sensorObservationRepository
                .findByFarm(farm)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SensorObservationResponse getObservation(
            UUID farmId,
            UUID observationId,
            String username) {

        Farm farm = getOwnedFarm(farmId, username);

        SensorObservation observation =
                sensorObservationRepository
                        .findByIdAndFarm(observationId, farm)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sensor observation not found"
                                ));

        return mapToResponse(observation);
    }

    @Transactional(readOnly = true)
    public List<SensorObservationResponse> getObservationsBetween(
            UUID farmId,
            OffsetDateTime start,
            OffsetDateTime end,
            String username) {

        Farm farm = getOwnedFarm(farmId, username);

        if (start == null || end == null) {
            throw new IllegalArgumentException(
                    "Start and end time are required"
            );
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "Start time must be before or equal to end time"
            );
        }

        return sensorObservationRepository
                .findByFarmAndObservedAtBetween(farm, start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SensorObservationResponse> getObservationsBySensorType(
            UUID farmId,
            String sensorType,
            String username) {

        Farm farm = getOwnedFarm(farmId, username);

        return sensorObservationRepository
                .findByFarmAndSensorType(farm, sensorType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SensorObservationResponse> getObservationsByMetric(
            UUID farmId,
            String metric,
            String username) {

        Farm farm = getOwnedFarm(farmId, username);

        return sensorObservationRepository
                .findByFarmAndMetric(farm, metric)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteObservation(
            UUID farmId,
            UUID observationId,
            String username) {

        Farm farm = getOwnedFarm(farmId, username);

        SensorObservation observation =
                sensorObservationRepository
                        .findByIdAndFarm(observationId, farm)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sensor observation not found"
                                ));

        sensorObservationRepository.delete(observation);
    }

    private Farm getOwnedFarm(
            UUID farmId,
            String username) {

        return userRepository.findByEmail(username)
                .map(user ->
                        farmRepository
                                .findByIdAndUser(farmId, user)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Farm not found or access denied"
                                        ))
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));
    }

    private SensorObservationResponse mapToResponse(
            SensorObservation observation) {

        SensorObservationResponse response =
                new SensorObservationResponse();

        response.setId(observation.getId());

        if (observation.getFarm() != null) {
            response.setFarmId(observation.getFarm().getId());
        }

        response.setObservedAt(observation.getObservedAt());
        response.setSensorType(observation.getSensorType());
        response.setMetric(observation.getMetric());
        response.setValue(observation.getValue());
        response.setUnit(observation.getUnit());
        response.setRawData(observation.getRawData());
        response.setCreatedAt(observation.getCreatedAt());

        return response;
    }
}