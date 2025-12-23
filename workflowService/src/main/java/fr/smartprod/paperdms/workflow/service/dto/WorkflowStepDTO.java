package fr.smartprod.paperdms.workflow.service.dto;

import fr.smartprod.paperdms.workflow.domain.enumeration.AssigneeType;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowStepType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.workflow.domain.WorkflowStep} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowStepDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer stepNumber;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    private WorkflowStepType stepType;

    private AssigneeType assigneeType;

    @Size(max = 50)
    private String assigneeId;

    @Size(max = 100)
    private String assigneeGroup;

    private Integer dueInDays;

    @NotNull
    private Boolean isRequired;

    @NotNull
    private Boolean canDelegate;

    @NotNull
    private Boolean canReject;

    @Lob
    private String configuration;

    @NotNull
    private WorkflowDTO workflow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkflowStepType getStepType() {
        return stepType;
    }

    public void setStepType(WorkflowStepType stepType) {
        this.stepType = stepType;
    }

    public AssigneeType getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(AssigneeType assigneeType) {
        this.assigneeType = assigneeType;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeGroup() {
        return assigneeGroup;
    }

    public void setAssigneeGroup(String assigneeGroup) {
        this.assigneeGroup = assigneeGroup;
    }

    public Integer getDueInDays() {
        return dueInDays;
    }

    public void setDueInDays(Integer dueInDays) {
        this.dueInDays = dueInDays;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getCanDelegate() {
        return canDelegate;
    }

    public void setCanDelegate(Boolean canDelegate) {
        this.canDelegate = canDelegate;
    }

    public Boolean getCanReject() {
        return canReject;
    }

    public void setCanReject(Boolean canReject) {
        this.canReject = canReject;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
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
        if (!(o instanceof WorkflowStepDTO)) {
            return false;
        }

        WorkflowStepDTO workflowStepDTO = (WorkflowStepDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workflowStepDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowStepDTO{" +
            "id=" + getId() +
            ", stepNumber=" + getStepNumber() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", stepType='" + getStepType() + "'" +
            ", assigneeType='" + getAssigneeType() + "'" +
            ", assigneeId='" + getAssigneeId() + "'" +
            ", assigneeGroup='" + getAssigneeGroup() + "'" +
            ", dueInDays=" + getDueInDays() +
            ", isRequired='" + getIsRequired() + "'" +
            ", canDelegate='" + getCanDelegate() + "'" +
            ", canReject='" + getCanReject() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", workflow=" + getWorkflow() +
            "}";
    }
}
