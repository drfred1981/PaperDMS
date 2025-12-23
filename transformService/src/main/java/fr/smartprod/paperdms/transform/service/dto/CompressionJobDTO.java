package fr.smartprod.paperdms.transform.service.dto;

import fr.smartprod.paperdms.transform.domain.enumeration.CompressionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.transform.domain.CompressionJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompressionJobDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private CompressionType compressionType;

    @Min(value = 0)
    @Max(value = 100)
    private Integer quality;

    private Long targetSizeKb;

    private Long originalSize;

    private Long compressedSize;

    private Double compressionRatio;

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

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Long getTargetSizeKb() {
        return targetSizeKb;
    }

    public void setTargetSizeKb(Long targetSizeKb) {
        this.targetSizeKb = targetSizeKb;
    }

    public Long getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(Long originalSize) {
        this.originalSize = originalSize;
    }

    public Long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(Long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public Double getCompressionRatio() {
        return compressionRatio;
    }

    public void setCompressionRatio(Double compressionRatio) {
        this.compressionRatio = compressionRatio;
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
        if (!(o instanceof CompressionJobDTO)) {
            return false;
        }

        CompressionJobDTO compressionJobDTO = (CompressionJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compressionJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompressionJobDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", compressionType='" + getCompressionType() + "'" +
            ", quality=" + getQuality() +
            ", targetSizeKb=" + getTargetSizeKb() +
            ", originalSize=" + getOriginalSize() +
            ", compressedSize=" + getCompressedSize() +
            ", compressionRatio=" + getCompressionRatio() +
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
