package fr.smartprod.paperdms.workflow.service.criteria;

import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory} entity. This class is used
 * in {@link fr.smartprod.paperdms.workflow.web.rest.WorkflowApprovalHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workflow-approval-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowApprovalHistoryCriteria implements Serializable, Criteria {

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

    private StringFilter documentSha256;

    private IntegerFilter stepNumber;

    private TaskActionFilter action;

    private InstantFilter actionDate;

    private StringFilter actionBy;

    private StringFilter previousAssignee;

    private LongFilter timeTaken;

    private LongFilter workflowInstanceId;

    private Boolean distinct;

    public WorkflowApprovalHistoryCriteria() {}

    public WorkflowApprovalHistoryCriteria(WorkflowApprovalHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.stepNumber = other.optionalStepNumber().map(IntegerFilter::copy).orElse(null);
        this.action = other.optionalAction().map(TaskActionFilter::copy).orElse(null);
        this.actionDate = other.optionalActionDate().map(InstantFilter::copy).orElse(null);
        this.actionBy = other.optionalActionBy().map(StringFilter::copy).orElse(null);
        this.previousAssignee = other.optionalPreviousAssignee().map(StringFilter::copy).orElse(null);
        this.timeTaken = other.optionalTimeTaken().map(LongFilter::copy).orElse(null);
        this.workflowInstanceId = other.optionalWorkflowInstanceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WorkflowApprovalHistoryCriteria copy() {
        return new WorkflowApprovalHistoryCriteria(this);
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

    public StringFilter getDocumentSha256() {
        return documentSha256;
    }

    public Optional<StringFilter> optionalDocumentSha256() {
        return Optional.ofNullable(documentSha256);
    }

    public StringFilter documentSha256() {
        if (documentSha256 == null) {
            setDocumentSha256(new StringFilter());
        }
        return documentSha256;
    }

    public void setDocumentSha256(StringFilter documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public IntegerFilter getStepNumber() {
        return stepNumber;
    }

    public Optional<IntegerFilter> optionalStepNumber() {
        return Optional.ofNullable(stepNumber);
    }

    public IntegerFilter stepNumber() {
        if (stepNumber == null) {
            setStepNumber(new IntegerFilter());
        }
        return stepNumber;
    }

    public void setStepNumber(IntegerFilter stepNumber) {
        this.stepNumber = stepNumber;
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

    public InstantFilter getActionDate() {
        return actionDate;
    }

    public Optional<InstantFilter> optionalActionDate() {
        return Optional.ofNullable(actionDate);
    }

    public InstantFilter actionDate() {
        if (actionDate == null) {
            setActionDate(new InstantFilter());
        }
        return actionDate;
    }

    public void setActionDate(InstantFilter actionDate) {
        this.actionDate = actionDate;
    }

    public StringFilter getActionBy() {
        return actionBy;
    }

    public Optional<StringFilter> optionalActionBy() {
        return Optional.ofNullable(actionBy);
    }

    public StringFilter actionBy() {
        if (actionBy == null) {
            setActionBy(new StringFilter());
        }
        return actionBy;
    }

    public void setActionBy(StringFilter actionBy) {
        this.actionBy = actionBy;
    }

    public StringFilter getPreviousAssignee() {
        return previousAssignee;
    }

    public Optional<StringFilter> optionalPreviousAssignee() {
        return Optional.ofNullable(previousAssignee);
    }

    public StringFilter previousAssignee() {
        if (previousAssignee == null) {
            setPreviousAssignee(new StringFilter());
        }
        return previousAssignee;
    }

    public void setPreviousAssignee(StringFilter previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public LongFilter getTimeTaken() {
        return timeTaken;
    }

    public Optional<LongFilter> optionalTimeTaken() {
        return Optional.ofNullable(timeTaken);
    }

    public LongFilter timeTaken() {
        if (timeTaken == null) {
            setTimeTaken(new LongFilter());
        }
        return timeTaken;
    }

    public void setTimeTaken(LongFilter timeTaken) {
        this.timeTaken = timeTaken;
    }

    public LongFilter getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    public Optional<LongFilter> optionalWorkflowInstanceId() {
        return Optional.ofNullable(workflowInstanceId);
    }

    public LongFilter workflowInstanceId() {
        if (workflowInstanceId == null) {
            setWorkflowInstanceId(new LongFilter());
        }
        return workflowInstanceId;
    }

    public void setWorkflowInstanceId(LongFilter workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
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
        final WorkflowApprovalHistoryCriteria that = (WorkflowApprovalHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(stepNumber, that.stepNumber) &&
            Objects.equals(action, that.action) &&
            Objects.equals(actionDate, that.actionDate) &&
            Objects.equals(actionBy, that.actionBy) &&
            Objects.equals(previousAssignee, that.previousAssignee) &&
            Objects.equals(timeTaken, that.timeTaken) &&
            Objects.equals(workflowInstanceId, that.workflowInstanceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentSha256,
            stepNumber,
            action,
            actionDate,
            actionBy,
            previousAssignee,
            timeTaken,
            workflowInstanceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowApprovalHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalStepNumber().map(f -> "stepNumber=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalActionDate().map(f -> "actionDate=" + f + ", ").orElse("") +
            optionalActionBy().map(f -> "actionBy=" + f + ", ").orElse("") +
            optionalPreviousAssignee().map(f -> "previousAssignee=" + f + ", ").orElse("") +
            optionalTimeTaken().map(f -> "timeTaken=" + f + ", ").orElse("") +
            optionalWorkflowInstanceId().map(f -> "workflowInstanceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
