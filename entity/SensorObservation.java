package com.sih26132.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "sensor_observations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @Column(name = "observed_at", nullable = false)
    private OffsetDateTime observedAt;

    @Column(name = "sensor_type", nullable = false, length = 100)
    private String sensorType;

    @Column(nullable = false, length = 100)
    private String metric;

    @Column(nullable = false)
    private Double value;

    @Column(length = 50)
    private String unit;

    @Column(name = "raw_data", columnDefinition = "JSONB")
    private String rawData;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}