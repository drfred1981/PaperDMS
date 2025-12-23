package fr.smartprod.paperdms.reporting.domain;

import fr.smartprod.paperdms.reporting.domain.enumeration.MetricType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PerformanceMetric.
 */
@Entity
@Table(name = "performance_metric")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PerformanceMetric implements Serializable {

    @Serial
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false)
    private MetricType metricType;

    @NotNull
    @Column(name = "value", nullable = false)
    private Double value;

    @NotNull
    @Size(max = 20)
    @Column(name = "unit", length = 20, nullable = false)
    private String unit;

    @Size(max = 100)
    @Column(name = "service_name", length = 100)
    private String serviceName;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PerformanceMetric id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetricName() {
        return this.metricName;
    }

    public PerformanceMetric metricName(String metricName) {
        this.setMetricName(metricName);
        return this;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public MetricType getMetricType() {
        return this.metricType;
    }

    public PerformanceMetric metricType(MetricType metricType) {
        this.setMetricType(metricType);
        return this;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public Double getValue() {
        return this.value;
    }

    public PerformanceMetric value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return this.unit;
    }

    public PerformanceMetric unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public PerformanceMetric serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public PerformanceMetric timestamp(Instant timestamp) {
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
        if (!(o instanceof PerformanceMetric)) {
            return false;
        }
        return getId() != null && getId().equals(((PerformanceMetric) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerformanceMetric{" +
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
