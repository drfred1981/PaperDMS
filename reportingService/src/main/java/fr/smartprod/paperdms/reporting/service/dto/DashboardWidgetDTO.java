package fr.smartprod.paperdms.reporting.service.dto;

import fr.smartprod.paperdms.reporting.domain.enumeration.WidgetType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.DashboardWidget} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DashboardWidgetDTO implements Serializable {

    private Long id;

    @NotNull
    private Long dashboardId;

    @NotNull
    private WidgetType widgetType;

    @NotNull
    @Size(max = 255)
    private String title;

    @Lob
    private String configuration;

    @Size(max = 255)
    private String dataSource;

    @NotNull
    private Integer position;

    @NotNull
    private Integer sizeX;

    @NotNull
    private Integer sizeY;

    @NotNull
    private DashboardDTO dashboard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public WidgetType getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSizeX() {
        return sizeX;
    }

    public void setSizeX(Integer sizeX) {
        this.sizeX = sizeX;
    }

    public Integer getSizeY() {
        return sizeY;
    }

    public void setSizeY(Integer sizeY) {
        this.sizeY = sizeY;
    }

    public DashboardDTO getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardDTO dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DashboardWidgetDTO)) {
            return false;
        }

        DashboardWidgetDTO dashboardWidgetDTO = (DashboardWidgetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dashboardWidgetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DashboardWidgetDTO{" +
            "id=" + getId() +
            ", dashboardId=" + getDashboardId() +
            ", widgetType='" + getWidgetType() + "'" +
            ", title='" + getTitle() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", dataSource='" + getDataSource() + "'" +
            ", position=" + getPosition() +
            ", sizeX=" + getSizeX() +
            ", sizeY=" + getSizeY() +
            ", dashboard=" + getDashboard() +
            "}";
    }
}
