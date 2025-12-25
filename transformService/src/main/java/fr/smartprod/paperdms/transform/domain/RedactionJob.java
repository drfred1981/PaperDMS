package fr.smartprod.paperdms.transform.domain;

import fr.smartprod.paperdms.transform.domain.enumeration.RedactionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RedactionJob.
 */
@Entity
@Table(name = "redaction_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RedactionJob implements Serializable {

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
    @Column(name = "redaction_areas", nullable = false)
    private String redactionAreas;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "redaction_type", nullable = false)
    private RedactionType redactionType;

    @Size(max = 7)
    @Column(name = "redaction_color", length = 7)
    private String redactionColor;

    @Size(max = 500)
    @Column(name = "replace_with", length = 500)
    private String replaceWith;

    @Size(max = 1000)
    @Column(name = "output_s_3_key", length = 1000)
    private String outputS3Key;

    @Column(name = "output_document_id")
    private Long outputDocumentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransformStatus status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RedactionJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public RedactionJob documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getRedactionAreas() {
        return this.redactionAreas;
    }

    public RedactionJob redactionAreas(String redactionAreas) {
        this.setRedactionAreas(redactionAreas);
        return this;
    }

    public void setRedactionAreas(String redactionAreas) {
        this.redactionAreas = redactionAreas;
    }

    public RedactionType getRedactionType() {
        return this.redactionType;
    }

    public RedactionJob redactionType(RedactionType redactionType) {
        this.setRedactionType(redactionType);
        return this;
    }

    public void setRedactionType(RedactionType redactionType) {
        this.redactionType = redactionType;
    }

    public String getRedactionColor() {
        return this.redactionColor;
    }

    public RedactionJob redactionColor(String redactionColor) {
        this.setRedactionColor(redactionColor);
        return this;
    }

    public void setRedactionColor(String redactionColor) {
        this.redactionColor = redactionColor;
    }

    public String getReplaceWith() {
        return this.replaceWith;
    }

    public RedactionJob replaceWith(String replaceWith) {
        this.setReplaceWith(replaceWith);
        return this;
    }

    public void setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public RedactionJob outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputDocumentId() {
        return this.outputDocumentId;
    }

    public RedactionJob outputDocumentId(Long outputDocumentId) {
        this.setOutputDocumentId(outputDocumentId);
        return this;
    }

    public void setOutputDocumentId(Long outputDocumentId) {
        this.outputDocumentId = outputDocumentId;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public RedactionJob status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public RedactionJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public RedactionJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public RedactionJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public RedactionJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public RedactionJob createdDate(Instant createdDate) {
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
        if (!(o instanceof RedactionJob)) {
            return false;
        }
        return getId() != null && getId().equals(((RedactionJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RedactionJob{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", redactionAreas='" + getRedactionAreas() + "'" +
            ", redactionType='" + getRedactionType() + "'" +
            ", redactionColor='" + getRedactionColor() + "'" +
            ", replaceWith='" + getReplaceWith() + "'" +
            ", outputS3Key='" + getOutputS3Key() + "'" +
            ", outputDocumentId=" + getOutputDocumentId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
