package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringAlertRuleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @NotNull
    private MonitoringAlertType alertType;

    @Lob
    private String conditions;

    @NotNull
    private Severity severity;

    @Lob
    private String recipients;

    @NotNull
    private Boolean isActive;

    private Integer triggerCount;

    private Instant lastTriggered;

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

    public MonitoringAlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(MonitoringAlertType alertType) {
        this.alertType = alertType;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Instant getLastTriggered() {
        return lastTriggered;
    }

    public void setLastTriggered(Instant lastTriggered) {
        this.lastTriggered = lastTriggered;
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
        if (!(o instanceof MonitoringAlertRuleDTO)) {
            return false;
        }

        MonitoringAlertRuleDTO monitoringAlertRuleDTO = (MonitoringAlertRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monitoringAlertRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringAlertRuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", alertType='" + getAlertType() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", recipients='" + getRecipients() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", triggerCount=" + getTriggerCount() +
            ", lastTriggered='" + getLastTriggered() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
