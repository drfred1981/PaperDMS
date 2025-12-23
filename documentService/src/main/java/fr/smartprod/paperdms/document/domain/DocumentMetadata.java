package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentMetadata.
 */
@Entity
@Table(name = "document_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "key", length = 100, nullable = false)
    private String key;

    @Lob
    @Column(name = "value", nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private MetadataType dataType;

    @NotNull
    @Column(name = "is_searchable", nullable = false)
    private Boolean isSearchable;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "folder", "documentType" }, allowSetters = true)
    private Document document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public DocumentMetadata key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public DocumentMetadata value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MetadataType getDataType() {
        return this.dataType;
    }

    public DocumentMetadata dataType(MetadataType dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(MetadataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getIsSearchable() {
        return this.isSearchable;
    }

    public DocumentMetadata isSearchable(Boolean isSearchable) {
        this.setIsSearchable(isSearchable);
        return this;
    }

    public void setIsSearchable(Boolean isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public DocumentMetadata createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentMetadata document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentMetadata)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentMetadata) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentMetadata{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", isSearchable='" + getIsSearchable() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
