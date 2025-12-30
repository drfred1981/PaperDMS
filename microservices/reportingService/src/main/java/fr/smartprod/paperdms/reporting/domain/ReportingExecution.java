package fr.smartprod.paperdms.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportingExecutionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportingExecution.
 */
@Entity
@Table(name = "reporting_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportingExecutionStatus status;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "records_processed")
    private Integer recordsProcessed;

    @Size(max = 1000)
    @Column(name = "output_s_3_key", length = 1000)
    private String outputS3Key;

    @Column(name = "output_size")
    private Long outputSize;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reportsExecutions" }, allowSetters = true)
    private ReportingScheduledReport scheduledReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportingExecution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportingExecutionStatus getStatus() {
        return this.status;
    }

    public ReportingExecution status(ReportingExecutionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReportingExecutionStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public ReportingExecution startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public ReportingExecution endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getRecordsProcessed() {
        return this.recordsProcessed;
    }

    public ReportingExecution recordsProcessed(Integer recordsProcessed) {
        this.setRecordsProcessed(recordsProcessed);
        return this;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public ReportingExecution outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputSize() {
        return this.outputSize;
    }

    public ReportingExecution outputSize(Long outputSize) {
        this.setOutputSize(outputSize);
        return this;
    }

    public void setOutputSize(Long outputSize) {
        this.outputSize = outputSize;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ReportingExecution errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ReportingScheduledReport getScheduledReport() {
        return this.scheduledReport;
    }

    public void setScheduledReport(ReportingScheduledReport reportingScheduledReport) {
        this.scheduledReport = reportingScheduledReport;
    }

    public ReportingExecution scheduledReport(ReportingScheduledReport reportingScheduledReport) {
        this.setScheduledReport(reportingScheduledReport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingExecution)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportingExecution) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingExecution{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", recordsProcessed=" + getRecordsProcessed() +
            ", outputS3Key='" + getOutputS3Key() + "'" +
            ", outputSize=" + getOutputSize() +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
