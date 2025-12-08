package com.ged.workflow.service.criteria;

import com.ged.workflow.domain.enumeration.TaskAction;
import com.ged.workflow.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.workflow.domain.WorkflowTask} entity. This class is used
 * in {@link com.ged.workflow.web.rest.WorkflowTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workflow-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowTaskCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TaskStatus
     */
    public static class TaskStatusFilter extends Filter<TaskStatus> {

        public TaskStatusFilter() {}

        public TaskStatusFilter(TaskStatusFilter filter) {
            super(filter);
        }

        @Override
        public TaskStatusFilter copy() {
            return new TaskStatusFilter(this);
        }
    }

    /**
     * Class for filtering TaskAction
     */
    public static class TaskActionFilter extends Filter<TaskAction> {

        public TaskActionFilter() {}

        public TaskActionFilter(TaskActionFilter filter) {
            super(filter);
        }

        @Override
        public TaskActionFilter copy() {
            return new TaskActionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assigneeId;

    private TaskStatusFilter status;

    private TaskActionFilter action;

    private InstantFilter assignedDate;

    private InstantFilter dueDate;

    private InstantFilter completedDate;

    private BooleanFilter reminderSent;

    private StringFilter delegatedTo;

    private InstantFilter delegatedDate;

    private LongFilter instanceId;

    private LongFilter stepId;

    private Boolean distinct;

    public WorkflowTaskCriteria() {}

    public WorkflowTaskCriteria(WorkflowTaskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.assigneeId = other.optionalAssigneeId().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TaskStatusFilter::copy).orElse(null);
        this.action = other.optionalAction().map(TaskActionFilter::copy).orElse(null);
        this.assignedDate = other.optionalAssignedDate().map(InstantFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(InstantFilter::copy).orElse(null);
        this.completedDate = other.optionalCompletedDate().map(InstantFilter::copy).orElse(null);
        this.reminderSent = other.optionalReminderSent().map(BooleanFilter::copy).orElse(null);
        this.delegatedTo = other.optionalDelegatedTo().map(StringFilter::copy).orElse(null);
        this.delegatedDate = other.optionalDelegatedDate().map(InstantFilter::copy).orElse(null);
        this.instanceId = other.optionalInstanceId().map(LongFilter::copy).orElse(null);
        this.stepId = other.optionalStepId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WorkflowTaskCriteria copy() {
        return new WorkflowTaskCriteria(this);
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

    public StringFilter getAssigneeId() {
        return assigneeId;
    }

    public Optional<StringFilter> optionalAssigneeId() {
        return Optional.ofNullable(assigneeId);
    }

    public StringFilter assigneeId() {
        if (assigneeId == null) {
            setAssigneeId(new StringFilter());
        }
        return assigneeId;
    }

    public void setAssigneeId(StringFilter assigneeId) {
        this.assigneeId = assigneeId;
    }

    public TaskStatusFilter getStatus() {
        return status;
    }

    public Optional<TaskStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TaskStatusFilter status() {
        if (status == null) {
            setStatus(new TaskStatusFilter());
        }
        return status;
    }

    public void setStatus(TaskStatusFilter status) {
        this.status = status;
    }

    public TaskActionFilter getAction() {
        return action;
    }

    public Optional<TaskActionFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public TaskActionFilter action() {
        if (action == null) {
            setAction(new TaskActionFilter());
        }
        return action;
    }

    public void setAction(TaskActionFilter action) {
        this.action = action;
    }

    public InstantFilter getAssignedDate() {
        return assignedDate;
    }

    public Optional<InstantFilter> optionalAssignedDate() {
        return Optional.ofNullable(assignedDate);
    }

    public InstantFilter assignedDate() {
        if (assignedDate == null) {
            setAssignedDate(new InstantFilter());
        }
        return assignedDate;
    }

    public void setAssignedDate(InstantFilter assignedDate) {
        this.assignedDate = assignedDate;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public Optional<InstantFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new InstantFilter());
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public InstantFilter getCompletedDate() {
        return completedDate;
    }

    public Optional<InstantFilter> optionalCompletedDate() {
        return Optional.ofNullable(completedDate);
    }

    public InstantFilter completedDate() {
        if (completedDate == null) {
            setCompletedDate(new InstantFilter());
        }
        return completedDate;
    }

    public void setCompletedDate(InstantFilter completedDate) {
        this.completedDate = completedDate;
    }

    public BooleanFilter getReminderSent() {
        return reminderSent;
    }

    public Optional<BooleanFilter> optionalReminderSent() {
        return Optional.ofNullable(reminderSent);
    }

    public BooleanFilter reminderSent() {
        if (reminderSent == null) {
            setReminderSent(new BooleanFilter());
        }
        return reminderSent;
    }

    public void setReminderSent(BooleanFilter reminderSent) {
        this.reminderSent = reminderSent;
    }

    public StringFilter getDelegatedTo() {
        return delegatedTo;
    }

    public Optional<StringFilter> optionalDelegatedTo() {
        return Optional.ofNullable(delegatedTo);
    }

    public StringFilter delegatedTo() {
        if (delegatedTo == null) {
            setDelegatedTo(new StringFilter());
        }
        return delegatedTo;
    }

    public void setDelegatedTo(StringFilter delegatedTo) {
        this.delegatedTo = delegatedTo;
    }

    public InstantFilter getDelegatedDate() {
        return delegatedDate;
    }

    public Optional<InstantFilter> optionalDelegatedDate() {
        return Optional.ofNullable(delegatedDate);
    }

    public InstantFilter delegatedDate() {
        if (delegatedDate == null) {
            setDelegatedDate(new InstantFilter());
        }
        return delegatedDate;
    }

    public void setDelegatedDate(InstantFilter delegatedDate) {
        this.delegatedDate = delegatedDate;
    }

    public LongFilter getInstanceId() {
        return instanceId;
    }

    public Optional<LongFilter> optionalInstanceId() {
        return Optional.ofNullable(instanceId);
    }

    public LongFilter instanceId() {
        if (instanceId == null) {
            setInstanceId(new LongFilter());
        }
        return instanceId;
    }

    public void setInstanceId(LongFilter instanceId) {
        this.instanceId = instanceId;
    }

    public LongFilter getStepId() {
        return stepId;
    }

    public Optional<LongFilter> optionalStepId() {
        return Optional.ofNullable(stepId);
    }

    public LongFilter stepId() {
        if (stepId == null) {
            setStepId(new LongFilter());
        }
        return stepId;
    }

    public void setStepId(LongFilter stepId) {
        this.stepId = stepId;
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
        final WorkflowTaskCriteria that = (WorkflowTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assigneeId, that.assigneeId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(action, that.action) &&
            Objects.equals(assignedDate, that.assignedDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(completedDate, that.completedDate) &&
            Objects.equals(reminderSent, that.reminderSent) &&
            Objects.equals(delegatedTo, that.delegatedTo) &&
            Objects.equals(delegatedDate, that.delegatedDate) &&
            Objects.equals(instanceId, that.instanceId) &&
            Objects.equals(stepId, that.stepId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assigneeId,
            status,
            action,
            assignedDate,
            dueDate,
            completedDate,
            reminderSent,
            delegatedTo,
            delegatedDate,
            instanceId,
            stepId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowTaskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAssigneeId().map(f -> "assigneeId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalAssignedDate().map(f -> "assignedDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalCompletedDate().map(f -> "completedDate=" + f + ", ").orElse("") +
            optionalReminderSent().map(f -> "reminderSent=" + f + ", ").orElse("") +
            optionalDelegatedTo().map(f -> "delegatedTo=" + f + ", ").orElse("") +
            optionalDelegatedDate().map(f -> "delegatedDate=" + f + ", ").orElse("") +
            optionalInstanceId().map(f -> "instanceId=" + f + ", ").orElse("") +
            optionalStepId().map(f -> "stepId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
