package com.sih26132.service;

public interface LlmService {

    /**
     * Generates a response using the supplied prompt.
     *
     * @param prompt complete prompt sent to the LLM
     * @return generated text
     */
    String generate(String prompt);
}