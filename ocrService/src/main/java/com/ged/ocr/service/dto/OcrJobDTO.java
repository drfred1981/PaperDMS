package com.ged.ocr.service.dto;

import com.ged.ocr.domain.enumeration.OcrStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ocr.domain.OcrJob} entity.
 */
@Schema(description = "Travail OCR avec Apache Tika")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrJobDTO implements Serializable {

    private Long id;

    @NotNull
    private OcrStatus status;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 1000)
    private String s3Key;

    @NotNull
    @Size(max = 255)
    private String s3Bucket;

    @Size(max = 10)
    private String language;

    @Size(max = 500)
    private String tikaEndpoint;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    private Integer pageCount;

    @Min(value = 0)
    @Max(value = 100)
    private Integer progress;

    private Integer retryCount;

    private Integer priority;

    @NotNull
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    private TikaConfigurationDTO tikaConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OcrStatus getStatus() {
        return status;
    }

    public void setStatus(OcrStatus status) {
        this.status = status;
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

    public String gets3Bucket() {
        return s3Bucket;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTikaEndpoint() {
        return tikaEndpoint;
    }

    public void setTikaEndpoint(String tikaEndpoint) {
        this.tikaEndpoint = tikaEndpoint;
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

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public TikaConfigurationDTO getTikaConfig() {
        return tikaConfig;
    }

    public void setTikaConfig(TikaConfigurationDTO tikaConfig) {
        this.tikaConfig = tikaConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrJobDTO)) {
            return false;
        }

        OcrJobDTO ocrJobDTO = (OcrJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ocrJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrJobDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", documentId=" + getDocumentId() +
            ", s3Key='" + gets3Key() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", language='" + getLanguage() + "'" +
            ", tikaEndpoint='" + getTikaEndpoint() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", pageCount=" + getPageCount() +
            ", progress=" + getProgress() +
            ", retryCount=" + getRetryCount() +
            ", priority=" + getPriority() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", tikaConfig=" + getTikaConfig() +
            "}";
    }
}
