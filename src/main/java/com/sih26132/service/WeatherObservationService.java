package com.sih26132.service;

import com.sih26132.dto.weather.WeatherObservationCreateRequest;
import com.sih26132.dto.weather.WeatherObservationResponse;
import com.sih26132.entity.WeatherObservation;
import com.sih26132.repository.WeatherObservationRepository;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WeatherObservationService {

    private final WeatherObservationRepository weatherObservationRepository;

    public WeatherObservationService(
            WeatherObservationRepository weatherObservationRepository) {

        this.weatherObservationRepository =
                weatherObservationRepository;
    }

    public WeatherObservationResponse createObservation(
            WeatherObservationCreateRequest request,
            Double latitude,
            Double longitude) {

        validateLocation(latitude, longitude);

        Point location = createPoint(latitude, longitude);

        WeatherObservation observation =
                WeatherObservation.builder()
                        .location(location)
                        .observedAt(request.getObservedAt())
                        .temperatureC(request.getTemperatureC())
                        .humidityPercent(request.getHumidityPercent())
                        .rainfallMm(request.getRainfallMm())
                        .windSpeedKmh(request.getWindSpeedKmh())
                        .leafWetness(request.getLeafWetness())
                        .source(request.getSource())
                        .rawData(request.getRawData())
                        .createdAt(OffsetDateTime.now())
                        .build();

        WeatherObservation savedObservation =
                weatherObservationRepository.save(observation);

        return mapToResponse(savedObservation);
    }

    @Transactional(readOnly = true)
    public List<WeatherObservationResponse> getAllObservations() {

        return weatherObservationRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WeatherObservationResponse> getObservationsBetween(
            OffsetDateTime start,
            OffsetDateTime end) {

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

        return weatherObservationRepository
                .findByObservedAtBetween(start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WeatherObservationResponse> getObservationsBySource(
            String source) {

        return weatherObservationRepository
                .findBySource(source)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WeatherObservationResponse getObservation(UUID id) {

        WeatherObservation observation =
                weatherObservationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Weather observation not found"
                                ));

        return mapToResponse(observation);
    }

    public void deleteObservation(UUID id) {

        WeatherObservation observation =
                weatherObservationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Weather observation not found"
                                ));

        weatherObservationRepository.delete(observation);
    }

    private void validateLocation(
            Double latitude,
            Double longitude) {

        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException(
                    "Latitude and longitude are required"
            );
        }

        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90"
            );
        }

        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180"
            );
        }
    }

    private Point createPoint(
            Double latitude,
            Double longitude) {

        GeometryFactory geometryFactory =
                new GeometryFactory(
                        new PrecisionModel(),
                        4326
                );

        Point point = geometryFactory.createPoint(
                new Coordinate(
                        longitude,
                        latitude
                )
        );

        point.setSRID(4326);

        return point;
    }

    private WeatherObservationResponse mapToResponse(
            WeatherObservation observation) {

        WeatherObservationResponse response =
                new WeatherObservationResponse();

        response.setId(
                observation.getId()
        );

        /*
         * Convert PostGIS/JTS Point back to
         * latitude and longitude for the API response.
         */
        if (observation.getLocation() != null) {

            Point point = observation.getLocation();

            response.setLongitude(
                    point.getX()
            );

            response.setLatitude(
                    point.getY()
            );
        }

        response.setObservedAt(
                observation.getObservedAt()
        );

        response.setTemperatureC(
                observation.getTemperatureC()
        );

        response.setHumidityPercent(
                observation.getHumidityPercent()
        );

        response.setRainfallMm(
                observation.getRainfallMm()
        );

        response.setWindSpeedKmh(
                observation.getWindSpeedKmh()
        );

        response.setLeafWetness(
                observation.getLeafWetness()
        );

        response.setSource(
                observation.getSource()
        );

        response.setRawData(
                observation.getRawData()
        );

        response.setCreatedAt(
                observation.getCreatedAt()
        );

        return response;
    }
}