package com.ged.workflow.service.dto;

import com.ged.workflow.domain.enumeration.TaskAction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.workflow.domain.ApprovalHistory} entity.
 */
@Schema(description = "Historique d'approbation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApprovalHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private Long workflowInstanceId;

    @NotNull
    private Integer stepNumber;

    @NotNull
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    public void setWorkflowInstanceId(Long workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalHistoryDTO)) {
            return false;
        }

        ApprovalHistoryDTO approvalHistoryDTO = (ApprovalHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, approvalHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalHistoryDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", workflowInstanceId=" + getWorkflowInstanceId() +
            ", stepNumber=" + getStepNumber() +
            ", action='" + getAction() + "'" +
            ", comment='" + getComment() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", actionBy='" + getActionBy() + "'" +
            ", previousAssignee='" + getPreviousAssignee() + "'" +
            ", timeTaken=" + getTimeTaken() +
            "}";
    }
}
