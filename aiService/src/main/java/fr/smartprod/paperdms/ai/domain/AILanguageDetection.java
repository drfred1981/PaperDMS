package fr.smartprod.paperdms.ai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.ai.domain.enumeration.AILanguageDetectionMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AILanguageDetection.
 */
@Entity
@Table(name = "ai_language_detection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ailanguagedetection")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AILanguageDetection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentSha256;

    @NotNull
    @Size(max = 10)
    @Column(name = "detected_language", length = 10, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detectedLanguage;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence", nullable = false)
    private Double confidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "detection_method")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AILanguageDetectionMethod detectionMethod;

    @Lob
    @Column(name = "alternative_languages")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String alternativeLanguages;

    @Lob
    @Column(name = "text_sample")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String textSample;

    @Size(max = 128)
    @Column(name = "result_cache_key", length = 128)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String resultCacheKey;

    @NotNull
    @Column(name = "is_cached", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isCached;

    @NotNull
    @Column(name = "detected_date", nullable = false)
    private Instant detectedDate;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String modelVersion;

    @JsonIgnoreProperties(
        value = { "aITypePrediction", "languagePrediction", "aITagPredictions", "aICorrespondentPredictions" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "languagePrediction")
    @org.springframework.data.annotation.Transient
    private AIAutoTagJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AILanguageDetection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public AILanguageDetection documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getDetectedLanguage() {
        return this.detectedLanguage;
    }

    public AILanguageDetection detectedLanguage(String detectedLanguage) {
        this.setDetectedLanguage(detectedLanguage);
        return this;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public AILanguageDetection confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public AILanguageDetectionMethod getDetectionMethod() {
        return this.detectionMethod;
    }

    public AILanguageDetection detectionMethod(AILanguageDetectionMethod detectionMethod) {
        this.setDetectionMethod(detectionMethod);
        return this;
    }

    public void setDetectionMethod(AILanguageDetectionMethod detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getAlternativeLanguages() {
        return this.alternativeLanguages;
    }

    public AILanguageDetection alternativeLanguages(String alternativeLanguages) {
        this.setAlternativeLanguages(alternativeLanguages);
        return this;
    }

    public void setAlternativeLanguages(String alternativeLanguages) {
        this.alternativeLanguages = alternativeLanguages;
    }

    public String getTextSample() {
        return this.textSample;
    }

    public AILanguageDetection textSample(String textSample) {
        this.setTextSample(textSample);
        return this;
    }

    public void setTextSample(String textSample) {
        this.textSample = textSample;
    }

    public String getResultCacheKey() {
        return this.resultCacheKey;
    }

    public AILanguageDetection resultCacheKey(String resultCacheKey) {
        this.setResultCacheKey(resultCacheKey);
        return this;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return this.isCached;
    }

    public AILanguageDetection isCached(Boolean isCached) {
        this.setIsCached(isCached);
        return this;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public Instant getDetectedDate() {
        return this.detectedDate;
    }

    public AILanguageDetection detectedDate(Instant detectedDate) {
        this.setDetectedDate(detectedDate);
        return this;
    }

    public void setDetectedDate(Instant detectedDate) {
        this.detectedDate = detectedDate;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public AILanguageDetection modelVersion(String modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public AIAutoTagJob getJob() {
        return this.job;
    }

    public void setJob(AIAutoTagJob aIAutoTagJob) {
        if (this.job != null) {
            this.job.setLanguagePrediction(null);
        }
        if (aIAutoTagJob != null) {
            aIAutoTagJob.setLanguagePrediction(this);
        }
        this.job = aIAutoTagJob;
    }

    public AILanguageDetection job(AIAutoTagJob aIAutoTagJob) {
        this.setJob(aIAutoTagJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AILanguageDetection)) {
            return false;
        }
        return getId() != null && getId().equals(((AILanguageDetection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AILanguageDetection{" +
            "id=" + getId() +
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
