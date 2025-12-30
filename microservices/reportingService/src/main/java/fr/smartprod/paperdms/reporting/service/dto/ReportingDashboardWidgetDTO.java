package fr.smartprod.paperdms.reporting.service.dto;

import fr.smartprod.paperdms.reporting.domain.enumeration.WidgetType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingDashboardWidgetDTO implements Serializable {

    private Long id;

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

    private ReportingDashboardDTO dashboar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ReportingDashboardDTO getDashboar() {
        return dashboar;
    }

    public void setDashboar(ReportingDashboardDTO dashboar) {
        this.dashboar = dashboar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingDashboardWidgetDTO)) {
            return false;
        }

        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = (ReportingDashboardWidgetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportingDashboardWidgetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingDashboardWidgetDTO{" +
            "id=" + getId() +
            ", widgetType='" + getWidgetType() + "'" +
            ", title='" + getTitle() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", dataSource='" + getDataSource() + "'" +
            ", position=" + getPosition() +
            ", sizeX=" + getSizeX() +
            ", sizeY=" + getSizeY() +
            ", dashboar=" + getDashboar() +
            "}";
    }
}
