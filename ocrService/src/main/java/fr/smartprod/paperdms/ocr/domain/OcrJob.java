package fr.smartprod.paperdms.ocr.domain;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OcrJob.
 */
@Entity
@Table(name = "ocr_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OcrStatus status;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    private String documentSha256;

    @NotNull
    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000, nullable = false)
    private String s3Key;

    @NotNull
    @Size(max = 255)
    @Column(name = "s_3_bucket", length = 255, nullable = false)
    private String s3Bucket;

    @Size(max = 10)
    @Column(name = "requested_language", length = 10)
    private String requestedLanguage;

    @Size(max = 10)
    @Column(name = "detected_language", length = 10)
    private String detectedLanguage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "language_confidence")
    private Double languageConfidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "ocr_engine")
    private OcrEngine ocrEngine;

    @Size(max = 500)
    @Column(name = "tika_endpoint", length = 500)
    private String tikaEndpoint;

    @Size(max = 100)
    @Column(name = "ai_provider", length = 100)
    private String aiProvider;

    @Size(max = 100)
    @Column(name = "ai_model", length = 100)
    private String aiModel;

    @Size(max = 128)
    @Column(name = "result_cache_key", length = 128)
    private String resultCacheKey;

    @NotNull
    @Column(name = "is_cached", nullable = false)
    private Boolean isCached;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "page_count")
    private Integer pageCount;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "progress")
    private Integer progress;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "processing_time")
    private Long processingTime;

    @Column(name = "cost_estimate")
    private Double costEstimate;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OcrJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OcrStatus getStatus() {
        return this.status;
    }

    public OcrJob status(OcrStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OcrStatus status) {
        this.status = status;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public OcrJob documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public OcrJob documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public OcrJob s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3Bucket() {
        return this.s3Bucket;
    }

    public OcrJob s3Bucket(String s3Bucket) {
        this.sets3Bucket(s3Bucket);
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getRequestedLanguage() {
        return this.requestedLanguage;
    }

    public OcrJob requestedLanguage(String requestedLanguage) {
        this.setRequestedLanguage(requestedLanguage);
        return this;
    }

    public void setRequestedLanguage(String requestedLanguage) {
        this.requestedLanguage = requestedLanguage;
    }

    public String getDetectedLanguage() {
        return this.detectedLanguage;
    }

    public OcrJob detectedLanguage(String detectedLanguage) {
        this.setDetectedLanguage(detectedLanguage);
        return this;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getLanguageConfidence() {
        return this.languageConfidence;
    }

    public OcrJob languageConfidence(Double languageConfidence) {
        this.setLanguageConfidence(languageConfidence);
        return this;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public OcrEngine getOcrEngine() {
        return this.ocrEngine;
    }

    public OcrJob ocrEngine(OcrEngine ocrEngine) {
        this.setOcrEngine(ocrEngine);
        return this;
    }

    public void setOcrEngine(OcrEngine ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public String getTikaEndpoint() {
        return this.tikaEndpoint;
    }

    public OcrJob tikaEndpoint(String tikaEndpoint) {
        this.setTikaEndpoint(tikaEndpoint);
        return this;
    }

    public void setTikaEndpoint(String tikaEndpoint) {
        this.tikaEndpoint = tikaEndpoint;
    }

    public String getAiProvider() {
        return this.aiProvider;
    }

    public OcrJob aiProvider(String aiProvider) {
        this.setAiProvider(aiProvider);
        return this;
    }

    public void setAiProvider(String aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String getAiModel() {
        return this.aiModel;
    }

    public OcrJob aiModel(String aiModel) {
        this.setAiModel(aiModel);
        return this;
    }

    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }

    public String getResultCacheKey() {
        return this.resultCacheKey;
    }

    public OcrJob resultCacheKey(String resultCacheKey) {
        this.setResultCacheKey(resultCacheKey);
        return this;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return this.isCached;
    }

    public OcrJob isCached(Boolean isCached) {
        this.setIsCached(isCached);
        return this;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public OcrJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public OcrJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public OcrJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public OcrJob pageCount(Integer pageCount) {
        this.setPageCount(pageCount);
        return this;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getProgress() {
        return this.progress;
    }

    public OcrJob progress(Integer progress) {
        this.setProgress(progress);
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public OcrJob retryCount(Integer retryCount) {
        this.setRetryCount(retryCount);
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public OcrJob priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getProcessingTime() {
        return this.processingTime;
    }

    public OcrJob processingTime(Long processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Double getCostEstimate() {
        return this.costEstimate;
    }

    public OcrJob costEstimate(Double costEstimate) {
        this.setCostEstimate(costEstimate);
        return this;
    }

    public void setCostEstimate(Double costEstimate) {
        this.costEstimate = costEstimate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public OcrJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public OcrJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrJob)) {
            return false;
        }
        return getId() != null && getId().equals(((OcrJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrJob{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", documentId=" + getDocumentId() +
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
