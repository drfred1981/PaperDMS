package fr.smartprod.paperdms.monitoring.service.criteria;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlert} entity. This class is used
 * in {@link fr.smartprod.paperdms.monitoring.web.rest.MonitoringAlertResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monitoring-alerts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringAlertCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Severity
     */
    public static class SeverityFilter extends Filter<Severity> {

        public SeverityFilter() {}

        public SeverityFilter(SeverityFilter filter) {
            super(filter);
        }

        @Override
        public SeverityFilter copy() {
            return new SeverityFilter(this);
        }
    }

    /**
     * Class for filtering MonitoringAlertStatus
     */
    public static class MonitoringAlertStatusFilter extends Filter<MonitoringAlertStatus> {

        public MonitoringAlertStatusFilter() {}

        public MonitoringAlertStatusFilter(MonitoringAlertStatusFilter filter) {
            super(filter);
        }

        @Override
        public MonitoringAlertStatusFilter copy() {
            return new MonitoringAlertStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SeverityFilter severity;

    private StringFilter title;

    private StringFilter entityType;

    private StringFilter entityName;

    private MonitoringAlertStatusFilter status;

    private InstantFilter triggeredDate;

    private StringFilter acknowledgedBy;

    private InstantFilter acknowledgedDate;

    private StringFilter resolvedBy;

    private InstantFilter resolvedDate;

    private LongFilter alertRuleId;

    private Boolean distinct;

    public MonitoringAlertCriteria() {}

    public MonitoringAlertCriteria(MonitoringAlertCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.severity = other.optionalSeverity().map(SeverityFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.entityType = other.optionalEntityType().map(StringFilter::copy).orElse(null);
        this.entityName = other.optionalEntityName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(MonitoringAlertStatusFilter::copy).orElse(null);
        this.triggeredDate = other.optionalTriggeredDate().map(InstantFilter::copy).orElse(null);
        this.acknowledgedBy = other.optionalAcknowledgedBy().map(StringFilter::copy).orElse(null);
        this.acknowledgedDate = other.optionalAcknowledgedDate().map(InstantFilter::copy).orElse(null);
        this.resolvedBy = other.optionalResolvedBy().map(StringFilter::copy).orElse(null);
        this.resolvedDate = other.optionalResolvedDate().map(InstantFilter::copy).orElse(null);
        this.alertRuleId = other.optionalAlertRuleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MonitoringAlertCriteria copy() {
        return new MonitoringAlertCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public SeverityFilter getSeverity() {
        return severity;
    }

    public Optional<SeverityFilter> optionalSeverity() {
        return Optional.ofNullable(severity);
    }

    public SeverityFilter severity() {
        if (severity == null) {
            setSeverity(new SeverityFilter());
        }
        return severity;
    }

    public void setSeverity(SeverityFilter severity) {
        this.severity = severity;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getEntityType() {
        return entityType;
    }

    public Optional<StringFilter> optionalEntityType() {
        return Optional.ofNullable(entityType);
    }

    public StringFilter entityType() {
        if (entityType == null) {
            setEntityType(new StringFilter());
        }
        return entityType;
    }

    public void setEntityType(StringFilter entityType) {
        this.entityType = entityType;
    }

    public StringFilter getEntityName() {
        return entityName;
    }

    public Optional<StringFilter> optionalEntityName() {
        return Optional.ofNullable(entityName);
    }

    public StringFilter entityName() {
        if (entityName == null) {
            setEntityName(new StringFilter());
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public MonitoringAlertStatusFilter getStatus() {
        return status;
    }

    public Optional<MonitoringAlertStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public MonitoringAlertStatusFilter status() {
        if (status == null) {
            setStatus(new MonitoringAlertStatusFilter());
        }
        return status;
    }

    public void setStatus(MonitoringAlertStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getTriggeredDate() {
        return triggeredDate;
    }

    public Optional<InstantFilter> optionalTriggeredDate() {
        return Optional.ofNullable(triggeredDate);
    }

    public InstantFilter triggeredDate() {
        if (triggeredDate == null) {
            setTriggeredDate(new InstantFilter());
        }
        return triggeredDate;
    }

    public void setTriggeredDate(InstantFilter triggeredDate) {
        this.triggeredDate = triggeredDate;
    }

    public StringFilter getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public Optional<StringFilter> optionalAcknowledgedBy() {
        return Optional.ofNullable(acknowledgedBy);
    }

    public StringFilter acknowledgedBy() {
        if (acknowledgedBy == null) {
            setAcknowledgedBy(new StringFilter());
        }
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(StringFilter acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public InstantFilter getAcknowledgedDate() {
        return acknowledgedDate;
    }

    public Optional<InstantFilter> optionalAcknowledgedDate() {
        return Optional.ofNullable(acknowledgedDate);
    }

    public InstantFilter acknowledgedDate() {
        if (acknowledgedDate == null) {
            setAcknowledgedDate(new InstantFilter());
        }
        return acknowledgedDate;
    }

    public void setAcknowledgedDate(InstantFilter acknowledgedDate) {
        this.acknowledgedDate = acknowledgedDate;
    }

    public StringFilter getResolvedBy() {
        return resolvedBy;
    }

    public Optional<StringFilter> optionalResolvedBy() {
        return Optional.ofNullable(resolvedBy);
    }

    public StringFilter resolvedBy() {
        if (resolvedBy == null) {
            setResolvedBy(new StringFilter());
        }
        return resolvedBy;
    }

    public void setResolvedBy(StringFilter resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public InstantFilter getResolvedDate() {
        return resolvedDate;
    }

    public Optional<InstantFilter> optionalResolvedDate() {
        return Optional.ofNullable(resolvedDate);
    }

    public InstantFilter resolvedDate() {
        if (resolvedDate == null) {
            setResolvedDate(new InstantFilter());
        }
        return resolvedDate;
    }

    public void setResolvedDate(InstantFilter resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public LongFilter getAlertRuleId() {
        return alertRuleId;
    }

    public Optional<LongFilter> optionalAlertRuleId() {
        return Optional.ofNullable(alertRuleId);
    }

    public LongFilter alertRuleId() {
        if (alertRuleId == null) {
            setAlertRuleId(new LongFilter());
        }
        return alertRuleId;
    }

    public void setAlertRuleId(LongFilter alertRuleId) {
        this.alertRuleId = alertRuleId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MonitoringAlertCriteria that = (MonitoringAlertCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(severity, that.severity) &&
            Objects.equals(title, that.title) &&
            Objects.equals(entityType, that.entityType) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(triggeredDate, that.triggeredDate) &&
            Objects.equals(acknowledgedBy, that.acknowledgedBy) &&
            Objects.equals(acknowledgedDate, that.acknowledgedDate) &&
            Objects.equals(resolvedBy, that.resolvedBy) &&
            Objects.equals(resolvedDate, that.resolvedDate) &&
            Objects.equals(alertRuleId, that.alertRuleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            severity,
            title,
            entityType,
            entityName,
            status,
            triggeredDate,
            acknowledgedBy,
            acknowledgedDate,
            resolvedBy,
            resolvedDate,
            alertRuleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringAlertCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSeverity().map(f -> "severity=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalEntityType().map(f -> "entityType=" + f + ", ").orElse("") +
            optionalEntityName().map(f -> "entityName=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTriggeredDate().map(f -> "triggeredDate=" + f + ", ").orElse("") +
            optionalAcknowledgedBy().map(f -> "acknowledgedBy=" + f + ", ").orElse("") +
            optionalAcknowledgedDate().map(f -> "acknowledgedDate=" + f + ", ").orElse("") +
            optionalResolvedBy().map(f -> "resolvedBy=" + f + ", ").orElse("") +
            optionalResolvedDate().map(f -> "resolvedDate=" + f + ", ").orElse("") +
            optionalAlertRuleId().map(f -> "alertRuleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
