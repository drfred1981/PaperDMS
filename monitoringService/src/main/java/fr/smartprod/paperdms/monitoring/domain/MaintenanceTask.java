package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MaintenanceType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.TransformStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MaintenanceTask.
 */
@Entity
@Table(name = "maintenance_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceTask implements Serializable {

    @Serial
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
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private MaintenanceType taskType;

    @NotNull
    @Size(max = 100)
    @Column(name = "schedule", length = 100, nullable = false)
    private String schedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransformStatus status;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_run")
    private Instant lastRun;

    @Column(name = "next_run")
    private Instant nextRun;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "records_processed")
    private Integer recordsProcessed;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaintenanceTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MaintenanceTask name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MaintenanceTask description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MaintenanceType getTaskType() {
        return this.taskType;
    }

    public MaintenanceTask taskType(MaintenanceType taskType) {
        this.setTaskType(taskType);
        return this;
    }

    public void setTaskType(MaintenanceType taskType) {
        this.taskType = taskType;
    }

    public String getSchedule() {
        return this.schedule;
    }

    public MaintenanceTask schedule(String schedule) {
        this.setSchedule(schedule);
        return this;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public MaintenanceTask status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MaintenanceTask isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getLastRun() {
        return this.lastRun;
    }

    public MaintenanceTask lastRun(Instant lastRun) {
        this.setLastRun(lastRun);
        return this;
    }

    public void setLastRun(Instant lastRun) {
        this.lastRun = lastRun;
    }

    public Instant getNextRun() {
        return this.nextRun;
    }

    public MaintenanceTask nextRun(Instant nextRun) {
        this.setNextRun(nextRun);
        return this;
    }

    public void setNextRun(Instant nextRun) {
        this.nextRun = nextRun;
    }

    public Long getDuration() {
        return this.duration;
    }

    public MaintenanceTask duration(Long duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getRecordsProcessed() {
        return this.recordsProcessed;
    }

    public MaintenanceTask recordsProcessed(Integer recordsProcessed) {
        this.setRecordsProcessed(recordsProcessed);
        return this;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MaintenanceTask createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MaintenanceTask createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintenanceTask)) {
            return false;
        }
        return getId() != null && getId().equals(((MaintenanceTask) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceTask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", taskType='" + getTaskType() + "'" +
            ", schedule='" + getSchedule() + "'" +
            ", status='" + getStatus() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastRun='" + getLastRun() + "'" +
            ", nextRun='" + getNextRun() + "'" +
            ", duration=" + getDuration() +
            ", recordsProcessed=" + getRecordsProcessed() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
