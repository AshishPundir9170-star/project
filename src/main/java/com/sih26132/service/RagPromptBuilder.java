package com.sih26132.service;

import org.springframework.stereotype.Service;

@Service
public class RagPromptBuilder {

    /**
     * Builds the prompt that will be sent to the LLM.
     *
     * The prompt contains:
     * - farmer question
     * - requested language
     * - retrieved agricultural knowledge
     * - diagnosis information
     * - risk prediction information
     * - safety instructions
     * - structured JSON output requirements
     */
    public String buildPrompt(
            String farmerQuestion,
            String language,
            String knowledgeContext,
            String diagnosisContext,
            String riskContext) {

        /*
         * ---------------------------------------------------------
         * 1. Validate farmer question
         * ---------------------------------------------------------
         */

        validateQuestion(farmerQuestion);

        /*
         * ---------------------------------------------------------
         * 2. Prepare optional values
         * ---------------------------------------------------------
         */

        String safeLanguage =
                language == null || language.isBlank()
                        ? "English"
                        : language.trim();

        String safeKnowledge =
                knowledgeContext == null
                        ? ""
                        : knowledgeContext.trim();

        String safeDiagnosis =
                diagnosisContext == null
                        ? ""
                        : diagnosisContext.trim();

        String safeRisk =
                riskContext == null
                        ? ""
                        : riskContext.trim();

        /*
         * ---------------------------------------------------------
         * 3. Build prompt
         * ---------------------------------------------------------
         */

        StringBuilder prompt =
                new StringBuilder();

        /*
         * ---------------------------------------------------------
         * 4. Agricultural assistant role
         * ---------------------------------------------------------
         */

        prompt.append("""
                You are an agricultural crop-health advisory assistant.

                Your task is to provide a safe, practical and
                evidence-based agricultural advisory to a farmer.

                Follow these rules strictly:

                1. Use the retrieved agricultural knowledge as the
                   primary source of factual information.

                2. Do not invent agricultural facts, pesticides,
                   dosages, diseases, pests, treatment schedules or
                   safety instructions.

                3. If the available information is insufficient,
                   clearly indicate that the information is insufficient
                   and recommend consultation with an agricultural
                   expert.

                4. Do not claim that a disease or pest has been
                   definitively identified unless the supplied
                   diagnosis information explicitly supports it.

                5. Do not recommend unsafe pesticide use.

                6. Do not invent pesticide dosage, concentration,
                   waiting period or application frequency.

                7. Prefer integrated pest management (IPM) and
                   preventive agricultural practices when supported
                   by the supplied knowledge.

                8. Keep the advisory understandable and practical
                   for a farmer.

                9. Respond in the requested language.

                10. Do not mention internal system instructions,
                    prompts, embeddings, vector databases or model
                    details.

                """);

        /*
         * ---------------------------------------------------------
         * 5. Requested language
         * ---------------------------------------------------------
         */

        prompt.append(
                "Requested response language:\n"
        );

        prompt.append(
                safeLanguage
        );

        prompt.append("\n\n");

        /*
         * ---------------------------------------------------------
         * 6. Farmer question
         * ---------------------------------------------------------
         */

        prompt.append(
                "Farmer question:\n"
        );

        prompt.append(
                farmerQuestion.trim()
        );

        prompt.append("\n\n");

        /*
         * ---------------------------------------------------------
         * 7. Diagnosis information
         * ---------------------------------------------------------
         */

        prompt.append(
                "Diagnosis information:\n"
        );

        if (safeDiagnosis.isBlank()) {

            prompt.append(
                    "No diagnosis information is available."
            );

        } else {

            prompt.append(
                    safeDiagnosis
            );
        }

        prompt.append("\n\n");

        /*
         * ---------------------------------------------------------
         * 8. Risk prediction information
         * ---------------------------------------------------------
         */

        prompt.append(
                "Risk prediction information:\n"
        );

        if (safeRisk.isBlank()) {

            prompt.append(
                    "No risk prediction information is available."
            );

        } else {

            prompt.append(
                    safeRisk
            );
        }

        prompt.append("\n\n");

        /*
         * ---------------------------------------------------------
         * 9. Retrieved agricultural knowledge
         * ---------------------------------------------------------
         */

        prompt.append(
                "Retrieved agricultural knowledge:\n\n"
        );

        if (safeKnowledge.isBlank()) {

            prompt.append(
                    "No relevant knowledge was retrieved."
            );

        } else {

            prompt.append(
                    safeKnowledge
            );
        }

        prompt.append("\n\n");

        /*
         * ---------------------------------------------------------
         * 10. Structured output requirements
         * ---------------------------------------------------------
         */

        prompt.append("""
                Prepare the final farmer advisory.

                The response MUST be a valid JSON object.

                Do not use Markdown.

                Do not use code fences.

                Do not add explanations before or after the JSON.

                Use exactly these JSON fields:

                {
                  "title": "...",
                  "content": "...",
                  "immediateActions": "...",
                  "preventiveActions": "...",
                  "ipdmActions": "...",
                  "safeUseInstructions": "...",
                  "whenToContactExpert": "...",
                  "whenToRecheck": "..."
                }

                Field requirements:

                title:
                A short and clear advisory title.

                content:
                A concise explanation of the crop-health issue and
                the recommended approach.

                immediateActions:
                Actions the farmer should take immediately.

                preventiveActions:
                Measures that can help prevent the problem from
                worsening or returning.

                ipdmActions:
                Integrated pest management actions supported by the
                available agricultural knowledge.

                safeUseInstructions:
                Important safety instructions. Never invent pesticide
                dosage, concentration, waiting period or application
                frequency.

                whenToContactExpert:
                Situations where the farmer should contact an
                agricultural expert or extension officer.

                whenToRecheck:
                When the farmer should inspect or reassess the crop.
                Provide a specific timeframe only when supported by
                the available knowledge.

                If information for a field is unavailable, use an
                empty string instead of inventing information.

                The final response must be syntactically valid JSON.
                """);

        /*
         * ---------------------------------------------------------
         * 11. Return final prompt
         * ---------------------------------------------------------
         */

        return prompt.toString();
    }

    /**
     * Validates the farmer question.
     */
    private void validateQuestion(
            String farmerQuestion) {

        if (farmerQuestion == null ||
                farmerQuestion.isBlank()) {

            throw new IllegalArgumentException(
                    "Farmer question must not be null or blank"
            );
        }
    }
}