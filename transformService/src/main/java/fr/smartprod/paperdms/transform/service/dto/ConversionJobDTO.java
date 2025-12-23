package fr.smartprod.paperdms.transform.service.dto;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.transform.domain.ConversionJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversionJobDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @NotNull
    @Size(max = 50)
    private String sourceFormat;

    @NotNull
    @Size(max = 50)
    private String targetFormat;

    @Size(max = 100)
    private String conversionEngine;

    @Lob
    private String options;

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

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getSourceFormat() {
        return sourceFormat;
    }

    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    public String getTargetFormat() {
        return targetFormat;
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }

    public String getConversionEngine() {
        return conversionEngine;
    }

    public void setConversionEngine(String conversionEngine) {
        this.conversionEngine = conversionEngine;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
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
        if (!(o instanceof ConversionJobDTO)) {
            return false;
        }

        ConversionJobDTO conversionJobDTO = (ConversionJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conversionJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversionJobDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", sourceFormat='" + getSourceFormat() + "'" +
            ", targetFormat='" + getTargetFormat() + "'" +
            ", conversionEngine='" + getConversionEngine() + "'" +
            ", options='" + getOptions() + "'" +
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
