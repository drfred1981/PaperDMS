package fr.smartprod.paperdms.monitoring.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonitoringAlertRule.
 */
@Entity
@Table(name = "monitoring_alert_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringAlertRule implements Serializable {

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
    @Column(name = "alert_type", nullable = false)
    private MonitoringAlertType alertType;

    @Lob
    @Column(name = "conditions", nullable = false)
    private String conditions;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @Lob
    @Column(name = "recipients", nullable = false)
    private String recipients;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "trigger_count")
    private Integer triggerCount;

    @Column(name = "last_triggered")
    private Instant lastTriggered;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alertRule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alertRule" }, allowSetters = true)
    private Set<MonitoringAlert> alerts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonitoringAlertRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MonitoringAlertRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MonitoringAlertRule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MonitoringAlertType getAlertType() {
        return this.alertType;
    }

    public MonitoringAlertRule alertType(MonitoringAlertType alertType) {
        this.setAlertType(alertType);
        return this;
    }

    public void setAlertType(MonitoringAlertType alertType) {
        this.alertType = alertType;
    }

    public String getConditions() {
        return this.conditions;
    }

    public MonitoringAlertRule conditions(String conditions) {
        this.setConditions(conditions);
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public MonitoringAlertRule severity(Severity severity) {
        this.setSeverity(severity);
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getRecipients() {
        return this.recipients;
    }

    public MonitoringAlertRule recipients(String recipients) {
        this.setRecipients(recipients);
        return this;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MonitoringAlertRule isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getTriggerCount() {
        return this.triggerCount;
    }

    public MonitoringAlertRule triggerCount(Integer triggerCount) {
        this.setTriggerCount(triggerCount);
        return this;
    }

    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Instant getLastTriggered() {
        return this.lastTriggered;
    }

    public MonitoringAlertRule lastTriggered(Instant lastTriggered) {
        this.setLastTriggered(lastTriggered);
        return this;
    }

    public void setLastTriggered(Instant lastTriggered) {
        this.lastTriggered = lastTriggered;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MonitoringAlertRule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MonitoringAlertRule createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<MonitoringAlert> getAlerts() {
        return this.alerts;
    }

    public void setAlerts(Set<MonitoringAlert> monitoringAlerts) {
        if (this.alerts != null) {
            this.alerts.forEach(i -> i.setAlertRule(null));
        }
        if (monitoringAlerts != null) {
            monitoringAlerts.forEach(i -> i.setAlertRule(this));
        }
        this.alerts = monitoringAlerts;
    }

    public MonitoringAlertRule alerts(Set<MonitoringAlert> monitoringAlerts) {
        this.setAlerts(monitoringAlerts);
        return this;
    }

    public MonitoringAlertRule addAlerts(MonitoringAlert monitoringAlert) {
        this.alerts.add(monitoringAlert);
        monitoringAlert.setAlertRule(this);
        return this;
    }

    public MonitoringAlertRule removeAlerts(MonitoringAlert monitoringAlert) {
        this.alerts.remove(monitoringAlert);
        monitoringAlert.setAlertRule(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringAlertRule)) {
            return false;
        }
        return getId() != null && getId().equals(((MonitoringAlertRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringAlertRule{" +
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
