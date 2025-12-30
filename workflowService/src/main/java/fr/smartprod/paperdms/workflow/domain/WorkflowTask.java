package fr.smartprod.paperdms.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkflowTask.
 */
@Entity
@Table(name = "workflow_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workflowtask")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "assignee_id", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assigneeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TaskAction action;

    @Lob
    @Column(name = "comment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String comment;

    @NotNull
    @Column(name = "assigned_date", nullable = false)
    private Instant assignedDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "completed_date")
    private Instant completedDate;

    @NotNull
    @Column(name = "reminder_sent", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean reminderSent;

    @Size(max = 50)
    @Column(name = "delegated_to", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String delegatedTo;

    @Column(name = "delegated_date")
    private Instant delegatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "approvalHistories", "workflowTasks", "workflow" }, allowSetters = true)
    private WorkflowInstance instance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "workflowTasks", "workflow" }, allowSetters = true)
    private WorkflowStep step;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkflowTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }

    public WorkflowTask assigneeId(String assigneeId) {
        this.setAssigneeId(assigneeId);
        return this;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public WorkflowTask status(TaskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskAction getAction() {
        return this.action;
    }

    public WorkflowTask action(TaskAction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(TaskAction action) {
        this.action = action;
    }

    public String getComment() {
        return this.comment;
    }

    public WorkflowTask comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getAssignedDate() {
        return this.assignedDate;
    }

    public WorkflowTask assignedDate(Instant assignedDate) {
        this.setAssignedDate(assignedDate);
        return this;
    }

    public void setAssignedDate(Instant assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public WorkflowTask dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCompletedDate() {
        return this.completedDate;
    }

    public WorkflowTask completedDate(Instant completedDate) {
        this.setCompletedDate(completedDate);
        return this;
    }

    public void setCompletedDate(Instant completedDate) {
        this.completedDate = completedDate;
    }

    public Boolean getReminderSent() {
        return this.reminderSent;
    }

    public WorkflowTask reminderSent(Boolean reminderSent) {
        this.setReminderSent(reminderSent);
        return this;
    }

    public void setReminderSent(Boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public String getDelegatedTo() {
        return this.delegatedTo;
    }

    public WorkflowTask delegatedTo(String delegatedTo) {
        this.setDelegatedTo(delegatedTo);
        return this;
    }

    public void setDelegatedTo(String delegatedTo) {
        this.delegatedTo = delegatedTo;
    }

    public Instant getDelegatedDate() {
        return this.delegatedDate;
    }

    public WorkflowTask delegatedDate(Instant delegatedDate) {
        this.setDelegatedDate(delegatedDate);
        return this;
    }

    public void setDelegatedDate(Instant delegatedDate) {
        this.delegatedDate = delegatedDate;
    }

    public WorkflowInstance getInstance() {
        return this.instance;
    }

    public void setInstance(WorkflowInstance workflowInstance) {
        this.instance = workflowInstance;
    }

    public WorkflowTask instance(WorkflowInstance workflowInstance) {
        this.setInstance(workflowInstance);
        return this;
    }

    public WorkflowStep getStep() {
        return this.step;
    }

    public void setStep(WorkflowStep workflowStep) {
        this.step = workflowStep;
    }

    public WorkflowTask step(WorkflowStep workflowStep) {
        this.setStep(workflowStep);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowTask)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkflowTask) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowTask{" +
            "id=" + getId() +
            ", assigneeId='" + getAssigneeId() + "'" +
            ", status='" + getStatus() + "'" +
            ", action='" + getAction() + "'" +
            ", comment='" + getComment() + "'" +
            ", assignedDate='" + getAssignedDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", completedDate='" + getCompletedDate() + "'" +
            ", reminderSent='" + getReminderSent() + "'" +
            ", delegatedTo='" + getDelegatedTo() + "'" +
            ", delegatedDate='" + getDelegatedDate() + "'" +
            "}";
    }
}
