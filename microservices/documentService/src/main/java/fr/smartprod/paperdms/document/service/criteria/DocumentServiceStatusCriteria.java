package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.ServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentServiceStatus} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentServiceStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-service-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentServiceStatusCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ServiceType
     */
    public static class ServiceTypeFilter extends Filter<ServiceType> {

        public ServiceTypeFilter() {}

        public ServiceTypeFilter(ServiceTypeFilter filter) {
            super(filter);
        }

        @Override
        public ServiceTypeFilter copy() {
            return new ServiceTypeFilter(this);
        }
    }

    /**
     * Class for filtering ServiceStatus
     */
    public static class ServiceStatusFilter extends Filter<ServiceStatus> {

        public ServiceStatusFilter() {}

        public ServiceStatusFilter(ServiceStatusFilter filter) {
            super(filter);
        }

        @Override
        public ServiceStatusFilter copy() {
            return new ServiceStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ServiceTypeFilter serviceType;

    private ServiceStatusFilter status;

    private IntegerFilter retryCount;

    private InstantFilter lastProcessedDate;

    private InstantFilter processingStartDate;

    private InstantFilter processingEndDate;

    private LongFilter processingDuration;

    private StringFilter jobId;

    private IntegerFilter priority;

    private StringFilter updatedBy;

    private InstantFilter updatedDate;

    private LongFilter documentId;

    private Boolean distinct;

    public DocumentServiceStatusCriteria() {}

    public DocumentServiceStatusCriteria(DocumentServiceStatusCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.serviceType = other.optionalServiceType().map(ServiceTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ServiceStatusFilter::copy).orElse(null);
        this.retryCount = other.optionalRetryCount().map(IntegerFilter::copy).orElse(null);
        this.lastProcessedDate = other.optionalLastProcessedDate().map(InstantFilter::copy).orElse(null);
        this.processingStartDate = other.optionalProcessingStartDate().map(InstantFilter::copy).orElse(null);
        this.processingEndDate = other.optionalProcessingEndDate().map(InstantFilter::copy).orElse(null);
        this.processingDuration = other.optionalProcessingDuration().map(LongFilter::copy).orElse(null);
        this.jobId = other.optionalJobId().map(StringFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedDate = other.optionalUpdatedDate().map(InstantFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentServiceStatusCriteria copy() {
        return new DocumentServiceStatusCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ServiceTypeFilter getServiceType() {
        return serviceType;
    }

    public Optional<ServiceTypeFilter> optionalServiceType() {
        return Optional.ofNullable(serviceType);
    }

    public ServiceTypeFilter serviceType() {
        if (serviceType == null) {
            setServiceType(new ServiceTypeFilter());
        }
        return serviceType;
    }

    public void setServiceType(ServiceTypeFilter serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceStatusFilter getStatus() {
        return status;
    }

    public Optional<ServiceStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ServiceStatusFilter status() {
        if (status == null) {
            setStatus(new ServiceStatusFilter());
        }
        return status;
    }

    public void setStatus(ServiceStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getRetryCount() {
        return retryCount;
    }

    public Optional<IntegerFilter> optionalRetryCount() {
        return Optional.ofNullable(retryCount);
    }

    public IntegerFilter retryCount() {
        if (retryCount == null) {
            setRetryCount(new IntegerFilter());
        }
        return retryCount;
    }

    public void setRetryCount(IntegerFilter retryCount) {
        this.retryCount = retryCount;
    }

    public InstantFilter getLastProcessedDate() {
        return lastProcessedDate;
    }

    public Optional<InstantFilter> optionalLastProcessedDate() {
        return Optional.ofNullable(lastProcessedDate);
    }

    public InstantFilter lastProcessedDate() {
        if (lastProcessedDate == null) {
            setLastProcessedDate(new InstantFilter());
        }
        return lastProcessedDate;
    }

    public void setLastProcessedDate(InstantFilter lastProcessedDate) {
        this.lastProcessedDate = lastProcessedDate;
    }

    public InstantFilter getProcessingStartDate() {
        return processingStartDate;
    }

    public Optional<InstantFilter> optionalProcessingStartDate() {
        return Optional.ofNullable(processingStartDate);
    }

    public InstantFilter processingStartDate() {
        if (processingStartDate == null) {
            setProcessingStartDate(new InstantFilter());
        }
        return processingStartDate;
    }

    public void setProcessingStartDate(InstantFilter processingStartDate) {
        this.processingStartDate = processingStartDate;
    }

    public InstantFilter getProcessingEndDate() {
        return processingEndDate;
    }

    public Optional<InstantFilter> optionalProcessingEndDate() {
        return Optional.ofNullable(processingEndDate);
    }

    public InstantFilter processingEndDate() {
        if (processingEndDate == null) {
            setProcessingEndDate(new InstantFilter());
        }
        return processingEndDate;
    }

    public void setProcessingEndDate(InstantFilter processingEndDate) {
        this.processingEndDate = processingEndDate;
    }

    public LongFilter getProcessingDuration() {
        return processingDuration;
    }

    public Optional<LongFilter> optionalProcessingDuration() {
        return Optional.ofNullable(processingDuration);
    }

    public LongFilter processingDuration() {
        if (processingDuration == null) {
            setProcessingDuration(new LongFilter());
        }
        return processingDuration;
    }

    public void setProcessingDuration(LongFilter processingDuration) {
        this.processingDuration = processingDuration;
    }

    public StringFilter getJobId() {
        return jobId;
    }

    public Optional<StringFilter> optionalJobId() {
        return Optional.ofNullable(jobId);
    }

    public StringFilter jobId() {
        if (jobId == null) {
            setJobId(new StringFilter());
        }
        return jobId;
    }

    public void setJobId(StringFilter jobId) {
        this.jobId = jobId;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
    }

    public Optional<StringFilter> optionalUpdatedBy() {
        return Optional.ofNullable(updatedBy);
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            setUpdatedBy(new StringFilter());
        }
        return updatedBy;
    }

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public InstantFilter getUpdatedDate() {
        return updatedDate;
    }

    public Optional<InstantFilter> optionalUpdatedDate() {
        return Optional.ofNullable(updatedDate);
    }

    public InstantFilter updatedDate() {
        if (updatedDate == null) {
            setUpdatedDate(new InstantFilter());
        }
        return updatedDate;
    }

    public void setUpdatedDate(InstantFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentServiceStatusCriteria that = (DocumentServiceStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serviceType, that.serviceType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(retryCount, that.retryCount) &&
            Objects.equals(lastProcessedDate, that.lastProcessedDate) &&
            Objects.equals(processingStartDate, that.processingStartDate) &&
            Objects.equals(processingEndDate, that.processingEndDate) &&
            Objects.equals(processingDuration, that.processingDuration) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            serviceType,
            status,
            retryCount,
            lastProcessedDate,
            processingStartDate,
            processingEndDate,
            processingDuration,
            jobId,
            priority,
            updatedBy,
            updatedDate,
            documentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentServiceStatusCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalServiceType().map(f -> "serviceType=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalRetryCount().map(f -> "retryCount=" + f + ", ").orElse("") +
            optionalLastProcessedDate().map(f -> "lastProcessedDate=" + f + ", ").orElse("") +
            optionalProcessingStartDate().map(f -> "processingStartDate=" + f + ", ").orElse("") +
            optionalProcessingEndDate().map(f -> "processingEndDate=" + f + ", ").orElse("") +
            optionalProcessingDuration().map(f -> "processingDuration=" + f + ", ").orElse("") +
            optionalJobId().map(f -> "jobId=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalUpdatedDate().map(f -> "updatedDate=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
