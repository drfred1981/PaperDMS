package fr.smartprod.paperdms.document.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentStatistics} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentStatisticsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-statistics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentStatisticsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter viewsTotal;

    private IntegerFilter downloadsTotal;

    private IntegerFilter uniqueViewers;

    private InstantFilter lastUpdated;

    private LongFilter documentId;

    private Boolean distinct;

    public DocumentStatisticsCriteria() {}

    public DocumentStatisticsCriteria(DocumentStatisticsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.viewsTotal = other.optionalViewsTotal().map(IntegerFilter::copy).orElse(null);
        this.downloadsTotal = other.optionalDownloadsTotal().map(IntegerFilter::copy).orElse(null);
        this.uniqueViewers = other.optionalUniqueViewers().map(IntegerFilter::copy).orElse(null);
        this.lastUpdated = other.optionalLastUpdated().map(InstantFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentStatisticsCriteria copy() {
        return new DocumentStatisticsCriteria(this);
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

    public IntegerFilter getViewsTotal() {
        return viewsTotal;
    }

    public Optional<IntegerFilter> optionalViewsTotal() {
        return Optional.ofNullable(viewsTotal);
    }

    public IntegerFilter viewsTotal() {
        if (viewsTotal == null) {
            setViewsTotal(new IntegerFilter());
        }
        return viewsTotal;
    }

    public void setViewsTotal(IntegerFilter viewsTotal) {
        this.viewsTotal = viewsTotal;
    }

    public IntegerFilter getDownloadsTotal() {
        return downloadsTotal;
    }

    public Optional<IntegerFilter> optionalDownloadsTotal() {
        return Optional.ofNullable(downloadsTotal);
    }

    public IntegerFilter downloadsTotal() {
        if (downloadsTotal == null) {
            setDownloadsTotal(new IntegerFilter());
        }
        return downloadsTotal;
    }

    public void setDownloadsTotal(IntegerFilter downloadsTotal) {
        this.downloadsTotal = downloadsTotal;
    }

    public IntegerFilter getUniqueViewers() {
        return uniqueViewers;
    }

    public Optional<IntegerFilter> optionalUniqueViewers() {
        return Optional.ofNullable(uniqueViewers);
    }

    public IntegerFilter uniqueViewers() {
        if (uniqueViewers == null) {
            setUniqueViewers(new IntegerFilter());
        }
        return uniqueViewers;
    }

    public void setUniqueViewers(IntegerFilter uniqueViewers) {
        this.uniqueViewers = uniqueViewers;
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

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
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
        final DocumentStatisticsCriteria that = (DocumentStatisticsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(viewsTotal, that.viewsTotal) &&
            Objects.equals(downloadsTotal, that.downloadsTotal) &&
            Objects.equals(uniqueViewers, that.uniqueViewers) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, viewsTotal, downloadsTotal, uniqueViewers, lastUpdated, documentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentStatisticsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalViewsTotal().map(f -> "viewsTotal=" + f + ", ").orElse("") +
            optionalDownloadsTotal().map(f -> "downloadsTotal=" + f + ", ").orElse("") +
            optionalUniqueViewers().map(f -> "uniqueViewers=" + f + ", ").orElse("") +
            optionalLastUpdated().map(f -> "lastUpdated=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
