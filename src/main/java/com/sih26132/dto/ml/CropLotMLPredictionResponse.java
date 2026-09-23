package com.sih26132.dto.ml;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropLotMLPredictionResponse {

    private String cropLotId;

    private Object pricePrediction;

    private Object saleWindowPrediction;

    private Object buyerMatchPrediction;

    private String status;
}