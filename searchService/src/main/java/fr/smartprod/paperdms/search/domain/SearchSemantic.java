package fr.smartprod.paperdms.search.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SearchSemantic.
 */
@Entity
@Table(name = "search_semantic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "searchsemantic")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchSemantic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "query", length = 1000, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String query;

    @Lob
    @Column(name = "query_embedding", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String queryEmbedding;

    @Lob
    @Column(name = "results", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String results;

    @Lob
    @Column(name = "relevance_scores")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String relevanceScores;

    @Size(max = 100)
    @Column(name = "model_used", length = 100)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String modelUsed;

    @Column(name = "execution_time")
    private Long executionTime;

    @Size(max = 50)
    @Column(name = "user_id", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String userId;

    @NotNull
    @Column(name = "search_date", nullable = false)
    private Instant searchDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchSemantic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public SearchSemantic query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryEmbedding() {
        return this.queryEmbedding;
    }

    public SearchSemantic queryEmbedding(String queryEmbedding) {
        this.setQueryEmbedding(queryEmbedding);
        return this;
    }

    public void setQueryEmbedding(String queryEmbedding) {
        this.queryEmbedding = queryEmbedding;
    }

    public String getResults() {
        return this.results;
    }

    public SearchSemantic results(String results) {
        this.setResults(results);
        return this;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRelevanceScores() {
        return this.relevanceScores;
    }

    public SearchSemantic relevanceScores(String relevanceScores) {
        this.setRelevanceScores(relevanceScores);
        return this;
    }

    public void setRelevanceScores(String relevanceScores) {
        this.relevanceScores = relevanceScores;
    }

    public String getModelUsed() {
        return this.modelUsed;
    }

    public SearchSemantic modelUsed(String modelUsed) {
        this.setModelUsed(modelUsed);
        return this;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public Long getExecutionTime() {
        return this.executionTime;
    }

    public SearchSemantic executionTime(Long executionTime) {
        this.setExecutionTime(executionTime);
        return this;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public SearchSemantic userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getSearchDate() {
        return this.searchDate;
    }

    public SearchSemantic searchDate(Instant searchDate) {
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
        if (!(o instanceof SearchSemantic)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchSemantic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchSemantic{" +
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
