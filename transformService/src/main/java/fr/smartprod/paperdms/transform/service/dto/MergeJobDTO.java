package fr.smartprod.paperdms.transform.service.dto;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.transform.domain.MergeJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MergeJobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String sourceDocumentIds;

    @Lob
    private String mergeOrder;

    private Boolean includeBookmarks;

    private Boolean includeToc;

    private Boolean addPageNumbers;

    @Size(max = 1000)
    private String outputS3Key;

    private Long outputDocumentId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceDocumentIds() {
        return sourceDocumentIds;
    }

    public void setSourceDocumentIds(String sourceDocumentIds) {
        this.sourceDocumentIds = sourceDocumentIds;
    }

    public String getMergeOrder() {
        return mergeOrder;
    }

    public void setMergeOrder(String mergeOrder) {
        this.mergeOrder = mergeOrder;
    }

    public Boolean getIncludeBookmarks() {
        return includeBookmarks;
    }

    public void setIncludeBookmarks(Boolean includeBookmarks) {
        this.includeBookmarks = includeBookmarks;
    }

    public Boolean getIncludeToc() {
        return includeToc;
    }

    public void setIncludeToc(Boolean includeToc) {
        this.includeToc = includeToc;
    }

    public Boolean getAddPageNumbers() {
        return addPageNumbers;
    }

    public void setAddPageNumbers(Boolean addPageNumbers) {
        this.addPageNumbers = addPageNumbers;
    }

    public String getOutputS3Key() {
        return outputS3Key;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputDocumentId() {
        return outputDocumentId;
    }

    public void setOutputDocumentId(Long outputDocumentId) {
        this.outputDocumentId = outputDocumentId;
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
        if (!(o instanceof MergeJobDTO)) {
            return false;
        }

        MergeJobDTO mergeJobDTO = (MergeJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mergeJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MergeJobDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sourceDocumentIds='" + getSourceDocumentIds() + "'" +
            ", mergeOrder='" + getMergeOrder() + "'" +
            ", includeBookmarks='" + getIncludeBookmarks() + "'" +
            ", includeToc='" + getIncludeToc() + "'" +
            ", addPageNumbers='" + getAddPageNumbers() + "'" +
            ", outputS3Key='" + getOutputS3Key() + "'" +
            ", outputDocumentId=" + getOutputDocumentId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
