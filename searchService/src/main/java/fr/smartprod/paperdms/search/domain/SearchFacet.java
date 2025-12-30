package fr.smartprod.paperdms.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.search.domain.enumeration.FacetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SearchFacet.
 */
@Entity
@Table(name = "search_facet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "searchfacet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchFacet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "facet_name", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String facetName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "facet_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private FacetType facetType;

    @Lob
    @Column(name = "values", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String values;

    @Lob
    @Column(name = "counts", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String counts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facets" }, allowSetters = true)
    private SearchQuery searchQuery;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchFacet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacetName() {
        return this.facetName;
    }

    public SearchFacet facetName(String facetName) {
        this.setFacetName(facetName);
        return this;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    public FacetType getFacetType() {
        return this.facetType;
    }

    public SearchFacet facetType(FacetType facetType) {
        this.setFacetType(facetType);
        return this;
    }

    public void setFacetType(FacetType facetType) {
        this.facetType = facetType;
    }

    public String getValues() {
        return this.values;
    }

    public SearchFacet values(String values) {
        this.setValues(values);
        return this;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getCounts() {
        return this.counts;
    }

    public SearchFacet counts(String counts) {
        this.setCounts(counts);
        return this;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public SearchQuery getSearchQuery() {
        return this.searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchFacet searchQuery(SearchQuery searchQuery) {
        this.setSearchQuery(searchQuery);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchFacet)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchFacet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchFacet{" +
            "id=" + getId() +
            ", facetName='" + getFacetName() + "'" +
            ", facetType='" + getFacetType() + "'" +
            ", values='" + getValues() + "'" +
            ", counts='" + getCounts() + "'" +
            "}";
    }
}
