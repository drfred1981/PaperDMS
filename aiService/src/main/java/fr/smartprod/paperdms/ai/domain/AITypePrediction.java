package fr.smartprod.paperdms.ai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AITypePrediction.
 */
@Entity
@Table(name = "ai_type_prediction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "aitypeprediction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AITypePrediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "document_type_name", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentTypeName;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence", nullable = false)
    private Double confidence;

    @Size(max = 500)
    @Column(name = "reason", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String reason;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String modelVersion;

    @Size(max = 1000)
    @Column(name = "prediction_s_3_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String predictionS3Key;

    @Column(name = "is_accepted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAccepted;

    @Size(max = 50)
    @Column(name = "accepted_by", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String acceptedBy;

    @Column(name = "accepted_date")
    private Instant acceptedDate;

    @NotNull
    @Column(name = "prediction_date", nullable = false)
    private Instant predictionDate;

    @JsonIgnoreProperties(
        value = { "aITypePrediction", "languagePrediction", "aITagPredictions", "aICorrespondentPredictions" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "aITypePrediction")
    @org.springframework.data.annotation.Transient
    private AIAutoTagJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AITypePrediction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    public AITypePrediction documentTypeName(String documentTypeName) {
        this.setDocumentTypeName(documentTypeName);
        return this;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public AITypePrediction confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getReason() {
        return this.reason;
    }

    public AITypePrediction reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public AITypePrediction modelVersion(String modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getPredictionS3Key() {
        return this.predictionS3Key;
    }

    public AITypePrediction predictionS3Key(String predictionS3Key) {
        this.setPredictionS3Key(predictionS3Key);
        return this;
    }

    public void setPredictionS3Key(String predictionS3Key) {
        this.predictionS3Key = predictionS3Key;
    }

    public Boolean getIsAccepted() {
        return this.isAccepted;
    }

    public AITypePrediction isAccepted(Boolean isAccepted) {
        this.setIsAccepted(isAccepted);
        return this;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getAcceptedBy() {
        return this.acceptedBy;
    }

    public AITypePrediction acceptedBy(String acceptedBy) {
        this.setAcceptedBy(acceptedBy);
        return this;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Instant getAcceptedDate() {
        return this.acceptedDate;
    }

    public AITypePrediction acceptedDate(Instant acceptedDate) {
        this.setAcceptedDate(acceptedDate);
        return this;
    }

    public void setAcceptedDate(Instant acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Instant getPredictionDate() {
        return this.predictionDate;
    }

    public AITypePrediction predictionDate(Instant predictionDate) {
        this.setPredictionDate(predictionDate);
        return this;
    }

    public void setPredictionDate(Instant predictionDate) {
        this.predictionDate = predictionDate;
    }

    public AIAutoTagJob getJob() {
        return this.job;
    }

    public void setJob(AIAutoTagJob aIAutoTagJob) {
        if (this.job != null) {
            this.job.setAITypePrediction(null);
        }
        if (aIAutoTagJob != null) {
            aIAutoTagJob.setAITypePrediction(this);
        }
        this.job = aIAutoTagJob;
    }

    public AITypePrediction job(AIAutoTagJob aIAutoTagJob) {
        this.setJob(aIAutoTagJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AITypePrediction)) {
            return false;
        }
        return getId() != null && getId().equals(((AITypePrediction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AITypePrediction{" +
            "id=" + getId() +
            ", documentTypeName='" + getDocumentTypeName() + "'" +
            ", confidence=" + getConfidence() +
            ", reason='" + getReason() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", predictionS3Key='" + getPredictionS3Key() + "'" +
            ", isAccepted='" + getIsAccepted() + "'" +
            ", acceptedBy='" + getAcceptedBy() + "'" +
            ", acceptedDate='" + getAcceptedDate() + "'" +
            ", predictionDate='" + getPredictionDate() + "'" +
            "}";
    }
}
