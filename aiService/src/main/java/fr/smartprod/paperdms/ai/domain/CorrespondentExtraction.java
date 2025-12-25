package fr.smartprod.paperdms.ai.domain;

import fr.smartprod.paperdms.ai.domain.enumeration.AiJobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CorrespondentExtraction.
 */
@Entity
@Table(name = "correspondent_extraction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrespondentExtraction implements Serializable {

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

    @Lob
    @Column(name = "extracted_text", nullable = false)
    private String extractedText;

    @NotNull
    @Size(max = 64)
    @Column(name = "extracted_text_sha_256", length = 64, nullable = false)
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

    @Size(max = 128)
    @Column(name = "result_cache_key", length = 128)
    private String resultCacheKey;

    @NotNull
    @Column(name = "is_cached", nullable = false)
    private Boolean isCached;

    @Size(max = 1000)
    @Column(name = "result_s_3_key", length = 1000)
    private String resultS3Key;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "senders_count")
    private Integer sendersCount;

    @Column(name = "recipients_count")
    private Integer recipientsCount;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CorrespondentExtraction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public CorrespondentExtraction documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public CorrespondentExtraction documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getExtractedText() {
        return this.extractedText;
    }

    public CorrespondentExtraction extractedText(String extractedText) {
        this.setExtractedText(extractedText);
        return this;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public String getExtractedTextSha256() {
        return this.extractedTextSha256;
    }

    public CorrespondentExtraction extractedTextSha256(String extractedTextSha256) {
        this.setExtractedTextSha256(extractedTextSha256);
        return this;
    }

    public void setExtractedTextSha256(String extractedTextSha256) {
        this.extractedTextSha256 = extractedTextSha256;
    }

    public String getDetectedLanguage() {
        return this.detectedLanguage;
    }

    public CorrespondentExtraction detectedLanguage(String detectedLanguage) {
        this.setDetectedLanguage(detectedLanguage);
        return this;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getLanguageConfidence() {
        return this.languageConfidence;
    }

    public CorrespondentExtraction languageConfidence(Double languageConfidence) {
        this.setLanguageConfidence(languageConfidence);
        return this;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public AiJobStatus getStatus() {
        return this.status;
    }

    public CorrespondentExtraction status(AiJobStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public String getResultCacheKey() {
        return this.resultCacheKey;
    }

    public CorrespondentExtraction resultCacheKey(String resultCacheKey) {
        this.setResultCacheKey(resultCacheKey);
        return this;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return this.isCached;
    }

    public CorrespondentExtraction isCached(Boolean isCached) {
        this.setIsCached(isCached);
        return this;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public String getResultS3Key() {
        return this.resultS3Key;
    }

    public CorrespondentExtraction resultS3Key(String resultS3Key) {
        this.setResultS3Key(resultS3Key);
        return this;
    }

    public void setResultS3Key(String resultS3Key) {
        this.resultS3Key = resultS3Key;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public CorrespondentExtraction startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public CorrespondentExtraction endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public CorrespondentExtraction errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getSendersCount() {
        return this.sendersCount;
    }

    public CorrespondentExtraction sendersCount(Integer sendersCount) {
        this.setSendersCount(sendersCount);
        return this;
    }

    public void setSendersCount(Integer sendersCount) {
        this.sendersCount = sendersCount;
    }

    public Integer getRecipientsCount() {
        return this.recipientsCount;
    }

    public CorrespondentExtraction recipientsCount(Integer recipientsCount) {
        this.setRecipientsCount(recipientsCount);
        return this;
    }

    public void setRecipientsCount(Integer recipientsCount) {
        this.recipientsCount = recipientsCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public CorrespondentExtraction createdDate(Instant createdDate) {
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
        if (!(o instanceof CorrespondentExtraction)) {
            return false;
        }
        return getId() != null && getId().equals(((CorrespondentExtraction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrespondentExtraction{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", extractedText='" + getExtractedText() + "'" +
            ", extractedTextSha256='" + getExtractedTextSha256() + "'" +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", languageConfidence=" + getLanguageConfidence() +
            ", status='" + getStatus() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", resultS3Key='" + getResultS3Key() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", sendersCount=" + getSendersCount() +
            ", recipientsCount=" + getRecipientsCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
