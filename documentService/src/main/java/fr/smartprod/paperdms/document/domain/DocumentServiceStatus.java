package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentServiceStatus.
 */
@Entity
@Table(name = "document_service_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentservicestatus")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentServiceStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ServiceType serviceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ServiceStatus status;

    @Lob
    @Column(name = "status_details")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String statusDetails;

    @Lob
    @Column(name = "error_message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorMessage;

    @Column(name = "retry_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer retryCount;

    @Column(name = "last_processed_date")
    private Instant lastProcessedDate;

    @Column(name = "processing_start_date")
    private Instant processingStartDate;

    @Column(name = "processing_end_date")
    private Instant processingEndDate;

    @Column(name = "processing_duration")
    private Long processingDuration;

    @Size(max = 100)
    @Column(name = "job_id", length = 100)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String jobId;

    @Column(name = "priority")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer priority;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String updatedBy;

    @NotNull
    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "documentVersions",
            "documentTags",
            "statuses",
            "documentExtractedFields",
            "permissions",
            "audits",
            "comments",
            "metadatas",
            "statistics",
            "documentType",
            "folder",
        },
        allowSetters = true
    )
    private Document document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentServiceStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceType getServiceType() {
        return this.serviceType;
    }

    public DocumentServiceStatus serviceType(ServiceType serviceType) {
        this.setServiceType(serviceType);
        return this;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceStatus getStatus() {
        return this.status;
    }

    public DocumentServiceStatus status(ServiceStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getStatusDetails() {
        return this.statusDetails;
    }

    public DocumentServiceStatus statusDetails(String statusDetails) {
        this.setStatusDetails(statusDetails);
        return this;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public DocumentServiceStatus errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public DocumentServiceStatus retryCount(Integer retryCount) {
        this.setRetryCount(retryCount);
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Instant getLastProcessedDate() {
        return this.lastProcessedDate;
    }

    public DocumentServiceStatus lastProcessedDate(Instant lastProcessedDate) {
        this.setLastProcessedDate(lastProcessedDate);
        return this;
    }

    public void setLastProcessedDate(Instant lastProcessedDate) {
        this.lastProcessedDate = lastProcessedDate;
    }

    public Instant getProcessingStartDate() {
        return this.processingStartDate;
    }

    public DocumentServiceStatus processingStartDate(Instant processingStartDate) {
        this.setProcessingStartDate(processingStartDate);
        return this;
    }

    public void setProcessingStartDate(Instant processingStartDate) {
        this.processingStartDate = processingStartDate;
    }

    public Instant getProcessingEndDate() {
        return this.processingEndDate;
    }

    public DocumentServiceStatus processingEndDate(Instant processingEndDate) {
        this.setProcessingEndDate(processingEndDate);
        return this;
    }

    public void setProcessingEndDate(Instant processingEndDate) {
        this.processingEndDate = processingEndDate;
    }

    public Long getProcessingDuration() {
        return this.processingDuration;
    }

    public DocumentServiceStatus processingDuration(Long processingDuration) {
        this.setProcessingDuration(processingDuration);
        return this;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    public String getJobId() {
        return this.jobId;
    }

    public DocumentServiceStatus jobId(String jobId) {
        this.setJobId(jobId);
        return this;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public DocumentServiceStatus priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public DocumentServiceStatus updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

    public DocumentServiceStatus updatedDate(Instant updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentServiceStatus document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentServiceStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentServiceStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentServiceStatus{" +
            "id=" + getId() +
            ", serviceType='" + getServiceType() + "'" +
            ", status='" + getStatus() + "'" +
            ", statusDetails='" + getStatusDetails() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", retryCount=" + getRetryCount() +
            ", lastProcessedDate='" + getLastProcessedDate() + "'" +
            ", processingStartDate='" + getProcessingStartDate() + "'" +
            ", processingEndDate='" + getProcessingEndDate() + "'" +
            ", processingDuration=" + getProcessingDuration() +
            ", jobId='" + getJobId() + "'" +
            ", priority=" + getPriority() +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
