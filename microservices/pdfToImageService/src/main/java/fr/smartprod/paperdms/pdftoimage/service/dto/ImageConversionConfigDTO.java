package fr.smartprod.paperdms.pdftoimage.service.dto;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig} entity.
 */
@Schema(description = "Entité - Configuration de conversion par défaut")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionConfigDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String configName;

    @Size(max = 500)
    private String description;

    @NotNull
    private ImageQuality defaultQuality;

    @NotNull
    private ImageFormat defaultFormat;

    @NotNull
    @Min(value = 72)
    @Max(value = 1200)
    private Integer defaultDpi;

    @NotNull
    private ConversionType defaultConversionType;

    @Min(value = 1)
    @Max(value = 5)
    private Integer defaultPriority;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isDefault;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageQuality getDefaultQuality() {
        return defaultQuality;
    }

    public void setDefaultQuality(ImageQuality defaultQuality) {
        this.defaultQuality = defaultQuality;
    }

    public ImageFormat getDefaultFormat() {
        return defaultFormat;
    }

    public void setDefaultFormat(ImageFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public Integer getDefaultDpi() {
        return defaultDpi;
    }

    public void setDefaultDpi(Integer defaultDpi) {
        this.defaultDpi = defaultDpi;
    }

    public ConversionType getDefaultConversionType() {
        return defaultConversionType;
    }

    public void setDefaultConversionType(ConversionType defaultConversionType) {
        this.defaultConversionType = defaultConversionType;
    }

    public Integer getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(Integer defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionConfigDTO)) {
            return false;
        }

        ImageConversionConfigDTO imageConversionConfigDTO = (ImageConversionConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageConversionConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionConfigDTO{" +
            "id=" + getId() +
            ", configName='" + getConfigName() + "'" +
            ", description='" + getDescription() + "'" +
            ", defaultQuality='" + getDefaultQuality() + "'" +
            ", defaultFormat='" + getDefaultFormat() + "'" +
            ", defaultDpi=" + getDefaultDpi() +
            ", defaultConversionType='" + getDefaultConversionType() + "'" +
            ", defaultPriority=" + getDefaultPriority() +
            ", isActive='" + getIsActive() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
