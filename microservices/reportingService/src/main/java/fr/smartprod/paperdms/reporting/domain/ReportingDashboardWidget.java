package fr.smartprod.paperdms.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.reporting.domain.enumeration.WidgetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportingDashboardWidget.
 */
@Entity
@Table(name = "reporting_dashboard_widget")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingDashboardWidget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "widget_type", nullable = false)
    private WidgetType widgetType;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "configuration", nullable = false)
    private String configuration;

    @Size(max = 255)
    @Column(name = "data_source", length = 255)
    private String dataSource;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer position;

    @NotNull
    @Column(name = "size_x", nullable = false)
    private Integer sizeX;

    @NotNull
    @Column(name = "size_y", nullable = false)
    private Integer sizeY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "widgets" }, allowSetters = true)
    private ReportingDashboard dashboar;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportingDashboardWidget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WidgetType getWidgetType() {
        return this.widgetType;
    }

    public ReportingDashboardWidget widgetType(WidgetType widgetType) {
        this.setWidgetType(widgetType);
        return this;
    }

    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }

    public String getTitle() {
        return this.title;
    }

    public ReportingDashboardWidget title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public ReportingDashboardWidget configuration(String configuration) {
        this.setConfiguration(configuration);
        return this;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public ReportingDashboardWidget dataSource(String dataSource) {
        this.setDataSource(dataSource);
        return this;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getPosition() {
        return this.position;
    }

    public ReportingDashboardWidget position(Integer position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSizeX() {
        return this.sizeX;
    }

    public ReportingDashboardWidget sizeX(Integer sizeX) {
        this.setSizeX(sizeX);
        return this;
    }

    public void setSizeX(Integer sizeX) {
        this.sizeX = sizeX;
    }

    public Integer getSizeY() {
        return this.sizeY;
    }

    public ReportingDashboardWidget sizeY(Integer sizeY) {
        this.setSizeY(sizeY);
        return this;
    }

    public void setSizeY(Integer sizeY) {
        this.sizeY = sizeY;
    }

    public ReportingDashboard getDashboar() {
        return this.dashboar;
    }

    public void setDashboar(ReportingDashboard reportingDashboard) {
        this.dashboar = reportingDashboard;
    }

    public ReportingDashboardWidget dashboar(ReportingDashboard reportingDashboard) {
        this.setDashboar(reportingDashboard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingDashboardWidget)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportingDashboardWidget) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingDashboardWidget{" +
            "id=" + getId() +
            ", widgetType='" + getWidgetType() + "'" +
            ", title='" + getTitle() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", dataSource='" + getDataSource() + "'" +
            ", position=" + getPosition() +
            ", sizeX=" + getSizeX() +
            ", sizeY=" + getSizeY() +
            "}";
    }
}
