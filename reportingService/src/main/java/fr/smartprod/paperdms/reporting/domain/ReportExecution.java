package fr.smartprod.paperdms.reporting.domain;

import fr.smartprod.paperdms.reporting.domain.enumeration.ReportExecutionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportExecution.
 */
@Entity
@Table(name = "report_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "scheduled_report_id", nullable = false)
    private Long scheduledReportId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportExecutionStatus status;

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

    @ManyToOne(optional = false)
    @NotNull
    private ScheduledReport scheduledReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportExecution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScheduledReportId() {
        return this.scheduledReportId;
    }

    public ReportExecution scheduledReportId(Long scheduledReportId) {
        this.setScheduledReportId(scheduledReportId);
        return this;
    }

    public void setScheduledReportId(Long scheduledReportId) {
        this.scheduledReportId = scheduledReportId;
    }

    public ReportExecutionStatus getStatus() {
        return this.status;
    }

    public ReportExecution status(ReportExecutionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReportExecutionStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public ReportExecution startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public ReportExecution endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getRecordsProcessed() {
        return this.recordsProcessed;
    }

    public ReportExecution recordsProcessed(Integer recordsProcessed) {
        this.setRecordsProcessed(recordsProcessed);
        return this;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public ReportExecution outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputSize() {
        return this.outputSize;
    }

    public ReportExecution outputSize(Long outputSize) {
        this.setOutputSize(outputSize);
        return this;
    }

    public void setOutputSize(Long outputSize) {
        this.outputSize = outputSize;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ReportExecution errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ScheduledReport getScheduledReport() {
        return this.scheduledReport;
    }

    public void setScheduledReport(ScheduledReport scheduledReport) {
        this.scheduledReport = scheduledReport;
    }

    public ReportExecution scheduledReport(ScheduledReport scheduledReport) {
        this.setScheduledReport(scheduledReport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportExecution)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportExecution) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportExecution{" +
            "id=" + getId() +
            ", scheduledReportId=" + getScheduledReportId() +
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
