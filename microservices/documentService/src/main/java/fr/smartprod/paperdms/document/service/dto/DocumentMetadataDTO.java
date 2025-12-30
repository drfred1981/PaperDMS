package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentMetadata} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentMetadataDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String key;

    @Lob
    private String value;

    private MetadataType dataType;

    @NotNull
    private Boolean isSearchable;

    @NotNull
    private Instant createdDate;

    private DocumentDTO document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MetadataType getDataType() {
        return dataType;
    }

    public void setDataType(MetadataType dataType) {
        this.dataType = dataType;
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
        if (!(o instanceof DocumentMetadataDTO)) {
            return false;
        }

        DocumentMetadataDTO documentMetadataDTO = (DocumentMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentMetadataDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", isSearchable='" + getIsSearchable() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", document=" + getDocument() +
            "}";
    }
}
