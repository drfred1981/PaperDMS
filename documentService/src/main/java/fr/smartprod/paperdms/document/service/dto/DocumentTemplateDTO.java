package fr.smartprod.paperdms.document.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 64)
    private String templateSha256;

    @NotNull
    @Size(max = 1000)
    private String templateS3Key;

    @NotNull
    private Boolean isActive;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    @NotNull
    private DocumentTypeDTO documentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateSha256() {
        return templateSha256;
    }

    public void setTemplateSha256(String templateSha256) {
        this.templateSha256 = templateSha256;
    }

    public String getTemplateS3Key() {
        return templateS3Key;
    }

    public void setTemplateS3Key(String templateS3Key) {
        this.templateS3Key = templateS3Key;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        if (!(o instanceof DocumentTemplateDTO)) {
            return false;
        }

        DocumentTemplateDTO documentTemplateDTO = (DocumentTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTemplateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", templateSha256='" + getTemplateSha256() + "'" +
            ", templateS3Key='" + getTemplateS3Key() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", documentType=" + getDocumentType() +
            "}";
    }
}
