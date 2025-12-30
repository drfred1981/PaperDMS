package fr.smartprod.paperdms.pdftoimage.service.criteria;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage} entity. This class is used
 * in {@link fr.smartprod.paperdms.pdftoimage.web.rest.ImageGeneratedImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-generated-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageGeneratedImageCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter pageNumber;

    private StringFilter fileName;

    private StringFilter s3Key;

    private StringFilter preSignedUrl;

    private InstantFilter urlExpiresAt;

    private ImageFormatFilter format;

    private ImageQualityFilter quality;

    private IntegerFilter width;

    private IntegerFilter height;

    private LongFilter fileSize;

    private IntegerFilter dpi;

    private StringFilter sha256Hash;

    private InstantFilter generatedAt;

    private LongFilter conversionRequestId;

    private Boolean distinct;

    public ImageGeneratedImageCriteria() {}

    public ImageGeneratedImageCriteria(ImageGeneratedImageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.pageNumber = other.optionalPageNumber().map(IntegerFilter::copy).orElse(null);
        this.fileName = other.optionalFileName().map(StringFilter::copy).orElse(null);
        this.s3Key = other.optionals3Key().map(StringFilter::copy).orElse(null);
        this.preSignedUrl = other.optionalPreSignedUrl().map(StringFilter::copy).orElse(null);
        this.urlExpiresAt = other.optionalUrlExpiresAt().map(InstantFilter::copy).orElse(null);
        this.format = other.optionalFormat().map(ImageFormatFilter::copy).orElse(null);
        this.quality = other.optionalQuality().map(ImageQualityFilter::copy).orElse(null);
        this.width = other.optionalWidth().map(IntegerFilter::copy).orElse(null);
        this.height = other.optionalHeight().map(IntegerFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.dpi = other.optionalDpi().map(IntegerFilter::copy).orElse(null);
        this.sha256Hash = other.optionalSha256Hash().map(StringFilter::copy).orElse(null);
        this.generatedAt = other.optionalGeneratedAt().map(InstantFilter::copy).orElse(null);
        this.conversionRequestId = other.optionalConversionRequestId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageGeneratedImageCriteria copy() {
        return new ImageGeneratedImageCriteria(this);
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

    public IntegerFilter getPageNumber() {
        return pageNumber;
    }

    public Optional<IntegerFilter> optionalPageNumber() {
        return Optional.ofNullable(pageNumber);
    }

    public IntegerFilter pageNumber() {
        if (pageNumber == null) {
            setPageNumber(new IntegerFilter());
        }
        return pageNumber;
    }

    public void setPageNumber(IntegerFilter pageNumber) {
        this.pageNumber = pageNumber;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public Optional<StringFilter> optionalFileName() {
        return Optional.ofNullable(fileName);
    }

    public StringFilter fileName() {
        if (fileName == null) {
            setFileName(new StringFilter());
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter gets3Key() {
        return s3Key;
    }

    public Optional<StringFilter> optionals3Key() {
        return Optional.ofNullable(s3Key);
    }

    public StringFilter s3Key() {
        if (s3Key == null) {
            sets3Key(new StringFilter());
        }
        return s3Key;
    }

    public void sets3Key(StringFilter s3Key) {
        this.s3Key = s3Key;
    }

    public StringFilter getPreSignedUrl() {
        return preSignedUrl;
    }

    public Optional<StringFilter> optionalPreSignedUrl() {
        return Optional.ofNullable(preSignedUrl);
    }

    public StringFilter preSignedUrl() {
        if (preSignedUrl == null) {
            setPreSignedUrl(new StringFilter());
        }
        return preSignedUrl;
    }

    public void setPreSignedUrl(StringFilter preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public InstantFilter getUrlExpiresAt() {
        return urlExpiresAt;
    }

    public Optional<InstantFilter> optionalUrlExpiresAt() {
        return Optional.ofNullable(urlExpiresAt);
    }

    public InstantFilter urlExpiresAt() {
        if (urlExpiresAt == null) {
            setUrlExpiresAt(new InstantFilter());
        }
        return urlExpiresAt;
    }

    public void setUrlExpiresAt(InstantFilter urlExpiresAt) {
        this.urlExpiresAt = urlExpiresAt;
    }

    public ImageFormatFilter getFormat() {
        return format;
    }

    public Optional<ImageFormatFilter> optionalFormat() {
        return Optional.ofNullable(format);
    }

    public ImageFormatFilter format() {
        if (format == null) {
            setFormat(new ImageFormatFilter());
        }
        return format;
    }

    public void setFormat(ImageFormatFilter format) {
        this.format = format;
    }

    public ImageQualityFilter getQuality() {
        return quality;
    }

    public Optional<ImageQualityFilter> optionalQuality() {
        return Optional.ofNullable(quality);
    }

    public ImageQualityFilter quality() {
        if (quality == null) {
            setQuality(new ImageQualityFilter());
        }
        return quality;
    }

    public void setQuality(ImageQualityFilter quality) {
        this.quality = quality;
    }

    public IntegerFilter getWidth() {
        return width;
    }

    public Optional<IntegerFilter> optionalWidth() {
        return Optional.ofNullable(width);
    }

    public IntegerFilter width() {
        if (width == null) {
            setWidth(new IntegerFilter());
        }
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public Optional<IntegerFilter> optionalHeight() {
        return Optional.ofNullable(height);
    }

    public IntegerFilter height() {
        if (height == null) {
            setHeight(new IntegerFilter());
        }
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public IntegerFilter getDpi() {
        return dpi;
    }

    public Optional<IntegerFilter> optionalDpi() {
        return Optional.ofNullable(dpi);
    }

    public IntegerFilter dpi() {
        if (dpi == null) {
            setDpi(new IntegerFilter());
        }
        return dpi;
    }

    public void setDpi(IntegerFilter dpi) {
        this.dpi = dpi;
    }

    public StringFilter getSha256Hash() {
        return sha256Hash;
    }

    public Optional<StringFilter> optionalSha256Hash() {
        return Optional.ofNullable(sha256Hash);
    }

    public StringFilter sha256Hash() {
        if (sha256Hash == null) {
            setSha256Hash(new StringFilter());
        }
        return sha256Hash;
    }

    public void setSha256Hash(StringFilter sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    public InstantFilter getGeneratedAt() {
        return generatedAt;
    }

    public Optional<InstantFilter> optionalGeneratedAt() {
        return Optional.ofNullable(generatedAt);
    }

    public InstantFilter generatedAt() {
        if (generatedAt == null) {
            setGeneratedAt(new InstantFilter());
        }
        return generatedAt;
    }

    public void setGeneratedAt(InstantFilter generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LongFilter getConversionRequestId() {
        return conversionRequestId;
    }

    public Optional<LongFilter> optionalConversionRequestId() {
        return Optional.ofNullable(conversionRequestId);
    }

    public LongFilter conversionRequestId() {
        if (conversionRequestId == null) {
            setConversionRequestId(new LongFilter());
        }
        return conversionRequestId;
    }

    public void setConversionRequestId(LongFilter conversionRequestId) {
        this.conversionRequestId = conversionRequestId;
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
        final ImageGeneratedImageCriteria that = (ImageGeneratedImageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(s3Key, that.s3Key) &&
            Objects.equals(preSignedUrl, that.preSignedUrl) &&
            Objects.equals(urlExpiresAt, that.urlExpiresAt) &&
            Objects.equals(format, that.format) &&
            Objects.equals(quality, that.quality) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(dpi, that.dpi) &&
            Objects.equals(sha256Hash, that.sha256Hash) &&
            Objects.equals(generatedAt, that.generatedAt) &&
            Objects.equals(conversionRequestId, that.conversionRequestId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pageNumber,
            fileName,
            s3Key,
            preSignedUrl,
            urlExpiresAt,
            format,
            quality,
            width,
            height,
            fileSize,
            dpi,
            sha256Hash,
            generatedAt,
            conversionRequestId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageGeneratedImageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPageNumber().map(f -> "pageNumber=" + f + ", ").orElse("") +
            optionalFileName().map(f -> "fileName=" + f + ", ").orElse("") +
            optionals3Key().map(f -> "s3Key=" + f + ", ").orElse("") +
            optionalPreSignedUrl().map(f -> "preSignedUrl=" + f + ", ").orElse("") +
            optionalUrlExpiresAt().map(f -> "urlExpiresAt=" + f + ", ").orElse("") +
            optionalFormat().map(f -> "format=" + f + ", ").orElse("") +
            optionalQuality().map(f -> "quality=" + f + ", ").orElse("") +
            optionalWidth().map(f -> "width=" + f + ", ").orElse("") +
            optionalHeight().map(f -> "height=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalDpi().map(f -> "dpi=" + f + ", ").orElse("") +
            optionalSha256Hash().map(f -> "sha256Hash=" + f + ", ").orElse("") +
            optionalGeneratedAt().map(f -> "generatedAt=" + f + ", ").orElse("") +
            optionalConversionRequestId().map(f -> "conversionRequestId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
