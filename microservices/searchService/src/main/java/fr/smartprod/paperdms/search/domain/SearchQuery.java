package fr.smartprod.paperdms.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SearchQuery.
 */
@Entity
@Table(name = "search_query")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "searchquery")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchQuery implements Serializable {

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
    @Column(name = "filters")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String filters;

    @Column(name = "result_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer resultCount;

    @Column(name = "execution_time")
    private Long executionTime;

    @Size(max = 50)
    @Column(name = "user_id", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String userId;

    @NotNull
    @Column(name = "search_date", nullable = false)
    private Instant searchDate;

    @Column(name = "is_relevant")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isRelevant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchQuery")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "searchQuery" }, allowSetters = true)
    private Set<SearchFacet> facets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchQuery id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public SearchQuery query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFilters() {
        return this.filters;
    }

    public SearchQuery filters(String filters) {
        this.setFilters(filters);
        return this;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Integer getResultCount() {
        return this.resultCount;
    }

    public SearchQuery resultCount(Integer resultCount) {
        this.setResultCount(resultCount);
        return this;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public Long getExecutionTime() {
        return this.executionTime;
    }

    public SearchQuery executionTime(Long executionTime) {
        this.setExecutionTime(executionTime);
        return this;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public SearchQuery userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getSearchDate() {
        return this.searchDate;
    }

    public SearchQuery searchDate(Instant searchDate) {
        this.setSearchDate(searchDate);
        return this;
    }

    public void setSearchDate(Instant searchDate) {
        this.searchDate = searchDate;
    }

    public Boolean getIsRelevant() {
        return this.isRelevant;
    }

    public SearchQuery isRelevant(Boolean isRelevant) {
        this.setIsRelevant(isRelevant);
        return this;
    }

    public void setIsRelevant(Boolean isRelevant) {
        this.isRelevant = isRelevant;
    }

    public Set<SearchFacet> getFacets() {
        return this.facets;
    }

    public void setFacets(Set<SearchFacet> searchFacets) {
        if (this.facets != null) {
            this.facets.forEach(i -> i.setSearchQuery(null));
        }
        if (searchFacets != null) {
            searchFacets.forEach(i -> i.setSearchQuery(this));
        }
        this.facets = searchFacets;
    }

    public SearchQuery facets(Set<SearchFacet> searchFacets) {
        this.setFacets(searchFacets);
        return this;
    }

    public SearchQuery addFacets(SearchFacet searchFacet) {
        this.facets.add(searchFacet);
        searchFacet.setSearchQuery(this);
        return this;
    }

    public SearchQuery removeFacets(SearchFacet searchFacet) {
        this.facets.remove(searchFacet);
        searchFacet.setSearchQuery(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchQuery)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchQuery) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchQuery{" +
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
