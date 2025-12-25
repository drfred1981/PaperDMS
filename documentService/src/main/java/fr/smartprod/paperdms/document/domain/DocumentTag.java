package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.document.domain.enumeration.TagSource;
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
    private String assignedBy;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @NotNull
    @Column(name = "is_auto_tagged", nullable = false)
    private Boolean isAutoTagged;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private TagSource source;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "folder", "documentType" }, allowSetters = true)
    private Document document;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tagCategory" }, allowSetters = true)
    private Tag tag;

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

    public Boolean getIsAutoTagged() {
        return this.isAutoTagged;
    }

    public DocumentTag isAutoTagged(Boolean isAutoTagged) {
        this.setIsAutoTagged(isAutoTagged);
        return this;
    }

    public void setIsAutoTagged(Boolean isAutoTagged) {
        this.isAutoTagged = isAutoTagged;
    }

    public TagSource getSource() {
        return this.source;
    }

    public DocumentTag source(TagSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(TagSource source) {
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

    public Tag getTag() {
        return this.tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public DocumentTag tag(Tag tag) {
        this.setTag(tag);
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
            ", isAutoTagged='" + getIsAutoTagged() + "'" +
            ", source='" + getSource() + "'" +
            "}";
    }
}
