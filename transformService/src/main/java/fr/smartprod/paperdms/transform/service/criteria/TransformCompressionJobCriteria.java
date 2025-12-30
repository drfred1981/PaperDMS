package fr.smartprod.paperdms.transform.service.criteria;

import fr.smartprod.paperdms.transform.domain.enumeration.CompressionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.transform.domain.TransformCompressionJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.transform.web.rest.TransformCompressionJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transform-compression-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransformCompressionJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CompressionType
     */
    public static class CompressionTypeFilter extends Filter<CompressionType> {

        public CompressionTypeFilter() {}

        public CompressionTypeFilter(CompressionTypeFilter filter) {
            super(filter);
        }

        @Override
        public CompressionTypeFilter copy() {
            return new CompressionTypeFilter(this);
        }
    }

    /**
     * Class for filtering TransformStatus
     */
    public static class TransformStatusFilter extends Filter<TransformStatus> {

        public TransformStatusFilter() {}

        public TransformStatusFilter(TransformStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransformStatusFilter copy() {
            return new TransformStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentSha256;

    private CompressionTypeFilter compressionType;

    private IntegerFilter quality;

    private LongFilter targetSizeKb;

    private LongFilter originalSize;

    private LongFilter compressedSize;

    private DoubleFilter compressionRatio;

    private StringFilter outputS3Key;

    private StringFilter outputDocumentSha256;

    private TransformStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public TransformCompressionJobCriteria() {}

    public TransformCompressionJobCriteria(TransformCompressionJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.compressionType = other.optionalCompressionType().map(CompressionTypeFilter::copy).orElse(null);
        this.quality = other.optionalQuality().map(IntegerFilter::copy).orElse(null);
        this.targetSizeKb = other.optionalTargetSizeKb().map(LongFilter::copy).orElse(null);
        this.originalSize = other.optionalOriginalSize().map(LongFilter::copy).orElse(null);
        this.compressedSize = other.optionalCompressedSize().map(LongFilter::copy).orElse(null);
        this.compressionRatio = other.optionalCompressionRatio().map(DoubleFilter::copy).orElse(null);
        this.outputS3Key = other.optionalOutputS3Key().map(StringFilter::copy).orElse(null);
        this.outputDocumentSha256 = other.optionalOutputDocumentSha256().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransformStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransformCompressionJobCriteria copy() {
        return new TransformCompressionJobCriteria(this);
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

    public StringFilter getDocumentSha256() {
        return documentSha256;
    }

    public Optional<StringFilter> optionalDocumentSha256() {
        return Optional.ofNullable(documentSha256);
    }

    public StringFilter documentSha256() {
        if (documentSha256 == null) {
            setDocumentSha256(new StringFilter());
        }
        return documentSha256;
    }

    public void setDocumentSha256(StringFilter documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public CompressionTypeFilter getCompressionType() {
        return compressionType;
    }

    public Optional<CompressionTypeFilter> optionalCompressionType() {
        return Optional.ofNullable(compressionType);
    }

    public CompressionTypeFilter compressionType() {
        if (compressionType == null) {
            setCompressionType(new CompressionTypeFilter());
        }
        return compressionType;
    }

    public void setCompressionType(CompressionTypeFilter compressionType) {
        this.compressionType = compressionType;
    }

    public IntegerFilter getQuality() {
        return quality;
    }

    public Optional<IntegerFilter> optionalQuality() {
        return Optional.ofNullable(quality);
    }

    public IntegerFilter quality() {
        if (quality == null) {
            setQuality(new IntegerFilter());
        }
        return quality;
    }

    public void setQuality(IntegerFilter quality) {
        this.quality = quality;
    }

    public LongFilter getTargetSizeKb() {
        return targetSizeKb;
    }

    public Optional<LongFilter> optionalTargetSizeKb() {
        return Optional.ofNullable(targetSizeKb);
    }

    public LongFilter targetSizeKb() {
        if (targetSizeKb == null) {
            setTargetSizeKb(new LongFilter());
        }
        return targetSizeKb;
    }

    public void setTargetSizeKb(LongFilter targetSizeKb) {
        this.targetSizeKb = targetSizeKb;
    }

    public LongFilter getOriginalSize() {
        return originalSize;
    }

    public Optional<LongFilter> optionalOriginalSize() {
        return Optional.ofNullable(originalSize);
    }

    public LongFilter originalSize() {
        if (originalSize == null) {
            setOriginalSize(new LongFilter());
        }
        return originalSize;
    }

    public void setOriginalSize(LongFilter originalSize) {
        this.originalSize = originalSize;
    }

    public LongFilter getCompressedSize() {
        return compressedSize;
    }

    public Optional<LongFilter> optionalCompressedSize() {
        return Optional.ofNullable(compressedSize);
    }

    public LongFilter compressedSize() {
        if (compressedSize == null) {
            setCompressedSize(new LongFilter());
        }
        return compressedSize;
    }

    public void setCompressedSize(LongFilter compressedSize) {
        this.compressedSize = compressedSize;
    }

    public DoubleFilter getCompressionRatio() {
        return compressionRatio;
    }

    public Optional<DoubleFilter> optionalCompressionRatio() {
        return Optional.ofNullable(compressionRatio);
    }

    public DoubleFilter compressionRatio() {
        if (compressionRatio == null) {
            setCompressionRatio(new DoubleFilter());
        }
        return compressionRatio;
    }

    public void setCompressionRatio(DoubleFilter compressionRatio) {
        this.compressionRatio = compressionRatio;
    }

    public StringFilter getOutputS3Key() {
        return outputS3Key;
    }

    public Optional<StringFilter> optionalOutputS3Key() {
        return Optional.ofNullable(outputS3Key);
    }

    public StringFilter outputS3Key() {
        if (outputS3Key == null) {
            setOutputS3Key(new StringFilter());
        }
        return outputS3Key;
    }

    public void setOutputS3Key(StringFilter outputS3Key) {
        this.outputS3Key = outputS3Key;
    }

    public StringFilter getOutputDocumentSha256() {
        return outputDocumentSha256;
    }

    public Optional<StringFilter> optionalOutputDocumentSha256() {
        return Optional.ofNullable(outputDocumentSha256);
    }

    public StringFilter outputDocumentSha256() {
        if (outputDocumentSha256 == null) {
            setOutputDocumentSha256(new StringFilter());
        }
        return outputDocumentSha256;
    }

    public void setOutputDocumentSha256(StringFilter outputDocumentSha256) {
        this.outputDocumentSha256 = outputDocumentSha256;
    }

    public TransformStatusFilter getStatus() {
        return status;
    }

    public Optional<TransformStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TransformStatusFilter status() {
        if (status == null) {
            setStatus(new TransformStatusFilter());
        }
        return status;
    }

    public void setStatus(TransformStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
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
        final TransformCompressionJobCriteria that = (TransformCompressionJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(compressionType, that.compressionType) &&
            Objects.equals(quality, that.quality) &&
            Objects.equals(targetSizeKb, that.targetSizeKb) &&
            Objects.equals(originalSize, that.originalSize) &&
            Objects.equals(compressedSize, that.compressedSize) &&
            Objects.equals(compressionRatio, that.compressionRatio) &&
            Objects.equals(outputS3Key, that.outputS3Key) &&
            Objects.equals(outputDocumentSha256, that.outputDocumentSha256) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentSha256,
            compressionType,
            quality,
            targetSizeKb,
            originalSize,
            compressedSize,
            compressionRatio,
            outputS3Key,
            outputDocumentSha256,
            status,
            startDate,
            endDate,
            createdBy,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransformCompressionJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalCompressionType().map(f -> "compressionType=" + f + ", ").orElse("") +
            optionalQuality().map(f -> "quality=" + f + ", ").orElse("") +
            optionalTargetSizeKb().map(f -> "targetSizeKb=" + f + ", ").orElse("") +
            optionalOriginalSize().map(f -> "originalSize=" + f + ", ").orElse("") +
            optionalCompressedSize().map(f -> "compressedSize=" + f + ", ").orElse("") +
            optionalCompressionRatio().map(f -> "compressionRatio=" + f + ", ").orElse("") +
            optionalOutputS3Key().map(f -> "outputS3Key=" + f + ", ").orElse("") +
            optionalOutputDocumentSha256().map(f -> "outputDocumentSha256=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
