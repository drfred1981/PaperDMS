package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.AlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Alert.
 */
@Entity
@Table(name = "alert")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "alert_rule_id", nullable = false)
    private Long alertRuleId;

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

    @Column(name = "entity_id")
    private Long entityId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status;

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

    @ManyToOne(optional = false)
    @NotNull
    private AlertRule alertRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlertRuleId() {
        return this.alertRuleId;
    }

    public Alert alertRuleId(Long alertRuleId) {
        this.setAlertRuleId(alertRuleId);
        return this;
    }

    public void setAlertRuleId(Long alertRuleId) {
        this.alertRuleId = alertRuleId;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public Alert severity(Severity severity) {
        this.setSeverity(severity);
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return this.title;
    }

    public Alert title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public Alert message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public Alert entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    public Alert entityId(Long entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public AlertStatus getStatus() {
        return this.status;
    }

    public Alert status(AlertStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public Instant getTriggeredDate() {
        return this.triggeredDate;
    }

    public Alert triggeredDate(Instant triggeredDate) {
        this.setTriggeredDate(triggeredDate);
        return this;
    }

    public void setTriggeredDate(Instant triggeredDate) {
        this.triggeredDate = triggeredDate;
    }

    public String getAcknowledgedBy() {
        return this.acknowledgedBy;
    }

    public Alert acknowledgedBy(String acknowledgedBy) {
        this.setAcknowledgedBy(acknowledgedBy);
        return this;
    }

    public void setAcknowledgedBy(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public Instant getAcknowledgedDate() {
        return this.acknowledgedDate;
    }

    public Alert acknowledgedDate(Instant acknowledgedDate) {
        this.setAcknowledgedDate(acknowledgedDate);
        return this;
    }

    public void setAcknowledgedDate(Instant acknowledgedDate) {
        this.acknowledgedDate = acknowledgedDate;
    }

    public String getResolvedBy() {
        return this.resolvedBy;
    }

    public Alert resolvedBy(String resolvedBy) {
        this.setResolvedBy(resolvedBy);
        return this;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Instant getResolvedDate() {
        return this.resolvedDate;
    }

    public Alert resolvedDate(Instant resolvedDate) {
        this.setResolvedDate(resolvedDate);
        return this;
    }

    public void setResolvedDate(Instant resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public AlertRule getAlertRule() {
        return this.alertRule;
    }

    public void setAlertRule(AlertRule alertRule) {
        this.alertRule = alertRule;
    }

    public Alert alertRule(AlertRule alertRule) {
        this.setAlertRule(alertRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alert)) {
            return false;
        }
        return getId() != null && getId().equals(((Alert) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alert{" +
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
            "}";
    }
}
