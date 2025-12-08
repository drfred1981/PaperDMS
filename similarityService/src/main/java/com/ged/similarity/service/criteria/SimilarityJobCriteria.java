package com.ged.similarity.service.criteria;

import com.ged.similarity.domain.enumeration.AiJobStatus;
import com.ged.similarity.domain.enumeration.SimilarityAlgorithm;
import com.ged.similarity.domain.enumeration.SimilarityScope;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.similarity.domain.SimilarityJob} entity. This class is used
 * in {@link com.ged.similarity.web.rest.SimilarityJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /similarity-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AiJobStatus
     */
    public static class AiJobStatusFilter extends Filter<AiJobStatus> {

        public AiJobStatusFilter() {}

        public AiJobStatusFilter(AiJobStatusFilter filter) {
            super(filter);
        }

        @Override
        public AiJobStatusFilter copy() {
            return new AiJobStatusFilter(this);
        }
    }

    /**
     * Class for filtering SimilarityAlgorithm
     */
    public static class SimilarityAlgorithmFilter extends Filter<SimilarityAlgorithm> {

        public SimilarityAlgorithmFilter() {}

        public SimilarityAlgorithmFilter(SimilarityAlgorithmFilter filter) {
            super(filter);
        }

        @Override
        public SimilarityAlgorithmFilter copy() {
            return new SimilarityAlgorithmFilter(this);
        }
    }

    /**
     * Class for filtering SimilarityScope
     */
    public static class SimilarityScopeFilter extends Filter<SimilarityScope> {

        public SimilarityScopeFilter() {}

        public SimilarityScopeFilter(SimilarityScopeFilter filter) {
            super(filter);
        }

        @Override
        public SimilarityScopeFilter copy() {
            return new SimilarityScopeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private AiJobStatusFilter status;

    private SimilarityAlgorithmFilter algorithm;

    private SimilarityScopeFilter scope;

    private DoubleFilter minSimilarityThreshold;

    private IntegerFilter matchesFound;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private InstantFilter createdDate;

    private StringFilter createdBy;

    private Boolean distinct;

    public SimilarityJobCriteria() {}

    public SimilarityJobCriteria(SimilarityJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AiJobStatusFilter::copy).orElse(null);
        this.algorithm = other.optionalAlgorithm().map(SimilarityAlgorithmFilter::copy).orElse(null);
        this.scope = other.optionalScope().map(SimilarityScopeFilter::copy).orElse(null);
        this.minSimilarityThreshold = other.optionalMinSimilarityThreshold().map(DoubleFilter::copy).orElse(null);
        this.matchesFound = other.optionalMatchesFound().map(IntegerFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SimilarityJobCriteria copy() {
        return new SimilarityJobCriteria(this);
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

    public AiJobStatusFilter getStatus() {
        return status;
    }

    public Optional<AiJobStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AiJobStatusFilter status() {
        if (status == null) {
            setStatus(new AiJobStatusFilter());
        }
        return status;
    }

    public void setStatus(AiJobStatusFilter status) {
        this.status = status;
    }

    public SimilarityAlgorithmFilter getAlgorithm() {
        return algorithm;
    }

    public Optional<SimilarityAlgorithmFilter> optionalAlgorithm() {
        return Optional.ofNullable(algorithm);
    }

    public SimilarityAlgorithmFilter algorithm() {
        if (algorithm == null) {
            setAlgorithm(new SimilarityAlgorithmFilter());
        }
        return algorithm;
    }

    public void setAlgorithm(SimilarityAlgorithmFilter algorithm) {
        this.algorithm = algorithm;
    }

    public SimilarityScopeFilter getScope() {
        return scope;
    }

    public Optional<SimilarityScopeFilter> optionalScope() {
        return Optional.ofNullable(scope);
    }

    public SimilarityScopeFilter scope() {
        if (scope == null) {
            setScope(new SimilarityScopeFilter());
        }
        return scope;
    }

    public void setScope(SimilarityScopeFilter scope) {
        this.scope = scope;
    }

    public DoubleFilter getMinSimilarityThreshold() {
        return minSimilarityThreshold;
    }

    public Optional<DoubleFilter> optionalMinSimilarityThreshold() {
        return Optional.ofNullable(minSimilarityThreshold);
    }

    public DoubleFilter minSimilarityThreshold() {
        if (minSimilarityThreshold == null) {
            setMinSimilarityThreshold(new DoubleFilter());
        }
        return minSimilarityThreshold;
    }

    public void setMinSimilarityThreshold(DoubleFilter minSimilarityThreshold) {
        this.minSimilarityThreshold = minSimilarityThreshold;
    }

    public IntegerFilter getMatchesFound() {
        return matchesFound;
    }

    public Optional<IntegerFilter> optionalMatchesFound() {
        return Optional.ofNullable(matchesFound);
    }

    public IntegerFilter matchesFound() {
        if (matchesFound == null) {
            setMatchesFound(new IntegerFilter());
        }
        return matchesFound;
    }

    public void setMatchesFound(IntegerFilter matchesFound) {
        this.matchesFound = matchesFound;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
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
        final SimilarityJobCriteria that = (SimilarityJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(algorithm, that.algorithm) &&
            Objects.equals(scope, that.scope) &&
            Objects.equals(minSimilarityThreshold, that.minSimilarityThreshold) &&
            Objects.equals(matchesFound, that.matchesFound) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            status,
            algorithm,
            scope,
            minSimilarityThreshold,
            matchesFound,
            startDate,
            endDate,
            createdDate,
            createdBy,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAlgorithm().map(f -> "algorithm=" + f + ", ").orElse("") +
            optionalScope().map(f -> "scope=" + f + ", ").orElse("") +
            optionalMinSimilarityThreshold().map(f -> "minSimilarityThreshold=" + f + ", ").orElse("") +
            optionalMatchesFound().map(f -> "matchesFound=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
