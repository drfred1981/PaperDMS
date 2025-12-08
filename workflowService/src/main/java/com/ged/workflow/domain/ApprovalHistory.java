package com.ged.workflow.domain;

import com.ged.workflow.domain.enumeration.TaskAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Historique d'approbation
 */
@Entity
@Table(name = "approval_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApprovalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Column(name = "workflow_instance_id", nullable = false)
    private Long workflowInstanceId;

    @NotNull
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private TaskAction action;

    @Lob
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "action_date", nullable = false)
    private Instant actionDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "action_by", length = 50, nullable = false)
    private String actionBy;

    @Size(max = 50)
    @Column(name = "previous_assignee", length = 50)
    private String previousAssignee;

    @Column(name = "time_taken")
    private Long timeTaken;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApprovalHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public ApprovalHistory documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getWorkflowInstanceId() {
        return this.workflowInstanceId;
    }

    public ApprovalHistory workflowInstanceId(Long workflowInstanceId) {
        this.setWorkflowInstanceId(workflowInstanceId);
        return this;
    }

    public void setWorkflowInstanceId(Long workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    public Integer getStepNumber() {
        return this.stepNumber;
    }

    public ApprovalHistory stepNumber(Integer stepNumber) {
        this.setStepNumber(stepNumber);
        return this;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public TaskAction getAction() {
        return this.action;
    }

    public ApprovalHistory action(TaskAction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(TaskAction action) {
        this.action = action;
    }

    public String getComment() {
        return this.comment;
    }

    public ApprovalHistory comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public ApprovalHistory actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionBy() {
        return this.actionBy;
    }

    public ApprovalHistory actionBy(String actionBy) {
        this.setActionBy(actionBy);
        return this;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getPreviousAssignee() {
        return this.previousAssignee;
    }

    public ApprovalHistory previousAssignee(String previousAssignee) {
        this.setPreviousAssignee(previousAssignee);
        return this;
    }

    public void setPreviousAssignee(String previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public Long getTimeTaken() {
        return this.timeTaken;
    }

    public ApprovalHistory timeTaken(Long timeTaken) {
        this.setTimeTaken(timeTaken);
        return this;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ApprovalHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalHistory{" +
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
