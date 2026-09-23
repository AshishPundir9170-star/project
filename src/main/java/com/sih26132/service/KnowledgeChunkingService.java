package com.sih26132.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeChunkingService {

    /*
     * Target chunk size.
     *
     * This is measured approximately in characters,
     * not tokens.
     */
    private static final int DEFAULT_CHUNK_SIZE = 1200;

    /*
     * Overlap between consecutive chunks.
     *
     * Overlap helps preserve context when a sentence or
     * concept crosses a chunk boundary.
     */
    private static final int DEFAULT_OVERLAP = 200;

    /**
     * Splits knowledge text into overlapping chunks.
     *
     * @param text complete knowledge document text
     * @return ordered list of chunks
     */
    public List<String> splitIntoChunks(String text) {

        return splitIntoChunks(
                text,
                DEFAULT_CHUNK_SIZE,
                DEFAULT_OVERLAP
        );
    }

    /**
     * Splits text using the supplied chunk size and overlap.
     *
     * @param text complete knowledge document text
     * @param chunkSize approximate maximum chunk size
     * @param overlap number of overlapping characters
     * @return ordered list of chunks
     */
    public List<String> splitIntoChunks(
            String text,
            int chunkSize,
            int overlap) {

        validateInput(
                text,
                chunkSize,
                overlap
        );

        String normalizedText =
                normalizeText(text);

        if (normalizedText.isBlank()) {
            return List.of();
        }

        /*
         * Small documents do not need splitting.
         */
        if (normalizedText.length() <= chunkSize) {
            return List.of(normalizedText);
        }

        List<String> chunks =
                new ArrayList<>();

        int start = 0;

        while (start < normalizedText.length()) {

            int targetEnd =
                    Math.min(
                            start + chunkSize,
                            normalizedText.length()
                    );

            int end =
                    findBestSplitPosition(
                            normalizedText,
                            start,
                            targetEnd
                    );

            /*
             * Safety check to prevent an infinite loop.
             */
            if (end <= start) {
                end = targetEnd;
            }

            String chunk =
                    normalizedText
                            .substring(start, end)
                            .trim();

            if (!chunk.isBlank()) {
                chunks.add(chunk);
            }

            /*
             * We have reached the end of the document.
             */
            if (end >= normalizedText.length()) {
                break;
            }

            /*
             * Move backwards by the overlap amount so
             * adjacent chunks retain some context.
             */
            start =
                    Math.max(
                            end - overlap,
                            start + 1
                    );
        }

        return chunks;
    }

    /**
     * Attempts to split at a natural text boundary.
     *
     * Priority:
     *
     * 1. Paragraph
     * 2. New line
     * 3. Sentence
     * 4. Space
     * 5. Hard boundary
     */
    private int findBestSplitPosition(
            String text,
            int start,
            int targetEnd) {

        /*
         * Search only within the latter part of the
         * chunk so that we don't create extremely small
         * chunks.
         */
        int searchStart =
                start +
                (targetEnd - start) / 2;

        /*
         * -------------------------------------------------
         * 1. Paragraph boundary
         * -------------------------------------------------
         */

        int paragraph =
                text.lastIndexOf(
                        "\n\n",
                        targetEnd
                );

        if (paragraph >= searchStart) {
            return paragraph + 2;
        }

        /*
         * -------------------------------------------------
         * 2. New-line boundary
         * -------------------------------------------------
         */

        int newline =
                text.lastIndexOf(
                        "\n",
                        targetEnd
                );

        if (newline >= searchStart) {
            return newline + 1;
        }

        /*
         * -------------------------------------------------
         * 3. Sentence boundary
         * -------------------------------------------------
         */

        int period =
                text.lastIndexOf(
                        ". ",
                        targetEnd
                );

        int question =
                text.lastIndexOf(
                        "? ",
                        targetEnd
                );

        int exclamation =
                text.lastIndexOf(
                        "! ",
                        targetEnd
                );

        int sentenceBoundary =
                Math.max(
                        period,
                        Math.max(
                                question,
                                exclamation
                        )
                );

        if (sentenceBoundary >= searchStart) {
            return sentenceBoundary + 2;
        }

        /*
         * -------------------------------------------------
         * 4. Word boundary
         * -------------------------------------------------
         */

        int space =
                text.lastIndexOf(
                        ' ',
                        targetEnd
                );

        if (space >= searchStart) {
            return space + 1;
        }

        /*
         * -------------------------------------------------
         * 5. Hard boundary
         * -------------------------------------------------
         */

        return targetEnd;
    }

    /**
     * Normalizes whitespace while preserving paragraph
     * and line boundaries as much as possible.
     */
    private String normalizeText(String text) {

        return text
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }

    /**
     * Validates chunking parameters.
     */
    private void validateInput(
            String text,
            int chunkSize,
            int overlap) {

        if (text == null ||
                text.isBlank()) {

            throw new IllegalArgumentException(
                    "Knowledge text must not be null or blank"
            );
        }

        if (chunkSize < 100) {

            throw new IllegalArgumentException(
                    "Chunk size must be at least 100"
            );
        }

        if (overlap < 0) {

            throw new IllegalArgumentException(
                    "Chunk overlap must not be negative"
            );
        }

        if (overlap >= chunkSize) {

            throw new IllegalArgumentException(
                    "Chunk overlap must be smaller than chunk size"
            );
        }
    }
}