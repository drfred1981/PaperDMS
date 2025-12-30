package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentStatistics.
 */
@Entity
@Table(name = "document_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentstatistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "views_total")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer viewsTotal;

    @Column(name = "downloads_total")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer downloadsTotal;

    @Column(name = "unique_viewers")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer uniqueViewers;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private Document document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getViewsTotal() {
        return this.viewsTotal;
    }

    public DocumentStatistics viewsTotal(Integer viewsTotal) {
        this.setViewsTotal(viewsTotal);
        return this;
    }

    public void setViewsTotal(Integer viewsTotal) {
        this.viewsTotal = viewsTotal;
    }

    public Integer getDownloadsTotal() {
        return this.downloadsTotal;
    }

    public DocumentStatistics downloadsTotal(Integer downloadsTotal) {
        this.setDownloadsTotal(downloadsTotal);
        return this;
    }

    public void setDownloadsTotal(Integer downloadsTotal) {
        this.downloadsTotal = downloadsTotal;
    }

    public Integer getUniqueViewers() {
        return this.uniqueViewers;
    }

    public DocumentStatistics uniqueViewers(Integer uniqueViewers) {
        this.setUniqueViewers(uniqueViewers);
        return this;
    }

    public void setUniqueViewers(Integer uniqueViewers) {
        this.uniqueViewers = uniqueViewers;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public DocumentStatistics lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentStatistics document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentStatistics)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentStatistics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentStatistics{" +
            "id=" + getId() +
            ", viewsTotal=" + getViewsTotal() +
            ", downloadsTotal=" + getDownloadsTotal() +
            ", uniqueViewers=" + getUniqueViewers() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
