package fr.smartprod.paperdms.reporting.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboard} entity. This class is used
 * in {@link fr.smartprod.paperdms.reporting.web.rest.ReportingDashboardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reporting-dashboards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingDashboardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter userId;

    private BooleanFilter isPublic;

    private IntegerFilter refreshInterval;

    private BooleanFilter isDefault;

    private InstantFilter createdDate;

    private LongFilter widgetsId;

    private Boolean distinct;

    public ReportingDashboardCriteria() {}

    public ReportingDashboardCriteria(ReportingDashboardCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.isPublic = other.optionalIsPublic().map(BooleanFilter::copy).orElse(null);
        this.refreshInterval = other.optionalRefreshInterval().map(IntegerFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.widgetsId = other.optionalWidgetsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportingDashboardCriteria copy() {
        return new ReportingDashboardCriteria(this);
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

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public Optional<BooleanFilter> optionalIsPublic() {
        return Optional.ofNullable(isPublic);
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            setIsPublic(new BooleanFilter());
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public IntegerFilter getRefreshInterval() {
        return refreshInterval;
    }

    public Optional<IntegerFilter> optionalRefreshInterval() {
        return Optional.ofNullable(refreshInterval);
    }

    public IntegerFilter refreshInterval() {
        if (refreshInterval == null) {
            setRefreshInterval(new IntegerFilter());
        }
        return refreshInterval;
    }

    public void setRefreshInterval(IntegerFilter refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
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

    public LongFilter getWidgetsId() {
        return widgetsId;
    }

    public Optional<LongFilter> optionalWidgetsId() {
        return Optional.ofNullable(widgetsId);
    }

    public LongFilter widgetsId() {
        if (widgetsId == null) {
            setWidgetsId(new LongFilter());
        }
        return widgetsId;
    }

    public void setWidgetsId(LongFilter widgetsId) {
        this.widgetsId = widgetsId;
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
        final ReportingDashboardCriteria that = (ReportingDashboardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(refreshInterval, that.refreshInterval) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(widgetsId, that.widgetsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, isPublic, refreshInterval, isDefault, createdDate, widgetsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingDashboardCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalIsPublic().map(f -> "isPublic=" + f + ", ").orElse("") +
            optionalRefreshInterval().map(f -> "refreshInterval=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalWidgetsId().map(f -> "widgetsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
