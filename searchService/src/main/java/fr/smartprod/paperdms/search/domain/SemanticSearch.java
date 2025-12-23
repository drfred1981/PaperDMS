package fr.smartprod.paperdms.search.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SemanticSearch.
 */
@Entity
@Table(name = "semantic_search")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SemanticSearch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "query", length = 1000, nullable = false)
    private String query;

    @Lob
    @Column(name = "query_embedding", nullable = false)
    private String queryEmbedding;

    @Lob
    @Column(name = "results", nullable = false)
    private String results;

    @Lob
    @Column(name = "relevance_scores")
    private String relevanceScores;

    @Size(max = 100)
    @Column(name = "model_used", length = 100)
    private String modelUsed;

    @Column(name = "execution_time")
    private Long executionTime;

    @Size(max = 50)
    @Column(name = "user_id", length = 50)
    private String userId;

    @NotNull
    @Column(name = "search_date", nullable = false)
    private Instant searchDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SemanticSearch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public SemanticSearch query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryEmbedding() {
        return this.queryEmbedding;
    }

    public SemanticSearch queryEmbedding(String queryEmbedding) {
        this.setQueryEmbedding(queryEmbedding);
        return this;
    }

    public void setQueryEmbedding(String queryEmbedding) {
        this.queryEmbedding = queryEmbedding;
    }

    public String getResults() {
        return this.results;
    }

    public SemanticSearch results(String results) {
        this.setResults(results);
        return this;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRelevanceScores() {
        return this.relevanceScores;
    }

    public SemanticSearch relevanceScores(String relevanceScores) {
        this.setRelevanceScores(relevanceScores);
        return this;
    }

    public void setRelevanceScores(String relevanceScores) {
        this.relevanceScores = relevanceScores;
    }

    public String getModelUsed() {
        return this.modelUsed;
    }

    public SemanticSearch modelUsed(String modelUsed) {
        this.setModelUsed(modelUsed);
        return this;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public Long getExecutionTime() {
        return this.executionTime;
    }

    public SemanticSearch executionTime(Long executionTime) {
        this.setExecutionTime(executionTime);
        return this;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public SemanticSearch userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getSearchDate() {
        return this.searchDate;
    }

    public SemanticSearch searchDate(Instant searchDate) {
        this.setSearchDate(searchDate);
        return this;
    }

    public void setSearchDate(Instant searchDate) {
        this.searchDate = searchDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemanticSearch)) {
            return false;
        }
        return getId() != null && getId().equals(((SemanticSearch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemanticSearch{" +
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
