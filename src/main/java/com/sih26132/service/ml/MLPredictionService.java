package com.sih26132.service.ml;

import com.sih26132.dto.ml.MLPredictionRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MLPredictionService {

    private final RestClient restClient;

    public MLPredictionService(
            RestClient.Builder restClientBuilder,

            @Value("${ml.service.url:http://localhost:8000}")
            String mlServiceUrl
    ) {

        this.restClient =
                restClientBuilder
                        .baseUrl(mlServiceUrl)
                        .build();
    }

    public Object predictPrice(
            MLPredictionRequest request
    ) {

        return restClient
                .post()
                .uri("/predict/price")
                .body(request)
                .retrieve()
                .body(Object.class);
    }

    public Object predictSaleWindow(
            MLPredictionRequest request
    ) {

        return restClient
                .post()
                .uri("/predict/sale-window")
                .body(request)
                .retrieve()
                .body(Object.class);
    }

    public Object predictBuyerMatch(
            MLPredictionRequest request
    ) {

        return restClient
                .post()
                .uri("/predict/buyer-match")
                .body(request)
                .retrieve()
                .body(Object.class);
    }
}