package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.HealthStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.SystemHealth} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemHealthDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String serviceName;

    @NotNull
    private HealthStatus status;

    @Size(max = 50)
    private String version;

    private Long uptime;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double errorRate;

    @NotNull
    private Instant lastCheck;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public HealthStatus getStatus() {
        return status;
    }

    public void setStatus(HealthStatus status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Instant getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Instant lastCheck) {
        this.lastCheck = lastCheck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemHealthDTO)) {
            return false;
        }

        SystemHealthDTO systemHealthDTO = (SystemHealthDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemHealthDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemHealthDTO{" +
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
