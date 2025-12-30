package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.document.domain.enumeration.MetaTagSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentTag.
 */
@Entity
@Table(name = "document_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documenttag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "assigned_date", nullable = false)
    private Instant assignedDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "assigned_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assignedBy;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @NotNull
    @Column(name = "is_auto_meta_tagged", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAutoMetaTagged;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MetaTagSource source;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "documentTags", "metaMetaTagCategory" }, allowSetters = true)
    private MetaTag metaTag;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssignedDate() {
        return this.assignedDate;
    }

    public DocumentTag assignedDate(Instant assignedDate) {
        this.setAssignedDate(assignedDate);
        return this;
    }

    public void setAssignedDate(Instant assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getAssignedBy() {
        return this.assignedBy;
    }

    public DocumentTag assignedBy(String assignedBy) {
        this.setAssignedBy(assignedBy);
        return this;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public DocumentTag confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Boolean getIsAutoMetaTagged() {
        return this.isAutoMetaTagged;
    }

    public DocumentTag isAutoMetaTagged(Boolean isAutoMetaTagged) {
        this.setIsAutoMetaTagged(isAutoMetaTagged);
        return this;
    }

    public void setIsAutoMetaTagged(Boolean isAutoMetaTagged) {
        this.isAutoMetaTagged = isAutoMetaTagged;
    }

    public MetaTagSource getSource() {
        return this.source;
    }

    public DocumentTag source(MetaTagSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(MetaTagSource source) {
        this.source = source;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentTag document(Document document) {
        this.setDocument(document);
        return this;
    }

    public MetaTag getMetaTag() {
        return this.metaTag;
    }

    public void setMetaTag(MetaTag metaTag) {
        this.metaTag = metaTag;
    }

    public DocumentTag metaTag(MetaTag metaTag) {
        this.setMetaTag(metaTag);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentTag)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentTag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTag{" +
            "id=" + getId() +
            ", assignedDate='" + getAssignedDate() + "'" +
            ", assignedBy='" + getAssignedBy() + "'" +
            ", confidence=" + getConfidence() +
            ", isAutoMetaTagged='" + getIsAutoMetaTagged() + "'" +
            ", source='" + getSource() + "'" +
            "}";
    }
}
