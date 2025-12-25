package fr.smartprod.paperdms.document.domain;

import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExtractedField.
 */
@Entity
@Table(name = "extracted_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "extractedfield")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtractedField implements Serializable {

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExtractedField id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public ExtractedField documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getFieldKey() {
        return this.fieldKey;
    }

    public ExtractedField fieldKey(String fieldKey) {
        this.setFieldKey(fieldKey);
        return this;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public ExtractedField fieldValue(String fieldValue) {
        this.setFieldValue(fieldValue);
        return this;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public ExtractedField confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public ExtractionMethod getExtractionMethod() {
        return this.extractionMethod;
    }

    public ExtractedField extractionMethod(ExtractionMethod extractionMethod) {
        this.setExtractionMethod(extractionMethod);
        return this;
    }

    public void setExtractionMethod(ExtractionMethod extractionMethod) {
        this.extractionMethod = extractionMethod;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public ExtractedField isVerified(Boolean isVerified) {
        this.setIsVerified(isVerified);
        return this;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Instant getExtractedDate() {
        return this.extractedDate;
    }

    public ExtractedField extractedDate(Instant extractedDate) {
        this.setExtractedDate(extractedDate);
        return this;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtractedField)) {
            return false;
        }
        return getId() != null && getId().equals(((ExtractedField) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtractedField{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", fieldKey='" + getFieldKey() + "'" +
            ", fieldValue='" + getFieldValue() + "'" +
            ", confidence=" + getConfidence() +
            ", extractionMethod='" + getExtractionMethod() + "'" +
            ", isVerified='" + getIsVerified() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            "}";
    }
}
