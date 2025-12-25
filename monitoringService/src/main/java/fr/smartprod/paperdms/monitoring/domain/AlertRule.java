package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.AlertType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlertRule.
 */
@Entity
@Table(name = "alert_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertRule implements Serializable {

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
    private AlertType alertType;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlertRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AlertRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AlertRule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlertType getAlertType() {
        return this.alertType;
    }

    public AlertRule alertType(AlertType alertType) {
        this.setAlertType(alertType);
        return this;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public String getConditions() {
        return this.conditions;
    }

    public AlertRule conditions(String conditions) {
        this.setConditions(conditions);
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public AlertRule severity(Severity severity) {
        this.setSeverity(severity);
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getRecipients() {
        return this.recipients;
    }

    public AlertRule recipients(String recipients) {
        this.setRecipients(recipients);
        return this;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AlertRule isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getTriggerCount() {
        return this.triggerCount;
    }

    public AlertRule triggerCount(Integer triggerCount) {
        this.setTriggerCount(triggerCount);
        return this;
    }

    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Instant getLastTriggered() {
        return this.lastTriggered;
    }

    public AlertRule lastTriggered(Instant lastTriggered) {
        this.setLastTriggered(lastTriggered);
        return this;
    }

    public void setLastTriggered(Instant lastTriggered) {
        this.lastTriggered = lastTriggered;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AlertRule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AlertRule createdDate(Instant createdDate) {
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
        if (!(o instanceof AlertRule)) {
            return false;
        }
        return getId() != null && getId().equals(((AlertRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertRule{" +
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
