package fr.smartprod.paperdms.similarity.service.criteria;

import fr.smartprod.paperdms.similarity.domain.enumeration.FingerprintType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint} entity. This class is used
 * in {@link fr.smartprod.paperdms.similarity.web.rest.SimilarityDocumentFingerprintResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /similarity-document-fingerprints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityDocumentFingerprintCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FingerprintType
     */
    public static class FingerprintTypeFilter extends Filter<FingerprintType> {

        public FingerprintTypeFilter() {}

        public FingerprintTypeFilter(FingerprintTypeFilter filter) {
            super(filter);
        }

        @Override
        public FingerprintTypeFilter copy() {
            return new FingerprintTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FingerprintTypeFilter fingerprintType;

    private InstantFilter computedDate;

    private InstantFilter lastUpdated;

    private Boolean distinct;

    public SimilarityDocumentFingerprintCriteria() {}

    public SimilarityDocumentFingerprintCriteria(SimilarityDocumentFingerprintCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fingerprintType = other.optionalFingerprintType().map(FingerprintTypeFilter::copy).orElse(null);
        this.computedDate = other.optionalComputedDate().map(InstantFilter::copy).orElse(null);
        this.lastUpdated = other.optionalLastUpdated().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SimilarityDocumentFingerprintCriteria copy() {
        return new SimilarityDocumentFingerprintCriteria(this);
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

    public FingerprintTypeFilter getFingerprintType() {
        return fingerprintType;
    }

    public Optional<FingerprintTypeFilter> optionalFingerprintType() {
        return Optional.ofNullable(fingerprintType);
    }

    public FingerprintTypeFilter fingerprintType() {
        if (fingerprintType == null) {
            setFingerprintType(new FingerprintTypeFilter());
        }
        return fingerprintType;
    }

    public void setFingerprintType(FingerprintTypeFilter fingerprintType) {
        this.fingerprintType = fingerprintType;
    }

    public InstantFilter getComputedDate() {
        return computedDate;
    }

    public Optional<InstantFilter> optionalComputedDate() {
        return Optional.ofNullable(computedDate);
    }

    public InstantFilter computedDate() {
        if (computedDate == null) {
            setComputedDate(new InstantFilter());
        }
        return computedDate;
    }

    public void setComputedDate(InstantFilter computedDate) {
        this.computedDate = computedDate;
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
        final SimilarityDocumentFingerprintCriteria that = (SimilarityDocumentFingerprintCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fingerprintType, that.fingerprintType) &&
            Objects.equals(computedDate, that.computedDate) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fingerprintType, computedDate, lastUpdated, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityDocumentFingerprintCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFingerprintType().map(f -> "fingerprintType=" + f + ", ").orElse("") +
            optionalComputedDate().map(f -> "computedDate=" + f + ", ").orElse("") +
            optionalLastUpdated().map(f -> "lastUpdated=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
