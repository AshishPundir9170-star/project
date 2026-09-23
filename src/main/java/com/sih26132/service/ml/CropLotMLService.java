package com.sih26132.service.ml;

import com.sih26132.dto.ml.CropLotMLPredictionResponse;
import com.sih26132.dto.ml.MLPredictionRequest;
import com.sih26132.entity.CropLot;
import com.sih26132.repository.CropLotRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CropLotMLService {

    private final CropLotRepository cropLotRepository;
    private final MLPredictionService mlPredictionService;

    public CropLotMLService(
            CropLotRepository cropLotRepository,
            MLPredictionService mlPredictionService
    ) {

        this.cropLotRepository = cropLotRepository;
        this.mlPredictionService = mlPredictionService;
    }

    @Transactional(readOnly = true)
    public CropLotMLPredictionResponse predictCropLot(
            UUID cropLotId
    ) {

        CropLot lot =
                cropLotRepository.findById(cropLotId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Crop lot not found: "
                                                + cropLotId
                                )
                        );

        MLPredictionRequest request =
                buildMLRequest(lot);

        Object pricePrediction =
                mlPredictionService.predictPrice(request);

        Object saleWindowPrediction =
                mlPredictionService.predictSaleWindow(request);

        Object buyerMatchPrediction =
                mlPredictionService.predictBuyerMatch(request);

        return CropLotMLPredictionResponse.builder()

                .cropLotId(cropLotId.toString())

                .pricePrediction(pricePrediction)

                .saleWindowPrediction(
                        saleWindowPrediction
                )

                .buyerMatchPrediction(
                        buyerMatchPrediction
                )

                .status("SUCCESS")

                .build();
    }

    private MLPredictionRequest buildMLRequest(
            CropLot lot
    ) {

        String cropName =
                lot.getCrop() != null
                        ? lot.getCrop().getName()
                        : "";

        double quantityTonnes =
                lot.getQuantityQuintal() != null
                        ? lot.getQuantityQuintal() / 10.0
                        : 0.0;

        double productionTonnes =
                lot.getProductionTonnes() != null
                        ? lot.getProductionTonnes()
                        : quantityTonnes;

        return MLPredictionRequest.builder()

                .state(
                        defaultString(
                                lot.getState(),
                                "Uttar Pradesh"
                        )
                )

                .district(
                        defaultString(
                                lot.getDistrict(),
                                "Meerut"
                        )
                )

                .crop(cropName)

                .season(
                        defaultString(
                                lot.getSeason(),
                                "Kharif"
                        )
                )

                .marketType(
                        defaultString(
                                lot.getMarketType(),
                                "Mandi"
                        )
                )

                .qualityGrade(
                        defaultString(
                                lot.getQualityGrade(),
                                "A"
                        )
                )

                .fpoMember(
                        defaultString(
                                lot.getFpoMember(),
                                "Yes"
                        )
                )

                .demandUrgency(
                        defaultString(
                                lot.getDemandUrgency(),
                                "Medium"
                        )
                )

                .buyerVerified(
                        defaultString(
                                lot.getBuyerVerified(),
                                "Yes"
                        )
                )

                .temperatureC(
                        defaultDouble(
                                lot.getTemperatureC(),
                                25.0
                        )
                )

                .humidityPct(
                        defaultDouble(
                                lot.getHumidityPct(),
                                60.0
                        )
                )

                .rainfallMm(
                        defaultDouble(
                                lot.getRainfallMm(),
                                0.0
                        )
                )

                .productionTonnes(
                        productionTonnes
                )

                .lotQuantityTonnes(
                        quantityTonnes
                )

                .currentMarketPriceRsPerQuintal(
                        defaultDouble(
                                lot.getCurrentMarketPrice(),
                                0.0
                        )
                )

                .minPriceRsPerQuintal(
                        defaultDouble(
                                lot.getMinPrice(),
                                0.0
                        )
                )

                .maxPriceRsPerQuintal(
                        defaultDouble(
                                lot.getMaxPrice(),
                                0.0
                        )
                )

                .demandIndex(
                        defaultDouble(
                                lot.getDemandIndex(),
                                50.0
                        )
                )

                .supplyIndex(
                        defaultDouble(
                                lot.getSupplyIndex(),
                                50.0
                        )
                )

                .priceTrend(
                        defaultDouble(
                                lot.getPriceTrend(),
                                0.0
                        )
                )

                .arrivalVolumeTonnes(
                        defaultDouble(
                                lot.getArrivalVolumeTonnes(),
                                0.0
                        )
                )

                .buyerDemandTonnes(
                        defaultDouble(
                                lot.getBuyerDemandTonnes(),
                                0.0
                        )
                )

                .storageCapacityUsedPct(
                        defaultDouble(
                                lot.getStorageCapacityUsedPct(),
                                50.0
                        )
                )

                .transportDistanceKm(
                        defaultDouble(
                                lot.getTransportDistanceKm(),
                                0.0
                        )
                )

                .transportCostRs(
                        defaultDouble(
                                lot.getTransportCost(),
                                0.0
                        )
                )

                .buyerOfferedPriceRsPerQuintal(
                        defaultDouble(
                                lot.getBuyerOfferedPrice(),
                                0.0
                        )
                )

                .buyerRating1To5(
                        defaultDouble(
                                lot.getBuyerRating(),
                                3.0
                        )
                )

                .paymentReliabilityPct(
                        defaultDouble(
                                lot.getPaymentReliabilityPct(),
                                70.0
                        )
                )

                .build();
    }

    private String defaultString(
            String value,
            String defaultValue
    ) {

        return value == null ||
                value.trim().isEmpty()
                ? defaultValue
                : value;
    }

    private Double defaultDouble(
            Double value,
            Double defaultValue
    ) {

        return value == null
                ? defaultValue
                : value;
    }
}