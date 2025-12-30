package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.MetaBookmarkType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.MetaBookmark} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaBookmarkDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String userId;

    @NotNull
    private MetaBookmarkType entityType;

    @NotNull
    private String entityName;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MetaBookmarkType getEntityType() {
        return entityType;
    }

    public void setEntityType(MetaBookmarkType entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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
        if (!(o instanceof MetaBookmarkDTO)) {
            return false;
        }

        MetaBookmarkDTO metaBookmarkDTO = (MetaBookmarkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, metaBookmarkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaBookmarkDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
