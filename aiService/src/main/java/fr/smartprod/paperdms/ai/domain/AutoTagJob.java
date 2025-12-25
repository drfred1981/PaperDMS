package fr.smartprod.paperdms.ai.domain;

import fr.smartprod.paperdms.ai.domain.enumeration.AiJobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AutoTagJob.
 */
@Entity
@Table(name = "auto_tag_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AutoTagJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

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

    @Lob
    @Column(name = "extracted_text")
    private String extractedText;

    @Size(max = 64)
    @Column(name = "extracted_text_sha_256", length = 64)
    private String extractedTextSha256;

    @Size(max = 10)
    @Column(name = "detected_language", length = 10)
    private String detectedLanguage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "language_confidence")
    private Double languageConfidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AiJobStatus status;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    private String modelVersion;

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

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AutoTagJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public AutoTagJob documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public AutoTagJob documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public AutoTagJob s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getExtractedText() {
        return this.extractedText;
    }

    public AutoTagJob extractedText(String extractedText) {
        this.setExtractedText(extractedText);
        return this;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public String getExtractedTextSha256() {
        return this.extractedTextSha256;
    }

    public AutoTagJob extractedTextSha256(String extractedTextSha256) {
        this.setExtractedTextSha256(extractedTextSha256);
        return this;
    }

    public void setExtractedTextSha256(String extractedTextSha256) {
        this.extractedTextSha256 = extractedTextSha256;
    }

    public String getDetectedLanguage() {
        return this.detectedLanguage;
    }

    public AutoTagJob detectedLanguage(String detectedLanguage) {
        this.setDetectedLanguage(detectedLanguage);
        return this;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getLanguageConfidence() {
        return this.languageConfidence;
    }

    public AutoTagJob languageConfidence(Double languageConfidence) {
        this.setLanguageConfidence(languageConfidence);
        return this;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public AiJobStatus getStatus() {
        return this.status;
    }

    public AutoTagJob status(AiJobStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public AutoTagJob modelVersion(String modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getResultCacheKey() {
        return this.resultCacheKey;
    }

    public AutoTagJob resultCacheKey(String resultCacheKey) {
        this.setResultCacheKey(resultCacheKey);
        return this;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return this.isCached;
    }

    public AutoTagJob isCached(Boolean isCached) {
        this.setIsCached(isCached);
        return this;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public AutoTagJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public AutoTagJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public AutoTagJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public AutoTagJob confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AutoTagJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoTagJob)) {
            return false;
        }
        return getId() != null && getId().equals(((AutoTagJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutoTagJob{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", extractedText='" + getExtractedText() + "'" +
            ", extractedTextSha256='" + getExtractedTextSha256() + "'" +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", languageConfidence=" + getLanguageConfidence() +
            ", status='" + getStatus() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", confidence=" + getConfidence() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
