package fr.smartprod.paperdms.reporting.service.dto;

import fr.smartprod.paperdms.reporting.domain.enumeration.ReportingExecutionStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.ReportingExecution} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingExecutionDTO implements Serializable {

    private Long id;

    @NotNull
    private ReportingExecutionStatus status;

    @NotNull
    private Instant startDate;

    private Instant endDate;

    private Integer recordsProcessed;

    @Size(max = 1000)
    private String outputS3Key;

    private Long outputSize;

    @Lob
    private String errorMessage;

    private ReportingScheduledReportDTO scheduledReport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportingExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ReportingExecutionStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public String getOutputS3Key() {
        return outputS3Key;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(Long outputSize) {
        this.outputSize = outputSize;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ReportingScheduledReportDTO getScheduledReport() {
        return scheduledReport;
    }

    public void setScheduledReport(ReportingScheduledReportDTO scheduledReport) {
        this.scheduledReport = scheduledReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingExecutionDTO)) {
            return false;
        }

        ReportingExecutionDTO reportingExecutionDTO = (ReportingExecutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportingExecutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingExecutionDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", recordsProcessed=" + getRecordsProcessed() +
            ", outputS3Key='" + getOutputS3Key() + "'" +
            ", outputSize=" + getOutputSize() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", scheduledReport=" + getScheduledReport() +
            "}";
    }
}
