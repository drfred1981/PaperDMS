package com.ged.workflow.domain;

import com.ged.workflow.domain.enumeration.AssigneeType;
import com.ged.workflow.domain.enumeration.WorkflowStepType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * �tape de workflow
 */
@Entity
@Table(name = "workflow_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false)
    private WorkflowStepType stepType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "assignee_type", nullable = false)
    private AssigneeType assigneeType;

    @Size(max = 50)
    @Column(name = "assignee_id", length = 50)
    private String assigneeId;

    @Size(max = 100)
    @Column(name = "assignee_group", length = 100)
    private String assigneeGroup;

    @Column(name = "due_in_days")
    private Integer dueInDays;

    @NotNull
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @NotNull
    @Column(name = "can_delegate", nullable = false)
    private Boolean canDelegate;

    @NotNull
    @Column(name = "can_reject", nullable = false)
    private Boolean canReject;

    @Lob
    @Column(name = "configuration")
    private String configuration;

    @ManyToOne(optional = false)
    @NotNull
    private Workflow workflow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkflowStep id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStepNumber() {
        return this.stepNumber;
    }

    public WorkflowStep stepNumber(Integer stepNumber) {
        this.setStepNumber(stepNumber);
        return this;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getName() {
        return this.name;
    }

    public WorkflowStep name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public WorkflowStep description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkflowStepType getStepType() {
        return this.stepType;
    }

    public WorkflowStep stepType(WorkflowStepType stepType) {
        this.setStepType(stepType);
        return this;
    }

    public void setStepType(WorkflowStepType stepType) {
        this.stepType = stepType;
    }

    public AssigneeType getAssigneeType() {
        return this.assigneeType;
    }

    public WorkflowStep assigneeType(AssigneeType assigneeType) {
        this.setAssigneeType(assigneeType);
        return this;
    }

    public void setAssigneeType(AssigneeType assigneeType) {
        this.assigneeType = assigneeType;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }

    public WorkflowStep assigneeId(String assigneeId) {
        this.setAssigneeId(assigneeId);
        return this;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeGroup() {
        return this.assigneeGroup;
    }

    public WorkflowStep assigneeGroup(String assigneeGroup) {
        this.setAssigneeGroup(assigneeGroup);
        return this;
    }

    public void setAssigneeGroup(String assigneeGroup) {
        this.assigneeGroup = assigneeGroup;
    }

    public Integer getDueInDays() {
        return this.dueInDays;
    }

    public WorkflowStep dueInDays(Integer dueInDays) {
        this.setDueInDays(dueInDays);
        return this;
    }

    public void setDueInDays(Integer dueInDays) {
        this.dueInDays = dueInDays;
    }

    public Boolean getIsRequired() {
        return this.isRequired;
    }

    public WorkflowStep isRequired(Boolean isRequired) {
        this.setIsRequired(isRequired);
        return this;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getCanDelegate() {
        return this.canDelegate;
    }

    public WorkflowStep canDelegate(Boolean canDelegate) {
        this.setCanDelegate(canDelegate);
        return this;
    }

    public void setCanDelegate(Boolean canDelegate) {
        this.canDelegate = canDelegate;
    }

    public Boolean getCanReject() {
        return this.canReject;
    }

    public WorkflowStep canReject(Boolean canReject) {
        this.setCanReject(canReject);
        return this;
    }

    public void setCanReject(Boolean canReject) {
        this.canReject = canReject;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public WorkflowStep configuration(String configuration) {
        this.setConfiguration(configuration);
        return this;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Workflow getWorkflow() {
        return this.workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public WorkflowStep workflow(Workflow workflow) {
        this.setWorkflow(workflow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowStep)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkflowStep) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowStep{" +
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
            "}";
    }
}
