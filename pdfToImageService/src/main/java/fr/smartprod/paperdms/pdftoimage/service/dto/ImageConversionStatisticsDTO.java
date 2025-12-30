package fr.smartprod.paperdms.pdftoimage.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics} entity.
 */
@Schema(description = "Entité - Statistiques de conversion (agrégées par jour)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionStatisticsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate statisticsDate;

    @Min(value = 0)
    private Integer totalConversions;

    @Min(value = 0)
    private Integer successfulConversions;

    @Min(value = 0)
    private Integer failedConversions;

    @Min(value = 0)
    private Integer totalPagesConverted;

    @Min(value = 0)
    private Integer totalImagesGenerated;

    @Min(value = 0L)
    private Long totalImagesSize;

    private Long averageProcessingDuration;

    private Long maxProcessingDuration;

    private Long minProcessingDuration;

    @NotNull
    private Instant calculatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(LocalDate statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Integer getTotalConversions() {
        return totalConversions;
    }

    public void setTotalConversions(Integer totalConversions) {
        this.totalConversions = totalConversions;
    }

    public Integer getSuccessfulConversions() {
        return successfulConversions;
    }

    public void setSuccessfulConversions(Integer successfulConversions) {
        this.successfulConversions = successfulConversions;
    }

    public Integer getFailedConversions() {
        return failedConversions;
    }

    public void setFailedConversions(Integer failedConversions) {
        this.failedConversions = failedConversions;
    }

    public Integer getTotalPagesConverted() {
        return totalPagesConverted;
    }

    public void setTotalPagesConverted(Integer totalPagesConverted) {
        this.totalPagesConverted = totalPagesConverted;
    }

    public Integer getTotalImagesGenerated() {
        return totalImagesGenerated;
    }

    public void setTotalImagesGenerated(Integer totalImagesGenerated) {
        this.totalImagesGenerated = totalImagesGenerated;
    }

    public Long getTotalImagesSize() {
        return totalImagesSize;
    }

    public void setTotalImagesSize(Long totalImagesSize) {
        this.totalImagesSize = totalImagesSize;
    }

    public Long getAverageProcessingDuration() {
        return averageProcessingDuration;
    }

    public void setAverageProcessingDuration(Long averageProcessingDuration) {
        this.averageProcessingDuration = averageProcessingDuration;
    }

    public Long getMaxProcessingDuration() {
        return maxProcessingDuration;
    }

    public void setMaxProcessingDuration(Long maxProcessingDuration) {
        this.maxProcessingDuration = maxProcessingDuration;
    }

    public Long getMinProcessingDuration() {
        return minProcessingDuration;
    }

    public void setMinProcessingDuration(Long minProcessingDuration) {
        this.minProcessingDuration = minProcessingDuration;
    }

    public Instant getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Instant calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionStatisticsDTO)) {
            return false;
        }

        ImageConversionStatisticsDTO imageConversionStatisticsDTO = (ImageConversionStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageConversionStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionStatisticsDTO{" +
            "id=" + getId() +
            ", statisticsDate='" + getStatisticsDate() + "'" +
            ", totalConversions=" + getTotalConversions() +
            ", successfulConversions=" + getSuccessfulConversions() +
            ", failedConversions=" + getFailedConversions() +
            ", totalPagesConverted=" + getTotalPagesConverted() +
            ", totalImagesGenerated=" + getTotalImagesGenerated() +
            ", totalImagesSize=" + getTotalImagesSize() +
            ", averageProcessingDuration=" + getAverageProcessingDuration() +
            ", maxProcessingDuration=" + getMaxProcessingDuration() +
            ", minProcessingDuration=" + getMinProcessingDuration() +
            ", calculatedAt='" + getCalculatedAt() + "'" +
            "}";
    }
}
