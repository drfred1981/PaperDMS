package fr.smartprod.paperdms.ai.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ai.domain.AICache} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AICacheDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String cacheKey;

    @NotNull
    @Size(max = 64)
    private String inputSha256;

    @NotNull
    @Size(max = 100)
    private String aiProvider;

    @Size(max = 100)
    private String aiModel;

    @NotNull
    @Size(max = 100)
    private String operation;

    @Lob
    private String inputData;

    @Lob
    private String resultData;

    @Size(max = 1000)
    private String s3ResultKey;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @Lob
    private String metadata;

    private Integer hits;

    private Double cost;

    private Instant lastAccessDate;

    @NotNull
    private Instant createdDate;

    private Instant expirationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getInputSha256() {
        return inputSha256;
    }

    public void setInputSha256(String inputSha256) {
        this.inputSha256 = inputSha256;
    }

    public String getAiProvider() {
        return aiProvider;
    }

    public void setAiProvider(String aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String getAiModel() {
        return aiModel;
    }

    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String gets3ResultKey() {
        return s3ResultKey;
    }

    public void sets3ResultKey(String s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Instant getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Instant lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AICacheDTO)) {
            return false;
        }

        AICacheDTO aICacheDTO = (AICacheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aICacheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AICacheDTO{" +
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
