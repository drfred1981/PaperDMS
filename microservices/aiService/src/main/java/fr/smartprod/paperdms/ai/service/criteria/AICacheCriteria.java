package fr.smartprod.paperdms.ai.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.ai.domain.AICache} entity. This class is used
 * in {@link fr.smartprod.paperdms.ai.web.rest.AICacheResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ai-caches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AICacheCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cacheKey;

    private StringFilter inputSha256;

    private StringFilter aiProvider;

    private StringFilter aiModel;

    private StringFilter operation;

    private StringFilter s3ResultKey;

    private DoubleFilter confidence;

    private IntegerFilter hits;

    private DoubleFilter cost;

    private InstantFilter lastAccessDate;

    private InstantFilter createdDate;

    private InstantFilter expirationDate;

    private Boolean distinct;

    public AICacheCriteria() {}

    public AICacheCriteria(AICacheCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cacheKey = other.optionalCacheKey().map(StringFilter::copy).orElse(null);
        this.inputSha256 = other.optionalInputSha256().map(StringFilter::copy).orElse(null);
        this.aiProvider = other.optionalAiProvider().map(StringFilter::copy).orElse(null);
        this.aiModel = other.optionalAiModel().map(StringFilter::copy).orElse(null);
        this.operation = other.optionalOperation().map(StringFilter::copy).orElse(null);
        this.s3ResultKey = other.optionals3ResultKey().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.hits = other.optionalHits().map(IntegerFilter::copy).orElse(null);
        this.cost = other.optionalCost().map(DoubleFilter::copy).orElse(null);
        this.lastAccessDate = other.optionalLastAccessDate().map(InstantFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.expirationDate = other.optionalExpirationDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AICacheCriteria copy() {
        return new AICacheCriteria(this);
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

    public StringFilter getCacheKey() {
        return cacheKey;
    }

    public Optional<StringFilter> optionalCacheKey() {
        return Optional.ofNullable(cacheKey);
    }

    public StringFilter cacheKey() {
        if (cacheKey == null) {
            setCacheKey(new StringFilter());
        }
        return cacheKey;
    }

    public void setCacheKey(StringFilter cacheKey) {
        this.cacheKey = cacheKey;
    }

    public StringFilter getInputSha256() {
        return inputSha256;
    }

    public Optional<StringFilter> optionalInputSha256() {
        return Optional.ofNullable(inputSha256);
    }

    public StringFilter inputSha256() {
        if (inputSha256 == null) {
            setInputSha256(new StringFilter());
        }
        return inputSha256;
    }

    public void setInputSha256(StringFilter inputSha256) {
        this.inputSha256 = inputSha256;
    }

    public StringFilter getAiProvider() {
        return aiProvider;
    }

    public Optional<StringFilter> optionalAiProvider() {
        return Optional.ofNullable(aiProvider);
    }

    public StringFilter aiProvider() {
        if (aiProvider == null) {
            setAiProvider(new StringFilter());
        }
        return aiProvider;
    }

    public void setAiProvider(StringFilter aiProvider) {
        this.aiProvider = aiProvider;
    }

    public StringFilter getAiModel() {
        return aiModel;
    }

    public Optional<StringFilter> optionalAiModel() {
        return Optional.ofNullable(aiModel);
    }

    public StringFilter aiModel() {
        if (aiModel == null) {
            setAiModel(new StringFilter());
        }
        return aiModel;
    }

    public void setAiModel(StringFilter aiModel) {
        this.aiModel = aiModel;
    }

    public StringFilter getOperation() {
        return operation;
    }

    public Optional<StringFilter> optionalOperation() {
        return Optional.ofNullable(operation);
    }

    public StringFilter operation() {
        if (operation == null) {
            setOperation(new StringFilter());
        }
        return operation;
    }

    public void setOperation(StringFilter operation) {
        this.operation = operation;
    }

    public StringFilter gets3ResultKey() {
        return s3ResultKey;
    }

    public Optional<StringFilter> optionals3ResultKey() {
        return Optional.ofNullable(s3ResultKey);
    }

    public StringFilter s3ResultKey() {
        if (s3ResultKey == null) {
            sets3ResultKey(new StringFilter());
        }
        return s3ResultKey;
    }

    public void sets3ResultKey(StringFilter s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public DoubleFilter getConfidence() {
        return confidence;
    }

    public Optional<DoubleFilter> optionalConfidence() {
        return Optional.ofNullable(confidence);
    }

    public DoubleFilter confidence() {
        if (confidence == null) {
            setConfidence(new DoubleFilter());
        }
        return confidence;
    }

    public void setConfidence(DoubleFilter confidence) {
        this.confidence = confidence;
    }

    public IntegerFilter getHits() {
        return hits;
    }

    public Optional<IntegerFilter> optionalHits() {
        return Optional.ofNullable(hits);
    }

    public IntegerFilter hits() {
        if (hits == null) {
            setHits(new IntegerFilter());
        }
        return hits;
    }

    public void setHits(IntegerFilter hits) {
        this.hits = hits;
    }

    public DoubleFilter getCost() {
        return cost;
    }

    public Optional<DoubleFilter> optionalCost() {
        return Optional.ofNullable(cost);
    }

    public DoubleFilter cost() {
        if (cost == null) {
            setCost(new DoubleFilter());
        }
        return cost;
    }

    public void setCost(DoubleFilter cost) {
        this.cost = cost;
    }

    public InstantFilter getLastAccessDate() {
        return lastAccessDate;
    }

    public Optional<InstantFilter> optionalLastAccessDate() {
        return Optional.ofNullable(lastAccessDate);
    }

    public InstantFilter lastAccessDate() {
        if (lastAccessDate == null) {
            setLastAccessDate(new InstantFilter());
        }
        return lastAccessDate;
    }

    public void setLastAccessDate(InstantFilter lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
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

    public InstantFilter getExpirationDate() {
        return expirationDate;
    }

    public Optional<InstantFilter> optionalExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public InstantFilter expirationDate() {
        if (expirationDate == null) {
            setExpirationDate(new InstantFilter());
        }
        return expirationDate;
    }

    public void setExpirationDate(InstantFilter expirationDate) {
        this.expirationDate = expirationDate;
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
        final AICacheCriteria that = (AICacheCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cacheKey, that.cacheKey) &&
            Objects.equals(inputSha256, that.inputSha256) &&
            Objects.equals(aiProvider, that.aiProvider) &&
            Objects.equals(aiModel, that.aiModel) &&
            Objects.equals(operation, that.operation) &&
            Objects.equals(s3ResultKey, that.s3ResultKey) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(hits, that.hits) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(lastAccessDate, that.lastAccessDate) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cacheKey,
            inputSha256,
            aiProvider,
            aiModel,
            operation,
            s3ResultKey,
            confidence,
            hits,
            cost,
            lastAccessDate,
            createdDate,
            expirationDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AICacheCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCacheKey().map(f -> "cacheKey=" + f + ", ").orElse("") +
            optionalInputSha256().map(f -> "inputSha256=" + f + ", ").orElse("") +
            optionalAiProvider().map(f -> "aiProvider=" + f + ", ").orElse("") +
            optionalAiModel().map(f -> "aiModel=" + f + ", ").orElse("") +
            optionalOperation().map(f -> "operation=" + f + ", ").orElse("") +
            optionals3ResultKey().map(f -> "s3ResultKey=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalHits().map(f -> "hits=" + f + ", ").orElse("") +
            optionalCost().map(f -> "cost=" + f + ", ").orElse("") +
            optionalLastAccessDate().map(f -> "lastAccessDate=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalExpirationDate().map(f -> "expirationDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
