package fr.smartprod.paperdms.monitoring.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonitoringAlert.
 */
@Entity
@Table(name = "monitoring_alert")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Size(max = 100)
    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_name")
    private String entityName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MonitoringAlertStatus status;

    @NotNull
    @Column(name = "triggered_date", nullable = false)
    private Instant triggeredDate;

    @Size(max = 50)
    @Column(name = "acknowledged_by", length = 50)
    private String acknowledgedBy;

    @Column(name = "acknowledged_date")
    private Instant acknowledgedDate;

    @Size(max = 50)
    @Column(name = "resolved_by", length = 50)
    private String resolvedBy;

    @Column(name = "resolved_date")
    private Instant resolvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alerts" }, allowSetters = true)
    private MonitoringAlertRule alertRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonitoringAlert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public MonitoringAlert severity(Severity severity) {
        this.setSeverity(severity);
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return this.title;
    }

    public MonitoringAlert title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public MonitoringAlert message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public MonitoringAlert entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public MonitoringAlert entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public MonitoringAlertStatus getStatus() {
        return this.status;
    }

    public MonitoringAlert status(MonitoringAlertStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MonitoringAlertStatus status) {
        this.status = status;
    }

    public Instant getTriggeredDate() {
        return this.triggeredDate;
    }

    public MonitoringAlert triggeredDate(Instant triggeredDate) {
        this.setTriggeredDate(triggeredDate);
        return this;
    }

    public void setTriggeredDate(Instant triggeredDate) {
        this.triggeredDate = triggeredDate;
    }

    public String getAcknowledgedBy() {
        return this.acknowledgedBy;
    }

    public MonitoringAlert acknowledgedBy(String acknowledgedBy) {
        this.setAcknowledgedBy(acknowledgedBy);
        return this;
    }

    public void setAcknowledgedBy(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public Instant getAcknowledgedDate() {
        return this.acknowledgedDate;
    }

    public MonitoringAlert acknowledgedDate(Instant acknowledgedDate) {
        this.setAcknowledgedDate(acknowledgedDate);
        return this;
    }

    public void setAcknowledgedDate(Instant acknowledgedDate) {
        this.acknowledgedDate = acknowledgedDate;
    }

    public String getResolvedBy() {
        return this.resolvedBy;
    }

    public MonitoringAlert resolvedBy(String resolvedBy) {
        this.setResolvedBy(resolvedBy);
        return this;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Instant getResolvedDate() {
        return this.resolvedDate;
    }

    public MonitoringAlert resolvedDate(Instant resolvedDate) {
        this.setResolvedDate(resolvedDate);
        return this;
    }

    public void setResolvedDate(Instant resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public MonitoringAlertRule getAlertRule() {
        return this.alertRule;
    }

    public void setAlertRule(MonitoringAlertRule monitoringAlertRule) {
        this.alertRule = monitoringAlertRule;
    }

    public MonitoringAlert alertRule(MonitoringAlertRule monitoringAlertRule) {
        this.setAlertRule(monitoringAlertRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringAlert)) {
            return false;
        }
        return getId() != null && getId().equals(((MonitoringAlert) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringAlert{" +
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
            "}";
    }
}
