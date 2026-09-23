package com.sih26132.dto.knowledge;

import com.sih26132.service.KnowledgeSearchService.KnowledgeSearchResult;

import java.util.List;

public class RagRetrievalResponse {

    private String query;

    private int resultCount;

    private List<KnowledgeSearchResult> results;

    private String combinedContext;

    public RagRetrievalResponse() {
    }

    public RagRetrievalResponse(
            String query,
            int resultCount,
            List<KnowledgeSearchResult> results,
            String combinedContext) {

        this.query = query;
        this.resultCount = resultCount;
        this.results = results;
        this.combinedContext = combinedContext;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<KnowledgeSearchResult> getResults() {
        return results;
    }

    public void setResults(
            List<KnowledgeSearchResult> results) {

        this.results = results;
    }

    public String getCombinedContext() {
        return combinedContext;
    }

    public void setCombinedContext(String combinedContext) {
        this.combinedContext = combinedContext;
    }
}