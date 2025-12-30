package fr.smartprod.paperdms.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportFormat;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportingScheduledReport.
 */
@Entity
@Table(name = "reporting_scheduled_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingScheduledReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Lob
    @Column(name = "query")
    private String query;

    @NotNull
    @Size(max = 100)
    @Column(name = "schedule", length = 100, nullable = false)
    private String schedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    private ReportFormat format;

    @Lob
    @Column(name = "recipients", nullable = false)
    private String recipients;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_run")
    private Instant lastRun;

    @Column(name = "next_run")
    private Instant nextRun;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduledReport")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "scheduledReport" }, allowSetters = true)
    private Set<ReportingExecution> reportsExecutions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportingScheduledReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ReportingScheduledReport name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ReportingScheduledReport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public ReportingScheduledReport reportType(ReportType reportType) {
        this.setReportType(reportType);
        return this;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getQuery() {
        return this.query;
    }

    public ReportingScheduledReport query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSchedule() {
        return this.schedule;
    }

    public ReportingScheduledReport schedule(String schedule) {
        this.setSchedule(schedule);
        return this;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public ReportFormat getFormat() {
        return this.format;
    }

    public ReportingScheduledReport format(ReportFormat format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(ReportFormat format) {
        this.format = format;
    }

    public String getRecipients() {
        return this.recipients;
    }

    public ReportingScheduledReport recipients(String recipients) {
        this.setRecipients(recipients);
        return this;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ReportingScheduledReport isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getLastRun() {
        return this.lastRun;
    }

    public ReportingScheduledReport lastRun(Instant lastRun) {
        this.setLastRun(lastRun);
        return this;
    }

    public void setLastRun(Instant lastRun) {
        this.lastRun = lastRun;
    }

    public Instant getNextRun() {
        return this.nextRun;
    }

    public ReportingScheduledReport nextRun(Instant nextRun) {
        this.setNextRun(nextRun);
        return this;
    }

    public void setNextRun(Instant nextRun) {
        this.nextRun = nextRun;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ReportingScheduledReport createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ReportingScheduledReport createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ReportingExecution> getReportsExecutions() {
        return this.reportsExecutions;
    }

    public void setReportsExecutions(Set<ReportingExecution> reportingExecutions) {
        if (this.reportsExecutions != null) {
            this.reportsExecutions.forEach(i -> i.setScheduledReport(null));
        }
        if (reportingExecutions != null) {
            reportingExecutions.forEach(i -> i.setScheduledReport(this));
        }
        this.reportsExecutions = reportingExecutions;
    }

    public ReportingScheduledReport reportsExecutions(Set<ReportingExecution> reportingExecutions) {
        this.setReportsExecutions(reportingExecutions);
        return this;
    }

    public ReportingScheduledReport addReportsExecutions(ReportingExecution reportingExecution) {
        this.reportsExecutions.add(reportingExecution);
        reportingExecution.setScheduledReport(this);
        return this;
    }

    public ReportingScheduledReport removeReportsExecutions(ReportingExecution reportingExecution) {
        this.reportsExecutions.remove(reportingExecution);
        reportingExecution.setScheduledReport(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingScheduledReport)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportingScheduledReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingScheduledReport{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", reportType='" + getReportType() + "'" +
            ", query='" + getQuery() + "'" +
            ", schedule='" + getSchedule() + "'" +
            ", format='" + getFormat() + "'" +
            ", recipients='" + getRecipients() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastRun='" + getLastRun() + "'" +
            ", nextRun='" + getNextRun() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
