package com.ged.workflow.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * D�finition de workflow
 */
@Entity
@Table(name = "workflow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_parallel", nullable = false)
    private Boolean isParallel;

    @NotNull
    @Column(name = "auto_start", nullable = false)
    private Boolean autoStart;

    @Size(max = 100)
    @Column(name = "trigger_event", length = 100)
    private String triggerEvent;

    @Lob
    @Column(name = "configuration")
    private String configuration;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

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
