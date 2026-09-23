package com.sih26132.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class LocalEmbeddingServiceImpl
        implements EmbeddingService {

    private static final int EMBEDDING_DIMENSIONS = 1536;


    @Override
    public List<Double> generateEmbedding(
            String text) {

        if (text == null ||
                text.isBlank()) {

            throw new IllegalArgumentException(
                    "Text must not be null or blank"
            );
        }


        List<Double> embedding =
                new ArrayList<>(
                        EMBEDDING_DIMENSIONS
                );


        byte[] seed =
                createSeed(text);


        for (int i = 0;
             i < EMBEDDING_DIMENSIONS;
             i++) {

            int index =
                    i % seed.length;


            int value =
                    (seed[index] & 0xFF)
                    + (i * 31);


            double normalized =
                    ((value % 2000) / 1000.0)
                    - 1.0;


            embedding.add(
                    normalized
            );
        }


        return embedding;
    }


    private byte[] createSeed(
            String text) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );


            return digest.digest(
                    text.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }
}