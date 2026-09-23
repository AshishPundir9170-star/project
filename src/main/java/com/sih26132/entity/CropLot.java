package com.sih26132.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "crop_lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropLot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(nullable = false, length = 50)
    private String season;

    @Column(name = "market_type", length = 100)
    private String marketType;

    @Column(name = "quality_grade", length = 10)
    private String qualityGrade;

    @Column(nullable = false)
    private Double quantityQuintal;

    @Column(name = "production_tonnes")
    private Double productionTonnes;

    @Column(name = "current_market_price")
    private Double currentMarketPrice;

    @Column(name = "min_price")
    private Double minPrice;

    @Column(name = "max_price")
    private Double maxPrice;

    @Column(name = "demand_index")
    private Double demandIndex;

    @Column(name = "supply_index")
    private Double supplyIndex;

    @Column(name = "price_trend")
    private Double priceTrend;

    @Column(name = "arrival_volume_tonnes")
    private Double arrivalVolumeTonnes;

    @Column(name = "buyer_demand_tonnes")
    private Double buyerDemandTonnes;

    @Column(name = "storage_capacity_used_pct")
    private Double storageCapacityUsedPct;

    @Column(name = "transport_distance_km")
    private Double transportDistanceKm;

    @Column(name = "transport_cost")
    private Double transportCost;

    @Column(name = "buyer_offered_price")
    private Double buyerOfferedPrice;

    @Column(name = "buyer_rating")
    private Double buyerRating;

    @Column(name = "payment_reliability_pct")
    private Double paymentReliabilityPct;

    // New ML features

    @Column(name = "temperature_c")
    private Double temperatureC;

    @Column(name = "humidity_pct")
    private Double humidityPct;

    @Column(name = "rainfall_mm")
    private Double rainfallMm;

    @Column(name = "fpo_member", length = 20)
    private String fpoMember;

    @Column(name = "demand_urgency", length = 30)
    private String demandUrgency;

    @Column(name = "buyer_verified", length = 20)
    private String buyerVerified;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();

        if (state == null) {
            state = "Uttar Pradesh";
        }

        if (temperatureC == null) {
            temperatureC = 25.0;
        }

        if (humidityPct == null) {
            humidityPct = 60.0;
        }

        if (rainfallMm == null) {
            rainfallMm = 0.0;
        }

        if (fpoMember == null) {
            fpoMember = "Yes";
        }

        if (demandUrgency == null) {
            demandUrgency = "Medium";
        }

        if (buyerVerified == null) {
            buyerVerified = "Yes";
        }
    }
}