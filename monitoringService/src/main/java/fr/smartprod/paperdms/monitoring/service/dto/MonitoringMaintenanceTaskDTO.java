package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MaintenanceType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.TransformStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringMaintenanceTaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @NotNull
    private MaintenanceType taskType;

    @NotNull
    @Size(max = 100)
    private String schedule;

    @NotNull
    private TransformStatus status;

    @NotNull
    private Boolean isActive;

    private Instant lastRun;

    private Instant nextRun;

    private Long duration;

    private Integer recordsProcessed;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MaintenanceType getTaskType() {
        return taskType;
    }

    public void setTaskType(MaintenanceType taskType) {
        this.taskType = taskType;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public TransformStatus getStatus() {
        return status;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getLastRun() {
        return lastRun;
    }

    public void setLastRun(Instant lastRun) {
        this.lastRun = lastRun;
    }

    public Instant getNextRun() {
        return nextRun;
    }

    public void setNextRun(Instant nextRun) {
        this.nextRun = nextRun;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringMaintenanceTaskDTO)) {
            return false;
        }

        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = (MonitoringMaintenanceTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monitoringMaintenanceTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringMaintenanceTaskDTO{" +
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
