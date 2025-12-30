package fr.smartprod.paperdms.monitoring.service.criteria;

import fr.smartprod.paperdms.monitoring.domain.enumeration.HealthStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth} entity. This class is used
 * in {@link fr.smartprod.paperdms.monitoring.web.rest.MonitoringSystemHealthResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monitoring-system-healths?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringSystemHealthCriteria implements Serializable, Criteria {

    /**
     * Class for filtering HealthStatus
     */
    public static class HealthStatusFilter extends Filter<HealthStatus> {

        public HealthStatusFilter() {}

        public HealthStatusFilter(HealthStatusFilter filter) {
            super(filter);
        }

        @Override
        public HealthStatusFilter copy() {
            return new HealthStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serviceName;

    private HealthStatusFilter status;

    private StringFilter version;

    private LongFilter uptime;

    private DoubleFilter cpuUsage;

    private DoubleFilter memoryUsage;

    private DoubleFilter errorRate;

    private InstantFilter lastCheck;

    private Boolean distinct;

    public MonitoringSystemHealthCriteria() {}

    public MonitoringSystemHealthCriteria(MonitoringSystemHealthCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.serviceName = other.optionalServiceName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(HealthStatusFilter::copy).orElse(null);
        this.version = other.optionalVersion().map(StringFilter::copy).orElse(null);
        this.uptime = other.optionalUptime().map(LongFilter::copy).orElse(null);
        this.cpuUsage = other.optionalCpuUsage().map(DoubleFilter::copy).orElse(null);
        this.memoryUsage = other.optionalMemoryUsage().map(DoubleFilter::copy).orElse(null);
        this.errorRate = other.optionalErrorRate().map(DoubleFilter::copy).orElse(null);
        this.lastCheck = other.optionalLastCheck().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MonitoringSystemHealthCriteria copy() {
        return new MonitoringSystemHealthCriteria(this);
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

    public HealthStatusFilter getStatus() {
        return status;
    }

    public Optional<HealthStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public HealthStatusFilter status() {
        if (status == null) {
            setStatus(new HealthStatusFilter());
        }
        return status;
    }

    public void setStatus(HealthStatusFilter status) {
        this.status = status;
    }

    public StringFilter getVersion() {
        return version;
    }

    public Optional<StringFilter> optionalVersion() {
        return Optional.ofNullable(version);
    }

    public StringFilter version() {
        if (version == null) {
            setVersion(new StringFilter());
        }
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public LongFilter getUptime() {
        return uptime;
    }

    public Optional<LongFilter> optionalUptime() {
        return Optional.ofNullable(uptime);
    }

    public LongFilter uptime() {
        if (uptime == null) {
            setUptime(new LongFilter());
        }
        return uptime;
    }

    public void setUptime(LongFilter uptime) {
        this.uptime = uptime;
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

    public DoubleFilter getErrorRate() {
        return errorRate;
    }

    public Optional<DoubleFilter> optionalErrorRate() {
        return Optional.ofNullable(errorRate);
    }

    public DoubleFilter errorRate() {
        if (errorRate == null) {
            setErrorRate(new DoubleFilter());
        }
        return errorRate;
    }

    public void setErrorRate(DoubleFilter errorRate) {
        this.errorRate = errorRate;
    }

    public InstantFilter getLastCheck() {
        return lastCheck;
    }

    public Optional<InstantFilter> optionalLastCheck() {
        return Optional.ofNullable(lastCheck);
    }

    public InstantFilter lastCheck() {
        if (lastCheck == null) {
            setLastCheck(new InstantFilter());
        }
        return lastCheck;
    }

    public void setLastCheck(InstantFilter lastCheck) {
        this.lastCheck = lastCheck;
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
        final MonitoringSystemHealthCriteria that = (MonitoringSystemHealthCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serviceName, that.serviceName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(version, that.version) &&
            Objects.equals(uptime, that.uptime) &&
            Objects.equals(cpuUsage, that.cpuUsage) &&
            Objects.equals(memoryUsage, that.memoryUsage) &&
            Objects.equals(errorRate, that.errorRate) &&
            Objects.equals(lastCheck, that.lastCheck) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, status, version, uptime, cpuUsage, memoryUsage, errorRate, lastCheck, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringSystemHealthCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalServiceName().map(f -> "serviceName=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalVersion().map(f -> "version=" + f + ", ").orElse("") +
            optionalUptime().map(f -> "uptime=" + f + ", ").orElse("") +
            optionalCpuUsage().map(f -> "cpuUsage=" + f + ", ").orElse("") +
            optionalMemoryUsage().map(f -> "memoryUsage=" + f + ", ").orElse("") +
            optionalErrorRate().map(f -> "errorRate=" + f + ", ").orElse("") +
            optionalLastCheck().map(f -> "lastCheck=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
