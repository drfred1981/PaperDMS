package fr.smartprod.paperdms.pdftoimage.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics} entity. This class is used
 * in {@link fr.smartprod.paperdms.pdftoimage.web.rest.ImageConversionStatisticsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /image-conversion-statistics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionStatisticsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter statisticsDate;

    private IntegerFilter totalConversions;

    private IntegerFilter successfulConversions;

    private IntegerFilter failedConversions;

    private IntegerFilter totalPagesConverted;

    private IntegerFilter totalImagesGenerated;

    private LongFilter totalImagesSize;

    private LongFilter averageProcessingDuration;

    private LongFilter maxProcessingDuration;

    private LongFilter minProcessingDuration;

    private InstantFilter calculatedAt;

    private Boolean distinct;

    public ImageConversionStatisticsCriteria() {}

    public ImageConversionStatisticsCriteria(ImageConversionStatisticsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.statisticsDate = other.optionalStatisticsDate().map(LocalDateFilter::copy).orElse(null);
        this.totalConversions = other.optionalTotalConversions().map(IntegerFilter::copy).orElse(null);
        this.successfulConversions = other.optionalSuccessfulConversions().map(IntegerFilter::copy).orElse(null);
        this.failedConversions = other.optionalFailedConversions().map(IntegerFilter::copy).orElse(null);
        this.totalPagesConverted = other.optionalTotalPagesConverted().map(IntegerFilter::copy).orElse(null);
        this.totalImagesGenerated = other.optionalTotalImagesGenerated().map(IntegerFilter::copy).orElse(null);
        this.totalImagesSize = other.optionalTotalImagesSize().map(LongFilter::copy).orElse(null);
        this.averageProcessingDuration = other.optionalAverageProcessingDuration().map(LongFilter::copy).orElse(null);
        this.maxProcessingDuration = other.optionalMaxProcessingDuration().map(LongFilter::copy).orElse(null);
        this.minProcessingDuration = other.optionalMinProcessingDuration().map(LongFilter::copy).orElse(null);
        this.calculatedAt = other.optionalCalculatedAt().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageConversionStatisticsCriteria copy() {
        return new ImageConversionStatisticsCriteria(this);
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

    public LocalDateFilter getStatisticsDate() {
        return statisticsDate;
    }

    public Optional<LocalDateFilter> optionalStatisticsDate() {
        return Optional.ofNullable(statisticsDate);
    }

    public LocalDateFilter statisticsDate() {
        if (statisticsDate == null) {
            setStatisticsDate(new LocalDateFilter());
        }
        return statisticsDate;
    }

    public void setStatisticsDate(LocalDateFilter statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public IntegerFilter getTotalConversions() {
        return totalConversions;
    }

    public Optional<IntegerFilter> optionalTotalConversions() {
        return Optional.ofNullable(totalConversions);
    }

    public IntegerFilter totalConversions() {
        if (totalConversions == null) {
            setTotalConversions(new IntegerFilter());
        }
        return totalConversions;
    }

    public void setTotalConversions(IntegerFilter totalConversions) {
        this.totalConversions = totalConversions;
    }

    public IntegerFilter getSuccessfulConversions() {
        return successfulConversions;
    }

    public Optional<IntegerFilter> optionalSuccessfulConversions() {
        return Optional.ofNullable(successfulConversions);
    }

    public IntegerFilter successfulConversions() {
        if (successfulConversions == null) {
            setSuccessfulConversions(new IntegerFilter());
        }
        return successfulConversions;
    }

    public void setSuccessfulConversions(IntegerFilter successfulConversions) {
        this.successfulConversions = successfulConversions;
    }

    public IntegerFilter getFailedConversions() {
        return failedConversions;
    }

    public Optional<IntegerFilter> optionalFailedConversions() {
        return Optional.ofNullable(failedConversions);
    }

    public IntegerFilter failedConversions() {
        if (failedConversions == null) {
            setFailedConversions(new IntegerFilter());
        }
        return failedConversions;
    }

    public void setFailedConversions(IntegerFilter failedConversions) {
        this.failedConversions = failedConversions;
    }

    public IntegerFilter getTotalPagesConverted() {
        return totalPagesConverted;
    }

    public Optional<IntegerFilter> optionalTotalPagesConverted() {
        return Optional.ofNullable(totalPagesConverted);
    }

    public IntegerFilter totalPagesConverted() {
        if (totalPagesConverted == null) {
            setTotalPagesConverted(new IntegerFilter());
        }
        return totalPagesConverted;
    }

    public void setTotalPagesConverted(IntegerFilter totalPagesConverted) {
        this.totalPagesConverted = totalPagesConverted;
    }

    public IntegerFilter getTotalImagesGenerated() {
        return totalImagesGenerated;
    }

    public Optional<IntegerFilter> optionalTotalImagesGenerated() {
        return Optional.ofNullable(totalImagesGenerated);
    }

    public IntegerFilter totalImagesGenerated() {
        if (totalImagesGenerated == null) {
            setTotalImagesGenerated(new IntegerFilter());
        }
        return totalImagesGenerated;
    }

    public void setTotalImagesGenerated(IntegerFilter totalImagesGenerated) {
        this.totalImagesGenerated = totalImagesGenerated;
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

    public LongFilter getAverageProcessingDuration() {
        return averageProcessingDuration;
    }

    public Optional<LongFilter> optionalAverageProcessingDuration() {
        return Optional.ofNullable(averageProcessingDuration);
    }

    public LongFilter averageProcessingDuration() {
        if (averageProcessingDuration == null) {
            setAverageProcessingDuration(new LongFilter());
        }
        return averageProcessingDuration;
    }

    public void setAverageProcessingDuration(LongFilter averageProcessingDuration) {
        this.averageProcessingDuration = averageProcessingDuration;
    }

    public LongFilter getMaxProcessingDuration() {
        return maxProcessingDuration;
    }

    public Optional<LongFilter> optionalMaxProcessingDuration() {
        return Optional.ofNullable(maxProcessingDuration);
    }

    public LongFilter maxProcessingDuration() {
        if (maxProcessingDuration == null) {
            setMaxProcessingDuration(new LongFilter());
        }
        return maxProcessingDuration;
    }

    public void setMaxProcessingDuration(LongFilter maxProcessingDuration) {
        this.maxProcessingDuration = maxProcessingDuration;
    }

    public LongFilter getMinProcessingDuration() {
        return minProcessingDuration;
    }

    public Optional<LongFilter> optionalMinProcessingDuration() {
        return Optional.ofNullable(minProcessingDuration);
    }

    public LongFilter minProcessingDuration() {
        if (minProcessingDuration == null) {
            setMinProcessingDuration(new LongFilter());
        }
        return minProcessingDuration;
    }

    public void setMinProcessingDuration(LongFilter minProcessingDuration) {
        this.minProcessingDuration = minProcessingDuration;
    }

    public InstantFilter getCalculatedAt() {
        return calculatedAt;
    }

    public Optional<InstantFilter> optionalCalculatedAt() {
        return Optional.ofNullable(calculatedAt);
    }

    public InstantFilter calculatedAt() {
        if (calculatedAt == null) {
            setCalculatedAt(new InstantFilter());
        }
        return calculatedAt;
    }

    public void setCalculatedAt(InstantFilter calculatedAt) {
        this.calculatedAt = calculatedAt;
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
        final ImageConversionStatisticsCriteria that = (ImageConversionStatisticsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(statisticsDate, that.statisticsDate) &&
            Objects.equals(totalConversions, that.totalConversions) &&
            Objects.equals(successfulConversions, that.successfulConversions) &&
            Objects.equals(failedConversions, that.failedConversions) &&
            Objects.equals(totalPagesConverted, that.totalPagesConverted) &&
            Objects.equals(totalImagesGenerated, that.totalImagesGenerated) &&
            Objects.equals(totalImagesSize, that.totalImagesSize) &&
            Objects.equals(averageProcessingDuration, that.averageProcessingDuration) &&
            Objects.equals(maxProcessingDuration, that.maxProcessingDuration) &&
            Objects.equals(minProcessingDuration, that.minProcessingDuration) &&
            Objects.equals(calculatedAt, that.calculatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            statisticsDate,
            totalConversions,
            successfulConversions,
            failedConversions,
            totalPagesConverted,
            totalImagesGenerated,
            totalImagesSize,
            averageProcessingDuration,
            maxProcessingDuration,
            minProcessingDuration,
            calculatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionStatisticsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatisticsDate().map(f -> "statisticsDate=" + f + ", ").orElse("") +
            optionalTotalConversions().map(f -> "totalConversions=" + f + ", ").orElse("") +
            optionalSuccessfulConversions().map(f -> "successfulConversions=" + f + ", ").orElse("") +
            optionalFailedConversions().map(f -> "failedConversions=" + f + ", ").orElse("") +
            optionalTotalPagesConverted().map(f -> "totalPagesConverted=" + f + ", ").orElse("") +
            optionalTotalImagesGenerated().map(f -> "totalImagesGenerated=" + f + ", ").orElse("") +
            optionalTotalImagesSize().map(f -> "totalImagesSize=" + f + ", ").orElse("") +
            optionalAverageProcessingDuration().map(f -> "averageProcessingDuration=" + f + ", ").orElse("") +
            optionalMaxProcessingDuration().map(f -> "maxProcessingDuration=" + f + ", ").orElse("") +
            optionalMinProcessingDuration().map(f -> "minProcessingDuration=" + f + ", ").orElse("") +
            optionalCalculatedAt().map(f -> "calculatedAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
