
package com.sih26132.service;

import com.sih26132.dto.CropLotResponse;
import com.sih26132.entity.Crop;
import com.sih26132.entity.CropLot;
import com.sih26132.entity.User;
import com.sih26132.repository.CropLotRepository;
import com.sih26132.repository.CropRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CropLotService {

    private final CropLotRepository cropLotRepository;
    private final CropRepository cropRepository;
    private final UserRepository userRepository;

    public CropLotService(
            CropLotRepository cropLotRepository,
            CropRepository cropRepository,
            UserRepository userRepository
    ) {
        this.cropLotRepository = cropLotRepository;
        this.cropRepository = cropRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE CROP LOT
    // =========================================================

    @Transactional
    public CropLot createCropLot(
            String username,
            UUID cropId,
            CropLot cropLot
    ) {

        User farmer = userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farmer not found: " + username
                        )
                );

        Crop crop = cropRepository
                .findById(cropId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Crop not found: " + cropId
                        )
                );

        cropLot.setFarmer(farmer);
        cropLot.setCrop(crop);

        return cropLotRepository.save(cropLot);
    }

    // =========================================================
    // GET FARMER CROP LOTS
    // =========================================================

    @Transactional(readOnly = true)
    public List<CropLotResponse> getFarmerCropLots(
            String username
    ) {

        User farmer = userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Farmer not found: " + username
                        )
                );

        List<CropLot> lots =
                cropLotRepository.findByFarmerId(
                        farmer.getId()
                );

        return lots.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET SINGLE CROP LOT
    // =========================================================

    @Transactional(readOnly = true)
    public CropLotResponse getCropLot(UUID id) {

        CropLot lot = cropLotRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Crop lot not found: " + id
                        )
                );

        return convertToResponse(lot);
    }

    // =========================================================
    // DELETE CROP LOT
    // =========================================================

    @Transactional
    public void deleteCropLot(UUID id) {

        if (!cropLotRepository.existsById(id)) {
            throw new RuntimeException(
                    "Crop lot not found: " + id
            );
        }

        cropLotRepository.deleteById(id);
    }

    // =========================================================
    // CONVERT ENTITY TO DTO
    // =========================================================

    private CropLotResponse convertToResponse(
            CropLot lot
    ) {

        UUID farmerId = null;
        String farmerName = null;

        UUID cropId = null;
        String cropName = null;

        // -----------------------------------------------------
        // FARMER
        // -----------------------------------------------------

        if (lot.getFarmer() != null) {

            farmerId = lot.getFarmer().getId();

            // User entity does not have getName().
            // Use email temporarily as the displayed farmer name.
            farmerName = lot.getFarmer().getEmail();
        }

        // -----------------------------------------------------
        // CROP
        // -----------------------------------------------------

        if (lot.getCrop() != null) {

            cropId = lot.getCrop().getId();
            cropName = lot.getCrop().getName();
        }

        // -----------------------------------------------------
        // BUILD RESPONSE
        // -----------------------------------------------------

        return CropLotResponse.builder()
                .id(lot.getId())

                .farmerId(farmerId)
                .farmerName(farmerName)

                .cropId(cropId)
                .cropName(cropName)

                .district(lot.getDistrict())
                .season(lot.getSeason())
                .marketType(lot.getMarketType())
                .qualityGrade(lot.getQualityGrade())

                .quantityQuintal(
                        lot.getQuantityQuintal()
                )

                .productionTonnes(
                        lot.getProductionTonnes()
                )

                .currentMarketPrice(
                        lot.getCurrentMarketPrice()
                )

                .minPrice(
                        lot.getMinPrice()
                )

                .maxPrice(
                        lot.getMaxPrice()
                )

                .demandIndex(
                        lot.getDemandIndex()
                )

                .supplyIndex(
                        lot.getSupplyIndex()
                )

                .priceTrend(
        lot.getPriceTrend()
)

.arrivalVolumeTonnes(
        lot.getArrivalVolumeTonnes() != null
                ? lot.getArrivalVolumeTonnes().doubleValue()
                : 0.0
)

.buyerDemandTonnes(
        lot.getBuyerDemandTonnes()
)

                .storageCapacityUsedPct(
                        lot.getStorageCapacityUsedPct()
                )

                .transportDistanceKm(
                        lot.getTransportDistanceKm()
                )

                .transportCost(
                        lot.getTransportCost()
                )

                .buyerOfferedPrice(
                        lot.getBuyerOfferedPrice()
                )

                .buyerRating(
                        lot.getBuyerRating()
                )

                .paymentReliabilityPct(
                        lot.getPaymentReliabilityPct()
                )

                .createdAt(
                        lot.getCreatedAt()
                )

                .build();
    }
}

