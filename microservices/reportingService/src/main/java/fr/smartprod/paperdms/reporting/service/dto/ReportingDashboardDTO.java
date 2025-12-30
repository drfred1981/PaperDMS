package fr.smartprod.paperdms.reporting.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingDashboardDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @Size(max = 50)
    private String userId;

    @NotNull
    private Boolean isPublic;

    @Lob
    private String layout;

    private Integer refreshInterval;

    private Boolean isDefault;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(Integer refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingDashboardDTO)) {
            return false;
        }

        ReportingDashboardDTO reportingDashboardDTO = (ReportingDashboardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportingDashboardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingDashboardDTO{" +
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
