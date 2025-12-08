package com.ged.ai.domain;

import com.ged.ai.domain.enumeration.AiJobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Travail de tagging automatique
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
    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000, nullable = false)
    private String s3Key;

    @Lob
    @Column(name = "extracted_text")
    private String extractedText;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AiJobStatus status;

    @Size(max = 50)
    @Column(name = "model_version", length = 50)
    private String modelVersion;

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
            ", s3Key='" + gets3Key() + "'" +
            ", extractedText='" + getExtractedText() + "'" +
            ", status='" + getStatus() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", confidence=" + getConfidence() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
