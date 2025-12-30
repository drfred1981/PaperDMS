package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringServiceStatusType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringServiceStatusDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String serviceName;

    @Size(max = 50)
    private String serviceType;

    @NotNull
    private MonitoringServiceStatusType status;

    @Size(max = 500)
    private String endpoint;

    private Integer port;

    @Size(max = 50)
    private String version;

    private Instant lastPing;

    @NotNull
    private Boolean isHealthy;

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public MonitoringServiceStatusType getStatus() {
        return status;
    }

    public void setStatus(MonitoringServiceStatusType status) {
        this.status = status;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public Boolean getIsHealthy() {
        return isHealthy;
    }

    public void setIsHealthy(Boolean isHealthy) {
        this.isHealthy = isHealthy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringServiceStatusDTO)) {
            return false;
        }

        MonitoringServiceStatusDTO monitoringServiceStatusDTO = (MonitoringServiceStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monitoringServiceStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringServiceStatusDTO{" +
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
