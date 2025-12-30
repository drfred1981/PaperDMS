package fr.smartprod.paperdms.ai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.ai.domain.enumeration.AiJobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AIAutoTagJob.
 */
@Entity
@Table(name = "ai_auto_tag_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "aiautotagjob")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AIAutoTagJob implements Serializable {

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
    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Key;

    @Lob
    @Column(name = "extracted_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String extractedText;

    @Size(max = 64)
    @Column(name = "extracted_text_sha_256", length = 64)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String extractedTextSha256;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AiJobStatus status;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String modelVersion;

    @Size(max = 128)
    @Column(name = "result_cache_key", length = 128)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String resultCacheKey;

    @NotNull
    @Column(name = "is_cached", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isCached;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorMessage;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AITypePrediction aITypePrediction;

    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AILanguageDetection languagePrediction;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Set<AITagPrediction> aITagPredictions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Set<AICorrespondentPrediction> aICorrespondentPredictions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AIAutoTagJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public AIAutoTagJob documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public AIAutoTagJob s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getExtractedText() {
        return this.extractedText;
    }

    public AIAutoTagJob extractedText(String extractedText) {
        this.setExtractedText(extractedText);
        return this;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public String getExtractedTextSha256() {
        return this.extractedTextSha256;
    }

    public AIAutoTagJob extractedTextSha256(String extractedTextSha256) {
        this.setExtractedTextSha256(extractedTextSha256);
        return this;
    }

    public void setExtractedTextSha256(String extractedTextSha256) {
        this.extractedTextSha256 = extractedTextSha256;
    }

    public AiJobStatus getStatus() {
        return this.status;
    }

    public AIAutoTagJob status(AiJobStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public AIAutoTagJob modelVersion(String modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getResultCacheKey() {
        return this.resultCacheKey;
    }

    public AIAutoTagJob resultCacheKey(String resultCacheKey) {
        this.setResultCacheKey(resultCacheKey);
        return this;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return this.isCached;
    }

    public AIAutoTagJob isCached(Boolean isCached) {
        this.setIsCached(isCached);
        return this;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public AIAutoTagJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public AIAutoTagJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public AIAutoTagJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AIAutoTagJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public AITypePrediction getAITypePrediction() {
        return this.aITypePrediction;
    }

    public void setAITypePrediction(AITypePrediction aITypePrediction) {
        this.aITypePrediction = aITypePrediction;
    }

    public AIAutoTagJob aITypePrediction(AITypePrediction aITypePrediction) {
        this.setAITypePrediction(aITypePrediction);
        return this;
    }

    public AILanguageDetection getLanguagePrediction() {
        return this.languagePrediction;
    }

    public void setLanguagePrediction(AILanguageDetection aILanguageDetection) {
        this.languagePrediction = aILanguageDetection;
    }

    public AIAutoTagJob languagePrediction(AILanguageDetection aILanguageDetection) {
        this.setLanguagePrediction(aILanguageDetection);
        return this;
    }

    public Set<AITagPrediction> getAITagPredictions() {
        return this.aITagPredictions;
    }

    public void setAITagPredictions(Set<AITagPrediction> aITagPredictions) {
        if (this.aITagPredictions != null) {
            this.aITagPredictions.forEach(i -> i.setJob(null));
        }
        if (aITagPredictions != null) {
            aITagPredictions.forEach(i -> i.setJob(this));
        }
        this.aITagPredictions = aITagPredictions;
    }

    public AIAutoTagJob aITagPredictions(Set<AITagPrediction> aITagPredictions) {
        this.setAITagPredictions(aITagPredictions);
        return this;
    }

    public AIAutoTagJob addAITagPredictions(AITagPrediction aITagPrediction) {
        this.aITagPredictions.add(aITagPrediction);
        aITagPrediction.setJob(this);
        return this;
    }

    public AIAutoTagJob removeAITagPredictions(AITagPrediction aITagPrediction) {
        this.aITagPredictions.remove(aITagPrediction);
        aITagPrediction.setJob(null);
        return this;
    }

    public Set<AICorrespondentPrediction> getAICorrespondentPredictions() {
        return this.aICorrespondentPredictions;
    }

    public void setAICorrespondentPredictions(Set<AICorrespondentPrediction> aICorrespondentPredictions) {
        if (this.aICorrespondentPredictions != null) {
            this.aICorrespondentPredictions.forEach(i -> i.setJob(null));
        }
        if (aICorrespondentPredictions != null) {
            aICorrespondentPredictions.forEach(i -> i.setJob(this));
        }
        this.aICorrespondentPredictions = aICorrespondentPredictions;
    }

    public AIAutoTagJob aICorrespondentPredictions(Set<AICorrespondentPrediction> aICorrespondentPredictions) {
        this.setAICorrespondentPredictions(aICorrespondentPredictions);
        return this;
    }

    public AIAutoTagJob addAICorrespondentPredictions(AICorrespondentPrediction aICorrespondentPrediction) {
        this.aICorrespondentPredictions.add(aICorrespondentPrediction);
        aICorrespondentPrediction.setJob(this);
        return this;
    }

    public AIAutoTagJob removeAICorrespondentPredictions(AICorrespondentPrediction aICorrespondentPrediction) {
        this.aICorrespondentPredictions.remove(aICorrespondentPrediction);
        aICorrespondentPrediction.setJob(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AIAutoTagJob)) {
            return false;
        }
        return getId() != null && getId().equals(((AIAutoTagJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AIAutoTagJob{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", extractedText='" + getExtractedText() + "'" +
            ", extractedTextSha256='" + getExtractedTextSha256() + "'" +
            ", status='" + getStatus() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
