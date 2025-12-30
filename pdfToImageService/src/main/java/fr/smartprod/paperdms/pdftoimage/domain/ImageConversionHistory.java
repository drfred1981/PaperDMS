package fr.smartprod.paperdms.pdftoimage.domain;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entité - Historique des conversions (archivage)
 */
@Entity
@Table(name = "image_conversion_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imageconversionhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "original_request_id", nullable = false)
    private Long originalRequestId;

    @NotNull
    @Column(name = "archived_at", nullable = false)
    private Instant archivedAt;

    @Lob
    @Column(name = "conversion_data", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String conversionData;

    @Min(value = 0)
    @Column(name = "images_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer imagesCount;

    @Min(value = 0L)
    @Column(name = "total_size")
    private Long totalSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "final_status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConversionStatus finalStatus;

    @Column(name = "processing_duration")
    private Long processingDuration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageConversionHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalRequestId() {
        return this.originalRequestId;
    }

    public ImageConversionHistory originalRequestId(Long originalRequestId) {
        this.setOriginalRequestId(originalRequestId);
        return this;
    }

    public void setOriginalRequestId(Long originalRequestId) {
        this.originalRequestId = originalRequestId;
    }

    public Instant getArchivedAt() {
        return this.archivedAt;
    }

    public ImageConversionHistory archivedAt(Instant archivedAt) {
        this.setArchivedAt(archivedAt);
        return this;
    }

    public void setArchivedAt(Instant archivedAt) {
        this.archivedAt = archivedAt;
    }

    public String getConversionData() {
        return this.conversionData;
    }

    public ImageConversionHistory conversionData(String conversionData) {
        this.setConversionData(conversionData);
        return this;
    }

    public void setConversionData(String conversionData) {
        this.conversionData = conversionData;
    }

    public Integer getImagesCount() {
        return this.imagesCount;
    }

    public ImageConversionHistory imagesCount(Integer imagesCount) {
        this.setImagesCount(imagesCount);
        return this;
    }

    public void setImagesCount(Integer imagesCount) {
        this.imagesCount = imagesCount;
    }

    public Long getTotalSize() {
        return this.totalSize;
    }

    public ImageConversionHistory totalSize(Long totalSize) {
        this.setTotalSize(totalSize);
        return this;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public ConversionStatus getFinalStatus() {
        return this.finalStatus;
    }

    public ImageConversionHistory finalStatus(ConversionStatus finalStatus) {
        this.setFinalStatus(finalStatus);
        return this;
    }

    public void setFinalStatus(ConversionStatus finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Long getProcessingDuration() {
        return this.processingDuration;
    }

    public ImageConversionHistory processingDuration(Long processingDuration) {
        this.setProcessingDuration(processingDuration);
        return this;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageConversionHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionHistory{" +
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
