package fr.smartprod.paperdms.document.domain;

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
 * A MetaMetaTagCategory.
 */
@Entity
@Table(name = "meta_meta_tag_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "metametatagcategory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaMetaTagCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Size(max = 7)
    @Column(name = "color", length = 7)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String color;

    @Column(name = "display_order")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer displayOrder;

    @NotNull
    @Column(name = "is_system", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isSystem;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "children", "metaTags", "parent" }, allowSetters = true)
    private Set<MetaMetaTagCategory> children = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "metaMetaTagCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "documentTags", "metaMetaTagCategory" }, allowSetters = true)
    private Set<MetaTag> metaTags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "metaTags", "parent" }, allowSetters = true)
    private MetaMetaTagCategory parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MetaMetaTagCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MetaMetaTagCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public MetaMetaTagCategory color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public MetaMetaTagCategory displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public MetaMetaTagCategory isSystem(Boolean isSystem) {
        this.setIsSystem(isSystem);
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MetaMetaTagCategory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MetaMetaTagCategory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<MetaMetaTagCategory> getChildren() {
        return this.children;
    }

    public void setChildren(Set<MetaMetaTagCategory> metaMetaTagCategories) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (metaMetaTagCategories != null) {
            metaMetaTagCategories.forEach(i -> i.setParent(this));
        }
        this.children = metaMetaTagCategories;
    }

    public MetaMetaTagCategory children(Set<MetaMetaTagCategory> metaMetaTagCategories) {
        this.setChildren(metaMetaTagCategories);
        return this;
    }

    public MetaMetaTagCategory addChildren(MetaMetaTagCategory metaMetaTagCategory) {
        this.children.add(metaMetaTagCategory);
        metaMetaTagCategory.setParent(this);
        return this;
    }

    public MetaMetaTagCategory removeChildren(MetaMetaTagCategory metaMetaTagCategory) {
        this.children.remove(metaMetaTagCategory);
        metaMetaTagCategory.setParent(null);
        return this;
    }

    public Set<MetaTag> getMetaTags() {
        return this.metaTags;
    }

    public void setMetaTags(Set<MetaTag> metaTags) {
        if (this.metaTags != null) {
            this.metaTags.forEach(i -> i.setMetaMetaTagCategory(null));
        }
        if (metaTags != null) {
            metaTags.forEach(i -> i.setMetaMetaTagCategory(this));
        }
        this.metaTags = metaTags;
    }

    public MetaMetaTagCategory metaTags(Set<MetaTag> metaTags) {
        this.setMetaTags(metaTags);
        return this;
    }

    public MetaMetaTagCategory addMetaTags(MetaTag metaTag) {
        this.metaTags.add(metaTag);
        metaTag.setMetaMetaTagCategory(this);
        return this;
    }

    public MetaMetaTagCategory removeMetaTags(MetaTag metaTag) {
        this.metaTags.remove(metaTag);
        metaTag.setMetaMetaTagCategory(null);
        return this;
    }

    public MetaMetaTagCategory getParent() {
        return this.parent;
    }

    public void setParent(MetaMetaTagCategory metaMetaTagCategory) {
        this.parent = metaMetaTagCategory;
    }

    public MetaMetaTagCategory parent(MetaMetaTagCategory metaMetaTagCategory) {
        this.setParent(metaMetaTagCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaMetaTagCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((MetaMetaTagCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaMetaTagCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", color='" + getColor() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isSystem='" + getIsSystem() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
