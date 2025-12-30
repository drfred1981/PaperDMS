package fr.smartprod.paperdms.pdftoimage.service.dto;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory} entity.
 */
@Schema(description = "Entité - Historique des conversions (archivage)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long originalRequestId;

    @NotNull
    private Instant archivedAt;

    @Lob
    private String conversionData;

    @Min(value = 0)
    private Integer imagesCount;

    @Min(value = 0L)
    private Long totalSize;

    @NotNull
    private ConversionStatus finalStatus;

    private Long processingDuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalRequestId() {
        return originalRequestId;
    }

    public void setOriginalRequestId(Long originalRequestId) {
        this.originalRequestId = originalRequestId;
    }

    public Instant getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(Instant archivedAt) {
        this.archivedAt = archivedAt;
    }

    public String getConversionData() {
        return conversionData;
    }

    public void setConversionData(String conversionData) {
        this.conversionData = conversionData;
    }

    public Integer getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(Integer imagesCount) {
        this.imagesCount = imagesCount;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public ConversionStatus getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(ConversionStatus finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Long getProcessingDuration() {
        return processingDuration;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionHistoryDTO)) {
            return false;
        }

        ImageConversionHistoryDTO imageConversionHistoryDTO = (ImageConversionHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageConversionHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionHistoryDTO{" +
            "id=" + getId() +
            ", originalRequestId=" + getOriginalRequestId() +
            ", archivedAt='" + getArchivedAt() + "'" +
            ", conversionData='" + getConversionData() + "'" +
            ", imagesCount=" + getImagesCount() +
            ", totalSize=" + getTotalSize() +
            ", finalStatus='" + getFinalStatus() + "'" +
            ", processingDuration=" + getProcessingDuration() +
            "}";
    }
}
