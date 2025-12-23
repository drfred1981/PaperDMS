package fr.smartprod.paperdms.reporting.service.dto;

import fr.smartprod.paperdms.reporting.domain.enumeration.ReportExecutionStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.ReportExecution} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportExecutionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long scheduledReportId;

    @NotNull
    private ReportExecutionStatus status;

    @NotNull
    private Instant startDate;

    private Instant endDate;

    private Integer recordsProcessed;

    @Size(max = 1000)
    private String outputS3Key;

    private Long outputSize;

    @Lob
    private String errorMessage;

    @NotNull
    private ScheduledReportDTO scheduledReport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScheduledReportId() {
        return scheduledReportId;
    }

    public void setScheduledReportId(Long scheduledReportId) {
        this.scheduledReportId = scheduledReportId;
    }

    public ReportExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ReportExecutionStatus status) {
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

    public ScheduledReportDTO getScheduledReport() {
        return scheduledReport;
    }

    public void setScheduledReport(ScheduledReportDTO scheduledReport) {
        this.scheduledReport = scheduledReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportExecutionDTO)) {
            return false;
        }

        ReportExecutionDTO reportExecutionDTO = (ReportExecutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportExecutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportExecutionDTO{" +
            "id=" + getId() +
            ", scheduledReportId=" + getScheduledReportId() +
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
