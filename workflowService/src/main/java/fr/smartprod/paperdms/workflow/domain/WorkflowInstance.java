package fr.smartprod.paperdms.workflow.domain;

import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowInstanceStatus;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowPriority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkflowInstance.
 */
@Entity
@Table(name = "workflow_instance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkflowInstanceStatus status;

    @Column(name = "current_step_number")
    private Integer currentStepNumber;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "completed_date")
    private Instant completedDate;

    @Column(name = "cancelled_date")
    private Instant cancelledDate;

    @Lob
    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private WorkflowPriority priority;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @ManyToOne(optional = false)
    @NotNull
    private Workflow workflow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkflowInstance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public WorkflowInstance documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public WorkflowInstanceStatus getStatus() {
        return this.status;
    }

    public WorkflowInstance status(WorkflowInstanceStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WorkflowInstanceStatus status) {
        this.status = status;
    }

    public Integer getCurrentStepNumber() {
        return this.currentStepNumber;
    }

    public WorkflowInstance currentStepNumber(Integer currentStepNumber) {
        this.setCurrentStepNumber(currentStepNumber);
        return this;
    }

    public void setCurrentStepNumber(Integer currentStepNumber) {
        this.currentStepNumber = currentStepNumber;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public WorkflowInstance startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public WorkflowInstance dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCompletedDate() {
        return this.completedDate;
    }

    public WorkflowInstance completedDate(Instant completedDate) {
        this.setCompletedDate(completedDate);
        return this;
    }

    public void setCompletedDate(Instant completedDate) {
        this.completedDate = completedDate;
    }

    public Instant getCancelledDate() {
        return this.cancelledDate;
    }

    public WorkflowInstance cancelledDate(Instant cancelledDate) {
        this.setCancelledDate(cancelledDate);
        return this;
    }

    public void setCancelledDate(Instant cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public WorkflowInstance cancellationReason(String cancellationReason) {
        this.setCancellationReason(cancellationReason);
        return this;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public WorkflowPriority getPriority() {
        return this.priority;
    }

    public WorkflowInstance priority(WorkflowPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(WorkflowPriority priority) {
        this.priority = priority;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public WorkflowInstance metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public WorkflowInstance createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Workflow getWorkflow() {
        return this.workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public WorkflowInstance workflow(Workflow workflow) {
        this.setWorkflow(workflow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowInstance)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkflowInstance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowInstance{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
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
            "}";
    }
}
