package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.MetaTagSource;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentTag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTagDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant assignedDate;

    @NotNull
    @Size(max = 50)
    private String assignedBy;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @NotNull
    private Boolean isAutoMetaTagged;

    private MetaTagSource source;

    private DocumentDTO document;

    private MetaTagDTO metaTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Instant assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Boolean getIsAutoMetaTagged() {
        return isAutoMetaTagged;
    }

    public void setIsAutoMetaTagged(Boolean isAutoMetaTagged) {
        this.isAutoMetaTagged = isAutoMetaTagged;
    }

    public MetaTagSource getSource() {
        return source;
    }

    public void setSource(MetaTagSource source) {
        this.source = source;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public MetaTagDTO getMetaTag() {
        return metaTag;
    }

    public void setMetaTag(MetaTagDTO metaTag) {
        this.metaTag = metaTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentTagDTO)) {
            return false;
        }

        DocumentTagDTO documentTagDTO = (DocumentTagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentTagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTagDTO{" +
            "id=" + getId() +
            ", assignedDate='" + getAssignedDate() + "'" +
            ", assignedBy='" + getAssignedBy() + "'" +
            ", confidence=" + getConfidence() +
            ", isAutoMetaTagged='" + getIsAutoMetaTagged() + "'" +
            ", source='" + getSource() + "'" +
            ", document=" + getDocument() +
            ", metaTag=" + getMetaTag() +
            "}";
    }
}
