package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.BookmarkType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.Bookmark} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookmarkDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String userId;

    @NotNull
    private BookmarkType entityType;

    @NotNull
    private Long entityId;

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

    public BookmarkType getEntityType() {
        return entityType;
    }

    public void setEntityType(BookmarkType entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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
        if (!(o instanceof BookmarkDTO)) {
            return false;
        }

        BookmarkDTO bookmarkDTO = (BookmarkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookmarkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookmarkDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityId=" + getEntityId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
