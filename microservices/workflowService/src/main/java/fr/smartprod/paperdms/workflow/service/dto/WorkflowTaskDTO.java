package fr.smartprod.paperdms.workflow.service.dto;

import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.workflow.domain.WorkflowTask} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowTaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String assigneeId;

    private TaskStatus status;

    private TaskAction action;

    @Lob
    private String comment;

    @NotNull
    private Instant assignedDate;

    private Instant dueDate;

    private Instant completedDate;

    @NotNull
    private Boolean reminderSent;

    @Size(max = 50)
    private String delegatedTo;

    private Instant delegatedDate;

    private WorkflowInstanceDTO instance;

    private WorkflowStepDTO step;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskAction getAction() {
        return action;
    }

    public void setAction(TaskAction action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Instant assignedDate) {
        this.assignedDate = assignedDate;
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

    public Boolean getReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(Boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public String getDelegatedTo() {
        return delegatedTo;
    }

    public void setDelegatedTo(String delegatedTo) {
        this.delegatedTo = delegatedTo;
    }

    public Instant getDelegatedDate() {
        return delegatedDate;
    }

    public void setDelegatedDate(Instant delegatedDate) {
        this.delegatedDate = delegatedDate;
    }

    public WorkflowInstanceDTO getInstance() {
        return instance;
    }

    public void setInstance(WorkflowInstanceDTO instance) {
        this.instance = instance;
    }

    public WorkflowStepDTO getStep() {
        return step;
    }

    public void setStep(WorkflowStepDTO step) {
        this.step = step;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowTaskDTO)) {
            return false;
        }

        WorkflowTaskDTO workflowTaskDTO = (WorkflowTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workflowTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowTaskDTO{" +
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
            ", instance=" + getInstance() +
            ", step=" + getStep() +
            "}";
    }
}
