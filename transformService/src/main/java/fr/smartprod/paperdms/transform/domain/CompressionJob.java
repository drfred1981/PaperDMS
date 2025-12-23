package fr.smartprod.paperdms.transform.domain;

import fr.smartprod.paperdms.transform.domain.enumeration.CompressionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CompressionJob.
 */
@Entity
@Table(name = "compression_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompressionJob implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "compression_type", nullable = false)
    private CompressionType compressionType;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "quality")
    private Integer quality;

    @Column(name = "target_size_kb")
    private Long targetSizeKb;

    @Column(name = "original_size")
    private Long originalSize;

    @Column(name = "compressed_size")
    private Long compressedSize;

    @Column(name = "compression_ratio")
    private Double compressionRatio;

    @Size(max = 1000)
    @Column(name = "output_s_3_key", length = 1000)
    private String outputS3Key;

    @Column(name = "output_document_id")
    private Long outputDocumentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransformStatus status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompressionJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public CompressionJob documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public CompressionType getCompressionType() {
        return this.compressionType;
    }

    public CompressionJob compressionType(CompressionType compressionType) {
        this.setCompressionType(compressionType);
        return this;
    }

    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    public Integer getQuality() {
        return this.quality;
    }

    public CompressionJob quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Long getTargetSizeKb() {
        return this.targetSizeKb;
    }

    public CompressionJob targetSizeKb(Long targetSizeKb) {
        this.setTargetSizeKb(targetSizeKb);
        return this;
    }

    public void setTargetSizeKb(Long targetSizeKb) {
        this.targetSizeKb = targetSizeKb;
    }

    public Long getOriginalSize() {
        return this.originalSize;
    }

    public CompressionJob originalSize(Long originalSize) {
        this.setOriginalSize(originalSize);
        return this;
    }

    public void setOriginalSize(Long originalSize) {
        this.originalSize = originalSize;
    }

    public Long getCompressedSize() {
        return this.compressedSize;
    }

    public CompressionJob compressedSize(Long compressedSize) {
        this.setCompressedSize(compressedSize);
        return this;
    }

    public void setCompressedSize(Long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public Double getCompressionRatio() {
        return this.compressionRatio;
    }

    public CompressionJob compressionRatio(Double compressionRatio) {
        this.setCompressionRatio(compressionRatio);
        return this;
    }

    public void setCompressionRatio(Double compressionRatio) {
        this.compressionRatio = compressionRatio;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public CompressionJob outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public Long getOutputDocumentId() {
        return this.outputDocumentId;
    }

    public CompressionJob outputDocumentId(Long outputDocumentId) {
        this.setOutputDocumentId(outputDocumentId);
        return this;
    }

    public void setOutputDocumentId(Long outputDocumentId) {
        this.outputDocumentId = outputDocumentId;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public CompressionJob status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public CompressionJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public CompressionJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public CompressionJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CompressionJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public CompressionJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompressionJob)) {
            return false;
        }
        return getId() != null && getId().equals(((CompressionJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompressionJob{" +
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
