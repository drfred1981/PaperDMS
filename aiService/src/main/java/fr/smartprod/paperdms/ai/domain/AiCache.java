package fr.smartprod.paperdms.ai.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AiCache.
 */
@Entity
@Table(name = "ai_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AiCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "cache_key", length = 128, nullable = false, unique = true)
    private String cacheKey;

    @NotNull
    @Size(max = 64)
    @Column(name = "input_sha_256", length = 64, nullable = false)
    private String inputSha256;

    @NotNull
    @Size(max = 100)
    @Column(name = "ai_provider", length = 100, nullable = false)
    private String aiProvider;

    @Size(max = 100)
    @Column(name = "ai_model", length = 100)
    private String aiModel;

    @NotNull
    @Size(max = 100)
    @Column(name = "operation", length = 100, nullable = false)
    private String operation;

    @Lob
    @Column(name = "input_data")
    private String inputData;

    @Lob
    @Column(name = "result_data")
    private String resultData;

    @Size(max = 1000)
    @Column(name = "s_3_result_key", length = 1000)
    private String s3ResultKey;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Column(name = "hits")
    private Integer hits;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "last_access_date")
    private Instant lastAccessDate;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AiCache id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public AiCache cacheKey(String cacheKey) {
        this.setCacheKey(cacheKey);
        return this;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getInputSha256() {
        return this.inputSha256;
    }

    public AiCache inputSha256(String inputSha256) {
        this.setInputSha256(inputSha256);
        return this;
    }

    public void setInputSha256(String inputSha256) {
        this.inputSha256 = inputSha256;
    }

    public String getAiProvider() {
        return this.aiProvider;
    }

    public AiCache aiProvider(String aiProvider) {
        this.setAiProvider(aiProvider);
        return this;
    }

    public void setAiProvider(String aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String getAiModel() {
        return this.aiModel;
    }

    public AiCache aiModel(String aiModel) {
        this.setAiModel(aiModel);
        return this;
    }

    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }

    public String getOperation() {
        return this.operation;
    }

    public AiCache operation(String operation) {
        this.setOperation(operation);
        return this;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getInputData() {
        return this.inputData;
    }

    public AiCache inputData(String inputData) {
        this.setInputData(inputData);
        return this;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getResultData() {
        return this.resultData;
    }

    public AiCache resultData(String resultData) {
        this.setResultData(resultData);
        return this;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String gets3ResultKey() {
        return this.s3ResultKey;
    }

    public AiCache s3ResultKey(String s3ResultKey) {
        this.sets3ResultKey(s3ResultKey);
        return this;
    }

    public void sets3ResultKey(String s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public AiCache confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public AiCache metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getHits() {
        return this.hits;
    }

    public AiCache hits(Integer hits) {
        this.setHits(hits);
        return this;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Double getCost() {
        return this.cost;
    }

    public AiCache cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Instant getLastAccessDate() {
        return this.lastAccessDate;
    }

    public AiCache lastAccessDate(Instant lastAccessDate) {
        this.setLastAccessDate(lastAccessDate);
        return this;
    }

    public void setLastAccessDate(Instant lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AiCache createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public AiCache expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AiCache)) {
            return false;
        }
        return getId() != null && getId().equals(((AiCache) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AiCache{" +
            "id=" + getId() +
            ", cacheKey='" + getCacheKey() + "'" +
            ", inputSha256='" + getInputSha256() + "'" +
            ", aiProvider='" + getAiProvider() + "'" +
            ", aiModel='" + getAiModel() + "'" +
            ", operation='" + getOperation() + "'" +
            ", inputData='" + getInputData() + "'" +
            ", resultData='" + getResultData() + "'" +
            ", s3ResultKey='" + gets3ResultKey() + "'" +
            ", confidence=" + getConfidence() +
            ", metadata='" + getMetadata() + "'" +
            ", hits=" + getHits() +
            ", cost=" + getCost() +
            ", lastAccessDate='" + getLastAccessDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
