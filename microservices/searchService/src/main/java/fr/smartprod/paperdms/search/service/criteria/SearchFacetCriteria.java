package fr.smartprod.paperdms.search.service.criteria;

import fr.smartprod.paperdms.search.domain.enumeration.FacetType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.search.domain.SearchFacet} entity. This class is used
 * in {@link fr.smartprod.paperdms.search.web.rest.SearchFacetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /search-facets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchFacetCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FacetType
     */
    public static class FacetTypeFilter extends Filter<FacetType> {

        public FacetTypeFilter() {}

        public FacetTypeFilter(FacetTypeFilter filter) {
            super(filter);
        }

        @Override
        public FacetTypeFilter copy() {
            return new FacetTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter facetName;

    private FacetTypeFilter facetType;

    private LongFilter searchQueryId;

    private Boolean distinct;

    public SearchFacetCriteria() {}

    public SearchFacetCriteria(SearchFacetCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.facetName = other.optionalFacetName().map(StringFilter::copy).orElse(null);
        this.facetType = other.optionalFacetType().map(FacetTypeFilter::copy).orElse(null);
        this.searchQueryId = other.optionalSearchQueryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SearchFacetCriteria copy() {
        return new SearchFacetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFacetName() {
        return facetName;
    }

    public Optional<StringFilter> optionalFacetName() {
        return Optional.ofNullable(facetName);
    }

    public StringFilter facetName() {
        if (facetName == null) {
            setFacetName(new StringFilter());
        }
        return facetName;
    }

    public void setFacetName(StringFilter facetName) {
        this.facetName = facetName;
    }

    public FacetTypeFilter getFacetType() {
        return facetType;
    }

    public Optional<FacetTypeFilter> optionalFacetType() {
        return Optional.ofNullable(facetType);
    }

    public FacetTypeFilter facetType() {
        if (facetType == null) {
            setFacetType(new FacetTypeFilter());
        }
        return facetType;
    }

    public void setFacetType(FacetTypeFilter facetType) {
        this.facetType = facetType;
    }

    public LongFilter getSearchQueryId() {
        return searchQueryId;
    }

    public Optional<LongFilter> optionalSearchQueryId() {
        return Optional.ofNullable(searchQueryId);
    }

    public LongFilter searchQueryId() {
        if (searchQueryId == null) {
            setSearchQueryId(new LongFilter());
        }
        return searchQueryId;
    }

    public void setSearchQueryId(LongFilter searchQueryId) {
        this.searchQueryId = searchQueryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SearchFacetCriteria that = (SearchFacetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(facetName, that.facetName) &&
            Objects.equals(facetType, that.facetType) &&
            Objects.equals(searchQueryId, that.searchQueryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, facetName, facetType, searchQueryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchFacetCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFacetName().map(f -> "facetName=" + f + ", ").orElse("") +
            optionalFacetType().map(f -> "facetType=" + f + ", ").orElse("") +
            optionalSearchQueryId().map(f -> "searchQueryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
