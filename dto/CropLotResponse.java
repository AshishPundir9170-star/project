package com.sih26132.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class CropLotResponse {

    private UUID id;

    private UUID farmerId;
    private String farmerName;

    private UUID cropId;
    private String cropName;

    private String district;
    private String season;
    private String marketType;
    private String qualityGrade;

    private Double quantityQuintal;
    private Double productionTonnes;

    private Double currentMarketPrice;
    private Double minPrice;
    private Double maxPrice;

    private Double demandIndex;
    private Double supplyIndex;
    private Double priceTrend;

    private Double arrivalVolumeTonnes;
    private Double buyerDemandTonnes;

    private Double storageCapacityUsedPct;

    private Double transportDistanceKm;
    private Double transportCost;

    private Double buyerOfferedPrice;
    private Double buyerRating;
    private Double paymentReliabilityPct;

    private OffsetDateTime createdAt;
}