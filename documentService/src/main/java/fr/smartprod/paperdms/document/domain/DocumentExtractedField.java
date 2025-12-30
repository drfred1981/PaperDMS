package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentExtractedField.
 */
@Entity
@Table(name = "document_extracted_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentextractedfield")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentExtractedField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "field_key", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fieldKey;

    @Lob
    @Column(name = "field_value", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fieldValue;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "extraction_method")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ExtractionMethod extractionMethod;

    @NotNull
    @Column(name = "is_verified", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isVerified;

    @NotNull
    @Column(name = "extracted_date", nullable = false)
    private Instant extractedDate;

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

    public DocumentExtractedField id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldKey() {
        return this.fieldKey;
    }

    public DocumentExtractedField fieldKey(String fieldKey) {
        this.setFieldKey(fieldKey);
        return this;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public DocumentExtractedField fieldValue(String fieldValue) {
        this.setFieldValue(fieldValue);
        return this;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public DocumentExtractedField confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public ExtractionMethod getExtractionMethod() {
        return this.extractionMethod;
    }

    public DocumentExtractedField extractionMethod(ExtractionMethod extractionMethod) {
        this.setExtractionMethod(extractionMethod);
        return this;
    }

    public void setExtractionMethod(ExtractionMethod extractionMethod) {
        this.extractionMethod = extractionMethod;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public DocumentExtractedField isVerified(Boolean isVerified) {
        this.setIsVerified(isVerified);
        return this;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Instant getExtractedDate() {
        return this.extractedDate;
    }

    public DocumentExtractedField extractedDate(Instant extractedDate) {
        this.setExtractedDate(extractedDate);
        return this;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentExtractedField document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentExtractedField)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentExtractedField) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentExtractedField{" +
            "id=" + getId() +
            ", fieldKey='" + getFieldKey() + "'" +
            ", fieldValue='" + getFieldValue() + "'" +
            ", confidence=" + getConfidence() +
            ", extractionMethod='" + getExtractionMethod() + "'" +
            ", isVerified='" + getIsVerified() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            "}";
    }
}
