package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.TagSource;
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
    private Boolean isAutoTagged;

    private TagSource source;

    @NotNull
    private DocumentDTO document;

    @NotNull
    private TagDTO tag;

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

    public Boolean getIsAutoTagged() {
        return isAutoTagged;
    }

    public void setIsAutoTagged(Boolean isAutoTagged) {
        this.isAutoTagged = isAutoTagged;
    }

    public TagSource getSource() {
        return source;
    }

    public void setSource(TagSource source) {
        this.source = source;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public TagDTO getTag() {
        return tag;
    }

    public void setTag(TagDTO tag) {
        this.tag = tag;
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
            ", isAutoTagged='" + getIsAutoTagged() + "'" +
            ", source='" + getSource() + "'" +
            ", document=" + getDocument() +
            ", tag=" + getTag() +
            "}";
    }
}
