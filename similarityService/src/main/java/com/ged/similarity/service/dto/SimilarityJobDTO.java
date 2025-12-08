package com.ged.similarity.service.dto;

import com.ged.similarity.domain.enumeration.AiJobStatus;
import com.ged.similarity.domain.enumeration.SimilarityAlgorithm;
import com.ged.similarity.domain.enumeration.SimilarityScope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.similarity.domain.SimilarityJob} entity.
 */
@Schema(description = "Travail de calcul de similarit�")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityJobDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private AiJobStatus status;

    @NotNull
    private SimilarityAlgorithm algorithm;

    @NotNull
    private SimilarityScope scope;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double minSimilarityThreshold;

    private Integer matchesFound;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    @NotNull
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public AiJobStatus getStatus() {
        return status;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public SimilarityScope getScope() {
        return scope;
    }

    public void setScope(SimilarityScope scope) {
        this.scope = scope;
    }

    public Double getMinSimilarityThreshold() {
        return minSimilarityThreshold;
    }

    public void setMinSimilarityThreshold(Double minSimilarityThreshold) {
        this.minSimilarityThreshold = minSimilarityThreshold;
    }

    public Integer getMatchesFound() {
        return matchesFound;
    }

    public void setMatchesFound(Integer matchesFound) {
        this.matchesFound = matchesFound;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimilarityJobDTO)) {
            return false;
        }

        SimilarityJobDTO similarityJobDTO = (SimilarityJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, similarityJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityJobDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", status='" + getStatus() + "'" +
            ", algorithm='" + getAlgorithm() + "'" +
            ", scope='" + getScope() + "'" +
            ", minSimilarityThreshold=" + getMinSimilarityThreshold() +
            ", matchesFound=" + getMatchesFound() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
