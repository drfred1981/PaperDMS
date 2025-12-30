package fr.smartprod.paperdms.search.service.dto;

import fr.smartprod.paperdms.search.domain.enumeration.FacetType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.search.domain.SearchFacet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchFacetDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String facetName;

    @NotNull
    private FacetType facetType;

    @Lob
    private String values;

    @Lob
    private String counts;

    private SearchQueryDTO searchQuery;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacetName() {
        return facetName;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    public FacetType getFacetType() {
        return facetType;
    }

    public void setFacetType(FacetType facetType) {
        this.facetType = facetType;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public SearchQueryDTO getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQueryDTO searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchFacetDTO)) {
            return false;
        }

        SearchFacetDTO searchFacetDTO = (SearchFacetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchFacetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchFacetDTO{" +
            "id=" + getId() +
            ", facetName='" + getFacetName() + "'" +
            ", facetType='" + getFacetType() + "'" +
            ", values='" + getValues() + "'" +
            ", counts='" + getCounts() + "'" +
            ", searchQuery=" + getSearchQuery() +
            "}";
    }
}
