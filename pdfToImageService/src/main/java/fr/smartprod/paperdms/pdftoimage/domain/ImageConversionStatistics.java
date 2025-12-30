package fr.smartprod.paperdms.pdftoimage.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entité - Statistiques de conversion (agrégées par jour)
 */
@Entity
@Table(name = "image_conversion_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imageconversionstatistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "statistics_date", nullable = false, unique = true)
    private LocalDate statisticsDate;

    @Min(value = 0)
    @Column(name = "total_conversions")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer totalConversions;

    @Min(value = 0)
    @Column(name = "successful_conversions")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer successfulConversions;

    @Min(value = 0)
    @Column(name = "failed_conversions")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer failedConversions;

    @Min(value = 0)
    @Column(name = "total_pages_converted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer totalPagesConverted;

    @Min(value = 0)
    @Column(name = "total_images_generated")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer totalImagesGenerated;

    @Min(value = 0L)
    @Column(name = "total_images_size")
    private Long totalImagesSize;

    @Column(name = "average_processing_duration")
    private Long averageProcessingDuration;

    @Column(name = "max_processing_duration")
    private Long maxProcessingDuration;

    @Column(name = "min_processing_duration")
    private Long minProcessingDuration;

    @NotNull
    @Column(name = "calculated_at", nullable = false)
    private Instant calculatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageConversionStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatisticsDate() {
        return this.statisticsDate;
    }

    public ImageConversionStatistics statisticsDate(LocalDate statisticsDate) {
        this.setStatisticsDate(statisticsDate);
        return this;
    }

    public void setStatisticsDate(LocalDate statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Integer getTotalConversions() {
        return this.totalConversions;
    }

    public ImageConversionStatistics totalConversions(Integer totalConversions) {
        this.setTotalConversions(totalConversions);
        return this;
    }

    public void setTotalConversions(Integer totalConversions) {
        this.totalConversions = totalConversions;
    }

    public Integer getSuccessfulConversions() {
        return this.successfulConversions;
    }

    public ImageConversionStatistics successfulConversions(Integer successfulConversions) {
        this.setSuccessfulConversions(successfulConversions);
        return this;
    }

    public void setSuccessfulConversions(Integer successfulConversions) {
        this.successfulConversions = successfulConversions;
    }

    public Integer getFailedConversions() {
        return this.failedConversions;
    }

    public ImageConversionStatistics failedConversions(Integer failedConversions) {
        this.setFailedConversions(failedConversions);
        return this;
    }

    public void setFailedConversions(Integer failedConversions) {
        this.failedConversions = failedConversions;
    }

    public Integer getTotalPagesConverted() {
        return this.totalPagesConverted;
    }

    public ImageConversionStatistics totalPagesConverted(Integer totalPagesConverted) {
        this.setTotalPagesConverted(totalPagesConverted);
        return this;
    }

    public void setTotalPagesConverted(Integer totalPagesConverted) {
        this.totalPagesConverted = totalPagesConverted;
    }

    public Integer getTotalImagesGenerated() {
        return this.totalImagesGenerated;
    }

    public ImageConversionStatistics totalImagesGenerated(Integer totalImagesGenerated) {
        this.setTotalImagesGenerated(totalImagesGenerated);
        return this;
    }

    public void setTotalImagesGenerated(Integer totalImagesGenerated) {
        this.totalImagesGenerated = totalImagesGenerated;
    }

    public Long getTotalImagesSize() {
        return this.totalImagesSize;
    }

    public ImageConversionStatistics totalImagesSize(Long totalImagesSize) {
        this.setTotalImagesSize(totalImagesSize);
        return this;
    }

    public void setTotalImagesSize(Long totalImagesSize) {
        this.totalImagesSize = totalImagesSize;
    }

    public Long getAverageProcessingDuration() {
        return this.averageProcessingDuration;
    }

    public ImageConversionStatistics averageProcessingDuration(Long averageProcessingDuration) {
        this.setAverageProcessingDuration(averageProcessingDuration);
        return this;
    }

    public void setAverageProcessingDuration(Long averageProcessingDuration) {
        this.averageProcessingDuration = averageProcessingDuration;
    }

    public Long getMaxProcessingDuration() {
        return this.maxProcessingDuration;
    }

    public ImageConversionStatistics maxProcessingDuration(Long maxProcessingDuration) {
        this.setMaxProcessingDuration(maxProcessingDuration);
        return this;
    }

    public void setMaxProcessingDuration(Long maxProcessingDuration) {
        this.maxProcessingDuration = maxProcessingDuration;
    }

    public Long getMinProcessingDuration() {
        return this.minProcessingDuration;
    }

    public ImageConversionStatistics minProcessingDuration(Long minProcessingDuration) {
        this.setMinProcessingDuration(minProcessingDuration);
        return this;
    }

    public void setMinProcessingDuration(Long minProcessingDuration) {
        this.minProcessingDuration = minProcessingDuration;
    }

    public Instant getCalculatedAt() {
        return this.calculatedAt;
    }

    public ImageConversionStatistics calculatedAt(Instant calculatedAt) {
        this.setCalculatedAt(calculatedAt);
        return this;
    }

    public void setCalculatedAt(Instant calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionStatistics)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageConversionStatistics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionStatistics{" +
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
