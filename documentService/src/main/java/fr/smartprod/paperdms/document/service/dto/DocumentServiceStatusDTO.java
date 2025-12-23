package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.common.enumeration.ServiceStatus;
import fr.smartprod.paperdms.common.enumeration.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentServiceStatus} entity.
 */
@Schema(
    description = "NOUVELLE ENTIT�: Permet de tracker le statut d'un document pour chaque service\nChaque ligne repr�sente l'�tat du document dans un service sp�cifique"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentServiceStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private ServiceStatus status;

    @Lob
    private String statusDetails;

    @Lob
    private String errorMessage;

    private Integer retryCount;

    private Instant lastProcessedDate;

    private Instant processingStartDate;

    private Instant processingEndDate;

    private Long processingDuration;

    @Size(max = 100)
    private String jobId;

    private Integer priority;

    @Size(max = 50)
    private String updatedBy;

    @NotNull
    private Instant updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Instant getLastProcessedDate() {
        return lastProcessedDate;
    }

    public void setLastProcessedDate(Instant lastProcessedDate) {
        this.lastProcessedDate = lastProcessedDate;
    }

    public Instant getProcessingStartDate() {
        return processingStartDate;
    }

    public void setProcessingStartDate(Instant processingStartDate) {
        this.processingStartDate = processingStartDate;
    }

    public Instant getProcessingEndDate() {
        return processingEndDate;
    }

    public void setProcessingEndDate(Instant processingEndDate) {
        this.processingEndDate = processingEndDate;
    }

    public Long getProcessingDuration() {
        return processingDuration;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentServiceStatusDTO)) {
            return false;
        }

        DocumentServiceStatusDTO documentServiceStatusDTO = (DocumentServiceStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentServiceStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentServiceStatusDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
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
