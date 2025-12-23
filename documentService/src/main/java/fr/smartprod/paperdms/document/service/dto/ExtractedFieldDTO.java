package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.ExtractedField} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtractedFieldDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtractedFieldDTO)) {
            return false;
        }

        ExtractedFieldDTO extractedFieldDTO = (ExtractedFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, extractedFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtractedFieldDTO{" +
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
