package com.sih26132.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MLPredictionRequest {

    @JsonProperty("State")
    private String state;

    @JsonProperty("District")
    private String district;

    @JsonProperty("Crop")
    private String crop;

    @JsonProperty("Season")
    private String season;

    @JsonProperty("Market_Type")
    private String marketType;

    @JsonProperty("Quality_Grade")
    private String qualityGrade;

    @JsonProperty("FPO_Member")
    private String fpoMember;

    @JsonProperty("Demand_Urgency")
    private String demandUrgency;

    @JsonProperty("Buyer_Verified")
    private String buyerVerified;

    @JsonProperty("Temperature_C")
    private Double temperatureC;

    @JsonProperty("Humidity_pct")
    private Double humidityPct;

    @JsonProperty("Rainfall_mm")
    private Double rainfallMm;

    @JsonProperty("Production_tonnes")
    private Double productionTonnes;

    @JsonProperty("Lot_Quantity_tonnes")
    private Double lotQuantityTonnes;

    @JsonProperty("Current_Market_Price_Rs_per_quintal")
    private Double currentMarketPriceRsPerQuintal;

    @JsonProperty("Min_Price_Rs_per_quintal")
    private Double minPriceRsPerQuintal;

    @JsonProperty("Max_Price_Rs_per_quintal")
    private Double maxPriceRsPerQuintal;

    @JsonProperty("Demand_Index")
    private Double demandIndex;

    @JsonProperty("Supply_Index")
    private Double supplyIndex;

    @JsonProperty("Price_Trend")
    private Double priceTrend;

    @JsonProperty("Arrival_Volume_tonnes")
    private Double arrivalVolumeTonnes;

    @JsonProperty("Buyer_Demand_tonnes")
    private Double buyerDemandTonnes;

    @JsonProperty("Storage_Capacity_Used_pct")
    private Double storageCapacityUsedPct;

    @JsonProperty("Transport_Distance_km")
    private Double transportDistanceKm;

    @JsonProperty("Transport_Cost_Rs")
    private Double transportCostRs;

    @JsonProperty("Buyer_Offered_Price_Rs_per_quintal")
    private Double buyerOfferedPriceRsPerQuintal;

    @JsonProperty("Buyer_Rating_1_5")
    private Double buyerRating1To5;

    @JsonProperty("Payment_Reliability_pct")
    private Double paymentReliabilityPct;
}