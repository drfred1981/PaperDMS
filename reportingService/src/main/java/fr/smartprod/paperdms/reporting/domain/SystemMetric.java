package fr.smartprod.paperdms.reporting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SystemMetric.
 */
@Entity
@Table(name = "system_metric")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemMetric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "metric_name", length = 100, nullable = false)
    private String metricName;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    @Column(name = "memory_usage")
    private Double memoryUsage;

    @Column(name = "disk_usage")
    private Double diskUsage;

    @Column(name = "network_in")
    private Long networkIn;

    @Column(name = "network_out")
    private Long networkOut;

    @Column(name = "active_connections")
    private Integer activeConnections;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemMetric id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetricName() {
        return this.metricName;
    }

    public SystemMetric metricName(String metricName) {
        this.setMetricName(metricName);
        return this;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Double getCpuUsage() {
        return this.cpuUsage;
    }

    public SystemMetric cpuUsage(Double cpuUsage) {
        this.setCpuUsage(cpuUsage);
        return this;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return this.memoryUsage;
    }

    public SystemMetric memoryUsage(Double memoryUsage) {
        this.setMemoryUsage(memoryUsage);
        return this;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getDiskUsage() {
        return this.diskUsage;
    }

    public SystemMetric diskUsage(Double diskUsage) {
        this.setDiskUsage(diskUsage);
        return this;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Long getNetworkIn() {
        return this.networkIn;
    }

    public SystemMetric networkIn(Long networkIn) {
        this.setNetworkIn(networkIn);
        return this;
    }

    public void setNetworkIn(Long networkIn) {
        this.networkIn = networkIn;
    }

    public Long getNetworkOut() {
        return this.networkOut;
    }

    public SystemMetric networkOut(Long networkOut) {
        this.setNetworkOut(networkOut);
        return this;
    }

    public void setNetworkOut(Long networkOut) {
        this.networkOut = networkOut;
    }

    public Integer getActiveConnections() {
        return this.activeConnections;
    }

    public SystemMetric activeConnections(Integer activeConnections) {
        this.setActiveConnections(activeConnections);
        return this;
    }

    public void setActiveConnections(Integer activeConnections) {
        this.activeConnections = activeConnections;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public SystemMetric timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemMetric)) {
            return false;
        }
        return getId() != null && getId().equals(((SystemMetric) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemMetric{" +
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
