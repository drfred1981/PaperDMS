package fr.smartprod.paperdms.ocr.service.dto;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ocr.domain.OcrJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrJobDTO implements Serializable {

    private Long id;

    @NotNull
    private OcrStatus status;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @NotNull
    @Size(max = 1000)
    private String s3Key;

    @NotNull
    @Size(max = 255)
    private String s3Bucket;

    @Size(max = 10)
    private String requestedLanguage;

    @Size(max = 10)
    private String detectedLanguage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double languageConfidence;

    private OcrEngine ocrEngine;

    @Size(max = 500)
    private String tikaEndpoint;

    @Size(max = 100)
    private String aiProvider;

    @Size(max = 100)
    private String aiModel;

    @Size(max = 128)
    private String resultCacheKey;

    @NotNull
    private Boolean isCached;

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

    private Long processingTime;

    private Double costEstimate;

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

    public OcrStatus getStatus() {
        return status;
    }

    public void setStatus(OcrStatus status) {
        this.status = status;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
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

    public String getRequestedLanguage() {
        return requestedLanguage;
    }

    public void setRequestedLanguage(String requestedLanguage) {
        this.requestedLanguage = requestedLanguage;
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getLanguageConfidence() {
        return languageConfidence;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public OcrEngine getOcrEngine() {
        return ocrEngine;
    }

    public void setOcrEngine(OcrEngine ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public String getTikaEndpoint() {
        return tikaEndpoint;
    }

    public void setTikaEndpoint(String tikaEndpoint) {
        this.tikaEndpoint = tikaEndpoint;
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

    public String getResultCacheKey() {
        return resultCacheKey;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return isCached;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
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

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Double getCostEstimate() {
        return costEstimate;
    }

    public void setCostEstimate(Double costEstimate) {
        this.costEstimate = costEstimate;
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
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", requestedLanguage='" + getRequestedLanguage() + "'" +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", languageConfidence=" + getLanguageConfidence() +
            ", ocrEngine='" + getOcrEngine() + "'" +
            ", tikaEndpoint='" + getTikaEndpoint() + "'" +
            ", aiProvider='" + getAiProvider() + "'" +
            ", aiModel='" + getAiModel() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", pageCount=" + getPageCount() +
            ", progress=" + getProgress() +
            ", retryCount=" + getRetryCount() +
            ", priority=" + getPriority() +
            ", processingTime=" + getProcessingTime() +
            ", costEstimate=" + getCostEstimate() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
