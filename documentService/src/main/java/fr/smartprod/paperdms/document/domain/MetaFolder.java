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
 * A MetaFolder.
 */
@Entity
@Table(name = "meta_folder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "metafolder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaFolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Size(max = 1000)
    @Column(name = "path", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String path;

    @NotNull
    @Column(name = "is_shared", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isShared;

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
    @JsonIgnoreProperties(value = { "children", "documents", "parent" }, allowSetters = true)
    private Set<MetaFolder> children = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "documentVersions",
            "documentTags",
            "statuses",
            "documentExtractedFields",
            "permissions",
            "audits",
            "comments",
            "metadatas",
            "statistics",
            "documentType",
            "folder",
        },
        allowSetters = true
    )
    private Set<Document> documents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "documents", "parent" }, allowSetters = true)
    private MetaFolder parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MetaFolder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MetaFolder name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MetaFolder description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return this.path;
    }

    public MetaFolder path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIsShared() {
        return this.isShared;
    }

    public MetaFolder isShared(Boolean isShared) {
        this.setIsShared(isShared);
        return this;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MetaFolder createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MetaFolder createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<MetaFolder> getChildren() {
        return this.children;
    }

    public void setChildren(Set<MetaFolder> metaFolders) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (metaFolders != null) {
            metaFolders.forEach(i -> i.setParent(this));
        }
        this.children = metaFolders;
    }

    public MetaFolder children(Set<MetaFolder> metaFolders) {
        this.setChildren(metaFolders);
        return this;
    }

    public MetaFolder addChildren(MetaFolder metaFolder) {
        this.children.add(metaFolder);
        metaFolder.setParent(this);
        return this;
    }

    public MetaFolder removeChildren(MetaFolder metaFolder) {
        this.children.remove(metaFolder);
        metaFolder.setParent(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setFolder(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setFolder(this));
        }
        this.documents = documents;
    }

    public MetaFolder documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public MetaFolder addDocuments(Document document) {
        this.documents.add(document);
        document.setFolder(this);
        return this;
    }

    public MetaFolder removeDocuments(Document document) {
        this.documents.remove(document);
        document.setFolder(null);
        return this;
    }

    public MetaFolder getParent() {
        return this.parent;
    }

    public void setParent(MetaFolder metaFolder) {
        this.parent = metaFolder;
    }

    public MetaFolder parent(MetaFolder metaFolder) {
        this.setParent(metaFolder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaFolder)) {
            return false;
        }
        return getId() != null && getId().equals(((MetaFolder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaFolder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", path='" + getPath() + "'" +
            ", isShared='" + getIsShared() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
