package fr.smartprod.paperdms.transform.domain;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MergeJob.
 */
@Entity
@Table(name = "merge_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MergeJob implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Lob
    @Column(name = "source_document_ids", nullable = false)
    private String sourceDocumentIds;

    @Lob
    @Column(name = "merge_order", nullable = false)
    private String mergeOrder;

    @Column(name = "include_bookmarks")
    private Boolean includeBookmarks;

    @Column(name = "include_toc")
    private Boolean includeToc;

    @Column(name = "add_page_numbers")
    private Boolean addPageNumbers;

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

    public MergeJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MergeJob name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceDocumentIds() {
        return this.sourceDocumentIds;
    }

    public MergeJob sourceDocumentIds(String sourceDocumentIds) {
        this.setSourceDocumentIds(sourceDocumentIds);
        return this;
    }

    public void setSourceDocumentIds(String sourceDocumentIds) {
        this.sourceDocumentIds = sourceDocumentIds;
    }

    public String getMergeOrder() {
        return this.mergeOrder;
    }

    public MergeJob mergeOrder(String mergeOrder) {
        this.setMergeOrder(mergeOrder);
        return this;
    }

    public void setMergeOrder(String mergeOrder) {
        this.mergeOrder = mergeOrder;
    }

    public Boolean getIncludeBookmarks() {
        return this.includeBookmarks;
    }

    public MergeJob includeBookmarks(Boolean includeBookmarks) {
        this.setIncludeBookmarks(includeBookmarks);
        return this;
    }

    public void setIncludeBookmarks(Boolean includeBookmarks) {
        this.includeBookmarks = includeBookmarks;
    }

    public Boolean getIncludeToc() {
        return this.includeToc;
    }

    public MergeJob includeToc(Boolean includeToc) {
        this.setIncludeToc(includeToc);
        return this;
    }

    public void setIncludeToc(Boolean includeToc) {
        this.includeToc = includeToc;
    }

    public Boolean getAddPageNumbers() {
        return this.addPageNumbers;
    }

    public MergeJob addPageNumbers(Boolean addPageNumbers) {
        this.setAddPageNumbers(addPageNumbers);
        return this;
    }

    public void setAddPageNumbers(Boolean addPageNumbers) {
        this.addPageNumbers = addPageNumbers;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public MergeJob outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputDocumentId() {
        return this.outputDocumentId;
    }

    public MergeJob outputDocumentId(Long outputDocumentId) {
        this.setOutputDocumentId(outputDocumentId);
        return this;
    }

    public void setOutputDocumentId(Long outputDocumentId) {
        this.outputDocumentId = outputDocumentId;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public MergeJob status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public MergeJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public MergeJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public MergeJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MergeJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MergeJob createdDate(Instant createdDate) {
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
        if (!(o instanceof MergeJob)) {
            return false;
        }
        return getId() != null && getId().equals(((MergeJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MergeJob{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sourceDocumentIds='" + getSourceDocumentIds() + "'" +
            ", mergeOrder='" + getMergeOrder() + "'" +
            ", includeBookmarks='" + getIncludeBookmarks() + "'" +
            ", includeToc='" + getIncludeToc() + "'" +
            ", addPageNumbers='" + getAddPageNumbers() + "'" +
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
