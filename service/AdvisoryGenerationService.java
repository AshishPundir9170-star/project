
package com.sih26132.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sih26132.dto.advisory.GeneratedAdvisory;
import com.sih26132.entity.Advisory;
import com.sih26132.entity.Case;
import com.sih26132.entity.Diagnosis;
import com.sih26132.entity.RiskPrediction;
import com.sih26132.repository.AdvisoryRepository;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.DiagnosisRepository;
import com.sih26132.repository.RiskPredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvisoryGenerationService {

    private final RagRetrievalService ragRetrievalService;
    private final RagPromptBuilder ragPromptBuilder;
    private final LlmService llmService;
    private final AdvisoryResponseParser advisoryResponseParser;
    private final AdvisoryRepository advisoryRepository;
    private final CaseRepository caseRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final RiskPredictionRepository riskPredictionRepository;
    private final ObjectMapper objectMapper;

    public Advisory generateAndSaveAdvisory(
            UUID caseId,
            String farmerQuestion,
            String language,
            int retrievalLimit) {

        validateCaseId(caseId);
        validateQuestion(farmerQuestion);
        validateRetrievalLimit(retrievalLimit);

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Case not found: " + caseId
                ));

        Diagnosis diagnosis = getLatestDiagnosis(caseEntity);

        RiskPrediction riskPrediction =
                getLatestRiskPrediction(caseEntity);

        String diagnosisContext =
                buildDiagnosisContext(diagnosis);

        String riskContext =
                buildRiskContext(riskPrediction);

        RagContext ragContext =
                ragRetrievalService.retrieve(
                        farmerQuestion,
                        retrievalLimit
                );

        String prompt =
                ragPromptBuilder.buildPrompt(
                        farmerQuestion,
                        normalizeLanguage(language),
                        ragContext.getCombinedContext(),
                        diagnosisContext,
                        riskContext
                );

        String llmResponse =
                llmService.generate(prompt);

        if (llmResponse == null || llmResponse.isBlank()) {
            throw new IllegalStateException(
                    "LLM generated an empty advisory"
            );
        }

        GeneratedAdvisory generatedAdvisory =
                advisoryResponseParser.parse(llmResponse);

        Map<String, Object> knowledgeSources =
                buildKnowledgeSources(ragContext);

        OffsetDateTime now = OffsetDateTime.now();

        Advisory advisory = Advisory.builder()
                .caseEntity(caseEntity)
                .language(normalizeLanguage(language))
                .title(generatedAdvisory.getTitle())
                .content(generatedAdvisory.getContent())
                .immediateActions(
                        generatedAdvisory.getImmediateActions()
                )
                .preventiveActions(
                        generatedAdvisory.getPreventiveActions()
                )
                .ipdmActions(
                        generatedAdvisory.getIpdmActions()
                )
                .safeUseInstructions(
                        generatedAdvisory.getSafeUseInstructions()
                )
                .whenToContactExpert(
                        generatedAdvisory.getWhenToContactExpert()
                )
                .whenToRecheck(
                        generatedAdvisory.getWhenToRecheck()
                )
                .knowledgeSources(knowledgeSources)
                .generatedBy("LLM")
                .status("GENERATED")
                .createdAt(now)
                .updatedAt(now)
                .build();

        return advisoryRepository.save(advisory);
    }

    private Diagnosis getLatestDiagnosis(Case caseEntity) {

        List<Diagnosis> diagnoses =
                diagnosisRepository.findByCaseEntity(caseEntity);

        if (diagnoses == null || diagnoses.isEmpty()) {
            throw new IllegalStateException(
                    "No diagnosis found for case: "
                            + caseEntity.getId()
            );
        }

        return diagnoses.stream()
                .filter(diagnosis ->
                        diagnosis.getCreatedAt() != null
                )
                .max(
                        Comparator.comparing(
                                Diagnosis::getCreatedAt
                        )
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No valid diagnosis found for case: "
                                        + caseEntity.getId()
                        )
                );
    }

    private RiskPrediction getLatestRiskPrediction(
            Case caseEntity) {

        List<RiskPrediction> predictions =
                riskPredictionRepository.findByCaseEntity(
                        caseEntity
                );

        if (predictions == null || predictions.isEmpty()) {
            throw new IllegalStateException(
                    "No risk prediction found for case: "
                            + caseEntity.getId()
            );
        }

        return predictions.stream()
                .filter(prediction ->
                        prediction.getCreatedAt() != null
                )
                .max(
                        Comparator.comparing(
                                RiskPrediction::getCreatedAt
                        )
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No valid risk prediction found for case: "
                                        + caseEntity.getId()
                        )
                );
    }

    private String buildDiagnosisContext(
            Diagnosis diagnosis) {

        Map<String, Object> context =
                new LinkedHashMap<>();

        context.put(
                "diagnosisId",
                diagnosis.getId()
        );

        context.put(
                "conditionName",
                diagnosis.getConditionName()
        );

        context.put(
                "conditionType",
                diagnosis.getConditionType()
        );

        context.put(
                "confidence",
                diagnosis.getConfidence()
        );

        context.put(
                "alternatives",
                diagnosis.getAlternatives()
        );

        context.put(
                "explainability",
                diagnosis.getExplainability()
        );

        context.put(
                "source",
                diagnosis.getSource()
        );

        context.put(
                "isFinal",
                diagnosis.isFinal()
        );

        try {
            return objectMapper.writeValueAsString(context);

        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Failed to build diagnosis context",
                    exception
            );
        }
    }

    private String buildRiskContext(
            RiskPrediction riskPrediction) {

        Map<String, Object> context =
                new LinkedHashMap<>();

        context.put(
                "riskPredictionId",
                riskPrediction.getId()
        );

        context.put(
                "riskScore",
                riskPrediction.getRiskScore()
        );

        context.put(
                "severity",
                riskPrediction.getSeverity()
        );

        context.put(
                "forecastStart",
                riskPrediction.getForecastStart()
        );

        context.put(
                "forecastEnd",
                riskPrediction.getForecastEnd()
        );

        context.put(
                "contributingFactors",
                riskPrediction.getContributingFactors()
        );

        try {
            return objectMapper.writeValueAsString(context);

        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Failed to build risk context",
                    exception
            );
        }
    }

    private Map<String, Object> buildKnowledgeSources(
            RagContext ragContext) {

        List<Map<String, Object>> sources =
                new ArrayList<>();

        if (ragContext != null &&
                ragContext.getResults() != null) {

            for (KnowledgeSearchService.KnowledgeSearchResult result
                    : ragContext.getResults()) {

                Map<String, Object> source =
                        new LinkedHashMap<>();

                source.put(
                        "chunkId",
                        result.getId()
                );

                source.put(
                        "documentId",
                        result.getDocumentId()
                );

                source.put(
                        "chunkIndex",
                        result.getChunkIndex()
                );

                source.put(
                        "similarityScore",
                        result.getSimilarityScore()
                );

                sources.add(source);
            }
        }

        Map<String, Object> knowledgeSources =
                new LinkedHashMap<>();

        knowledgeSources.put(
                "sources",
                sources
        );

        return knowledgeSources;
    }

    private void validateCaseId(UUID caseId) {

        if (caseId == null) {
            throw new IllegalArgumentException(
                    "Case ID must not be null"
            );
        }
    }

    private void validateQuestion(
            String farmerQuestion) {

        if (farmerQuestion == null ||
                farmerQuestion.isBlank()) {

            throw new IllegalArgumentException(
                    "Farmer question must not be null or blank"
            );
        }
    }

    private void validateRetrievalLimit(
            int retrievalLimit) {

        if (retrievalLimit < 1) {
            throw new IllegalArgumentException(
                    "Retrieval limit must be at least 1"
            );
        }

        if (retrievalLimit > 100) {
            throw new IllegalArgumentException(
                    "Retrieval limit must not exceed 100"
            );
        }
    }

    private String normalizeLanguage(
            String language) {

        if (language == null ||
                language.isBlank()) {

            return "English";
        }

        return language.trim();
    }
}

