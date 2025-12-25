package fr.smartprod.paperdms.ai.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TagPrediction.
 */
@Entity
@Table(name = "tag_prediction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagPrediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "tag_name", length = 100, nullable = false)
    private String tagName;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence", nullable = false)
    private Double confidence;

    @Size(max = 500)
    @Column(name = "reason", length = 500)
    private String reason;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    private String modelVersion;

    @Size(max = 1000)
    @Column(name = "prediction_s_3_key", length = 1000)
    private String predictionS3Key;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Size(max = 50)
    @Column(name = "accepted_by", length = 50)
    private String acceptedBy;

    @Column(name = "accepted_date")
    private Instant acceptedDate;

    @NotNull
    @Column(name = "prediction_date", nullable = false)
    private Instant predictionDate;

    @ManyToOne(optional = false)
    @NotNull
    private AutoTagJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TagPrediction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return this.tagName;
    }

    public TagPrediction tagName(String tagName) {
        this.setTagName(tagName);
        return this;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public TagPrediction confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getReason() {
        return this.reason;
    }

    public TagPrediction reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public TagPrediction modelVersion(String modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getPredictionS3Key() {
        return this.predictionS3Key;
    }

    public TagPrediction predictionS3Key(String predictionS3Key) {
        this.setPredictionS3Key(predictionS3Key);
        return this;
    }

    public void setPredictionS3Key(String predictionS3Key) {
        this.predictionS3Key = predictionS3Key;
    }

    public Boolean getIsAccepted() {
        return this.isAccepted;
    }

    public TagPrediction isAccepted(Boolean isAccepted) {
        this.setIsAccepted(isAccepted);
        return this;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getAcceptedBy() {
        return this.acceptedBy;
    }

    public TagPrediction acceptedBy(String acceptedBy) {
        this.setAcceptedBy(acceptedBy);
        return this;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Instant getAcceptedDate() {
        return this.acceptedDate;
    }

    public TagPrediction acceptedDate(Instant acceptedDate) {
        this.setAcceptedDate(acceptedDate);
        return this;
    }

    public void setAcceptedDate(Instant acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Instant getPredictionDate() {
        return this.predictionDate;
    }

    public TagPrediction predictionDate(Instant predictionDate) {
        this.setPredictionDate(predictionDate);
        return this;
    }

    public void setPredictionDate(Instant predictionDate) {
        this.predictionDate = predictionDate;
    }

    public AutoTagJob getJob() {
        return this.job;
    }

    public void setJob(AutoTagJob autoTagJob) {
        this.job = autoTagJob;
    }

    public TagPrediction job(AutoTagJob autoTagJob) {
        this.setJob(autoTagJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagPrediction)) {
            return false;
        }
        return getId() != null && getId().equals(((TagPrediction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagPrediction{" +
            "id=" + getId() +
            ", tagName='" + getTagName() + "'" +
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
