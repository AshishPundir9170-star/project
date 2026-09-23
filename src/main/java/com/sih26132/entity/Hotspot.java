package com.sih26132.entity;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "hotspots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotspot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "location",
            nullable = false,
            columnDefinition = "geometry(Point,4326)"
    )
    private Object location;

    @Column(name = "detected_at", nullable = false)
    private OffsetDateTime detectedAt;

    @Column(name = "condition_name", nullable = false, length = 300)
    private String conditionName;

    @Column(name = "hotspot_score", precision = 10, scale = 4)
    private BigDecimal hotspotScore;

    @Column(length = 50)
    private String severity;

    @Column(name = "case_count", nullable = false)
    private Integer caseCount;

    @Column(length = 150)
    private String state;

    @Column(length = 150)
    private String district;

    @Column(length = 150)
    private String block;

    @Column(nullable = false, length = 50)
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "radius_km")
    private Double radiusKm;

    @Column(name = "risk_score")
    private Double riskScore;
}