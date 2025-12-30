package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentTypeField} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTypeFieldDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String fieldKey;

    @NotNull
    @Size(max = 255)
    private String fieldLabel;

    private MetadataType dataType;

    @NotNull
    private Boolean isRequired;

    @NotNull
    private Boolean isSearchable;

    @NotNull
    private Instant createdDate;

    private DocumentTypeDTO documentType;

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

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public MetadataType getDataType() {
        return dataType;
    }

    public void setDataType(MetadataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(Boolean isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public DocumentTypeDTO getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeDTO documentType) {
        this.documentType = documentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentTypeFieldDTO)) {
            return false;
        }

        DocumentTypeFieldDTO documentTypeFieldDTO = (DocumentTypeFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentTypeFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTypeFieldDTO{" +
            "id=" + getId() +
            ", fieldKey='" + getFieldKey() + "'" +
            ", fieldLabel='" + getFieldLabel() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", isRequired='" + getIsRequired() + "'" +
            ", isSearchable='" + getIsSearchable() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", documentType=" + getDocumentType() +
            "}";
    }
}
