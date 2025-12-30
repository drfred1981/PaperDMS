package fr.smartprod.paperdms.similarity.service.criteria;

import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.similarity.domain.SimilarityCluster} entity. This class is used
 * in {@link fr.smartprod.paperdms.similarity.web.rest.SimilarityClusterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /similarity-clusters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityClusterCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private SimilarityAlgorithmFilter algorithm;

    private IntegerFilter documentCount;

    private DoubleFilter avgSimilarity;

    private InstantFilter createdDate;

    private InstantFilter lastUpdated;

    private Boolean distinct;

    public SimilarityClusterCriteria() {}

    public SimilarityClusterCriteria(SimilarityClusterCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.algorithm = other.optionalAlgorithm().map(SimilarityAlgorithmFilter::copy).orElse(null);
        this.documentCount = other.optionalDocumentCount().map(IntegerFilter::copy).orElse(null);
        this.avgSimilarity = other.optionalAvgSimilarity().map(DoubleFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastUpdated = other.optionalLastUpdated().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SimilarityClusterCriteria copy() {
        return new SimilarityClusterCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public IntegerFilter getDocumentCount() {
        return documentCount;
    }

    public Optional<IntegerFilter> optionalDocumentCount() {
        return Optional.ofNullable(documentCount);
    }

    public IntegerFilter documentCount() {
        if (documentCount == null) {
            setDocumentCount(new IntegerFilter());
        }
        return documentCount;
    }

    public void setDocumentCount(IntegerFilter documentCount) {
        this.documentCount = documentCount;
    }

    public DoubleFilter getAvgSimilarity() {
        return avgSimilarity;
    }

    public Optional<DoubleFilter> optionalAvgSimilarity() {
        return Optional.ofNullable(avgSimilarity);
    }

    public DoubleFilter avgSimilarity() {
        if (avgSimilarity == null) {
            setAvgSimilarity(new DoubleFilter());
        }
        return avgSimilarity;
    }

    public void setAvgSimilarity(DoubleFilter avgSimilarity) {
        this.avgSimilarity = avgSimilarity;
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
        final SimilarityClusterCriteria that = (SimilarityClusterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(algorithm, that.algorithm) &&
            Objects.equals(documentCount, that.documentCount) &&
            Objects.equals(avgSimilarity, that.avgSimilarity) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, algorithm, documentCount, avgSimilarity, createdDate, lastUpdated, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityClusterCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAlgorithm().map(f -> "algorithm=" + f + ", ").orElse("") +
            optionalDocumentCount().map(f -> "documentCount=" + f + ", ").orElse("") +
            optionalAvgSimilarity().map(f -> "avgSimilarity=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastUpdated().map(f -> "lastUpdated=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
