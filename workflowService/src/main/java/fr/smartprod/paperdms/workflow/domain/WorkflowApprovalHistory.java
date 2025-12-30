package fr.smartprod.paperdms.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkflowApprovalHistory.
 */
@Entity
@Table(name = "workflow_approval_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workflowapprovalhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowApprovalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentSha256;

    @NotNull
    @Column(name = "step_number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer stepNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TaskAction action;

    @Lob
    @Column(name = "comment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String comment;

    @NotNull
    @Column(name = "action_date", nullable = false)
    private Instant actionDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "action_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String actionBy;

    @Size(max = 50)
    @Column(name = "previous_assignee", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String previousAssignee;

    @Column(name = "time_taken")
    private Long timeTaken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "approvalHistories", "workflowTasks", "workflow" }, allowSetters = true)
    private WorkflowInstance workflowInstance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkflowApprovalHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public WorkflowApprovalHistory documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public Integer getStepNumber() {
        return this.stepNumber;
    }

    public WorkflowApprovalHistory stepNumber(Integer stepNumber) {
        this.setStepNumber(stepNumber);
        return this;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public TaskAction getAction() {
        return this.action;
    }

    public WorkflowApprovalHistory action(TaskAction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(TaskAction action) {
        this.action = action;
    }

    public String getComment() {
        return this.comment;
    }

    public WorkflowApprovalHistory comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public WorkflowApprovalHistory actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionBy() {
        return this.actionBy;
    }

    public WorkflowApprovalHistory actionBy(String actionBy) {
        this.setActionBy(actionBy);
        return this;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getPreviousAssignee() {
        return this.previousAssignee;
    }

    public WorkflowApprovalHistory previousAssignee(String previousAssignee) {
        this.setPreviousAssignee(previousAssignee);
        return this;
    }

    public void setPreviousAssignee(String previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public Long getTimeTaken() {
        return this.timeTaken;
    }

    public WorkflowApprovalHistory timeTaken(Long timeTaken) {
        this.setTimeTaken(timeTaken);
        return this;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public WorkflowInstance getWorkflowInstance() {
        return this.workflowInstance;
    }

    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }

    public WorkflowApprovalHistory workflowInstance(WorkflowInstance workflowInstance) {
        this.setWorkflowInstance(workflowInstance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowApprovalHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkflowApprovalHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowApprovalHistory{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
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
