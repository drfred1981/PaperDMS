package fr.smartprod.paperdms.pdftoimage.service.dto;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionBatchDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String batchName;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Instant createdAt;

    @NotNull
    private ConversionStatus status;

    @Min(value = 0)
    private Integer totalConversions;

    @Min(value = 0)
    private Integer completedConversions;

    @Min(value = 0)
    private Integer failedConversions;

    private Instant startedAt;

    private Instant completedAt;

    private Long totalProcessingDuration;

    private Long createdByUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ConversionStatus getStatus() {
        return status;
    }

    public void setStatus(ConversionStatus status) {
        this.status = status;
    }

    public Integer getTotalConversions() {
        return totalConversions;
    }

    public void setTotalConversions(Integer totalConversions) {
        this.totalConversions = totalConversions;
    }

    public Integer getCompletedConversions() {
        return completedConversions;
    }

    public void setCompletedConversions(Integer completedConversions) {
        this.completedConversions = completedConversions;
    }

    public Integer getFailedConversions() {
        return failedConversions;
    }

    public void setFailedConversions(Integer failedConversions) {
        this.failedConversions = failedConversions;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getTotalProcessingDuration() {
        return totalProcessingDuration;
    }

    public void setTotalProcessingDuration(Long totalProcessingDuration) {
        this.totalProcessingDuration = totalProcessingDuration;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionBatchDTO)) {
            return false;
        }

        ImageConversionBatchDTO imageConversionBatchDTO = (ImageConversionBatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageConversionBatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionBatchDTO{" +
            "id=" + getId() +
            ", batchName='" + getBatchName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalConversions=" + getTotalConversions() +
            ", completedConversions=" + getCompletedConversions() +
            ", failedConversions=" + getFailedConversions() +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", totalProcessingDuration=" + getTotalProcessingDuration() +
            ", createdByUserId=" + getCreatedByUserId() +
            "}";
    }
}
