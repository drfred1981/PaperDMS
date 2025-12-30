package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringAlertDTO implements Serializable {

    private Long id;

    @NotNull
    private Severity severity;

    @NotNull
    @Size(max = 255)
    private String title;

    @Lob
    private String message;

    @Size(max = 100)
    private String entityType;

    private String entityName;

    @NotNull
    private MonitoringAlertStatus status;

    @NotNull
    private Instant triggeredDate;

    @Size(max = 50)
    private String acknowledgedBy;

    private Instant acknowledgedDate;

    @Size(max = 50)
    private String resolvedBy;

    private Instant resolvedDate;

    private MonitoringAlertRuleDTO alertRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public MonitoringAlertStatus getStatus() {
        return status;
    }

    public void setStatus(MonitoringAlertStatus status) {
        this.status = status;
    }

    public Instant getTriggeredDate() {
        return triggeredDate;
    }

    public void setTriggeredDate(Instant triggeredDate) {
        this.triggeredDate = triggeredDate;
    }

    public String getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public Instant getAcknowledgedDate() {
        return acknowledgedDate;
    }

    public void setAcknowledgedDate(Instant acknowledgedDate) {
        this.acknowledgedDate = acknowledgedDate;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Instant getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(Instant resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public MonitoringAlertRuleDTO getAlertRule() {
        return alertRule;
    }

    public void setAlertRule(MonitoringAlertRuleDTO alertRule) {
        this.alertRule = alertRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringAlertDTO)) {
            return false;
        }

        MonitoringAlertDTO monitoringAlertDTO = (MonitoringAlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monitoringAlertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringAlertDTO{" +
            "id=" + getId() +
            ", severity='" + getSeverity() + "'" +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", status='" + getStatus() + "'" +
            ", triggeredDate='" + getTriggeredDate() + "'" +
            ", acknowledgedBy='" + getAcknowledgedBy() + "'" +
            ", acknowledgedDate='" + getAcknowledgedDate() + "'" +
            ", resolvedBy='" + getResolvedBy() + "'" +
            ", resolvedDate='" + getResolvedDate() + "'" +
            ", alertRule=" + getAlertRule() +
            "}";
    }
}
