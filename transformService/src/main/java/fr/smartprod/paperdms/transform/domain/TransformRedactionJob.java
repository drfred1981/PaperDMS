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
 * A TransformRedactionJob.
 */
@Entity
@Table(name = "transform_redaction_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransformRedactionJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    private String documentSha256;

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

    @Size(max = 64)
    @Column(name = "output_document_sha_256", length = 64)
    private String outputDocumentSha256;

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

    public TransformRedactionJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public TransformRedactionJob documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getRedactionAreas() {
        return this.redactionAreas;
    }

    public TransformRedactionJob redactionAreas(String redactionAreas) {
        this.setRedactionAreas(redactionAreas);
        return this;
    }

    public void setRedactionAreas(String redactionAreas) {
        this.redactionAreas = redactionAreas;
    }

    public RedactionType getRedactionType() {
        return this.redactionType;
    }

    public TransformRedactionJob redactionType(RedactionType redactionType) {
        this.setRedactionType(redactionType);
        return this;
    }

    public void setRedactionType(RedactionType redactionType) {
        this.redactionType = redactionType;
    }

    public String getRedactionColor() {
        return this.redactionColor;
    }

    public TransformRedactionJob redactionColor(String redactionColor) {
        this.setRedactionColor(redactionColor);
        return this;
    }

    public void setRedactionColor(String redactionColor) {
        this.redactionColor = redactionColor;
    }

    public String getReplaceWith() {
        return this.replaceWith;
    }

    public TransformRedactionJob replaceWith(String replaceWith) {
        this.setReplaceWith(replaceWith);
        return this;
    }

    public void setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public TransformRedactionJob outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public String getOutputDocumentSha256() {
        return this.outputDocumentSha256;
    }

    public TransformRedactionJob outputDocumentSha256(String outputDocumentSha256) {
        this.setOutputDocumentSha256(outputDocumentSha256);
        return this;
    }

    public void setOutputDocumentSha256(String outputDocumentSha256) {
        this.outputDocumentSha256 = outputDocumentSha256;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public TransformRedactionJob status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public TransformRedactionJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public TransformRedactionJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public TransformRedactionJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public TransformRedactionJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public TransformRedactionJob createdDate(Instant createdDate) {
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
        if (!(o instanceof TransformRedactionJob)) {
            return false;
        }
        return getId() != null && getId().equals(((TransformRedactionJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransformRedactionJob{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", redactionAreas='" + getRedactionAreas() + "'" +
            ", redactionType='" + getRedactionType() + "'" +
            ", redactionColor='" + getRedactionColor() + "'" +
            ", replaceWith='" + getReplaceWith() + "'" +
            ", outputS3Key='" + getOutputS3Key() + "'" +
            ", outputDocumentSha256='" + getOutputDocumentSha256() + "'" +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
