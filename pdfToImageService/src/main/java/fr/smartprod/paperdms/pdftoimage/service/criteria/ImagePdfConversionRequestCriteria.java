package fr.smartprod.paperdms.pdftoimage.service.criteria;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
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
 * Criteria class for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest} entity. This class is used
 * in {@link fr.smartprod.paperdms.pdftoimage.web.rest.ImagePdfConversionRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-pdf-conversion-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagePdfConversionRequestCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering ConversionStatus
     */
    public static class ConversionStatusFilter extends Filter<ConversionStatus> {

        public ConversionStatusFilter() {}

        public ConversionStatusFilter(ConversionStatusFilter filter) {
            super(filter);
        }

        @Override
        public ConversionStatusFilter copy() {
            return new ConversionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter sourceDocumentId;

    private StringFilter sourceFileName;

    private StringFilter sourcePdfS3Key;

    private ImageQualityFilter imageQuality;

    private ImageFormatFilter imageFormat;

    private ConversionTypeFilter conversionType;

    private IntegerFilter startPage;

    private IntegerFilter endPage;

    private IntegerFilter totalPages;

    private ConversionStatusFilter status;

    private StringFilter errorMessage;

    private InstantFilter requestedAt;

    private InstantFilter startedAt;

    private InstantFilter completedAt;

    private LongFilter processingDuration;

    private LongFilter totalImagesSize;

    private IntegerFilter imagesGenerated;

    private IntegerFilter dpi;

    private LongFilter requestedByUserId;

    private IntegerFilter priority;

    private LongFilter generatedImagesId;

    private LongFilter batchId;

    private Boolean distinct;

    public ImagePdfConversionRequestCriteria() {}

    public ImagePdfConversionRequestCriteria(ImagePdfConversionRequestCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sourceDocumentId = other.optionalSourceDocumentId().map(LongFilter::copy).orElse(null);
        this.sourceFileName = other.optionalSourceFileName().map(StringFilter::copy).orElse(null);
        this.sourcePdfS3Key = other.optionalSourcePdfS3Key().map(StringFilter::copy).orElse(null);
        this.imageQuality = other.optionalImageQuality().map(ImageQualityFilter::copy).orElse(null);
        this.imageFormat = other.optionalImageFormat().map(ImageFormatFilter::copy).orElse(null);
        this.conversionType = other.optionalConversionType().map(ConversionTypeFilter::copy).orElse(null);
        this.startPage = other.optionalStartPage().map(IntegerFilter::copy).orElse(null);
        this.endPage = other.optionalEndPage().map(IntegerFilter::copy).orElse(null);
        this.totalPages = other.optionalTotalPages().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ConversionStatusFilter::copy).orElse(null);
        this.errorMessage = other.optionalErrorMessage().map(StringFilter::copy).orElse(null);
        this.requestedAt = other.optionalRequestedAt().map(InstantFilter::copy).orElse(null);
        this.startedAt = other.optionalStartedAt().map(InstantFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.processingDuration = other.optionalProcessingDuration().map(LongFilter::copy).orElse(null);
        this.totalImagesSize = other.optionalTotalImagesSize().map(LongFilter::copy).orElse(null);
        this.imagesGenerated = other.optionalImagesGenerated().map(IntegerFilter::copy).orElse(null);
        this.dpi = other.optionalDpi().map(IntegerFilter::copy).orElse(null);
        this.requestedByUserId = other.optionalRequestedByUserId().map(LongFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.generatedImagesId = other.optionalGeneratedImagesId().map(LongFilter::copy).orElse(null);
        this.batchId = other.optionalBatchId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImagePdfConversionRequestCriteria copy() {
        return new ImagePdfConversionRequestCriteria(this);
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

    public LongFilter getSourceDocumentId() {
        return sourceDocumentId;
    }

    public Optional<LongFilter> optionalSourceDocumentId() {
        return Optional.ofNullable(sourceDocumentId);
    }

    public LongFilter sourceDocumentId() {
        if (sourceDocumentId == null) {
            setSourceDocumentId(new LongFilter());
        }
        return sourceDocumentId;
    }

    public void setSourceDocumentId(LongFilter sourceDocumentId) {
        this.sourceDocumentId = sourceDocumentId;
    }

    public StringFilter getSourceFileName() {
        return sourceFileName;
    }

    public Optional<StringFilter> optionalSourceFileName() {
        return Optional.ofNullable(sourceFileName);
    }

    public StringFilter sourceFileName() {
        if (sourceFileName == null) {
            setSourceFileName(new StringFilter());
        }
        return sourceFileName;
    }

    public void setSourceFileName(StringFilter sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public StringFilter getSourcePdfS3Key() {
        return sourcePdfS3Key;
    }

    public Optional<StringFilter> optionalSourcePdfS3Key() {
        return Optional.ofNullable(sourcePdfS3Key);
    }

    public StringFilter sourcePdfS3Key() {
        if (sourcePdfS3Key == null) {
            setSourcePdfS3Key(new StringFilter());
        }
        return sourcePdfS3Key;
    }

    public void setSourcePdfS3Key(StringFilter sourcePdfS3Key) {
        this.sourcePdfS3Key = sourcePdfS3Key;
    }

    public ImageQualityFilter getImageQuality() {
        return imageQuality;
    }

    public Optional<ImageQualityFilter> optionalImageQuality() {
        return Optional.ofNullable(imageQuality);
    }

    public ImageQualityFilter imageQuality() {
        if (imageQuality == null) {
            setImageQuality(new ImageQualityFilter());
        }
        return imageQuality;
    }

    public void setImageQuality(ImageQualityFilter imageQuality) {
        this.imageQuality = imageQuality;
    }

    public ImageFormatFilter getImageFormat() {
        return imageFormat;
    }

    public Optional<ImageFormatFilter> optionalImageFormat() {
        return Optional.ofNullable(imageFormat);
    }

    public ImageFormatFilter imageFormat() {
        if (imageFormat == null) {
            setImageFormat(new ImageFormatFilter());
        }
        return imageFormat;
    }

    public void setImageFormat(ImageFormatFilter imageFormat) {
        this.imageFormat = imageFormat;
    }

    public ConversionTypeFilter getConversionType() {
        return conversionType;
    }

    public Optional<ConversionTypeFilter> optionalConversionType() {
        return Optional.ofNullable(conversionType);
    }

    public ConversionTypeFilter conversionType() {
        if (conversionType == null) {
            setConversionType(new ConversionTypeFilter());
        }
        return conversionType;
    }

    public void setConversionType(ConversionTypeFilter conversionType) {
        this.conversionType = conversionType;
    }

    public IntegerFilter getStartPage() {
        return startPage;
    }

    public Optional<IntegerFilter> optionalStartPage() {
        return Optional.ofNullable(startPage);
    }

    public IntegerFilter startPage() {
        if (startPage == null) {
            setStartPage(new IntegerFilter());
        }
        return startPage;
    }

    public void setStartPage(IntegerFilter startPage) {
        this.startPage = startPage;
    }

    public IntegerFilter getEndPage() {
        return endPage;
    }

    public Optional<IntegerFilter> optionalEndPage() {
        return Optional.ofNullable(endPage);
    }

    public IntegerFilter endPage() {
        if (endPage == null) {
            setEndPage(new IntegerFilter());
        }
        return endPage;
    }

    public void setEndPage(IntegerFilter endPage) {
        this.endPage = endPage;
    }

    public IntegerFilter getTotalPages() {
        return totalPages;
    }

    public Optional<IntegerFilter> optionalTotalPages() {
        return Optional.ofNullable(totalPages);
    }

    public IntegerFilter totalPages() {
        if (totalPages == null) {
            setTotalPages(new IntegerFilter());
        }
        return totalPages;
    }

    public void setTotalPages(IntegerFilter totalPages) {
        this.totalPages = totalPages;
    }

    public ConversionStatusFilter getStatus() {
        return status;
    }

    public Optional<ConversionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ConversionStatusFilter status() {
        if (status == null) {
            setStatus(new ConversionStatusFilter());
        }
        return status;
    }

    public void setStatus(ConversionStatusFilter status) {
        this.status = status;
    }

    public StringFilter getErrorMessage() {
        return errorMessage;
    }

    public Optional<StringFilter> optionalErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    public StringFilter errorMessage() {
        if (errorMessage == null) {
            setErrorMessage(new StringFilter());
        }
        return errorMessage;
    }

    public void setErrorMessage(StringFilter errorMessage) {
        this.errorMessage = errorMessage;
    }

    public InstantFilter getRequestedAt() {
        return requestedAt;
    }

    public Optional<InstantFilter> optionalRequestedAt() {
        return Optional.ofNullable(requestedAt);
    }

    public InstantFilter requestedAt() {
        if (requestedAt == null) {
            setRequestedAt(new InstantFilter());
        }
        return requestedAt;
    }

    public void setRequestedAt(InstantFilter requestedAt) {
        this.requestedAt = requestedAt;
    }

    public InstantFilter getStartedAt() {
        return startedAt;
    }

    public Optional<InstantFilter> optionalStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public InstantFilter startedAt() {
        if (startedAt == null) {
            setStartedAt(new InstantFilter());
        }
        return startedAt;
    }

    public void setStartedAt(InstantFilter startedAt) {
        this.startedAt = startedAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public LongFilter getProcessingDuration() {
        return processingDuration;
    }

    public Optional<LongFilter> optionalProcessingDuration() {
        return Optional.ofNullable(processingDuration);
    }

    public LongFilter processingDuration() {
        if (processingDuration == null) {
            setProcessingDuration(new LongFilter());
        }
        return processingDuration;
    }

    public void setProcessingDuration(LongFilter processingDuration) {
        this.processingDuration = processingDuration;
    }

    public LongFilter getTotalImagesSize() {
        return totalImagesSize;
    }

    public Optional<LongFilter> optionalTotalImagesSize() {
        return Optional.ofNullable(totalImagesSize);
    }

    public LongFilter totalImagesSize() {
        if (totalImagesSize == null) {
            setTotalImagesSize(new LongFilter());
        }
        return totalImagesSize;
    }

    public void setTotalImagesSize(LongFilter totalImagesSize) {
        this.totalImagesSize = totalImagesSize;
    }

    public IntegerFilter getImagesGenerated() {
        return imagesGenerated;
    }

    public Optional<IntegerFilter> optionalImagesGenerated() {
        return Optional.ofNullable(imagesGenerated);
    }

    public IntegerFilter imagesGenerated() {
        if (imagesGenerated == null) {
            setImagesGenerated(new IntegerFilter());
        }
        return imagesGenerated;
    }

    public void setImagesGenerated(IntegerFilter imagesGenerated) {
        this.imagesGenerated = imagesGenerated;
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

    public LongFilter getRequestedByUserId() {
        return requestedByUserId;
    }

    public Optional<LongFilter> optionalRequestedByUserId() {
        return Optional.ofNullable(requestedByUserId);
    }

    public LongFilter requestedByUserId() {
        if (requestedByUserId == null) {
            setRequestedByUserId(new LongFilter());
        }
        return requestedByUserId;
    }

    public void setRequestedByUserId(LongFilter requestedByUserId) {
        this.requestedByUserId = requestedByUserId;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
    }

    public LongFilter getGeneratedImagesId() {
        return generatedImagesId;
    }

    public Optional<LongFilter> optionalGeneratedImagesId() {
        return Optional.ofNullable(generatedImagesId);
    }

    public LongFilter generatedImagesId() {
        if (generatedImagesId == null) {
            setGeneratedImagesId(new LongFilter());
        }
        return generatedImagesId;
    }

    public void setGeneratedImagesId(LongFilter generatedImagesId) {
        this.generatedImagesId = generatedImagesId;
    }

    public LongFilter getBatchId() {
        return batchId;
    }

    public Optional<LongFilter> optionalBatchId() {
        return Optional.ofNullable(batchId);
    }

    public LongFilter batchId() {
        if (batchId == null) {
            setBatchId(new LongFilter());
        }
        return batchId;
    }

    public void setBatchId(LongFilter batchId) {
        this.batchId = batchId;
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
        final ImagePdfConversionRequestCriteria that = (ImagePdfConversionRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sourceDocumentId, that.sourceDocumentId) &&
            Objects.equals(sourceFileName, that.sourceFileName) &&
            Objects.equals(sourcePdfS3Key, that.sourcePdfS3Key) &&
            Objects.equals(imageQuality, that.imageQuality) &&
            Objects.equals(imageFormat, that.imageFormat) &&
            Objects.equals(conversionType, that.conversionType) &&
            Objects.equals(startPage, that.startPage) &&
            Objects.equals(endPage, that.endPage) &&
            Objects.equals(totalPages, that.totalPages) &&
            Objects.equals(status, that.status) &&
            Objects.equals(errorMessage, that.errorMessage) &&
            Objects.equals(requestedAt, that.requestedAt) &&
            Objects.equals(startedAt, that.startedAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(processingDuration, that.processingDuration) &&
            Objects.equals(totalImagesSize, that.totalImagesSize) &&
            Objects.equals(imagesGenerated, that.imagesGenerated) &&
            Objects.equals(dpi, that.dpi) &&
            Objects.equals(requestedByUserId, that.requestedByUserId) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(generatedImagesId, that.generatedImagesId) &&
            Objects.equals(batchId, that.batchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sourceDocumentId,
            sourceFileName,
            sourcePdfS3Key,
            imageQuality,
            imageFormat,
            conversionType,
            startPage,
            endPage,
            totalPages,
            status,
            errorMessage,
            requestedAt,
            startedAt,
            completedAt,
            processingDuration,
            totalImagesSize,
            imagesGenerated,
            dpi,
            requestedByUserId,
            priority,
            generatedImagesId,
            batchId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagePdfConversionRequestCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSourceDocumentId().map(f -> "sourceDocumentId=" + f + ", ").orElse("") +
            optionalSourceFileName().map(f -> "sourceFileName=" + f + ", ").orElse("") +
            optionalSourcePdfS3Key().map(f -> "sourcePdfS3Key=" + f + ", ").orElse("") +
            optionalImageQuality().map(f -> "imageQuality=" + f + ", ").orElse("") +
            optionalImageFormat().map(f -> "imageFormat=" + f + ", ").orElse("") +
            optionalConversionType().map(f -> "conversionType=" + f + ", ").orElse("") +
            optionalStartPage().map(f -> "startPage=" + f + ", ").orElse("") +
            optionalEndPage().map(f -> "endPage=" + f + ", ").orElse("") +
            optionalTotalPages().map(f -> "totalPages=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalErrorMessage().map(f -> "errorMessage=" + f + ", ").orElse("") +
            optionalRequestedAt().map(f -> "requestedAt=" + f + ", ").orElse("") +
            optionalStartedAt().map(f -> "startedAt=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalProcessingDuration().map(f -> "processingDuration=" + f + ", ").orElse("") +
            optionalTotalImagesSize().map(f -> "totalImagesSize=" + f + ", ").orElse("") +
            optionalImagesGenerated().map(f -> "imagesGenerated=" + f + ", ").orElse("") +
            optionalDpi().map(f -> "dpi=" + f + ", ").orElse("") +
            optionalRequestedByUserId().map(f -> "requestedByUserId=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalGeneratedImagesId().map(f -> "generatedImagesId=" + f + ", ").orElse("") +
            optionalBatchId().map(f -> "batchId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
