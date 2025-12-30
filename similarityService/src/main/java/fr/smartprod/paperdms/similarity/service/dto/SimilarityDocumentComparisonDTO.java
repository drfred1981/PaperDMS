package fr.smartprod.paperdms.similarity.service.dto;

import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityDocumentComparisonDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String sourceDocumentSha256;

    @NotNull
    @Size(max = 64)
    private String targetDocumentSha256;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double similarityScore;

    private SimilarityAlgorithm algorithm;

    @Lob
    private String features;

    @NotNull
    private Instant computedDate;

    private Boolean isRelevant;

    @Size(max = 50)
    private String reviewedBy;

    private Instant reviewedDate;

    private SimilarityJobDTO job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceDocumentSha256() {
        return sourceDocumentSha256;
    }

    public void setSourceDocumentSha256(String sourceDocumentSha256) {
        this.sourceDocumentSha256 = sourceDocumentSha256;
    }

    public String getTargetDocumentSha256() {
        return targetDocumentSha256;
    }

    public void setTargetDocumentSha256(String targetDocumentSha256) {
        this.targetDocumentSha256 = targetDocumentSha256;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Instant getComputedDate() {
        return computedDate;
    }

    public void setComputedDate(Instant computedDate) {
        this.computedDate = computedDate;
    }

    public Boolean getIsRelevant() {
        return isRelevant;
    }

    public void setIsRelevant(Boolean isRelevant) {
        this.isRelevant = isRelevant;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Instant getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(Instant reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public SimilarityJobDTO getJob() {
        return job;
    }

    public void setJob(SimilarityJobDTO job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimilarityDocumentComparisonDTO)) {
            return false;
        }

        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = (SimilarityDocumentComparisonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, similarityDocumentComparisonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityDocumentComparisonDTO{" +
            "id=" + getId() +
            ", sourceDocumentSha256='" + getSourceDocumentSha256() + "'" +
            ", targetDocumentSha256='" + getTargetDocumentSha256() + "'" +
            ", similarityScore=" + getSimilarityScore() +
            ", algorithm='" + getAlgorithm() + "'" +
            ", features='" + getFeatures() + "'" +
            ", computedDate='" + getComputedDate() + "'" +
            ", isRelevant='" + getIsRelevant() + "'" +
            ", reviewedBy='" + getReviewedBy() + "'" +
            ", reviewedDate='" + getReviewedDate() + "'" +
            ", job=" + getJob() +
            "}";
    }
}
