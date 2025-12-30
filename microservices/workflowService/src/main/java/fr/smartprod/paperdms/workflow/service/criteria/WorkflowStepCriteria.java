package fr.smartprod.paperdms.workflow.service.criteria;

import fr.smartprod.paperdms.workflow.domain.enumeration.AssigneeType;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowStepType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.workflow.domain.WorkflowStep} entity. This class is used
 * in {@link fr.smartprod.paperdms.workflow.web.rest.WorkflowStepResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workflow-steps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowStepCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkflowStepType
     */
    public static class WorkflowStepTypeFilter extends Filter<WorkflowStepType> {

        public WorkflowStepTypeFilter() {}

        public WorkflowStepTypeFilter(WorkflowStepTypeFilter filter) {
            super(filter);
        }

        @Override
        public WorkflowStepTypeFilter copy() {
            return new WorkflowStepTypeFilter(this);
        }
    }

    /**
     * Class for filtering AssigneeType
     */
    public static class AssigneeTypeFilter extends Filter<AssigneeType> {

        public AssigneeTypeFilter() {}

        public AssigneeTypeFilter(AssigneeTypeFilter filter) {
            super(filter);
        }

        @Override
        public AssigneeTypeFilter copy() {
            return new AssigneeTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter stepNumber;

    private StringFilter name;

    private WorkflowStepTypeFilter stepType;

    private AssigneeTypeFilter assigneeType;

    private StringFilter assigneeId;

    private StringFilter assigneeGroup;

    private IntegerFilter dueInDays;

    private BooleanFilter isRequired;

    private BooleanFilter canDelegate;

    private BooleanFilter canReject;

    private LongFilter workflowTasksId;

    private LongFilter workflowId;

    private Boolean distinct;

    public WorkflowStepCriteria() {}

    public WorkflowStepCriteria(WorkflowStepCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.stepNumber = other.optionalStepNumber().map(IntegerFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.stepType = other.optionalStepType().map(WorkflowStepTypeFilter::copy).orElse(null);
        this.assigneeType = other.optionalAssigneeType().map(AssigneeTypeFilter::copy).orElse(null);
        this.assigneeId = other.optionalAssigneeId().map(StringFilter::copy).orElse(null);
        this.assigneeGroup = other.optionalAssigneeGroup().map(StringFilter::copy).orElse(null);
        this.dueInDays = other.optionalDueInDays().map(IntegerFilter::copy).orElse(null);
        this.isRequired = other.optionalIsRequired().map(BooleanFilter::copy).orElse(null);
        this.canDelegate = other.optionalCanDelegate().map(BooleanFilter::copy).orElse(null);
        this.canReject = other.optionalCanReject().map(BooleanFilter::copy).orElse(null);
        this.workflowTasksId = other.optionalWorkflowTasksId().map(LongFilter::copy).orElse(null);
        this.workflowId = other.optionalWorkflowId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WorkflowStepCriteria copy() {
        return new WorkflowStepCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public WorkflowStepTypeFilter getStepType() {
        return stepType;
    }

    public Optional<WorkflowStepTypeFilter> optionalStepType() {
        return Optional.ofNullable(stepType);
    }

    public WorkflowStepTypeFilter stepType() {
        if (stepType == null) {
            setStepType(new WorkflowStepTypeFilter());
        }
        return stepType;
    }

    public void setStepType(WorkflowStepTypeFilter stepType) {
        this.stepType = stepType;
    }

    public AssigneeTypeFilter getAssigneeType() {
        return assigneeType;
    }

    public Optional<AssigneeTypeFilter> optionalAssigneeType() {
        return Optional.ofNullable(assigneeType);
    }

    public AssigneeTypeFilter assigneeType() {
        if (assigneeType == null) {
            setAssigneeType(new AssigneeTypeFilter());
        }
        return assigneeType;
    }

    public void setAssigneeType(AssigneeTypeFilter assigneeType) {
        this.assigneeType = assigneeType;
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

    public StringFilter getAssigneeGroup() {
        return assigneeGroup;
    }

    public Optional<StringFilter> optionalAssigneeGroup() {
        return Optional.ofNullable(assigneeGroup);
    }

    public StringFilter assigneeGroup() {
        if (assigneeGroup == null) {
            setAssigneeGroup(new StringFilter());
        }
        return assigneeGroup;
    }

    public void setAssigneeGroup(StringFilter assigneeGroup) {
        this.assigneeGroup = assigneeGroup;
    }

    public IntegerFilter getDueInDays() {
        return dueInDays;
    }

    public Optional<IntegerFilter> optionalDueInDays() {
        return Optional.ofNullable(dueInDays);
    }

    public IntegerFilter dueInDays() {
        if (dueInDays == null) {
            setDueInDays(new IntegerFilter());
        }
        return dueInDays;
    }

    public void setDueInDays(IntegerFilter dueInDays) {
        this.dueInDays = dueInDays;
    }

    public BooleanFilter getIsRequired() {
        return isRequired;
    }

    public Optional<BooleanFilter> optionalIsRequired() {
        return Optional.ofNullable(isRequired);
    }

    public BooleanFilter isRequired() {
        if (isRequired == null) {
            setIsRequired(new BooleanFilter());
        }
        return isRequired;
    }

    public void setIsRequired(BooleanFilter isRequired) {
        this.isRequired = isRequired;
    }

    public BooleanFilter getCanDelegate() {
        return canDelegate;
    }

    public Optional<BooleanFilter> optionalCanDelegate() {
        return Optional.ofNullable(canDelegate);
    }

    public BooleanFilter canDelegate() {
        if (canDelegate == null) {
            setCanDelegate(new BooleanFilter());
        }
        return canDelegate;
    }

    public void setCanDelegate(BooleanFilter canDelegate) {
        this.canDelegate = canDelegate;
    }

    public BooleanFilter getCanReject() {
        return canReject;
    }

    public Optional<BooleanFilter> optionalCanReject() {
        return Optional.ofNullable(canReject);
    }

    public BooleanFilter canReject() {
        if (canReject == null) {
            setCanReject(new BooleanFilter());
        }
        return canReject;
    }

    public void setCanReject(BooleanFilter canReject) {
        this.canReject = canReject;
    }

    public LongFilter getWorkflowTasksId() {
        return workflowTasksId;
    }

    public Optional<LongFilter> optionalWorkflowTasksId() {
        return Optional.ofNullable(workflowTasksId);
    }

    public LongFilter workflowTasksId() {
        if (workflowTasksId == null) {
            setWorkflowTasksId(new LongFilter());
        }
        return workflowTasksId;
    }

    public void setWorkflowTasksId(LongFilter workflowTasksId) {
        this.workflowTasksId = workflowTasksId;
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
        final WorkflowStepCriteria that = (WorkflowStepCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stepNumber, that.stepNumber) &&
            Objects.equals(name, that.name) &&
            Objects.equals(stepType, that.stepType) &&
            Objects.equals(assigneeType, that.assigneeType) &&
            Objects.equals(assigneeId, that.assigneeId) &&
            Objects.equals(assigneeGroup, that.assigneeGroup) &&
            Objects.equals(dueInDays, that.dueInDays) &&
            Objects.equals(isRequired, that.isRequired) &&
            Objects.equals(canDelegate, that.canDelegate) &&
            Objects.equals(canReject, that.canReject) &&
            Objects.equals(workflowTasksId, that.workflowTasksId) &&
            Objects.equals(workflowId, that.workflowId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stepNumber,
            name,
            stepType,
            assigneeType,
            assigneeId,
            assigneeGroup,
            dueInDays,
            isRequired,
            canDelegate,
            canReject,
            workflowTasksId,
            workflowId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowStepCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStepNumber().map(f -> "stepNumber=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalStepType().map(f -> "stepType=" + f + ", ").orElse("") +
            optionalAssigneeType().map(f -> "assigneeType=" + f + ", ").orElse("") +
            optionalAssigneeId().map(f -> "assigneeId=" + f + ", ").orElse("") +
            optionalAssigneeGroup().map(f -> "assigneeGroup=" + f + ", ").orElse("") +
            optionalDueInDays().map(f -> "dueInDays=" + f + ", ").orElse("") +
            optionalIsRequired().map(f -> "isRequired=" + f + ", ").orElse("") +
            optionalCanDelegate().map(f -> "canDelegate=" + f + ", ").orElse("") +
            optionalCanReject().map(f -> "canReject=" + f + ", ").orElse("") +
            optionalWorkflowTasksId().map(f -> "workflowTasksId=" + f + ", ").orElse("") +
            optionalWorkflowId().map(f -> "workflowId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
