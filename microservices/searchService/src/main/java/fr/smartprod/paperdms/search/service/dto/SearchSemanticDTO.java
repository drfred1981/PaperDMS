package fr.smartprod.paperdms.search.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.search.domain.SearchSemantic} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchSemanticDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 1000)
    private String query;

    @Lob
    private String queryEmbedding;

    @Lob
    private String results;

    @Lob
    private String relevanceScores;

    @Size(max = 100)
    private String modelUsed;

    private Long executionTime;

    @Size(max = 50)
    private String userId;

    @NotNull
    private Instant searchDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryEmbedding() {
        return queryEmbedding;
    }

    public void setQueryEmbedding(String queryEmbedding) {
        this.queryEmbedding = queryEmbedding;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRelevanceScores() {
        return relevanceScores;
    }

    public void setRelevanceScores(String relevanceScores) {
        this.relevanceScores = relevanceScores;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Instant searchDate) {
        this.searchDate = searchDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchSemanticDTO)) {
            return false;
        }

        SearchSemanticDTO searchSemanticDTO = (SearchSemanticDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchSemanticDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchSemanticDTO{" +
            "id=" + getId() +
            ", query='" + getQuery() + "'" +
            ", queryEmbedding='" + getQueryEmbedding() + "'" +
            ", results='" + getResults() + "'" +
            ", relevanceScores='" + getRelevanceScores() + "'" +
            ", modelUsed='" + getModelUsed() + "'" +
            ", executionTime=" + getExecutionTime() +
            ", userId='" + getUserId() + "'" +
            ", searchDate='" + getSearchDate() + "'" +
            "}";
    }
}
