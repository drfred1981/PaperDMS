package fr.smartprod.paperdms.pdftoimage.service.criteria;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory} entity. This class is used
 * in {@link fr.smartprod.paperdms.pdftoimage.web.rest.ImageConversionHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-conversion-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionHistoryCriteria implements Serializable, Criteria {

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

    private LongFilter originalRequestId;

    private InstantFilter archivedAt;

    private IntegerFilter imagesCount;

    private LongFilter totalSize;

    private ConversionStatusFilter finalStatus;

    private LongFilter processingDuration;

    private Boolean distinct;

    public ImageConversionHistoryCriteria() {}

    public ImageConversionHistoryCriteria(ImageConversionHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.originalRequestId = other.optionalOriginalRequestId().map(LongFilter::copy).orElse(null);
        this.archivedAt = other.optionalArchivedAt().map(InstantFilter::copy).orElse(null);
        this.imagesCount = other.optionalImagesCount().map(IntegerFilter::copy).orElse(null);
        this.totalSize = other.optionalTotalSize().map(LongFilter::copy).orElse(null);
        this.finalStatus = other.optionalFinalStatus().map(ConversionStatusFilter::copy).orElse(null);
        this.processingDuration = other.optionalProcessingDuration().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageConversionHistoryCriteria copy() {
        return new ImageConversionHistoryCriteria(this);
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

    public LongFilter getOriginalRequestId() {
        return originalRequestId;
    }

    public Optional<LongFilter> optionalOriginalRequestId() {
        return Optional.ofNullable(originalRequestId);
    }

    public LongFilter originalRequestId() {
        if (originalRequestId == null) {
            setOriginalRequestId(new LongFilter());
        }
        return originalRequestId;
    }

    public void setOriginalRequestId(LongFilter originalRequestId) {
        this.originalRequestId = originalRequestId;
    }

    public InstantFilter getArchivedAt() {
        return archivedAt;
    }

    public Optional<InstantFilter> optionalArchivedAt() {
        return Optional.ofNullable(archivedAt);
    }

    public InstantFilter archivedAt() {
        if (archivedAt == null) {
            setArchivedAt(new InstantFilter());
        }
        return archivedAt;
    }

    public void setArchivedAt(InstantFilter archivedAt) {
        this.archivedAt = archivedAt;
    }

    public IntegerFilter getImagesCount() {
        return imagesCount;
    }

    public Optional<IntegerFilter> optionalImagesCount() {
        return Optional.ofNullable(imagesCount);
    }

    public IntegerFilter imagesCount() {
        if (imagesCount == null) {
            setImagesCount(new IntegerFilter());
        }
        return imagesCount;
    }

    public void setImagesCount(IntegerFilter imagesCount) {
        this.imagesCount = imagesCount;
    }

    public LongFilter getTotalSize() {
        return totalSize;
    }

    public Optional<LongFilter> optionalTotalSize() {
        return Optional.ofNullable(totalSize);
    }

    public LongFilter totalSize() {
        if (totalSize == null) {
            setTotalSize(new LongFilter());
        }
        return totalSize;
    }

    public void setTotalSize(LongFilter totalSize) {
        this.totalSize = totalSize;
    }

    public ConversionStatusFilter getFinalStatus() {
        return finalStatus;
    }

    public Optional<ConversionStatusFilter> optionalFinalStatus() {
        return Optional.ofNullable(finalStatus);
    }

    public ConversionStatusFilter finalStatus() {
        if (finalStatus == null) {
            setFinalStatus(new ConversionStatusFilter());
        }
        return finalStatus;
    }

    public void setFinalStatus(ConversionStatusFilter finalStatus) {
        this.finalStatus = finalStatus;
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
        final ImageConversionHistoryCriteria that = (ImageConversionHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(originalRequestId, that.originalRequestId) &&
            Objects.equals(archivedAt, that.archivedAt) &&
            Objects.equals(imagesCount, that.imagesCount) &&
            Objects.equals(totalSize, that.totalSize) &&
            Objects.equals(finalStatus, that.finalStatus) &&
            Objects.equals(processingDuration, that.processingDuration) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originalRequestId, archivedAt, imagesCount, totalSize, finalStatus, processingDuration, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOriginalRequestId().map(f -> "originalRequestId=" + f + ", ").orElse("") +
            optionalArchivedAt().map(f -> "archivedAt=" + f + ", ").orElse("") +
            optionalImagesCount().map(f -> "imagesCount=" + f + ", ").orElse("") +
            optionalTotalSize().map(f -> "totalSize=" + f + ", ").orElse("") +
            optionalFinalStatus().map(f -> "finalStatus=" + f + ", ").orElse("") +
            optionalProcessingDuration().map(f -> "processingDuration=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
