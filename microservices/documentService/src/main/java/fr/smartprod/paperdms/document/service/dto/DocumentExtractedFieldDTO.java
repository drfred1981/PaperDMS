package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentExtractedField} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentExtractedFieldDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String fieldKey;

    @Lob
    private String fieldValue;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    private ExtractionMethod extractionMethod;

    @NotNull
    private Boolean isVerified;

    @NotNull
    private Instant extractedDate;

    private DocumentDTO document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public ExtractionMethod getExtractionMethod() {
        return extractionMethod;
    }

    public void setExtractionMethod(ExtractionMethod extractionMethod) {
        this.extractionMethod = extractionMethod;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Instant getExtractedDate() {
        return extractedDate;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentExtractedFieldDTO)) {
            return false;
        }

        DocumentExtractedFieldDTO documentExtractedFieldDTO = (DocumentExtractedFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentExtractedFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentExtractedFieldDTO{" +
            "id=" + getId() +
            ", fieldKey='" + getFieldKey() + "'" +
            ", fieldValue='" + getFieldValue() + "'" +
            ", confidence=" + getConfidence() +
            ", extractionMethod='" + getExtractionMethod() + "'" +
            ", isVerified='" + getIsVerified() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            ", document=" + getDocument() +
            "}";
    }
}
