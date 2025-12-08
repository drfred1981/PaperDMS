package com.ged.ai.service.dto;

import com.ged.ai.domain.enumeration.AiJobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ai.domain.AutoTagJob} entity.
 */
@Schema(description = "Travail de tagging automatique")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AutoTagJobDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 1000)
    private String s3Key;

    @Lob
    private String extractedText;

    @NotNull
    private AiJobStatus status;

    @Size(max = 50)
    private String modelVersion;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @NotNull
    private Instant createdDate;

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

    public String gets3Key() {
        return s3Key;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public AiJobStatus getStatus() {
        return status;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
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

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoTagJobDTO)) {
            return false;
        }

        AutoTagJobDTO autoTagJobDTO = (AutoTagJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, autoTagJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutoTagJobDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", s3Key='" + gets3Key() + "'" +
            ", extractedText='" + getExtractedText() + "'" +
            ", status='" + getStatus() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", confidence=" + getConfidence() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
