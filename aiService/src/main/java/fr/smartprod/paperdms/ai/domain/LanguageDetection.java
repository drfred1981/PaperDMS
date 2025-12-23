package fr.smartprod.paperdms.ai.domain;

import fr.smartprod.paperdms.ai.domain.enumeration.LanguageDetectionMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LanguageDetection.
 */
@Entity
@Table(name = "language_detection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LanguageDetection implements Serializable {

    @Serial
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
    @Size(max = 10)
    @Column(name = "detected_language", length = 10, nullable = false)
    private String detectedLanguage;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence", nullable = false)
    private Double confidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "detection_method")
    private LanguageDetectionMethod detectionMethod;

    @Lob
    @Column(name = "alternative_languages")
    private String alternativeLanguages;

    @Lob
    @Column(name = "text_sample")
    private String textSample;

    @Size(max = 128)
    @Column(name = "result_cache_key", length = 128)
    private String resultCacheKey;

    @NotNull
    @Column(name = "is_cached", nullable = false)
    private Boolean isCached;

    @NotNull
    @Column(name = "detected_date", nullable = false)
    private Instant detectedDate;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    private String modelVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LanguageDetection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public LanguageDetection documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public LanguageDetection documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getDetectedLanguage() {
        return this.detectedLanguage;
    }

    public LanguageDetection detectedLanguage(String detectedLanguage) {
        this.setDetectedLanguage(detectedLanguage);
        return this;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public LanguageDetection confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public LanguageDetectionMethod getDetectionMethod() {
        return this.detectionMethod;
    }

    public LanguageDetection detectionMethod(LanguageDetectionMethod detectionMethod) {
        this.setDetectionMethod(detectionMethod);
        return this;
    }

    public void setDetectionMethod(LanguageDetectionMethod detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getAlternativeLanguages() {
        return this.alternativeLanguages;
    }

    public LanguageDetection alternativeLanguages(String alternativeLanguages) {
        this.setAlternativeLanguages(alternativeLanguages);
        return this;
    }

    public void setAlternativeLanguages(String alternativeLanguages) {
        this.alternativeLanguages = alternativeLanguages;
    }

    public String getTextSample() {
        return this.textSample;
    }

    public LanguageDetection textSample(String textSample) {
        this.setTextSample(textSample);
        return this;
    }

    public void setTextSample(String textSample) {
        this.textSample = textSample;
    }

    public String getResultCacheKey() {
        return this.resultCacheKey;
    }

    public LanguageDetection resultCacheKey(String resultCacheKey) {
        this.setResultCacheKey(resultCacheKey);
        return this;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return this.isCached;
    }

    public LanguageDetection isCached(Boolean isCached) {
        this.setIsCached(isCached);
        return this;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public Instant getDetectedDate() {
        return this.detectedDate;
    }

    public LanguageDetection detectedDate(Instant detectedDate) {
        this.setDetectedDate(detectedDate);
        return this;
    }

    public void setDetectedDate(Instant detectedDate) {
        this.detectedDate = detectedDate;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public LanguageDetection modelVersion(String modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LanguageDetection)) {
            return false;
        }
        return getId() != null && getId().equals(((LanguageDetection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageDetection{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", confidence=" + getConfidence() +
            ", detectionMethod='" + getDetectionMethod() + "'" +
            ", alternativeLanguages='" + getAlternativeLanguages() + "'" +
            ", textSample='" + getTextSample() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", detectedDate='" + getDetectedDate() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            "}";
    }
}
