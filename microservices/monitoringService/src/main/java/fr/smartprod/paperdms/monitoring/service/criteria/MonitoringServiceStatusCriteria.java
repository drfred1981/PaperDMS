package fr.smartprod.paperdms.monitoring.service.criteria;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringServiceStatusType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus} entity. This class is used
 * in {@link fr.smartprod.paperdms.monitoring.web.rest.MonitoringServiceStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monitoring-service-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringServiceStatusCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MonitoringServiceStatusType
     */
    public static class MonitoringServiceStatusTypeFilter extends Filter<MonitoringServiceStatusType> {

        public MonitoringServiceStatusTypeFilter() {}

        public MonitoringServiceStatusTypeFilter(MonitoringServiceStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public MonitoringServiceStatusTypeFilter copy() {
            return new MonitoringServiceStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serviceName;

    private StringFilter serviceType;

    private MonitoringServiceStatusTypeFilter status;

    private StringFilter endpoint;

    private IntegerFilter port;

    private StringFilter version;

    private InstantFilter lastPing;

    private BooleanFilter isHealthy;

    private Boolean distinct;

    public MonitoringServiceStatusCriteria() {}

    public MonitoringServiceStatusCriteria(MonitoringServiceStatusCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.serviceName = other.optionalServiceName().map(StringFilter::copy).orElse(null);
        this.serviceType = other.optionalServiceType().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(MonitoringServiceStatusTypeFilter::copy).orElse(null);
        this.endpoint = other.optionalEndpoint().map(StringFilter::copy).orElse(null);
        this.port = other.optionalPort().map(IntegerFilter::copy).orElse(null);
        this.version = other.optionalVersion().map(StringFilter::copy).orElse(null);
        this.lastPing = other.optionalLastPing().map(InstantFilter::copy).orElse(null);
        this.isHealthy = other.optionalIsHealthy().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MonitoringServiceStatusCriteria copy() {
        return new MonitoringServiceStatusCriteria(this);
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

    public StringFilter getServiceType() {
        return serviceType;
    }

    public Optional<StringFilter> optionalServiceType() {
        return Optional.ofNullable(serviceType);
    }

    public StringFilter serviceType() {
        if (serviceType == null) {
            setServiceType(new StringFilter());
        }
        return serviceType;
    }

    public void setServiceType(StringFilter serviceType) {
        this.serviceType = serviceType;
    }

    public MonitoringServiceStatusTypeFilter getStatus() {
        return status;
    }

    public Optional<MonitoringServiceStatusTypeFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public MonitoringServiceStatusTypeFilter status() {
        if (status == null) {
            setStatus(new MonitoringServiceStatusTypeFilter());
        }
        return status;
    }

    public void setStatus(MonitoringServiceStatusTypeFilter status) {
        this.status = status;
    }

    public StringFilter getEndpoint() {
        return endpoint;
    }

    public Optional<StringFilter> optionalEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    public StringFilter endpoint() {
        if (endpoint == null) {
            setEndpoint(new StringFilter());
        }
        return endpoint;
    }

    public void setEndpoint(StringFilter endpoint) {
        this.endpoint = endpoint;
    }

    public IntegerFilter getPort() {
        return port;
    }

    public Optional<IntegerFilter> optionalPort() {
        return Optional.ofNullable(port);
    }

    public IntegerFilter port() {
        if (port == null) {
            setPort(new IntegerFilter());
        }
        return port;
    }

    public void setPort(IntegerFilter port) {
        this.port = port;
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

    public InstantFilter getLastPing() {
        return lastPing;
    }

    public Optional<InstantFilter> optionalLastPing() {
        return Optional.ofNullable(lastPing);
    }

    public InstantFilter lastPing() {
        if (lastPing == null) {
            setLastPing(new InstantFilter());
        }
        return lastPing;
    }

    public void setLastPing(InstantFilter lastPing) {
        this.lastPing = lastPing;
    }

    public BooleanFilter getIsHealthy() {
        return isHealthy;
    }

    public Optional<BooleanFilter> optionalIsHealthy() {
        return Optional.ofNullable(isHealthy);
    }

    public BooleanFilter isHealthy() {
        if (isHealthy == null) {
            setIsHealthy(new BooleanFilter());
        }
        return isHealthy;
    }

    public void setIsHealthy(BooleanFilter isHealthy) {
        this.isHealthy = isHealthy;
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
        final MonitoringServiceStatusCriteria that = (MonitoringServiceStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serviceName, that.serviceName) &&
            Objects.equals(serviceType, that.serviceType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(endpoint, that.endpoint) &&
            Objects.equals(port, that.port) &&
            Objects.equals(version, that.version) &&
            Objects.equals(lastPing, that.lastPing) &&
            Objects.equals(isHealthy, that.isHealthy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, serviceType, status, endpoint, port, version, lastPing, isHealthy, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringServiceStatusCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalServiceName().map(f -> "serviceName=" + f + ", ").orElse("") +
            optionalServiceType().map(f -> "serviceType=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalEndpoint().map(f -> "endpoint=" + f + ", ").orElse("") +
            optionalPort().map(f -> "port=" + f + ", ").orElse("") +
            optionalVersion().map(f -> "version=" + f + ", ").orElse("") +
            optionalLastPing().map(f -> "lastPing=" + f + ", ").orElse("") +
            optionalIsHealthy().map(f -> "isHealthy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
