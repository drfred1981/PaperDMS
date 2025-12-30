package fr.smartprod.paperdms.transform.service.dto;

import fr.smartprod.paperdms.transform.domain.enumeration.RedactionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.transform.domain.TransformRedactionJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransformRedactionJobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @Lob
    private String redactionAreas;

    @NotNull
    private RedactionType redactionType;

    @Size(max = 7)
    private String redactionColor;

    @Size(max = 500)
    private String replaceWith;

    @Size(max = 1000)
    private String outputS3Key;

    @Size(max = 64)
    private String outputDocumentSha256;

    @NotNull
    private TransformStatus status;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getRedactionAreas() {
        return redactionAreas;
    }

    public void setRedactionAreas(String redactionAreas) {
        this.redactionAreas = redactionAreas;
    }

    public RedactionType getRedactionType() {
        return redactionType;
    }

    public void setRedactionType(RedactionType redactionType) {
        this.redactionType = redactionType;
    }

    public String getRedactionColor() {
        return redactionColor;
    }

    public void setRedactionColor(String redactionColor) {
        this.redactionColor = redactionColor;
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public void setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
    }

    public String getOutputS3Key() {
        return outputS3Key;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public String getOutputDocumentSha256() {
        return outputDocumentSha256;
    }

    public void setOutputDocumentSha256(String outputDocumentSha256) {
        this.outputDocumentSha256 = outputDocumentSha256;
    }

    public TransformStatus getStatus() {
        return status;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransformRedactionJobDTO)) {
            return false;
        }

        TransformRedactionJobDTO transformRedactionJobDTO = (TransformRedactionJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transformRedactionJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransformRedactionJobDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", redactionAreas='" + getRedactionAreas() + "'" +
            ", redactionType='" + getRedactionType() + "'" +
            ", redactionColor='" + getRedactionColor() + "'" +
            ", replaceWith='" + getReplaceWith() + "'" +
            ", outputS3Key='" + getOutputS3Key() + "'" +
            ", outputDocumentSha256='" + getOutputDocumentSha256() + "'" +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
