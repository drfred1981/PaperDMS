package fr.smartprod.paperdms.reporting.service.criteria;

import fr.smartprod.paperdms.reporting.domain.enumeration.WidgetType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget} entity. This class is used
 * in {@link fr.smartprod.paperdms.reporting.web.rest.ReportingDashboardWidgetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reporting-dashboard-widgets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingDashboardWidgetCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WidgetType
     */
    public static class WidgetTypeFilter extends Filter<WidgetType> {

        public WidgetTypeFilter() {}

        public WidgetTypeFilter(WidgetTypeFilter filter) {
            super(filter);
        }

        @Override
        public WidgetTypeFilter copy() {
            return new WidgetTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private WidgetTypeFilter widgetType;

    private StringFilter title;

    private StringFilter dataSource;

    private IntegerFilter position;

    private IntegerFilter sizeX;

    private IntegerFilter sizeY;

    private LongFilter dashboarId;

    private Boolean distinct;

    public ReportingDashboardWidgetCriteria() {}

    public ReportingDashboardWidgetCriteria(ReportingDashboardWidgetCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.widgetType = other.optionalWidgetType().map(WidgetTypeFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.dataSource = other.optionalDataSource().map(StringFilter::copy).orElse(null);
        this.position = other.optionalPosition().map(IntegerFilter::copy).orElse(null);
        this.sizeX = other.optionalSizeX().map(IntegerFilter::copy).orElse(null);
        this.sizeY = other.optionalSizeY().map(IntegerFilter::copy).orElse(null);
        this.dashboarId = other.optionalDashboarId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportingDashboardWidgetCriteria copy() {
        return new ReportingDashboardWidgetCriteria(this);
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

    public WidgetTypeFilter getWidgetType() {
        return widgetType;
    }

    public Optional<WidgetTypeFilter> optionalWidgetType() {
        return Optional.ofNullable(widgetType);
    }

    public WidgetTypeFilter widgetType() {
        if (widgetType == null) {
            setWidgetType(new WidgetTypeFilter());
        }
        return widgetType;
    }

    public void setWidgetType(WidgetTypeFilter widgetType) {
        this.widgetType = widgetType;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDataSource() {
        return dataSource;
    }

    public Optional<StringFilter> optionalDataSource() {
        return Optional.ofNullable(dataSource);
    }

    public StringFilter dataSource() {
        if (dataSource == null) {
            setDataSource(new StringFilter());
        }
        return dataSource;
    }

    public void setDataSource(StringFilter dataSource) {
        this.dataSource = dataSource;
    }

    public IntegerFilter getPosition() {
        return position;
    }

    public Optional<IntegerFilter> optionalPosition() {
        return Optional.ofNullable(position);
    }

    public IntegerFilter position() {
        if (position == null) {
            setPosition(new IntegerFilter());
        }
        return position;
    }

    public void setPosition(IntegerFilter position) {
        this.position = position;
    }

    public IntegerFilter getSizeX() {
        return sizeX;
    }

    public Optional<IntegerFilter> optionalSizeX() {
        return Optional.ofNullable(sizeX);
    }

    public IntegerFilter sizeX() {
        if (sizeX == null) {
            setSizeX(new IntegerFilter());
        }
        return sizeX;
    }

    public void setSizeX(IntegerFilter sizeX) {
        this.sizeX = sizeX;
    }

    public IntegerFilter getSizeY() {
        return sizeY;
    }

    public Optional<IntegerFilter> optionalSizeY() {
        return Optional.ofNullable(sizeY);
    }

    public IntegerFilter sizeY() {
        if (sizeY == null) {
            setSizeY(new IntegerFilter());
        }
        return sizeY;
    }

    public void setSizeY(IntegerFilter sizeY) {
        this.sizeY = sizeY;
    }

    public LongFilter getDashboarId() {
        return dashboarId;
    }

    public Optional<LongFilter> optionalDashboarId() {
        return Optional.ofNullable(dashboarId);
    }

    public LongFilter dashboarId() {
        if (dashboarId == null) {
            setDashboarId(new LongFilter());
        }
        return dashboarId;
    }

    public void setDashboarId(LongFilter dashboarId) {
        this.dashboarId = dashboarId;
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
        final ReportingDashboardWidgetCriteria that = (ReportingDashboardWidgetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(widgetType, that.widgetType) &&
            Objects.equals(title, that.title) &&
            Objects.equals(dataSource, that.dataSource) &&
            Objects.equals(position, that.position) &&
            Objects.equals(sizeX, that.sizeX) &&
            Objects.equals(sizeY, that.sizeY) &&
            Objects.equals(dashboarId, that.dashboarId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, widgetType, title, dataSource, position, sizeX, sizeY, dashboarId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingDashboardWidgetCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalWidgetType().map(f -> "widgetType=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDataSource().map(f -> "dataSource=" + f + ", ").orElse("") +
            optionalPosition().map(f -> "position=" + f + ", ").orElse("") +
            optionalSizeX().map(f -> "sizeX=" + f + ", ").orElse("") +
            optionalSizeY().map(f -> "sizeY=" + f + ", ").orElse("") +
            optionalDashboarId().map(f -> "dashboarId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
