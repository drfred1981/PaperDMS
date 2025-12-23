package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.AlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.Alert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertDTO implements Serializable {

    private Long id;

    @NotNull
    private Long alertRuleId;

    @NotNull
    private Severity severity;

    @NotNull
    @Size(max = 255)
    private String title;

    @Lob
    private String message;

    @Size(max = 100)
    private String entityType;

    private Long entityId;

    @NotNull
    private AlertStatus status;

    @NotNull
    private Instant triggeredDate;

    @Size(max = 50)
    private String acknowledgedBy;

    private Instant acknowledgedDate;

    @Size(max = 50)
    private String resolvedBy;

    private Instant resolvedDate;

    @NotNull
    private AlertRuleDTO alertRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlertRuleId() {
        return alertRuleId;
    }

    public void setAlertRuleId(Long alertRuleId) {
        this.alertRuleId = alertRuleId;
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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
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

    public AlertRuleDTO getAlertRule() {
        return alertRule;
    }

    public void setAlertRule(AlertRuleDTO alertRule) {
        this.alertRule = alertRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertDTO)) {
            return false;
        }

        AlertDTO alertDTO = (AlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertDTO{" +
            "id=" + getId() +
            ", alertRuleId=" + getAlertRuleId() +
            ", severity='" + getSeverity() + "'" +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityId=" + getEntityId() +
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
