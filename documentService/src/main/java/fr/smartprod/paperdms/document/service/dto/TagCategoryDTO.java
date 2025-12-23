package fr.smartprod.paperdms.document.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.TagCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 7)
    private String color;

    private Integer displayOrder;

    @NotNull
    private Boolean isSystem;

    @NotNull
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    private TagCategoryDTO parent;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public TagCategoryDTO getParent() {
        return parent;
    }

    public void setParent(TagCategoryDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagCategoryDTO)) {
            return false;
        }

        TagCategoryDTO tagCategoryDTO = (TagCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", color='" + getColor() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isSystem='" + getIsSystem() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
