package fr.smartprod.paperdms.transform.domain;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkPosition;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransformWatermarkJob.
 */
@Entity
@Table(name = "transform_watermark_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransformWatermarkJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    private String documentSha256;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "watermark_type", nullable = false)
    private WatermarkType watermarkType;

    @Size(max = 500)
    @Column(name = "watermark_text", length = 500)
    private String watermarkText;

    @Size(max = 1000)
    @Column(name = "watermark_image_s_3_key", length = 1000)
    private String watermarkImageS3Key;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private WatermarkPosition position;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "opacity")
    private Integer opacity;

    @Column(name = "font_size")
    private Integer fontSize;

    @Size(max = 7)
    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "rotation")
    private Integer rotation;

    @Column(name = "tiled")
    private Boolean tiled;

    @Size(max = 1000)
    @Column(name = "output_s_3_key", length = 1000)
    private String outputS3Key;

    @Size(max = 64)
    @Column(name = "output_document_sha_256", length = 64)
    private String outputDocumentSha256;

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

    public TransformWatermarkJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public TransformWatermarkJob documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public WatermarkType getWatermarkType() {
        return this.watermarkType;
    }

    public TransformWatermarkJob watermarkType(WatermarkType watermarkType) {
        this.setWatermarkType(watermarkType);
        return this;
    }

    public void setWatermarkType(WatermarkType watermarkType) {
        this.watermarkType = watermarkType;
    }

    public String getWatermarkText() {
        return this.watermarkText;
    }

    public TransformWatermarkJob watermarkText(String watermarkText) {
        this.setWatermarkText(watermarkText);
        return this;
    }

    public void setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
    }

    public String getWatermarkImageS3Key() {
        return this.watermarkImageS3Key;
    }

    public TransformWatermarkJob watermarkImageS3Key(String watermarkImageS3Key) {
        this.setWatermarkImageS3Key(watermarkImageS3Key);
        return this;
    }

    public void setWatermarkImageS3Key(String watermarkImageS3Key) {
        this.watermarkImageS3Key = watermarkImageS3Key;
    }

    public WatermarkPosition getPosition() {
        return this.position;
    }

    public TransformWatermarkJob position(WatermarkPosition position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(WatermarkPosition position) {
        this.position = position;
    }

    public Integer getOpacity() {
        return this.opacity;
    }

    public TransformWatermarkJob opacity(Integer opacity) {
        this.setOpacity(opacity);
        return this;
    }

    public void setOpacity(Integer opacity) {
        this.opacity = opacity;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public TransformWatermarkJob fontSize(Integer fontSize) {
        this.setFontSize(fontSize);
        return this;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getColor() {
        return this.color;
    }

    public TransformWatermarkJob color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getRotation() {
        return this.rotation;
    }

    public TransformWatermarkJob rotation(Integer rotation) {
        this.setRotation(rotation);
        return this;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Boolean getTiled() {
        return this.tiled;
    }

    public TransformWatermarkJob tiled(Boolean tiled) {
        this.setTiled(tiled);
        return this;
    }

    public void setTiled(Boolean tiled) {
        this.tiled = tiled;
    }

    public String getOutputS3Key() {
        return this.outputS3Key;
    }

    public TransformWatermarkJob outputS3Key(String outputS3Key) {
        this.setOutputS3Key(outputS3Key);
        return this;
    }

    public void setOutputS3Key(String outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public String getOutputDocumentSha256() {
        return this.outputDocumentSha256;
    }

    public TransformWatermarkJob outputDocumentSha256(String outputDocumentSha256) {
        this.setOutputDocumentSha256(outputDocumentSha256);
        return this;
    }

    public void setOutputDocumentSha256(String outputDocumentSha256) {
        this.outputDocumentSha256 = outputDocumentSha256;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public TransformWatermarkJob status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public TransformWatermarkJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public TransformWatermarkJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public TransformWatermarkJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public TransformWatermarkJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public TransformWatermarkJob createdDate(Instant createdDate) {
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
        if (!(o instanceof TransformWatermarkJob)) {
            return false;
        }
        return getId() != null && getId().equals(((TransformWatermarkJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransformWatermarkJob{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", watermarkType='" + getWatermarkType() + "'" +
            ", watermarkText='" + getWatermarkText() + "'" +
            ", watermarkImageS3Key='" + getWatermarkImageS3Key() + "'" +
            ", position='" + getPosition() + "'" +
            ", opacity=" + getOpacity() +
            ", fontSize=" + getFontSize() +
            ", color='" + getColor() + "'" +
            ", rotation=" + getRotation() +
            ", tiled='" + getTiled() + "'" +
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
