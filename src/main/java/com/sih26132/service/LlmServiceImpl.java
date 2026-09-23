package com.sih26132.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {

    private final ObjectMapper objectMapper;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.chat-url}")
    private String responseUrl;

    @Value("${openai.chat-model}")
    private String chatModel;

    private final RestClient restClient =
            RestClient.builder().build();

    @Override
    public String generate(String prompt) {

        /*
         * 1. Validate prompt
         */
        if (prompt == null || prompt.isBlank()) {

            throw new IllegalArgumentException(
                    "Prompt must not be null or blank"
            );
        }

        /*
         * 2. Validate OpenAI API key
         */
        if (apiKey == null || apiKey.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI API key is not configured"
            );
        }

        /*
         * 3. Validate OpenAI Responses API URL
         */
        if (responseUrl == null || responseUrl.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI response URL is not configured"
            );
        }

        /*
         * 4. Validate model
         */
        if (chatModel == null || chatModel.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI chat model is not configured"
            );
        }

        /*
         * 5. Structured output schema
         *
         * The LLM must return an object containing
         * the fields expected by AdvisoryResponseParser.
         */
        Map<String, Object> advisorySchema =
                Map.of(

                        "type",
                        "object",

                        "properties",
                        Map.of(

                                "title",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "content",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "immediateActions",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "preventiveActions",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "ipdmActions",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "safeUseInstructions",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "whenToContactExpert",
                                Map.of(
                                        "type",
                                        "string"
                                ),

                                "whenToRecheck",
                                Map.of(
                                        "type",
                                        "string"
                                )
                        ),

                        "required",
                        List.of(

                                "title",

                                "content",

                                "immediateActions",

                                "preventiveActions",

                                "ipdmActions",

                                "safeUseInstructions",

                                "whenToContactExpert",

                                "whenToRecheck"
                        ),

                        "additionalProperties",
                        false
                );

        /*
         * 6. Configure structured JSON output
         */
        Map<String, Object> responseFormat =
                Map.of(

                        "type",
                        "json_schema",

                        "name",
                        "crop_health_advisory",

                        "description",
                        "Structured crop health advisory response",

                        "strict",
                        true,

                        "schema",
                        advisorySchema
                );

        Map<String, Object> textConfiguration =
                Map.of(
                        "format",
                        responseFormat
                );

        /*
         * 7. Create user input message
         */
        Map<String, Object> userMessage =
                Map.of(

                        "role",
                        "user",

                        "content",
                        List.of(
                                Map.of(

                                        "type",
                                        "input_text",

                                        "text",
                                        prompt
                                )
                        )
                );

        /*
         * 8. Create Responses API request
         */
        Map<String, Object> requestBody =
                Map.of(

                        "model",
                        chatModel,

                        "input",
                        List.of(userMessage),

                        "text",
                        textConfiguration,

                        "store",
                        false
                );

        /*
         * 9. Call OpenAI Responses API
         */
        String responseBody;

        try {

            responseBody =
                    restClient
                            .post()
                            .uri(responseUrl)
                            .header(
                                    HttpHeaders.AUTHORIZATION,
                                    "Bearer " + apiKey
                            )
                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )
                            .body(requestBody)
                            .retrieve()
                            .body(String.class);

        } catch (RestClientResponseException exception) {

            /*
             * IMPORTANT:
             *
             * Print the real HTTP status and OpenAI
             * response body instead of hiding it.
             */

            System.err.println();
            System.err.println(
                    "========== OPENAI API ERROR =========="
            );

            System.err.println(
                    "HTTP STATUS: "
                            + exception.getStatusCode()
            );

            System.err.println(
                    "RESPONSE BODY: "
                            + exception.getResponseBodyAsString()
            );

            System.err.println(
                    "======================================"
            );

            System.err.println();

            throw new IllegalStateException(

                    "OpenAI API error: "
                            + exception.getStatusCode()
                            + " - "
                            + exception.getResponseBodyAsString(),

                    exception
            );

        } catch (Exception exception) {

            /*
             * Handle non-HTTP errors such as:
             *
             * - connection failure
             * - DNS failure
             * - timeout
             * - configuration problem
             */

            System.err.println();
            System.err.println(
                    "======= OPENAI CONNECTION ERROR ======="
            );

            exception.printStackTrace();

            System.err.println(
                    "======================================="
            );

            System.err.println();

            throw new IllegalStateException(

                    "Failed to call OpenAI Responses API: "
                            + exception.getMessage(),

                    exception
            );
        }

        /*
         * 10. Validate response body
         */
        if (responseBody == null ||
                responseBody.isBlank()) {

            throw new IllegalStateException(
                    "OpenAI returned an empty response"
            );
        }

        /*
         * 11. Extract generated text
         */
        return extractOutputText(responseBody);
    }

    /**
     * Extracts generated text from the OpenAI
     * Responses API response.
     */
    private String extractOutputText(
            String responseBody) {

        try {

            JsonNode root =
                    objectMapper.readTree(
                            responseBody
                    );

            /*
             * Responses API structure:
             *
             * output[]
             *   -> message
             *      -> content[]
             *         -> output_text
             *            -> text
             */

            JsonNode output =
                    root.path("output");

            if (!output.isArray() ||
                    output.isEmpty()) {

                throw new IllegalStateException(

                        "OpenAI response does not contain "
                                + "any output items"
                );
            }

            StringBuilder generatedText =
                    new StringBuilder();

            /*
             * Iterate through output items.
             */
            for (JsonNode outputItem : output) {

                /*
                 * We only need message output items.
                 */
                if (!"message".equals(
                        outputItem
                                .path("type")
                                .asText()
                )) {

                    continue;
                }

                JsonNode content =
                        outputItem.path("content");

                if (!content.isArray()) {

                    continue;
                }

                /*
                 * Iterate through message content.
                 */
                for (JsonNode contentItem : content) {

                    /*
                     * We need output_text.
                     */
                    if (!"output_text".equals(

                            contentItem
                                    .path("type")
                                    .asText()

                    )) {

                        continue;
                    }

                    JsonNode text =
                            contentItem.path("text");

                    if (text.isTextual()) {

                        if (generatedText.length() > 0) {

                            generatedText.append(
                                    "\n"
                            );
                        }

                        generatedText.append(
                                text.asText()
                        );
                    }
                }
            }

            /*
             * Convert generated text to String.
             */
            String result =
                    generatedText
                            .toString()
                            .trim();

            /*
             * Validate generated text.
             */
            if (result.isBlank()) {

                throw new IllegalStateException(

                        "OpenAI response does not contain "
                                + "valid generated text"
                );
            }

            /*
             * Verify that generated text is valid JSON.
             */
            JsonNode advisoryJson =
                    objectMapper.readTree(
                            result
                    );

            if (!advisoryJson.isObject()) {

                throw new IllegalStateException(

                        "OpenAI generated advisory "
                                + "must be a JSON object"
                );
            }

            return result;

        } catch (IllegalStateException exception) {

            /*
             * Preserve our meaningful validation
             * errors.
             */
            throw exception;

        } catch (Exception exception) {

            throw new IllegalStateException(

                    "Failed to parse OpenAI Responses API response",

                    exception
            );
        }
    }
}