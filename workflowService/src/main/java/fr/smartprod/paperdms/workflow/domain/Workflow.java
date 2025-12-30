package fr.smartprod.paperdms.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Workflow.
 */
@Entity
@Table(name = "workflow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workflow")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Workflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Column(name = "version", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer version;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_parallel", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isParallel;

    @NotNull
    @Column(name = "auto_start", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean autoStart;

    @Size(max = 100)
    @Column(name = "trigger_event", length = 100)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String triggerEvent;

    @Lob
    @Column(name = "configuration")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String configuration;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflow")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "workflowTasks", "workflow" }, allowSetters = true)
    private Set<WorkflowStep> workflowStpes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflow")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "approvalHistories", "workflowTasks", "workflow" }, allowSetters = true)
    private Set<WorkflowInstance> workflowInstances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Workflow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Workflow name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Workflow description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Workflow version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Workflow isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsParallel() {
        return this.isParallel;
    }

    public Workflow isParallel(Boolean isParallel) {
        this.setIsParallel(isParallel);
        return this;
    }

    public void setIsParallel(Boolean isParallel) {
        this.isParallel = isParallel;
    }

    public Boolean getAutoStart() {
        return this.autoStart;
    }

    public Workflow autoStart(Boolean autoStart) {
        this.setAutoStart(autoStart);
        return this;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public String getTriggerEvent() {
        return this.triggerEvent;
    }

    public Workflow triggerEvent(String triggerEvent) {
        this.setTriggerEvent(triggerEvent);
        return this;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public Workflow configuration(String configuration) {
        this.setConfiguration(configuration);
        return this;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Workflow createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Workflow lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Workflow createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<WorkflowStep> getWorkflowStpes() {
        return this.workflowStpes;
    }

    public void setWorkflowStpes(Set<WorkflowStep> workflowSteps) {
        if (this.workflowStpes != null) {
            this.workflowStpes.forEach(i -> i.setWorkflow(null));
        }
        if (workflowSteps != null) {
            workflowSteps.forEach(i -> i.setWorkflow(this));
        }
        this.workflowStpes = workflowSteps;
    }

    public Workflow workflowStpes(Set<WorkflowStep> workflowSteps) {
        this.setWorkflowStpes(workflowSteps);
        return this;
    }

    public Workflow addWorkflowStpes(WorkflowStep workflowStep) {
        this.workflowStpes.add(workflowStep);
        workflowStep.setWorkflow(this);
        return this;
    }

    public Workflow removeWorkflowStpes(WorkflowStep workflowStep) {
        this.workflowStpes.remove(workflowStep);
        workflowStep.setWorkflow(null);
        return this;
    }

    public Set<WorkflowInstance> getWorkflowInstances() {
        return this.workflowInstances;
    }

    public void setWorkflowInstances(Set<WorkflowInstance> workflowInstances) {
        if (this.workflowInstances != null) {
            this.workflowInstances.forEach(i -> i.setWorkflow(null));
        }
        if (workflowInstances != null) {
            workflowInstances.forEach(i -> i.setWorkflow(this));
        }
        this.workflowInstances = workflowInstances;
    }

    public Workflow workflowInstances(Set<WorkflowInstance> workflowInstances) {
        this.setWorkflowInstances(workflowInstances);
        return this;
    }

    public Workflow addWorkflowInstances(WorkflowInstance workflowInstance) {
        this.workflowInstances.add(workflowInstance);
        workflowInstance.setWorkflow(this);
        return this;
    }

    public Workflow removeWorkflowInstances(WorkflowInstance workflowInstance) {
        this.workflowInstances.remove(workflowInstance);
        workflowInstance.setWorkflow(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workflow)) {
            return false;
        }
        return getId() != null && getId().equals(((Workflow) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Workflow{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", version=" + getVersion() +
            ", isActive='" + getIsActive() + "'" +
            ", isParallel='" + getIsParallel() + "'" +
            ", autoStart='" + getAutoStart() + "'" +
            ", triggerEvent='" + getTriggerEvent() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
