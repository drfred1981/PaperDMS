package fr.smartprod.paperdms.similarity.service.criteria;

import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.similarity.domain.DocumentSimilarity} entity. This class is used
 * in {@link fr.smartprod.paperdms.similarity.web.rest.DocumentSimilarityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-similarities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentSimilarityCriteria implements Serializable, Criteria {

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

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId1;

    private LongFilter documentId2;

    private DoubleFilter similarityScore;

    private SimilarityAlgorithmFilter algorithm;

    private InstantFilter computedDate;

    private BooleanFilter isRelevant;

    private StringFilter reviewedBy;

    private InstantFilter reviewedDate;

    private LongFilter jobId;

    private Boolean distinct;

    public DocumentSimilarityCriteria() {}

    public DocumentSimilarityCriteria(DocumentSimilarityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId1 = other.optionalDocumentId1().map(LongFilter::copy).orElse(null);
        this.documentId2 = other.optionalDocumentId2().map(LongFilter::copy).orElse(null);
        this.similarityScore = other.optionalSimilarityScore().map(DoubleFilter::copy).orElse(null);
        this.algorithm = other.optionalAlgorithm().map(SimilarityAlgorithmFilter::copy).orElse(null);
        this.computedDate = other.optionalComputedDate().map(InstantFilter::copy).orElse(null);
        this.isRelevant = other.optionalIsRelevant().map(BooleanFilter::copy).orElse(null);
        this.reviewedBy = other.optionalReviewedBy().map(StringFilter::copy).orElse(null);
        this.reviewedDate = other.optionalReviewedDate().map(InstantFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentSimilarityCriteria copy() {
        return new DocumentSimilarityCriteria(this);
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

    public LongFilter getDocumentId1() {
        return documentId1;
    }

    public Optional<LongFilter> optionalDocumentId1() {
        return Optional.ofNullable(documentId1);
    }

    public LongFilter documentId1() {
        if (documentId1 == null) {
            setDocumentId1(new LongFilter());
        }
        return documentId1;
    }

    public void setDocumentId1(LongFilter documentId1) {
        this.documentId1 = documentId1;
    }

    public LongFilter getDocumentId2() {
        return documentId2;
    }

    public Optional<LongFilter> optionalDocumentId2() {
        return Optional.ofNullable(documentId2);
    }

    public LongFilter documentId2() {
        if (documentId2 == null) {
            setDocumentId2(new LongFilter());
        }
        return documentId2;
    }

    public void setDocumentId2(LongFilter documentId2) {
        this.documentId2 = documentId2;
    }

    public DoubleFilter getSimilarityScore() {
        return similarityScore;
    }

    public Optional<DoubleFilter> optionalSimilarityScore() {
        return Optional.ofNullable(similarityScore);
    }

    public DoubleFilter similarityScore() {
        if (similarityScore == null) {
            setSimilarityScore(new DoubleFilter());
        }
        return similarityScore;
    }

    public void setSimilarityScore(DoubleFilter similarityScore) {
        this.similarityScore = similarityScore;
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

    public BooleanFilter getIsRelevant() {
        return isRelevant;
    }

    public Optional<BooleanFilter> optionalIsRelevant() {
        return Optional.ofNullable(isRelevant);
    }

    public BooleanFilter isRelevant() {
        if (isRelevant == null) {
            setIsRelevant(new BooleanFilter());
        }
        return isRelevant;
    }

    public void setIsRelevant(BooleanFilter isRelevant) {
        this.isRelevant = isRelevant;
    }

    public StringFilter getReviewedBy() {
        return reviewedBy;
    }

    public Optional<StringFilter> optionalReviewedBy() {
        return Optional.ofNullable(reviewedBy);
    }

    public StringFilter reviewedBy() {
        if (reviewedBy == null) {
            setReviewedBy(new StringFilter());
        }
        return reviewedBy;
    }

    public void setReviewedBy(StringFilter reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public InstantFilter getReviewedDate() {
        return reviewedDate;
    }

    public Optional<InstantFilter> optionalReviewedDate() {
        return Optional.ofNullable(reviewedDate);
    }

    public InstantFilter reviewedDate() {
        if (reviewedDate == null) {
            setReviewedDate(new InstantFilter());
        }
        return reviewedDate;
    }

    public void setReviewedDate(InstantFilter reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public Optional<LongFilter> optionalJobId() {
        return Optional.ofNullable(jobId);
    }

    public LongFilter jobId() {
        if (jobId == null) {
            setJobId(new LongFilter());
        }
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
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
        final DocumentSimilarityCriteria that = (DocumentSimilarityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId1, that.documentId1) &&
            Objects.equals(documentId2, that.documentId2) &&
            Objects.equals(similarityScore, that.similarityScore) &&
            Objects.equals(algorithm, that.algorithm) &&
            Objects.equals(computedDate, that.computedDate) &&
            Objects.equals(isRelevant, that.isRelevant) &&
            Objects.equals(reviewedBy, that.reviewedBy) &&
            Objects.equals(reviewedDate, that.reviewedDate) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId1,
            documentId2,
            similarityScore,
            algorithm,
            computedDate,
            isRelevant,
            reviewedBy,
            reviewedDate,
            jobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSimilarityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId1().map(f -> "documentId1=" + f + ", ").orElse("") +
            optionalDocumentId2().map(f -> "documentId2=" + f + ", ").orElse("") +
            optionalSimilarityScore().map(f -> "similarityScore=" + f + ", ").orElse("") +
            optionalAlgorithm().map(f -> "algorithm=" + f + ", ").orElse("") +
            optionalComputedDate().map(f -> "computedDate=" + f + ", ").orElse("") +
            optionalIsRelevant().map(f -> "isRelevant=" + f + ", ").orElse("") +
            optionalReviewedBy().map(f -> "reviewedBy=" + f + ", ").orElse("") +
            optionalReviewedDate().map(f -> "reviewedDate=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
