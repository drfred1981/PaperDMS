package fr.smartprod.paperdms.workflow.service.dto;

import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowInstanceStatus;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowPriority;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.workflow.domain.WorkflowInstance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowInstanceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    private WorkflowInstanceStatus status;

    private Integer currentStepNumber;

    @NotNull
    private Instant startDate;

    private Instant dueDate;

    private Instant completedDate;

    private Instant cancelledDate;

    @Lob
    private String cancellationReason;

    private WorkflowPriority priority;

    @Lob
    private String metadata;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    private WorkflowDTO workflow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public WorkflowInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowInstanceStatus status) {
        this.status = status;
    }

    public Integer getCurrentStepNumber() {
        return currentStepNumber;
    }

    public void setCurrentStepNumber(Integer currentStepNumber) {
        this.currentStepNumber = currentStepNumber;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Instant completedDate) {
        this.completedDate = completedDate;
    }

    public Instant getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Instant cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public WorkflowPriority getPriority() {
        return priority;
    }

    public void setPriority(WorkflowPriority priority) {
        this.priority = priority;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public WorkflowDTO getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowDTO workflow) {
        this.workflow = workflow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowInstanceDTO)) {
            return false;
        }

        WorkflowInstanceDTO workflowInstanceDTO = (WorkflowInstanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workflowInstanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowInstanceDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", status='" + getStatus() + "'" +
            ", currentStepNumber=" + getCurrentStepNumber() +
            ", startDate='" + getStartDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", completedDate='" + getCompletedDate() + "'" +
            ", cancelledDate='" + getCancelledDate() + "'" +
            ", cancellationReason='" + getCancellationReason() + "'" +
            ", priority='" + getPriority() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", workflow=" + getWorkflow() +
            "}";
    }
}
