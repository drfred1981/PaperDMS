package fr.smartprod.paperdms.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowInstanceStatus;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowPriority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkflowInstance.
 */
@Entity
@Table(name = "workflow_instance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workflowinstance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentSha256;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private WorkflowInstanceStatus status;

    @Column(name = "current_step_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
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
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cancellationReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private WorkflowPriority priority;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String metadata;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflowInstance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "workflowInstance" }, allowSetters = true)
    private Set<WorkflowApprovalHistory> approvalHistories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "instance", "step" }, allowSetters = true)
    private Set<WorkflowTask> workflowTasks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "workflowStpes", "workflowInstances" }, allowSetters = true)
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

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public WorkflowInstance documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
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

    public Set<WorkflowApprovalHistory> getApprovalHistories() {
        return this.approvalHistories;
    }

    public void setApprovalHistories(Set<WorkflowApprovalHistory> workflowApprovalHistories) {
        if (this.approvalHistories != null) {
            this.approvalHistories.forEach(i -> i.setWorkflowInstance(null));
        }
        if (workflowApprovalHistories != null) {
            workflowApprovalHistories.forEach(i -> i.setWorkflowInstance(this));
        }
        this.approvalHistories = workflowApprovalHistories;
    }

    public WorkflowInstance approvalHistories(Set<WorkflowApprovalHistory> workflowApprovalHistories) {
        this.setApprovalHistories(workflowApprovalHistories);
        return this;
    }

    public WorkflowInstance addApprovalHistories(WorkflowApprovalHistory workflowApprovalHistory) {
        this.approvalHistories.add(workflowApprovalHistory);
        workflowApprovalHistory.setWorkflowInstance(this);
        return this;
    }

    public WorkflowInstance removeApprovalHistories(WorkflowApprovalHistory workflowApprovalHistory) {
        this.approvalHistories.remove(workflowApprovalHistory);
        workflowApprovalHistory.setWorkflowInstance(null);
        return this;
    }

    public Set<WorkflowTask> getWorkflowTasks() {
        return this.workflowTasks;
    }

    public void setWorkflowTasks(Set<WorkflowTask> workflowTasks) {
        if (this.workflowTasks != null) {
            this.workflowTasks.forEach(i -> i.setInstance(null));
        }
        if (workflowTasks != null) {
            workflowTasks.forEach(i -> i.setInstance(this));
        }
        this.workflowTasks = workflowTasks;
    }

    public WorkflowInstance workflowTasks(Set<WorkflowTask> workflowTasks) {
        this.setWorkflowTasks(workflowTasks);
        return this;
    }

    public WorkflowInstance addWorkflowTasks(WorkflowTask workflowTask) {
        this.workflowTasks.add(workflowTask);
        workflowTask.setInstance(this);
        return this;
    }

    public WorkflowInstance removeWorkflowTasks(WorkflowTask workflowTask) {
        this.workflowTasks.remove(workflowTask);
        workflowTask.setInstance(null);
        return this;
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
            "}";
    }
}
