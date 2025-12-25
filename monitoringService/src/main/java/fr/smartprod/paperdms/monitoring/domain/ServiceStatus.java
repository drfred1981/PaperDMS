package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.ServiceStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceStatus.
 */
@Entity
@Table(name = "service_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceStatus implements Serializable {

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
    private ServiceStatusType status;

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

    public ServiceStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public ServiceStatus serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public ServiceStatus serviceType(String serviceType) {
        this.setServiceType(serviceType);
        return this;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceStatusType getStatus() {
        return this.status;
    }

    public ServiceStatus status(ServiceStatusType status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ServiceStatusType status) {
        this.status = status;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public ServiceStatus endpoint(String endpoint) {
        this.setEndpoint(endpoint);
        return this;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getPort() {
        return this.port;
    }

    public ServiceStatus port(Integer port) {
        this.setPort(port);
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return this.version;
    }

    public ServiceStatus version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getLastPing() {
        return this.lastPing;
    }

    public ServiceStatus lastPing(Instant lastPing) {
        this.setLastPing(lastPing);
        return this;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public Boolean getIsHealthy() {
        return this.isHealthy;
    }

    public ServiceStatus isHealthy(Boolean isHealthy) {
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
        if (!(o instanceof ServiceStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceStatus{" +
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
