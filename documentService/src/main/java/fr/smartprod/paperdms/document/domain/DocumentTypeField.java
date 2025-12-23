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
 * A DocumentTypeField.
 */
@Entity
@Table(name = "document_type_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTypeField implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "field_key", length = 100, nullable = false)
    private String fieldKey;

    @NotNull
    @Size(max = 255)
    @Column(name = "field_label", length = 255, nullable = false)
    private String fieldLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private MetadataType dataType;

    @NotNull
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @NotNull
    @Column(name = "is_searchable", nullable = false)
    private Boolean isSearchable;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fields" }, allowSetters = true)
    private DocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentTypeField id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldKey() {
        return this.fieldKey;
    }

    public DocumentTypeField fieldKey(String fieldKey) {
        this.setFieldKey(fieldKey);
        return this;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldLabel() {
        return this.fieldLabel;
    }

    public DocumentTypeField fieldLabel(String fieldLabel) {
        this.setFieldLabel(fieldLabel);
        return this;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public MetadataType getDataType() {
        return this.dataType;
    }

    public DocumentTypeField dataType(MetadataType dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(MetadataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getIsRequired() {
        return this.isRequired;
    }

    public DocumentTypeField isRequired(Boolean isRequired) {
        this.setIsRequired(isRequired);
        return this;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsSearchable() {
        return this.isSearchable;
    }

    public DocumentTypeField isSearchable(Boolean isSearchable) {
        this.setIsSearchable(isSearchable);
        return this;
    }

    public void setIsSearchable(Boolean isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public DocumentTypeField createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public DocumentTypeField documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentTypeField)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentTypeField) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTypeField{" +
            "id=" + getId() +
            ", fieldKey='" + getFieldKey() + "'" +
            ", fieldLabel='" + getFieldLabel() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", isRequired='" + getIsRequired() + "'" +
            ", isSearchable='" + getIsSearchable() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
