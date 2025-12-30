package fr.smartprod.paperdms.similarity.service.dto;

import fr.smartprod.paperdms.similarity.domain.enumeration.AiJobStatus;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityScope;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.similarity.domain.SimilarityJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityJobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    private AiJobStatus status;

    private SimilarityAlgorithm algorithm;

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

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
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
            ", documentSha256='" + getDocumentSha256() + "'" +
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
