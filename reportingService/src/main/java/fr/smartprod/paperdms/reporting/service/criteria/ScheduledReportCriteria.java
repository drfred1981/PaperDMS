package fr.smartprod.paperdms.reporting.service.criteria;

import fr.smartprod.paperdms.reporting.domain.enumeration.ReportFormat;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.reporting.domain.ScheduledReport} entity. This class is used
 * in {@link fr.smartprod.paperdms.reporting.web.rest.ScheduledReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scheduled-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduledReportCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReportType
     */
    public static class ReportTypeFilter extends Filter<ReportType> {

        public ReportTypeFilter() {}

        public ReportTypeFilter(ReportTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReportTypeFilter copy() {
            return new ReportTypeFilter(this);
        }
    }

    /**
     * Class for filtering ReportFormat
     */
    public static class ReportFormatFilter extends Filter<ReportFormat> {

        public ReportFormatFilter() {}

        public ReportFormatFilter(ReportFormatFilter filter) {
            super(filter);
        }

        @Override
        public ReportFormatFilter copy() {
            return new ReportFormatFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ReportTypeFilter reportType;

    private StringFilter schedule;

    private ReportFormatFilter format;

    private BooleanFilter isActive;

    private InstantFilter lastRun;

    private InstantFilter nextRun;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public ScheduledReportCriteria() {}

    public ScheduledReportCriteria(ScheduledReportCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.reportType = other.optionalReportType().map(ReportTypeFilter::copy).orElse(null);
        this.schedule = other.optionalSchedule().map(StringFilter::copy).orElse(null);
        this.format = other.optionalFormat().map(ReportFormatFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.lastRun = other.optionalLastRun().map(InstantFilter::copy).orElse(null);
        this.nextRun = other.optionalNextRun().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ScheduledReportCriteria copy() {
        return new ScheduledReportCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ReportTypeFilter getReportType() {
        return reportType;
    }

    public Optional<ReportTypeFilter> optionalReportType() {
        return Optional.ofNullable(reportType);
    }

    public ReportTypeFilter reportType() {
        if (reportType == null) {
            setReportType(new ReportTypeFilter());
        }
        return reportType;
    }

    public void setReportType(ReportTypeFilter reportType) {
        this.reportType = reportType;
    }

    public StringFilter getSchedule() {
        return schedule;
    }

    public Optional<StringFilter> optionalSchedule() {
        return Optional.ofNullable(schedule);
    }

    public StringFilter schedule() {
        if (schedule == null) {
            setSchedule(new StringFilter());
        }
        return schedule;
    }

    public void setSchedule(StringFilter schedule) {
        this.schedule = schedule;
    }

    public ReportFormatFilter getFormat() {
        return format;
    }

    public Optional<ReportFormatFilter> optionalFormat() {
        return Optional.ofNullable(format);
    }

    public ReportFormatFilter format() {
        if (format == null) {
            setFormat(new ReportFormatFilter());
        }
        return format;
    }

    public void setFormat(ReportFormatFilter format) {
        this.format = format;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public InstantFilter getLastRun() {
        return lastRun;
    }

    public Optional<InstantFilter> optionalLastRun() {
        return Optional.ofNullable(lastRun);
    }

    public InstantFilter lastRun() {
        if (lastRun == null) {
            setLastRun(new InstantFilter());
        }
        return lastRun;
    }

    public void setLastRun(InstantFilter lastRun) {
        this.lastRun = lastRun;
    }

    public InstantFilter getNextRun() {
        return nextRun;
    }

    public Optional<InstantFilter> optionalNextRun() {
        return Optional.ofNullable(nextRun);
    }

    public InstantFilter nextRun() {
        if (nextRun == null) {
            setNextRun(new InstantFilter());
        }
        return nextRun;
    }

    public void setNextRun(InstantFilter nextRun) {
        this.nextRun = nextRun;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ScheduledReportCriteria that = (ScheduledReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(reportType, that.reportType) &&
            Objects.equals(schedule, that.schedule) &&
            Objects.equals(format, that.format) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(lastRun, that.lastRun) &&
            Objects.equals(nextRun, that.nextRun) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reportType, schedule, format, isActive, lastRun, nextRun, createdBy, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduledReportCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalReportType().map(f -> "reportType=" + f + ", ").orElse("") +
            optionalSchedule().map(f -> "schedule=" + f + ", ").orElse("") +
            optionalFormat().map(f -> "format=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalLastRun().map(f -> "lastRun=" + f + ", ").orElse("") +
            optionalNextRun().map(f -> "nextRun=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
