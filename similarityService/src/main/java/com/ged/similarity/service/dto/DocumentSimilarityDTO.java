package com.ged.similarity.service.dto;

import com.ged.similarity.domain.enumeration.SimilarityAlgorithm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.similarity.domain.DocumentSimilarity} entity.
 */
@Schema(description = "Similarit� entre documents")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentSimilarityDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId1;

    @NotNull
    private Long documentId2;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double similarityScore;

    @NotNull
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

    public Long getDocumentId1() {
        return documentId1;
    }

    public void setDocumentId1(Long documentId1) {
        this.documentId1 = documentId1;
    }

    public Long getDocumentId2() {
        return documentId2;
    }

    public void setDocumentId2(Long documentId2) {
        this.documentId2 = documentId2;
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
        if (!(o instanceof DocumentSimilarityDTO)) {
            return false;
        }

        DocumentSimilarityDTO documentSimilarityDTO = (DocumentSimilarityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentSimilarityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSimilarityDTO{" +
            "id=" + getId() +
            ", documentId1=" + getDocumentId1() +
            ", documentId2=" + getDocumentId2() +
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
