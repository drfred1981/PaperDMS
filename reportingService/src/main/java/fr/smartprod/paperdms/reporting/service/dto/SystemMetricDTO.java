package fr.smartprod.paperdms.reporting.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.SystemMetric} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemMetricDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String metricName;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double diskUsage;

    private Long networkIn;

    private Long networkOut;

    private Integer activeConnections;

    @NotNull
    private Instant timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
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

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Long getNetworkIn() {
        return networkIn;
    }

    public void setNetworkIn(Long networkIn) {
        this.networkIn = networkIn;
    }

    public Long getNetworkOut() {
        return networkOut;
    }

    public void setNetworkOut(Long networkOut) {
        this.networkOut = networkOut;
    }

    public Integer getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(Integer activeConnections) {
        this.activeConnections = activeConnections;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemMetricDTO)) {
            return false;
        }

        SystemMetricDTO systemMetricDTO = (SystemMetricDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemMetricDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemMetricDTO{" +
            "id=" + getId() +
            ", metricName='" + getMetricName() + "'" +
            ", cpuUsage=" + getCpuUsage() +
            ", memoryUsage=" + getMemoryUsage() +
            ", diskUsage=" + getDiskUsage() +
            ", networkIn=" + getNetworkIn() +
            ", networkOut=" + getNetworkOut() +
            ", activeConnections=" + getActiveConnections() +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
