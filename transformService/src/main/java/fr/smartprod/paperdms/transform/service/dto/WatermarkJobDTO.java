package fr.smartprod.paperdms.transform.service.dto;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkPosition;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.transform.domain.WatermarkJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WatermarkJobDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private WatermarkType watermarkType;

    @Size(max = 500)
    private String watermarkText;

    @Size(max = 1000)
    private String watermarkImageS3Key;

    @NotNull
    private WatermarkPosition position;

    @Min(value = 0)
    @Max(value = 100)
    private Integer opacity;

    private Integer fontSize;

    @Size(max = 7)
    private String color;

    private Integer rotation;

    private Boolean tiled;

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

    public WatermarkType getWatermarkType() {
        return watermarkType;
    }

    public void setWatermarkType(WatermarkType watermarkType) {
        this.watermarkType = watermarkType;
    }

    public String getWatermarkText() {
        return watermarkText;
    }

    public void setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
    }

    public String getWatermarkImageS3Key() {
        return watermarkImageS3Key;
    }

    public void setWatermarkImageS3Key(String watermarkImageS3Key) {
        this.watermarkImageS3Key = watermarkImageS3Key;
    }

    public WatermarkPosition getPosition() {
        return position;
    }

    public void setPosition(WatermarkPosition position) {
        this.position = position;
    }

    public Integer getOpacity() {
        return opacity;
    }

    public void setOpacity(Integer opacity) {
        this.opacity = opacity;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Boolean getTiled() {
        return tiled;
    }

    public void setTiled(Boolean tiled) {
        this.tiled = tiled;
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
        if (!(o instanceof WatermarkJobDTO)) {
            return false;
        }

        WatermarkJobDTO watermarkJobDTO = (WatermarkJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, watermarkJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WatermarkJobDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
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
