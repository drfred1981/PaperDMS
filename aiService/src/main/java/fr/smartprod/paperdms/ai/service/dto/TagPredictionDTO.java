package fr.smartprod.paperdms.ai.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ai.domain.TagPrediction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagPredictionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String tagName;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @Size(max = 500)
    private String reason;

    @Size(max = 50)
    private String modelVersion;

    @Size(max = 1000)
    private String predictionS3Key;

    private Boolean isAccepted;

    @Size(max = 50)
    private String acceptedBy;

    private Instant acceptedDate;

    @NotNull
    private Instant predictionDate;

    @NotNull
    private AutoTagJobDTO job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getPredictionS3Key() {
        return predictionS3Key;
    }

    public void setPredictionS3Key(String predictionS3Key) {
        this.predictionS3Key = predictionS3Key;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Instant getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Instant acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Instant getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(Instant predictionDate) {
        this.predictionDate = predictionDate;
    }

    public AutoTagJobDTO getJob() {
        return job;
    }

    public void setJob(AutoTagJobDTO job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagPredictionDTO)) {
            return false;
        }

        TagPredictionDTO tagPredictionDTO = (TagPredictionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagPredictionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagPredictionDTO{" +
            "id=" + getId() +
            ", tagName='" + getTagName() + "'" +
            ", confidence=" + getConfidence() +
            ", reason='" + getReason() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", predictionS3Key='" + getPredictionS3Key() + "'" +
            ", isAccepted='" + getIsAccepted() + "'" +
            ", acceptedBy='" + getAcceptedBy() + "'" +
            ", acceptedDate='" + getAcceptedDate() + "'" +
            ", predictionDate='" + getPredictionDate() + "'" +
            ", job=" + getJob() +
            "}";
    }
}
