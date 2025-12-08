package com.ged.similarity.service.dto;

import com.ged.similarity.domain.enumeration.SimilarityAlgorithm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.similarity.domain.SimilarityCluster} entity.
 */
@Schema(description = "Cluster de documents similaires")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityClusterDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @NotNull
    private SimilarityAlgorithm algorithm;

    @Lob
    private String centroid;

    private Integer documentCount;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double avgSimilarity;

    @NotNull
    private Instant createdDate;

    private Instant lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getCentroid() {
        return centroid;
    }

    public void setCentroid(String centroid) {
        this.centroid = centroid;
    }

    public Integer getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Double getAvgSimilarity() {
        return avgSimilarity;
    }

    public void setAvgSimilarity(Double avgSimilarity) {
        this.avgSimilarity = avgSimilarity;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimilarityClusterDTO)) {
            return false;
        }

        SimilarityClusterDTO similarityClusterDTO = (SimilarityClusterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, similarityClusterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityClusterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", algorithm='" + getAlgorithm() + "'" +
            ", centroid='" + getCentroid() + "'" +
            ", documentCount=" + getDocumentCount() +
            ", avgSimilarity=" + getAvgSimilarity() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
