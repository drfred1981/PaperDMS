package fr.smartprod.paperdms.pdftoimage.domain;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entité - Configuration de conversion par défaut
 */
@Entity
@Table(name = "image_conversion_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imageconversionconfig")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "config_name", length = 100, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String configName;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "default_quality", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ImageQuality defaultQuality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "default_format", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ImageFormat defaultFormat;

    @NotNull
    @Min(value = 72)
    @Max(value = 1200)
    @Column(name = "default_dpi", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer defaultDpi;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "default_conversion_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConversionType defaultConversionType;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "default_priority")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer defaultPriority;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_default", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDefault;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageConversionConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return this.configName;
    }

    public ImageConversionConfig configName(String configName) {
        this.setConfigName(configName);
        return this;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDescription() {
        return this.description;
    }

    public ImageConversionConfig description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageQuality getDefaultQuality() {
        return this.defaultQuality;
    }

    public ImageConversionConfig defaultQuality(ImageQuality defaultQuality) {
        this.setDefaultQuality(defaultQuality);
        return this;
    }

    public void setDefaultQuality(ImageQuality defaultQuality) {
        this.defaultQuality = defaultQuality;
    }

    public ImageFormat getDefaultFormat() {
        return this.defaultFormat;
    }

    public ImageConversionConfig defaultFormat(ImageFormat defaultFormat) {
        this.setDefaultFormat(defaultFormat);
        return this;
    }

    public void setDefaultFormat(ImageFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public Integer getDefaultDpi() {
        return this.defaultDpi;
    }

    public ImageConversionConfig defaultDpi(Integer defaultDpi) {
        this.setDefaultDpi(defaultDpi);
        return this;
    }

    public void setDefaultDpi(Integer defaultDpi) {
        this.defaultDpi = defaultDpi;
    }

    public ConversionType getDefaultConversionType() {
        return this.defaultConversionType;
    }

    public ImageConversionConfig defaultConversionType(ConversionType defaultConversionType) {
        this.setDefaultConversionType(defaultConversionType);
        return this;
    }

    public void setDefaultConversionType(ConversionType defaultConversionType) {
        this.defaultConversionType = defaultConversionType;
    }

    public Integer getDefaultPriority() {
        return this.defaultPriority;
    }

    public ImageConversionConfig defaultPriority(Integer defaultPriority) {
        this.setDefaultPriority(defaultPriority);
        return this;
    }

    public void setDefaultPriority(Integer defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ImageConversionConfig isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public ImageConversionConfig isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ImageConversionConfig createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ImageConversionConfig updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageConversionConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionConfig{" +
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
