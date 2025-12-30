package fr.smartprod.paperdms.pdftoimage.service.dto;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest} entity.
 */
@Schema(description = "Entite principale - Demande de conversion PDF vers image(s)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagePdfConversionRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private Long sourceDocumentId;

    @NotNull
    @Size(max = 255)
    private String sourceFileName;

    @NotNull
    @Size(max = 500)
    private String sourcePdfS3Key;

    @NotNull
    private ImageQuality imageQuality;

    @NotNull
    private ImageFormat imageFormat;

    @NotNull
    private ConversionType conversionType;

    @Min(value = 1)
    private Integer startPage;

    @Min(value = 1)
    private Integer endPage;

    @Min(value = 1)
    private Integer totalPages;

    @NotNull
    private ConversionStatus status;

    @Size(max = 2000)
    private String errorMessage;

    @NotNull
    private Instant requestedAt;

    private Instant startedAt;

    private Instant completedAt;

    private Long processingDuration;

    private Long totalImagesSize;

    @Min(value = 0)
    private Integer imagesGenerated;

    @Min(value = 72)
    @Max(value = 1200)
    private Integer dpi;

    private Long requestedByUserId;

    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    @Lob
    private String additionalOptions;

    private ImageConversionBatchDTO batch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceDocumentId() {
        return sourceDocumentId;
    }

    public void setSourceDocumentId(Long sourceDocumentId) {
        this.sourceDocumentId = sourceDocumentId;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public String getSourcePdfS3Key() {
        return sourcePdfS3Key;
    }

    public void setSourcePdfS3Key(String sourcePdfS3Key) {
        this.sourcePdfS3Key = sourcePdfS3Key;
    }

    public ImageQuality getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(ImageQuality imageQuality) {
        this.imageQuality = imageQuality;
    }

    public ImageFormat getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(ImageFormat imageFormat) {
        this.imageFormat = imageFormat;
    }

    public ConversionType getConversionType() {
        return conversionType;
    }

    public void setConversionType(ConversionType conversionType) {
        this.conversionType = conversionType;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public ConversionStatus getStatus() {
        return status;
    }

    public void setStatus(ConversionStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getProcessingDuration() {
        return processingDuration;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    public Long getTotalImagesSize() {
        return totalImagesSize;
    }

    public void setTotalImagesSize(Long totalImagesSize) {
        this.totalImagesSize = totalImagesSize;
    }

    public Integer getImagesGenerated() {
        return imagesGenerated;
    }

    public void setImagesGenerated(Integer imagesGenerated) {
        this.imagesGenerated = imagesGenerated;
    }

    public Integer getDpi() {
        return dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public Long getRequestedByUserId() {
        return requestedByUserId;
    }

    public void setRequestedByUserId(Long requestedByUserId) {
        this.requestedByUserId = requestedByUserId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAdditionalOptions() {
        return additionalOptions;
    }

    public void setAdditionalOptions(String additionalOptions) {
        this.additionalOptions = additionalOptions;
    }

    public ImageConversionBatchDTO getBatch() {
        return batch;
    }

    public void setBatch(ImageConversionBatchDTO batch) {
        this.batch = batch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagePdfConversionRequestDTO)) {
            return false;
        }

        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = (ImagePdfConversionRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagePdfConversionRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagePdfConversionRequestDTO{" +
            "id=" + getId() +
            ", sourceDocumentId=" + getSourceDocumentId() +
            ", sourceFileName='" + getSourceFileName() + "'" +
            ", sourcePdfS3Key='" + getSourcePdfS3Key() + "'" +
            ", imageQuality='" + getImageQuality() + "'" +
            ", imageFormat='" + getImageFormat() + "'" +
            ", conversionType='" + getConversionType() + "'" +
            ", startPage=" + getStartPage() +
            ", endPage=" + getEndPage() +
            ", totalPages=" + getTotalPages() +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", requestedAt='" + getRequestedAt() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", processingDuration=" + getProcessingDuration() +
            ", totalImagesSize=" + getTotalImagesSize() +
            ", imagesGenerated=" + getImagesGenerated() +
            ", dpi=" + getDpi() +
            ", requestedByUserId=" + getRequestedByUserId() +
            ", priority=" + getPriority() +
            ", additionalOptions='" + getAdditionalOptions() + "'" +
            ", batch=" + getBatch() +
            "}";
    }
}
