package fr.smartprod.paperdms.reporting.service.criteria;

import fr.smartprod.paperdms.reporting.domain.enumeration.MetricType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric} entity. This class is used
 * in {@link fr.smartprod.paperdms.reporting.web.rest.ReportingPerformanceMetricResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reporting-performance-metrics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingPerformanceMetricCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MetricType
     */
    public static class MetricTypeFilter extends Filter<MetricType> {

        public MetricTypeFilter() {}

        public MetricTypeFilter(MetricTypeFilter filter) {
            super(filter);
        }

        @Override
        public MetricTypeFilter copy() {
            return new MetricTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter metricName;

    private MetricTypeFilter metricType;

    private DoubleFilter value;

    private StringFilter unit;

    private StringFilter serviceName;

    private InstantFilter timestamp;

    private Boolean distinct;

    public ReportingPerformanceMetricCriteria() {}

    public ReportingPerformanceMetricCriteria(ReportingPerformanceMetricCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.metricName = other.optionalMetricName().map(StringFilter::copy).orElse(null);
        this.metricType = other.optionalMetricType().map(MetricTypeFilter::copy).orElse(null);
        this.value = other.optionalValue().map(DoubleFilter::copy).orElse(null);
        this.unit = other.optionalUnit().map(StringFilter::copy).orElse(null);
        this.serviceName = other.optionalServiceName().map(StringFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportingPerformanceMetricCriteria copy() {
        return new ReportingPerformanceMetricCriteria(this);
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

    public StringFilter getMetricName() {
        return metricName;
    }

    public Optional<StringFilter> optionalMetricName() {
        return Optional.ofNullable(metricName);
    }

    public StringFilter metricName() {
        if (metricName == null) {
            setMetricName(new StringFilter());
        }
        return metricName;
    }

    public void setMetricName(StringFilter metricName) {
        this.metricName = metricName;
    }

    public MetricTypeFilter getMetricType() {
        return metricType;
    }

    public Optional<MetricTypeFilter> optionalMetricType() {
        return Optional.ofNullable(metricType);
    }

    public MetricTypeFilter metricType() {
        if (metricType == null) {
            setMetricType(new MetricTypeFilter());
        }
        return metricType;
    }

    public void setMetricType(MetricTypeFilter metricType) {
        this.metricType = metricType;
    }

    public DoubleFilter getValue() {
        return value;
    }

    public Optional<DoubleFilter> optionalValue() {
        return Optional.ofNullable(value);
    }

    public DoubleFilter value() {
        if (value == null) {
            setValue(new DoubleFilter());
        }
        return value;
    }

    public void setValue(DoubleFilter value) {
        this.value = value;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public Optional<StringFilter> optionalUnit() {
        return Optional.ofNullable(unit);
    }

    public StringFilter unit() {
        if (unit == null) {
            setUnit(new StringFilter());
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public StringFilter getServiceName() {
        return serviceName;
    }

    public Optional<StringFilter> optionalServiceName() {
        return Optional.ofNullable(serviceName);
    }

    public StringFilter serviceName() {
        if (serviceName == null) {
            setServiceName(new StringFilter());
        }
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public Optional<InstantFilter> optionalTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            setTimestamp(new InstantFilter());
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
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
        final ReportingPerformanceMetricCriteria that = (ReportingPerformanceMetricCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(metricName, that.metricName) &&
            Objects.equals(metricType, that.metricType) &&
            Objects.equals(value, that.value) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(serviceName, that.serviceName) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metricName, metricType, value, unit, serviceName, timestamp, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingPerformanceMetricCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMetricName().map(f -> "metricName=" + f + ", ").orElse("") +
            optionalMetricType().map(f -> "metricType=" + f + ", ").orElse("") +
            optionalValue().map(f -> "value=" + f + ", ").orElse("") +
            optionalUnit().map(f -> "unit=" + f + ", ").orElse("") +
            optionalServiceName().map(f -> "serviceName=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
