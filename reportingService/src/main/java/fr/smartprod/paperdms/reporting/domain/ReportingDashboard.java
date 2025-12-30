package fr.smartprod.paperdms.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportingDashboard.
 */
@Entity
@Table(name = "reporting_dashboard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingDashboard implements Serializable {

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

    @Size(max = 50)
    @Column(name = "user_id", length = 50)
    private String userId;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Lob
    @Column(name = "layout", nullable = false)
    private String layout;

    @Column(name = "refresh_interval")
    private Integer refreshInterval;

    @Column(name = "is_default")
    private Boolean isDefault;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dashboar")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dashboar" }, allowSetters = true)
    private Set<ReportingDashboardWidget> widgets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportingDashboard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ReportingDashboard name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ReportingDashboard description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return this.userId;
    }

    public ReportingDashboard userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public ReportingDashboard isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getLayout() {
        return this.layout;
    }

    public ReportingDashboard layout(String layout) {
        this.setLayout(layout);
        return this;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getRefreshInterval() {
        return this.refreshInterval;
    }

    public ReportingDashboard refreshInterval(Integer refreshInterval) {
        this.setRefreshInterval(refreshInterval);
        return this;
    }

    public void setRefreshInterval(Integer refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public ReportingDashboard isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ReportingDashboard createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ReportingDashboardWidget> getWidgets() {
        return this.widgets;
    }

    public void setWidgets(Set<ReportingDashboardWidget> reportingDashboardWidgets) {
        if (this.widgets != null) {
            this.widgets.forEach(i -> i.setDashboar(null));
        }
        if (reportingDashboardWidgets != null) {
            reportingDashboardWidgets.forEach(i -> i.setDashboar(this));
        }
        this.widgets = reportingDashboardWidgets;
    }

    public ReportingDashboard widgets(Set<ReportingDashboardWidget> reportingDashboardWidgets) {
        this.setWidgets(reportingDashboardWidgets);
        return this;
    }

    public ReportingDashboard addWidgets(ReportingDashboardWidget reportingDashboardWidget) {
        this.widgets.add(reportingDashboardWidget);
        reportingDashboardWidget.setDashboar(this);
        return this;
    }

    public ReportingDashboard removeWidgets(ReportingDashboardWidget reportingDashboardWidget) {
        this.widgets.remove(reportingDashboardWidget);
        reportingDashboardWidget.setDashboar(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingDashboard)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportingDashboard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingDashboard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", userId='" + getUserId() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", layout='" + getLayout() + "'" +
            ", refreshInterval=" + getRefreshInterval() +
            ", isDefault='" + getIsDefault() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
