package fr.smartprod.paperdms.search.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.search.domain.SearchSemantic} entity. This class is used
 * in {@link fr.smartprod.paperdms.search.web.rest.SearchSemanticResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /search-semantics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchSemanticCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter query;

    private StringFilter modelUsed;

    private LongFilter executionTime;

    private StringFilter userId;

    private InstantFilter searchDate;

    private Boolean distinct;

    public SearchSemanticCriteria() {}

    public SearchSemanticCriteria(SearchSemanticCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.query = other.optionalQuery().map(StringFilter::copy).orElse(null);
        this.modelUsed = other.optionalModelUsed().map(StringFilter::copy).orElse(null);
        this.executionTime = other.optionalExecutionTime().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.searchDate = other.optionalSearchDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SearchSemanticCriteria copy() {
        return new SearchSemanticCriteria(this);
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

    public StringFilter getQuery() {
        return query;
    }

    public Optional<StringFilter> optionalQuery() {
        return Optional.ofNullable(query);
    }

    public StringFilter query() {
        if (query == null) {
            setQuery(new StringFilter());
        }
        return query;
    }

    public void setQuery(StringFilter query) {
        this.query = query;
    }

    public StringFilter getModelUsed() {
        return modelUsed;
    }

    public Optional<StringFilter> optionalModelUsed() {
        return Optional.ofNullable(modelUsed);
    }

    public StringFilter modelUsed() {
        if (modelUsed == null) {
            setModelUsed(new StringFilter());
        }
        return modelUsed;
    }

    public void setModelUsed(StringFilter modelUsed) {
        this.modelUsed = modelUsed;
    }

    public LongFilter getExecutionTime() {
        return executionTime;
    }

    public Optional<LongFilter> optionalExecutionTime() {
        return Optional.ofNullable(executionTime);
    }

    public LongFilter executionTime() {
        if (executionTime == null) {
            setExecutionTime(new LongFilter());
        }
        return executionTime;
    }

    public void setExecutionTime(LongFilter executionTime) {
        this.executionTime = executionTime;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public InstantFilter getSearchDate() {
        return searchDate;
    }

    public Optional<InstantFilter> optionalSearchDate() {
        return Optional.ofNullable(searchDate);
    }

    public InstantFilter searchDate() {
        if (searchDate == null) {
            setSearchDate(new InstantFilter());
        }
        return searchDate;
    }

    public void setSearchDate(InstantFilter searchDate) {
        this.searchDate = searchDate;
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
        final SearchSemanticCriteria that = (SearchSemanticCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(query, that.query) &&
            Objects.equals(modelUsed, that.modelUsed) &&
            Objects.equals(executionTime, that.executionTime) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(searchDate, that.searchDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, modelUsed, executionTime, userId, searchDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchSemanticCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuery().map(f -> "query=" + f + ", ").orElse("") +
            optionalModelUsed().map(f -> "modelUsed=" + f + ", ").orElse("") +
            optionalExecutionTime().map(f -> "executionTime=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalSearchDate().map(f -> "searchDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
