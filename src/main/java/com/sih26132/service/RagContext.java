package com.sih26132.service;

import java.util.List;

public class RagContext {

    private String query;

    private List<KnowledgeSearchService.KnowledgeSearchResult> results;

    private String combinedContext;

    public RagContext() {
    }

    public RagContext(
            String query,
            List<KnowledgeSearchService.KnowledgeSearchResult> results,
            String combinedContext) {

        this.query = query;
        this.results = results;
        this.combinedContext = combinedContext;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<KnowledgeSearchService.KnowledgeSearchResult> getResults() {
        return results;
    }

    public void setResults(
            List<KnowledgeSearchService.KnowledgeSearchResult> results) {

        this.results = results;
    }

    public String getCombinedContext() {
        return combinedContext;
    }

    public void setCombinedContext(String combinedContext) {
        this.combinedContext = combinedContext;
    }
}