package fr.smartprod.paperdms.transform.service.criteria;

import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkPosition;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.transform.domain.WatermarkJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.transform.web.rest.WatermarkJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /watermark-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WatermarkJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WatermarkType
     */
    public static class WatermarkTypeFilter extends Filter<WatermarkType> {

        public WatermarkTypeFilter() {}

        public WatermarkTypeFilter(WatermarkTypeFilter filter) {
            super(filter);
        }

        @Override
        public WatermarkTypeFilter copy() {
            return new WatermarkTypeFilter(this);
        }
    }

    /**
     * Class for filtering WatermarkPosition
     */
    public static class WatermarkPositionFilter extends Filter<WatermarkPosition> {

        public WatermarkPositionFilter() {}

        public WatermarkPositionFilter(WatermarkPositionFilter filter) {
            super(filter);
        }

        @Override
        public WatermarkPositionFilter copy() {
            return new WatermarkPositionFilter(this);
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

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private WatermarkTypeFilter watermarkType;

    private StringFilter watermarkText;

    private StringFilter watermarkImageS3Key;

    private WatermarkPositionFilter position;

    private IntegerFilter opacity;

    private IntegerFilter fontSize;

    private StringFilter color;

    private IntegerFilter rotation;

    private BooleanFilter tiled;

    private StringFilter outputS3Key;

    private LongFilter outputDocumentId;

    private TransformStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public WatermarkJobCriteria() {}

    public WatermarkJobCriteria(WatermarkJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.watermarkType = other.optionalWatermarkType().map(WatermarkTypeFilter::copy).orElse(null);
        this.watermarkText = other.optionalWatermarkText().map(StringFilter::copy).orElse(null);
        this.watermarkImageS3Key = other.optionalWatermarkImageS3Key().map(StringFilter::copy).orElse(null);
        this.position = other.optionalPosition().map(WatermarkPositionFilter::copy).orElse(null);
        this.opacity = other.optionalOpacity().map(IntegerFilter::copy).orElse(null);
        this.fontSize = other.optionalFontSize().map(IntegerFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.rotation = other.optionalRotation().map(IntegerFilter::copy).orElse(null);
        this.tiled = other.optionalTiled().map(BooleanFilter::copy).orElse(null);
        this.outputS3Key = other.optionalOutputS3Key().map(StringFilter::copy).orElse(null);
        this.outputDocumentId = other.optionalOutputDocumentId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransformStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WatermarkJobCriteria copy() {
        return new WatermarkJobCriteria(this);
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

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public WatermarkTypeFilter getWatermarkType() {
        return watermarkType;
    }

    public Optional<WatermarkTypeFilter> optionalWatermarkType() {
        return Optional.ofNullable(watermarkType);
    }

    public WatermarkTypeFilter watermarkType() {
        if (watermarkType == null) {
            setWatermarkType(new WatermarkTypeFilter());
        }
        return watermarkType;
    }

    public void setWatermarkType(WatermarkTypeFilter watermarkType) {
        this.watermarkType = watermarkType;
    }

    public StringFilter getWatermarkText() {
        return watermarkText;
    }

    public Optional<StringFilter> optionalWatermarkText() {
        return Optional.ofNullable(watermarkText);
    }

    public StringFilter watermarkText() {
        if (watermarkText == null) {
            setWatermarkText(new StringFilter());
        }
        return watermarkText;
    }

    public void setWatermarkText(StringFilter watermarkText) {
        this.watermarkText = watermarkText;
    }

    public StringFilter getWatermarkImageS3Key() {
        return watermarkImageS3Key;
    }

    public Optional<StringFilter> optionalWatermarkImageS3Key() {
        return Optional.ofNullable(watermarkImageS3Key);
    }

    public StringFilter watermarkImageS3Key() {
        if (watermarkImageS3Key == null) {
            setWatermarkImageS3Key(new StringFilter());
        }
        return watermarkImageS3Key;
    }

    public void setWatermarkImageS3Key(StringFilter watermarkImageS3Key) {
        this.watermarkImageS3Key = watermarkImageS3Key;
    }

    public WatermarkPositionFilter getPosition() {
        return position;
    }

    public Optional<WatermarkPositionFilter> optionalPosition() {
        return Optional.ofNullable(position);
    }

    public WatermarkPositionFilter position() {
        if (position == null) {
            setPosition(new WatermarkPositionFilter());
        }
        return position;
    }

    public void setPosition(WatermarkPositionFilter position) {
        this.position = position;
    }

    public IntegerFilter getOpacity() {
        return opacity;
    }

    public Optional<IntegerFilter> optionalOpacity() {
        return Optional.ofNullable(opacity);
    }

    public IntegerFilter opacity() {
        if (opacity == null) {
            setOpacity(new IntegerFilter());
        }
        return opacity;
    }

    public void setOpacity(IntegerFilter opacity) {
        this.opacity = opacity;
    }

    public IntegerFilter getFontSize() {
        return fontSize;
    }

    public Optional<IntegerFilter> optionalFontSize() {
        return Optional.ofNullable(fontSize);
    }

    public IntegerFilter fontSize() {
        if (fontSize == null) {
            setFontSize(new IntegerFilter());
        }
        return fontSize;
    }

    public void setFontSize(IntegerFilter fontSize) {
        this.fontSize = fontSize;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public IntegerFilter getRotation() {
        return rotation;
    }

    public Optional<IntegerFilter> optionalRotation() {
        return Optional.ofNullable(rotation);
    }

    public IntegerFilter rotation() {
        if (rotation == null) {
            setRotation(new IntegerFilter());
        }
        return rotation;
    }

    public void setRotation(IntegerFilter rotation) {
        this.rotation = rotation;
    }

    public BooleanFilter getTiled() {
        return tiled;
    }

    public Optional<BooleanFilter> optionalTiled() {
        return Optional.ofNullable(tiled);
    }

    public BooleanFilter tiled() {
        if (tiled == null) {
            setTiled(new BooleanFilter());
        }
        return tiled;
    }

    public void setTiled(BooleanFilter tiled) {
        this.tiled = tiled;
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

    public LongFilter getOutputDocumentId() {
        return outputDocumentId;
    }

    public Optional<LongFilter> optionalOutputDocumentId() {
        return Optional.ofNullable(outputDocumentId);
    }

    public LongFilter outputDocumentId() {
        if (outputDocumentId == null) {
            setOutputDocumentId(new LongFilter());
        }
        return outputDocumentId;
    }

    public void setOutputDocumentId(LongFilter outputDocumentId) {
        this.outputDocumentId = outputDocumentId;
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
        final WatermarkJobCriteria that = (WatermarkJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(watermarkType, that.watermarkType) &&
            Objects.equals(watermarkText, that.watermarkText) &&
            Objects.equals(watermarkImageS3Key, that.watermarkImageS3Key) &&
            Objects.equals(position, that.position) &&
            Objects.equals(opacity, that.opacity) &&
            Objects.equals(fontSize, that.fontSize) &&
            Objects.equals(color, that.color) &&
            Objects.equals(rotation, that.rotation) &&
            Objects.equals(tiled, that.tiled) &&
            Objects.equals(outputS3Key, that.outputS3Key) &&
            Objects.equals(outputDocumentId, that.outputDocumentId) &&
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
            documentId,
            watermarkType,
            watermarkText,
            watermarkImageS3Key,
            position,
            opacity,
            fontSize,
            color,
            rotation,
            tiled,
            outputS3Key,
            outputDocumentId,
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
        return "WatermarkJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalWatermarkType().map(f -> "watermarkType=" + f + ", ").orElse("") +
            optionalWatermarkText().map(f -> "watermarkText=" + f + ", ").orElse("") +
            optionalWatermarkImageS3Key().map(f -> "watermarkImageS3Key=" + f + ", ").orElse("") +
            optionalPosition().map(f -> "position=" + f + ", ").orElse("") +
            optionalOpacity().map(f -> "opacity=" + f + ", ").orElse("") +
            optionalFontSize().map(f -> "fontSize=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalRotation().map(f -> "rotation=" + f + ", ").orElse("") +
            optionalTiled().map(f -> "tiled=" + f + ", ").orElse("") +
            optionalOutputS3Key().map(f -> "outputS3Key=" + f + ", ").orElse("") +
            optionalOutputDocumentId().map(f -> "outputDocumentId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
