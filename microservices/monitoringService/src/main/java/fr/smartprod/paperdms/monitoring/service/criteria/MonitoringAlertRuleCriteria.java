package fr.smartprod.paperdms.monitoring.service.criteria;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule} entity. This class is used
 * in {@link fr.smartprod.paperdms.monitoring.web.rest.MonitoringAlertRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monitoring-alert-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringAlertRuleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MonitoringAlertType
     */
    public static class MonitoringAlertTypeFilter extends Filter<MonitoringAlertType> {

        public MonitoringAlertTypeFilter() {}

        public MonitoringAlertTypeFilter(MonitoringAlertTypeFilter filter) {
            super(filter);
        }

        @Override
        public MonitoringAlertTypeFilter copy() {
            return new MonitoringAlertTypeFilter(this);
        }
    }

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private MonitoringAlertTypeFilter alertType;

    private SeverityFilter severity;

    private BooleanFilter isActive;

    private IntegerFilter triggerCount;

    private InstantFilter lastTriggered;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter alertsId;

    private Boolean distinct;

    public MonitoringAlertRuleCriteria() {}

    public MonitoringAlertRuleCriteria(MonitoringAlertRuleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.alertType = other.optionalAlertType().map(MonitoringAlertTypeFilter::copy).orElse(null);
        this.severity = other.optionalSeverity().map(SeverityFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.triggerCount = other.optionalTriggerCount().map(IntegerFilter::copy).orElse(null);
        this.lastTriggered = other.optionalLastTriggered().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.alertsId = other.optionalAlertsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MonitoringAlertRuleCriteria copy() {
        return new MonitoringAlertRuleCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public MonitoringAlertTypeFilter getAlertType() {
        return alertType;
    }

    public Optional<MonitoringAlertTypeFilter> optionalAlertType() {
        return Optional.ofNullable(alertType);
    }

    public MonitoringAlertTypeFilter alertType() {
        if (alertType == null) {
            setAlertType(new MonitoringAlertTypeFilter());
        }
        return alertType;
    }

    public void setAlertType(MonitoringAlertTypeFilter alertType) {
        this.alertType = alertType;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public IntegerFilter getTriggerCount() {
        return triggerCount;
    }

    public Optional<IntegerFilter> optionalTriggerCount() {
        return Optional.ofNullable(triggerCount);
    }

    public IntegerFilter triggerCount() {
        if (triggerCount == null) {
            setTriggerCount(new IntegerFilter());
        }
        return triggerCount;
    }

    public void setTriggerCount(IntegerFilter triggerCount) {
        this.triggerCount = triggerCount;
    }

    public InstantFilter getLastTriggered() {
        return lastTriggered;
    }

    public Optional<InstantFilter> optionalLastTriggered() {
        return Optional.ofNullable(lastTriggered);
    }

    public InstantFilter lastTriggered() {
        if (lastTriggered == null) {
            setLastTriggered(new InstantFilter());
        }
        return lastTriggered;
    }

    public void setLastTriggered(InstantFilter lastTriggered) {
        this.lastTriggered = lastTriggered;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getAlertsId() {
        return alertsId;
    }

    public Optional<LongFilter> optionalAlertsId() {
        return Optional.ofNullable(alertsId);
    }

    public LongFilter alertsId() {
        if (alertsId == null) {
            setAlertsId(new LongFilter());
        }
        return alertsId;
    }

    public void setAlertsId(LongFilter alertsId) {
        this.alertsId = alertsId;
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
        final MonitoringAlertRuleCriteria that = (MonitoringAlertRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(alertType, that.alertType) &&
            Objects.equals(severity, that.severity) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(triggerCount, that.triggerCount) &&
            Objects.equals(lastTriggered, that.lastTriggered) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(alertsId, that.alertsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            alertType,
            severity,
            isActive,
            triggerCount,
            lastTriggered,
            createdBy,
            createdDate,
            alertsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringAlertRuleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAlertType().map(f -> "alertType=" + f + ", ").orElse("") +
            optionalSeverity().map(f -> "severity=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalTriggerCount().map(f -> "triggerCount=" + f + ", ").orElse("") +
            optionalLastTriggered().map(f -> "lastTriggered=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalAlertsId().map(f -> "alertsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
