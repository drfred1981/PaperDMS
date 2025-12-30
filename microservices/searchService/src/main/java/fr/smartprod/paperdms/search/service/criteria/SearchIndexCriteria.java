package fr.smartprod.paperdms.search.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.search.domain.SearchIndex} entity. This class is used
 * in {@link fr.smartprod.paperdms.search.web.rest.SearchIndexResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /search-indices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchIndexCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentSha256;

    private StringFilter tags;

    private StringFilter correspondents;

    private InstantFilter indexedDate;

    private InstantFilter lastUpdated;

    private Boolean distinct;

    public SearchIndexCriteria() {}

    public SearchIndexCriteria(SearchIndexCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.tags = other.optionalTags().map(StringFilter::copy).orElse(null);
        this.correspondents = other.optionalCorrespondents().map(StringFilter::copy).orElse(null);
        this.indexedDate = other.optionalIndexedDate().map(InstantFilter::copy).orElse(null);
        this.lastUpdated = other.optionalLastUpdated().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SearchIndexCriteria copy() {
        return new SearchIndexCriteria(this);
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

    public StringFilter getDocumentSha256() {
        return documentSha256;
    }

    public Optional<StringFilter> optionalDocumentSha256() {
        return Optional.ofNullable(documentSha256);
    }

    public StringFilter documentSha256() {
        if (documentSha256 == null) {
            setDocumentSha256(new StringFilter());
        }
        return documentSha256;
    }

    public void setDocumentSha256(StringFilter documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public StringFilter getTags() {
        return tags;
    }

    public Optional<StringFilter> optionalTags() {
        return Optional.ofNullable(tags);
    }

    public StringFilter tags() {
        if (tags == null) {
            setTags(new StringFilter());
        }
        return tags;
    }

    public void setTags(StringFilter tags) {
        this.tags = tags;
    }

    public StringFilter getCorrespondents() {
        return correspondents;
    }

    public Optional<StringFilter> optionalCorrespondents() {
        return Optional.ofNullable(correspondents);
    }

    public StringFilter correspondents() {
        if (correspondents == null) {
            setCorrespondents(new StringFilter());
        }
        return correspondents;
    }

    public void setCorrespondents(StringFilter correspondents) {
        this.correspondents = correspondents;
    }

    public InstantFilter getIndexedDate() {
        return indexedDate;
    }

    public Optional<InstantFilter> optionalIndexedDate() {
        return Optional.ofNullable(indexedDate);
    }

    public InstantFilter indexedDate() {
        if (indexedDate == null) {
            setIndexedDate(new InstantFilter());
        }
        return indexedDate;
    }

    public void setIndexedDate(InstantFilter indexedDate) {
        this.indexedDate = indexedDate;
    }

    public InstantFilter getLastUpdated() {
        return lastUpdated;
    }

    public Optional<InstantFilter> optionalLastUpdated() {
        return Optional.ofNullable(lastUpdated);
    }

    public InstantFilter lastUpdated() {
        if (lastUpdated == null) {
            setLastUpdated(new InstantFilter());
        }
        return lastUpdated;
    }

    public void setLastUpdated(InstantFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
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
        final SearchIndexCriteria that = (SearchIndexCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(tags, that.tags) &&
            Objects.equals(correspondents, that.correspondents) &&
            Objects.equals(indexedDate, that.indexedDate) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentSha256, tags, correspondents, indexedDate, lastUpdated, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchIndexCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalTags().map(f -> "tags=" + f + ", ").orElse("") +
            optionalCorrespondents().map(f -> "correspondents=" + f + ", ").orElse("") +
            optionalIndexedDate().map(f -> "indexedDate=" + f + ", ").orElse("") +
            optionalLastUpdated().map(f -> "lastUpdated=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
