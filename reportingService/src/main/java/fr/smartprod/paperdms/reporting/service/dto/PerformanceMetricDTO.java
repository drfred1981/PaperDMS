package fr.smartprod.paperdms.reporting.service.dto;

import fr.smartprod.paperdms.reporting.domain.enumeration.MetricType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.PerformanceMetric} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PerformanceMetricDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String metricName;

    @NotNull
    private MetricType metricType;

    @NotNull
    private Double value;

    @NotNull
    @Size(max = 20)
    private String unit;

    @Size(max = 100)
    private String serviceName;

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

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
        if (!(o instanceof PerformanceMetricDTO)) {
            return false;
        }

        PerformanceMetricDTO performanceMetricDTO = (PerformanceMetricDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, performanceMetricDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerformanceMetricDTO{" +
            "id=" + getId() +
            ", metricName='" + getMetricName() + "'" +
            ", metricType='" + getMetricType() + "'" +
            ", value=" + getValue() +
            ", unit='" + getUnit() + "'" +
            ", serviceName='" + getServiceName() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
