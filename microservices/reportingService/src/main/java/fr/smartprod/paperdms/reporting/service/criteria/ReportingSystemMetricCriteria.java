package fr.smartprod.paperdms.reporting.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric} entity. This class is used
 * in {@link fr.smartprod.paperdms.reporting.web.rest.ReportingSystemMetricResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reporting-system-metrics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingSystemMetricCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter metricName;

    private DoubleFilter cpuUsage;

    private DoubleFilter memoryUsage;

    private DoubleFilter diskUsage;

    private LongFilter networkIn;

    private LongFilter networkOut;

    private IntegerFilter activeConnections;

    private InstantFilter timestamp;

    private Boolean distinct;

    public ReportingSystemMetricCriteria() {}

    public ReportingSystemMetricCriteria(ReportingSystemMetricCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.metricName = other.optionalMetricName().map(StringFilter::copy).orElse(null);
        this.cpuUsage = other.optionalCpuUsage().map(DoubleFilter::copy).orElse(null);
        this.memoryUsage = other.optionalMemoryUsage().map(DoubleFilter::copy).orElse(null);
        this.diskUsage = other.optionalDiskUsage().map(DoubleFilter::copy).orElse(null);
        this.networkIn = other.optionalNetworkIn().map(LongFilter::copy).orElse(null);
        this.networkOut = other.optionalNetworkOut().map(LongFilter::copy).orElse(null);
        this.activeConnections = other.optionalActiveConnections().map(IntegerFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportingSystemMetricCriteria copy() {
        return new ReportingSystemMetricCriteria(this);
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

    public DoubleFilter getCpuUsage() {
        return cpuUsage;
    }

    public Optional<DoubleFilter> optionalCpuUsage() {
        return Optional.ofNullable(cpuUsage);
    }

    public DoubleFilter cpuUsage() {
        if (cpuUsage == null) {
            setCpuUsage(new DoubleFilter());
        }
        return cpuUsage;
    }

    public void setCpuUsage(DoubleFilter cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public DoubleFilter getMemoryUsage() {
        return memoryUsage;
    }

    public Optional<DoubleFilter> optionalMemoryUsage() {
        return Optional.ofNullable(memoryUsage);
    }

    public DoubleFilter memoryUsage() {
        if (memoryUsage == null) {
            setMemoryUsage(new DoubleFilter());
        }
        return memoryUsage;
    }

    public void setMemoryUsage(DoubleFilter memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public DoubleFilter getDiskUsage() {
        return diskUsage;
    }

    public Optional<DoubleFilter> optionalDiskUsage() {
        return Optional.ofNullable(diskUsage);
    }

    public DoubleFilter diskUsage() {
        if (diskUsage == null) {
            setDiskUsage(new DoubleFilter());
        }
        return diskUsage;
    }

    public void setDiskUsage(DoubleFilter diskUsage) {
        this.diskUsage = diskUsage;
    }

    public LongFilter getNetworkIn() {
        return networkIn;
    }

    public Optional<LongFilter> optionalNetworkIn() {
        return Optional.ofNullable(networkIn);
    }

    public LongFilter networkIn() {
        if (networkIn == null) {
            setNetworkIn(new LongFilter());
        }
        return networkIn;
    }

    public void setNetworkIn(LongFilter networkIn) {
        this.networkIn = networkIn;
    }

    public LongFilter getNetworkOut() {
        return networkOut;
    }

    public Optional<LongFilter> optionalNetworkOut() {
        return Optional.ofNullable(networkOut);
    }

    public LongFilter networkOut() {
        if (networkOut == null) {
            setNetworkOut(new LongFilter());
        }
        return networkOut;
    }

    public void setNetworkOut(LongFilter networkOut) {
        this.networkOut = networkOut;
    }

    public IntegerFilter getActiveConnections() {
        return activeConnections;
    }

    public Optional<IntegerFilter> optionalActiveConnections() {
        return Optional.ofNullable(activeConnections);
    }

    public IntegerFilter activeConnections() {
        if (activeConnections == null) {
            setActiveConnections(new IntegerFilter());
        }
        return activeConnections;
    }

    public void setActiveConnections(IntegerFilter activeConnections) {
        this.activeConnections = activeConnections;
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
        final ReportingSystemMetricCriteria that = (ReportingSystemMetricCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(metricName, that.metricName) &&
            Objects.equals(cpuUsage, that.cpuUsage) &&
            Objects.equals(memoryUsage, that.memoryUsage) &&
            Objects.equals(diskUsage, that.diskUsage) &&
            Objects.equals(networkIn, that.networkIn) &&
            Objects.equals(networkOut, that.networkOut) &&
            Objects.equals(activeConnections, that.activeConnections) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            metricName,
            cpuUsage,
            memoryUsage,
            diskUsage,
            networkIn,
            networkOut,
            activeConnections,
            timestamp,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingSystemMetricCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMetricName().map(f -> "metricName=" + f + ", ").orElse("") +
            optionalCpuUsage().map(f -> "cpuUsage=" + f + ", ").orElse("") +
            optionalMemoryUsage().map(f -> "memoryUsage=" + f + ", ").orElse("") +
            optionalDiskUsage().map(f -> "diskUsage=" + f + ", ").orElse("") +
            optionalNetworkIn().map(f -> "networkIn=" + f + ", ").orElse("") +
            optionalNetworkOut().map(f -> "networkOut=" + f + ", ").orElse("") +
            optionalActiveConnections().map(f -> "activeConnections=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
