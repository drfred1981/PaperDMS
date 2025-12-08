package com.ged.workflow.service.criteria;

import com.ged.workflow.domain.enumeration.WorkflowInstanceStatus;
import com.ged.workflow.domain.enumeration.WorkflowPriority;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.workflow.domain.WorkflowInstance} entity. This class is used
 * in {@link com.ged.workflow.web.rest.WorkflowInstanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workflow-instances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowInstanceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkflowInstanceStatus
     */
    public static class WorkflowInstanceStatusFilter extends Filter<WorkflowInstanceStatus> {

        public WorkflowInstanceStatusFilter() {}

        public WorkflowInstanceStatusFilter(WorkflowInstanceStatusFilter filter) {
            super(filter);
        }

        @Override
        public WorkflowInstanceStatusFilter copy() {
            return new WorkflowInstanceStatusFilter(this);
        }
    }

    /**
     * Class for filtering WorkflowPriority
     */
    public static class WorkflowPriorityFilter extends Filter<WorkflowPriority> {

        public WorkflowPriorityFilter() {}

        public WorkflowPriorityFilter(WorkflowPriorityFilter filter) {
            super(filter);
        }

        @Override
        public WorkflowPriorityFilter copy() {
            return new WorkflowPriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private WorkflowInstanceStatusFilter status;

    private IntegerFilter currentStepNumber;

    private InstantFilter startDate;

    private InstantFilter dueDate;

    private InstantFilter completedDate;

    private InstantFilter cancelledDate;

    private WorkflowPriorityFilter priority;

    private StringFilter createdBy;

    private LongFilter workflowId;

    private Boolean distinct;

    public WorkflowInstanceCriteria() {}

    public WorkflowInstanceCriteria(WorkflowInstanceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(WorkflowInstanceStatusFilter::copy).orElse(null);
        this.currentStepNumber = other.optionalCurrentStepNumber().map(IntegerFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(InstantFilter::copy).orElse(null);
        this.completedDate = other.optionalCompletedDate().map(InstantFilter::copy).orElse(null);
        this.cancelledDate = other.optionalCancelledDate().map(InstantFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(WorkflowPriorityFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.workflowId = other.optionalWorkflowId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WorkflowInstanceCriteria copy() {
        return new WorkflowInstanceCriteria(this);
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

    public WorkflowInstanceStatusFilter getStatus() {
        return status;
    }

    public Optional<WorkflowInstanceStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public WorkflowInstanceStatusFilter status() {
        if (status == null) {
            setStatus(new WorkflowInstanceStatusFilter());
        }
        return status;
    }

    public void setStatus(WorkflowInstanceStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getCurrentStepNumber() {
        return currentStepNumber;
    }

    public Optional<IntegerFilter> optionalCurrentStepNumber() {
        return Optional.ofNullable(currentStepNumber);
    }

    public IntegerFilter currentStepNumber() {
        if (currentStepNumber == null) {
            setCurrentStepNumber(new IntegerFilter());
        }
        return currentStepNumber;
    }

    public void setCurrentStepNumber(IntegerFilter currentStepNumber) {
        this.currentStepNumber = currentStepNumber;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
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

    public InstantFilter getCancelledDate() {
        return cancelledDate;
    }

    public Optional<InstantFilter> optionalCancelledDate() {
        return Optional.ofNullable(cancelledDate);
    }

    public InstantFilter cancelledDate() {
        if (cancelledDate == null) {
            setCancelledDate(new InstantFilter());
        }
        return cancelledDate;
    }

    public void setCancelledDate(InstantFilter cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public WorkflowPriorityFilter getPriority() {
        return priority;
    }

    public Optional<WorkflowPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public WorkflowPriorityFilter priority() {
        if (priority == null) {
            setPriority(new WorkflowPriorityFilter());
        }
        return priority;
    }

    public void setPriority(WorkflowPriorityFilter priority) {
        this.priority = priority;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public LongFilter getWorkflowId() {
        return workflowId;
    }

    public Optional<LongFilter> optionalWorkflowId() {
        return Optional.ofNullable(workflowId);
    }

    public LongFilter workflowId() {
        if (workflowId == null) {
            setWorkflowId(new LongFilter());
        }
        return workflowId;
    }

    public void setWorkflowId(LongFilter workflowId) {
        this.workflowId = workflowId;
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
        final WorkflowInstanceCriteria that = (WorkflowInstanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(currentStepNumber, that.currentStepNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(completedDate, that.completedDate) &&
            Objects.equals(cancelledDate, that.cancelledDate) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(workflowId, that.workflowId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            status,
            currentStepNumber,
            startDate,
            dueDate,
            completedDate,
            cancelledDate,
            priority,
            createdBy,
            workflowId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowInstanceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCurrentStepNumber().map(f -> "currentStepNumber=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalCompletedDate().map(f -> "completedDate=" + f + ", ").orElse("") +
            optionalCancelledDate().map(f -> "cancelledDate=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalWorkflowId().map(f -> "workflowId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
