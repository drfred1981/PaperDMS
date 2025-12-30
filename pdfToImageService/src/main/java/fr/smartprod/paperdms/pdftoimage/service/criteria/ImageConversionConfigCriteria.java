package fr.smartprod.paperdms.pdftoimage.service.criteria;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig} entity. This class is used
 * in {@link fr.smartprod.paperdms.pdftoimage.web.rest.ImageConversionConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-conversion-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionConfigCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ImageQuality
     */
    public static class ImageQualityFilter extends Filter<ImageQuality> {

        public ImageQualityFilter() {}

        public ImageQualityFilter(ImageQualityFilter filter) {
            super(filter);
        }

        @Override
        public ImageQualityFilter copy() {
            return new ImageQualityFilter(this);
        }
    }

    /**
     * Class for filtering ImageFormat
     */
    public static class ImageFormatFilter extends Filter<ImageFormat> {

        public ImageFormatFilter() {}

        public ImageFormatFilter(ImageFormatFilter filter) {
            super(filter);
        }

        @Override
        public ImageFormatFilter copy() {
            return new ImageFormatFilter(this);
        }
    }

    /**
     * Class for filtering ConversionType
     */
    public static class ConversionTypeFilter extends Filter<ConversionType> {

        public ConversionTypeFilter() {}

        public ConversionTypeFilter(ConversionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ConversionTypeFilter copy() {
            return new ConversionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter configName;

    private StringFilter description;

    private ImageQualityFilter defaultQuality;

    private ImageFormatFilter defaultFormat;

    private IntegerFilter defaultDpi;

    private ConversionTypeFilter defaultConversionType;

    private IntegerFilter defaultPriority;

    private BooleanFilter isActive;

    private BooleanFilter isDefault;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public ImageConversionConfigCriteria() {}

    public ImageConversionConfigCriteria(ImageConversionConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.configName = other.optionalConfigName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.defaultQuality = other.optionalDefaultQuality().map(ImageQualityFilter::copy).orElse(null);
        this.defaultFormat = other.optionalDefaultFormat().map(ImageFormatFilter::copy).orElse(null);
        this.defaultDpi = other.optionalDefaultDpi().map(IntegerFilter::copy).orElse(null);
        this.defaultConversionType = other.optionalDefaultConversionType().map(ConversionTypeFilter::copy).orElse(null);
        this.defaultPriority = other.optionalDefaultPriority().map(IntegerFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageConversionConfigCriteria copy() {
        return new ImageConversionConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getConfigName() {
        return configName;
    }

    public Optional<StringFilter> optionalConfigName() {
        return Optional.ofNullable(configName);
    }

    public StringFilter configName() {
        if (configName == null) {
            setConfigName(new StringFilter());
        }
        return configName;
    }

    public void setConfigName(StringFilter configName) {
        this.configName = configName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ImageQualityFilter getDefaultQuality() {
        return defaultQuality;
    }

    public Optional<ImageQualityFilter> optionalDefaultQuality() {
        return Optional.ofNullable(defaultQuality);
    }

    public ImageQualityFilter defaultQuality() {
        if (defaultQuality == null) {
            setDefaultQuality(new ImageQualityFilter());
        }
        return defaultQuality;
    }

    public void setDefaultQuality(ImageQualityFilter defaultQuality) {
        this.defaultQuality = defaultQuality;
    }

    public ImageFormatFilter getDefaultFormat() {
        return defaultFormat;
    }

    public Optional<ImageFormatFilter> optionalDefaultFormat() {
        return Optional.ofNullable(defaultFormat);
    }

    public ImageFormatFilter defaultFormat() {
        if (defaultFormat == null) {
            setDefaultFormat(new ImageFormatFilter());
        }
        return defaultFormat;
    }

    public void setDefaultFormat(ImageFormatFilter defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public IntegerFilter getDefaultDpi() {
        return defaultDpi;
    }

    public Optional<IntegerFilter> optionalDefaultDpi() {
        return Optional.ofNullable(defaultDpi);
    }

    public IntegerFilter defaultDpi() {
        if (defaultDpi == null) {
            setDefaultDpi(new IntegerFilter());
        }
        return defaultDpi;
    }

    public void setDefaultDpi(IntegerFilter defaultDpi) {
        this.defaultDpi = defaultDpi;
    }

    public ConversionTypeFilter getDefaultConversionType() {
        return defaultConversionType;
    }

    public Optional<ConversionTypeFilter> optionalDefaultConversionType() {
        return Optional.ofNullable(defaultConversionType);
    }

    public ConversionTypeFilter defaultConversionType() {
        if (defaultConversionType == null) {
            setDefaultConversionType(new ConversionTypeFilter());
        }
        return defaultConversionType;
    }

    public void setDefaultConversionType(ConversionTypeFilter defaultConversionType) {
        this.defaultConversionType = defaultConversionType;
    }

    public IntegerFilter getDefaultPriority() {
        return defaultPriority;
    }

    public Optional<IntegerFilter> optionalDefaultPriority() {
        return Optional.ofNullable(defaultPriority);
    }

    public IntegerFilter defaultPriority() {
        if (defaultPriority == null) {
            setDefaultPriority(new IntegerFilter());
        }
        return defaultPriority;
    }

    public void setDefaultPriority(IntegerFilter defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImageConversionConfigCriteria that = (ImageConversionConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(configName, that.configName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(defaultQuality, that.defaultQuality) &&
            Objects.equals(defaultFormat, that.defaultFormat) &&
            Objects.equals(defaultDpi, that.defaultDpi) &&
            Objects.equals(defaultConversionType, that.defaultConversionType) &&
            Objects.equals(defaultPriority, that.defaultPriority) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            configName,
            description,
            defaultQuality,
            defaultFormat,
            defaultDpi,
            defaultConversionType,
            defaultPriority,
            isActive,
            isDefault,
            createdAt,
            updatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalConfigName().map(f -> "configName=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDefaultQuality().map(f -> "defaultQuality=" + f + ", ").orElse("") +
            optionalDefaultFormat().map(f -> "defaultFormat=" + f + ", ").orElse("") +
            optionalDefaultDpi().map(f -> "defaultDpi=" + f + ", ").orElse("") +
            optionalDefaultConversionType().map(f -> "defaultConversionType=" + f + ", ").orElse("") +
            optionalDefaultPriority().map(f -> "defaultPriority=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
