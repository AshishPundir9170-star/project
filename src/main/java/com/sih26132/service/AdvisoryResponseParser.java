package com.sih26132.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sih26132.dto.advisory.GeneratedAdvisory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvisoryResponseParser {

    private final ObjectMapper objectMapper;

    public GeneratedAdvisory parse(String llmResponse) {

        if (llmResponse == null ||
                llmResponse.isBlank()) {

            throw new IllegalArgumentException(
                    "LLM response must not be null or blank"
            );
        }

        try {
            JsonNode root =
                    objectMapper.readTree(
                            llmResponse.trim()
                    );

            if (!root.isObject()) {
                throw new IllegalStateException(
                        "LLM response must be a JSON object"
                );
            }

            GeneratedAdvisory advisory =
                    new GeneratedAdvisory();

            advisory.setTitle(
                    getTextField(root, "title")
            );

            advisory.setContent(
                    getTextField(root, "content")
            );

            advisory.setImmediateActions(
                    getTextField(root, "immediateActions")
            );

            advisory.setPreventiveActions(
                    getTextField(root, "preventiveActions")
            );

            advisory.setIpdmActions(
                    getTextField(root, "ipdmActions")
            );

            advisory.setSafeUseInstructions(
                    getTextField(root, "safeUseInstructions")
            );

            advisory.setWhenToContactExpert(
                    getTextField(root, "whenToContactExpert")
            );

            advisory.setWhenToRecheck(
                    getTextField(root, "whenToRecheck")
            );

            validateAdvisory(advisory);

            return advisory;

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to parse LLM advisory response",
                    e
            );
        }
    }

    private String getTextField(
            JsonNode root,
            String fieldName) {

        JsonNode node =
                root.get(fieldName);

        if (node == null ||
                node.isNull()) {

            return "";
        }

        if (!node.isTextual()) {

            throw new IllegalStateException(
                    "LLM field '" +
                    fieldName +
                    "' must be a string"
            );
        }

        return node.asText().trim();
    }

    private void validateAdvisory(
            GeneratedAdvisory advisory) {

        if (isBlank(advisory.getTitle())) {

            throw new IllegalStateException(
                    "Generated advisory title is missing"
            );
        }

        if (isBlank(advisory.getContent())) {

            throw new IllegalStateException(
                    "Generated advisory content is missing"
            );
        }
    }

    private boolean isBlank(
            String value) {

        return value == null ||
                value.isBlank();
    }
}