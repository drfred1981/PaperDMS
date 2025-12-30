package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.AlertFrequency;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.MetaSavedSearch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaSavedSearchDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String query;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private Boolean isAlert;

    private AlertFrequency alertFrequency;

    @NotNull
    @Size(max = 50)
    private String userId;

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsAlert() {
        return isAlert;
    }

    public void setIsAlert(Boolean isAlert) {
        this.isAlert = isAlert;
    }

    public AlertFrequency getAlertFrequency() {
        return alertFrequency;
    }

    public void setAlertFrequency(AlertFrequency alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        if (!(o instanceof MetaSavedSearchDTO)) {
            return false;
        }

        MetaSavedSearchDTO metaSavedSearchDTO = (MetaSavedSearchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, metaSavedSearchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaSavedSearchDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", query='" + getQuery() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", isAlert='" + getIsAlert() + "'" +
            ", alertFrequency='" + getAlertFrequency() + "'" +
            ", userId='" + getUserId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
