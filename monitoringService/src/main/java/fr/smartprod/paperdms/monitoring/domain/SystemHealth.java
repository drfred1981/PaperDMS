package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.HealthStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SystemHealth.
 */
@Entity
@Table(name = "system_health")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemHealth implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "service_name", length = 100, nullable = false)
    private String serviceName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HealthStatus status;

    @Size(max = 50)
    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "uptime")
    private Long uptime;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    @Column(name = "memory_usage")
    private Double memoryUsage;

    @Column(name = "error_rate")
    private Double errorRate;

    @NotNull
    @Column(name = "last_check", nullable = false)
    private Instant lastCheck;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemHealth id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public SystemHealth serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public HealthStatus getStatus() {
        return this.status;
    }

    public SystemHealth status(HealthStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(HealthStatus status) {
        this.status = status;
    }

    public String getVersion() {
        return this.version;
    }

    public SystemHealth version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getUptime() {
        return this.uptime;
    }

    public SystemHealth uptime(Long uptime) {
        this.setUptime(uptime);
        return this;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Double getCpuUsage() {
        return this.cpuUsage;
    }

    public SystemHealth cpuUsage(Double cpuUsage) {
        this.setCpuUsage(cpuUsage);
        return this;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return this.memoryUsage;
    }

    public SystemHealth memoryUsage(Double memoryUsage) {
        this.setMemoryUsage(memoryUsage);
        return this;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getErrorRate() {
        return this.errorRate;
    }

    public SystemHealth errorRate(Double errorRate) {
        this.setErrorRate(errorRate);
        return this;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Instant getLastCheck() {
        return this.lastCheck;
    }

    public SystemHealth lastCheck(Instant lastCheck) {
        this.setLastCheck(lastCheck);
        return this;
    }

    public void setLastCheck(Instant lastCheck) {
        this.lastCheck = lastCheck;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemHealth)) {
            return false;
        }
        return getId() != null && getId().equals(((SystemHealth) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemHealth{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", status='" + getStatus() + "'" +
            ", version='" + getVersion() + "'" +
            ", uptime=" + getUptime() +
            ", cpuUsage=" + getCpuUsage() +
            ", memoryUsage=" + getMemoryUsage() +
            ", errorRate=" + getErrorRate() +
            ", lastCheck='" + getLastCheck() + "'" +
            "}";
    }
}
