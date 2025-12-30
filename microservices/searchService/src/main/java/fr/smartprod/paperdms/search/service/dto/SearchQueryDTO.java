package fr.smartprod.paperdms.search.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.search.domain.SearchQuery} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchQueryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 1000)
    private String query;

    @Lob
    private String filters;

    private Integer resultCount;

    private Long executionTime;

    @Size(max = 50)
    private String userId;

    @NotNull
    private Instant searchDate;

    private Boolean isRelevant;

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

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
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

    public Boolean getIsRelevant() {
        return isRelevant;
    }

    public void setIsRelevant(Boolean isRelevant) {
        this.isRelevant = isRelevant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchQueryDTO)) {
            return false;
        }

        SearchQueryDTO searchQueryDTO = (SearchQueryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchQueryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchQueryDTO{" +
            "id=" + getId() +
            ", query='" + getQuery() + "'" +
            ", filters='" + getFilters() + "'" +
            ", resultCount=" + getResultCount() +
            ", executionTime=" + getExecutionTime() +
            ", userId='" + getUserId() + "'" +
            ", searchDate='" + getSearchDate() + "'" +
            ", isRelevant='" + getIsRelevant() + "'" +
            "}";
    }
}
