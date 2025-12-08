package com.ged.ai.domain;

import com.ged.ai.domain.enumeration.AiJobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Extraction de correspondants
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

    @Lob
    @Column(name = "extracted_text", nullable = false)
    private String extractedText;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AiJobStatus status;

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
            ", extractedText='" + getExtractedText() + "'" +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", sendersCount=" + getSendersCount() +
            ", recipientsCount=" + getRecipientsCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
