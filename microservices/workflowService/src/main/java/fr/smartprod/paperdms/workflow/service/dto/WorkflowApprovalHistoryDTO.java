package fr.smartprod.paperdms.workflow.service.dto;

import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowApprovalHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @NotNull
    private Integer stepNumber;

    private TaskAction action;

    @Lob
    private String comment;

    @NotNull
    private Instant actionDate;

    @NotNull
    @Size(max = 50)
    private String actionBy;

    @Size(max = 50)
    private String previousAssignee;

    private Long timeTaken;

    private WorkflowInstanceDTO workflowInstance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
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

    public Instant getActionDate() {
        return actionDate;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getPreviousAssignee() {
        return previousAssignee;
    }

    public void setPreviousAssignee(String previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public WorkflowInstanceDTO getWorkflowInstance() {
        return workflowInstance;
    }

    public void setWorkflowInstance(WorkflowInstanceDTO workflowInstance) {
        this.workflowInstance = workflowInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowApprovalHistoryDTO)) {
            return false;
        }

        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = (WorkflowApprovalHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workflowApprovalHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowApprovalHistoryDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", stepNumber=" + getStepNumber() +
            ", action='" + getAction() + "'" +
            ", comment='" + getComment() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", actionBy='" + getActionBy() + "'" +
            ", previousAssignee='" + getPreviousAssignee() + "'" +
            ", timeTaken=" + getTimeTaken() +
            ", workflowInstance=" + getWorkflowInstance() +
            "}";
    }
}
