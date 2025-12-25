package fr.smartprod.paperdms.business.domain;

import fr.smartprod.paperdms.business.domain.enumeration.ManualStatus;
import fr.smartprod.paperdms.business.domain.enumeration.ManualType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Manual.
 */
@Entity
@Table(name = "manual")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "manual")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Manual implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Size(max = 500)
    @Column(name = "title", length = 500, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "manual_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ManualType manualType;

    @NotNull
    @Size(max = 50)
    @Column(name = "version", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String version;

    @NotNull
    @Size(max = 10)
    @Column(name = "language", length = 10, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String language;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "page_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pageCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ManualStatus status;

    @NotNull
    @Column(name = "is_public", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPublic;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Manual id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public Manual documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return this.title;
    }

    public Manual title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ManualType getManualType() {
        return this.manualType;
    }

    public Manual manualType(ManualType manualType) {
        this.setManualType(manualType);
        return this;
    }

    public void setManualType(ManualType manualType) {
        this.manualType = manualType;
    }

    public String getVersion() {
        return this.version;
    }

    public Manual version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return this.language;
    }

    public Manual language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getPublicationDate() {
        return this.publicationDate;
    }

    public Manual publicationDate(LocalDate publicationDate) {
        this.setPublicationDate(publicationDate);
        return this;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public Manual pageCount(Integer pageCount) {
        this.setPageCount(pageCount);
        return this;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public ManualStatus getStatus() {
        return this.status;
    }

    public Manual status(ManualStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ManualStatus status) {
        this.status = status;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public Manual isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Manual createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manual)) {
            return false;
        }
        return getId() != null && getId().equals(((Manual) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manual{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", title='" + getTitle() + "'" +
            ", manualType='" + getManualType() + "'" +
            ", version='" + getVersion() + "'" +
            ", language='" + getLanguage() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", pageCount=" + getPageCount() +
            ", status='" + getStatus() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
