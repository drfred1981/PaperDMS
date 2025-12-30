package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringServiceStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonitoringServiceStatus.
 */
@Entity
@Table(name = "monitoring_service_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringServiceStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "service_name", length = 100, nullable = false, unique = true)
    private String serviceName;

    @Size(max = 50)
    @Column(name = "service_type", length = 50)
    private String serviceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MonitoringServiceStatusType status;

    @Size(max = 500)
    @Column(name = "endpoint", length = 500)
    private String endpoint;

    @Column(name = "port")
    private Integer port;

    @Size(max = 50)
    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "last_ping")
    private Instant lastPing;

    @NotNull
    @Column(name = "is_healthy", nullable = false)
    private Boolean isHealthy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonitoringServiceStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public MonitoringServiceStatus serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public MonitoringServiceStatus serviceType(String serviceType) {
        this.setServiceType(serviceType);
        return this;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public MonitoringServiceStatusType getStatus() {
        return this.status;
    }

    public MonitoringServiceStatus status(MonitoringServiceStatusType status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MonitoringServiceStatusType status) {
        this.status = status;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public MonitoringServiceStatus endpoint(String endpoint) {
        this.setEndpoint(endpoint);
        return this;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getPort() {
        return this.port;
    }

    public MonitoringServiceStatus port(Integer port) {
        this.setPort(port);
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return this.version;
    }

    public MonitoringServiceStatus version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getLastPing() {
        return this.lastPing;
    }

    public MonitoringServiceStatus lastPing(Instant lastPing) {
        this.setLastPing(lastPing);
        return this;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public Boolean getIsHealthy() {
        return this.isHealthy;
    }

    public MonitoringServiceStatus isHealthy(Boolean isHealthy) {
        this.setIsHealthy(isHealthy);
        return this;
    }

    public void setIsHealthy(Boolean isHealthy) {
        this.isHealthy = isHealthy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringServiceStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((MonitoringServiceStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringServiceStatus{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", serviceType='" + getServiceType() + "'" +
            ", status='" + getStatus() + "'" +
            ", endpoint='" + getEndpoint() + "'" +
            ", port=" + getPort() +
            ", version='" + getVersion() + "'" +
            ", lastPing='" + getLastPing() + "'" +
            ", isHealthy='" + getIsHealthy() + "'" +
            "}";
    }
}
