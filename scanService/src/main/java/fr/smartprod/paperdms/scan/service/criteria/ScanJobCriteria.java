package fr.smartprod.paperdms.scan.service.criteria;

import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.scan.domain.ScanJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.scan.web.rest.ScanJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scan-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScanJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ScanStatus
     */
    public static class ScanStatusFilter extends Filter<ScanStatus> {

        public ScanStatusFilter() {}

        public ScanStatusFilter(ScanStatusFilter filter) {
            super(filter);
        }

        @Override
        public ScanStatusFilter copy() {
            return new ScanStatusFilter(this);
        }
    }

    /**
     * Class for filtering ColorMode
     */
    public static class ColorModeFilter extends Filter<ColorMode> {

        public ColorModeFilter() {}

        public ColorModeFilter(ColorModeFilter filter) {
            super(filter);
        }

        @Override
        public ColorModeFilter copy() {
            return new ColorModeFilter(this);
        }
    }

    /**
     * Class for filtering ScanFormat
     */
    public static class ScanFormatFilter extends Filter<ScanFormat> {

        public ScanFormatFilter() {}

        public ScanFormatFilter(ScanFormatFilter filter) {
            super(filter);
        }

        @Override
        public ScanFormatFilter copy() {
            return new ScanFormatFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter scannerConfigId;

    private LongFilter batchId;

    private LongFilter documentTypeId;

    private LongFilter folderId;

    private IntegerFilter pageCount;

    private ScanStatusFilter status;

    private ColorModeFilter colorMode;

    private IntegerFilter resolution;

    private ScanFormatFilter fileFormat;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter scannerConfigId;

    private LongFilter batchId;

    private Boolean distinct;

    public ScanJobCriteria() {}

    public ScanJobCriteria(ScanJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.scannerConfigId = other.optionalScannerConfigId().map(LongFilter::copy).orElse(null);
        this.batchId = other.optionalBatchId().map(LongFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.folderId = other.optionalFolderId().map(LongFilter::copy).orElse(null);
        this.pageCount = other.optionalPageCount().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ScanStatusFilter::copy).orElse(null);
        this.colorMode = other.optionalColorMode().map(ColorModeFilter::copy).orElse(null);
        this.resolution = other.optionalResolution().map(IntegerFilter::copy).orElse(null);
        this.fileFormat = other.optionalFileFormat().map(ScanFormatFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.scannerConfigId = other.optionalScannerConfigId().map(LongFilter::copy).orElse(null);
        this.batchId = other.optionalBatchId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ScanJobCriteria copy() {
        return new ScanJobCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getScannerConfigId() {
        return scannerConfigId;
    }

    public Optional<LongFilter> optionalScannerConfigId() {
        return Optional.ofNullable(scannerConfigId);
    }

    public LongFilter scannerConfigId() {
        if (scannerConfigId == null) {
            setScannerConfigId(new LongFilter());
        }
        return scannerConfigId;
    }

    public void setScannerConfigId(LongFilter scannerConfigId) {
        this.scannerConfigId = scannerConfigId;
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

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public Optional<LongFilter> optionalDocumentTypeId() {
        return Optional.ofNullable(documentTypeId);
    }

    public LongFilter documentTypeId() {
        if (documentTypeId == null) {
            setDocumentTypeId(new LongFilter());
        }
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public LongFilter getFolderId() {
        return folderId;
    }

    public Optional<LongFilter> optionalFolderId() {
        return Optional.ofNullable(folderId);
    }

    public LongFilter folderId() {
        if (folderId == null) {
            setFolderId(new LongFilter());
        }
        return folderId;
    }

    public void setFolderId(LongFilter folderId) {
        this.folderId = folderId;
    }

    public IntegerFilter getPageCount() {
        return pageCount;
    }

    public Optional<IntegerFilter> optionalPageCount() {
        return Optional.ofNullable(pageCount);
    }

    public IntegerFilter pageCount() {
        if (pageCount == null) {
            setPageCount(new IntegerFilter());
        }
        return pageCount;
    }

    public void setPageCount(IntegerFilter pageCount) {
        this.pageCount = pageCount;
    }

    public ScanStatusFilter getStatus() {
        return status;
    }

    public Optional<ScanStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ScanStatusFilter status() {
        if (status == null) {
            setStatus(new ScanStatusFilter());
        }
        return status;
    }

    public void setStatus(ScanStatusFilter status) {
        this.status = status;
    }

    public ColorModeFilter getColorMode() {
        return colorMode;
    }

    public Optional<ColorModeFilter> optionalColorMode() {
        return Optional.ofNullable(colorMode);
    }

    public ColorModeFilter colorMode() {
        if (colorMode == null) {
            setColorMode(new ColorModeFilter());
        }
        return colorMode;
    }

    public void setColorMode(ColorModeFilter colorMode) {
        this.colorMode = colorMode;
    }

    public IntegerFilter getResolution() {
        return resolution;
    }

    public Optional<IntegerFilter> optionalResolution() {
        return Optional.ofNullable(resolution);
    }

    public IntegerFilter resolution() {
        if (resolution == null) {
            setResolution(new IntegerFilter());
        }
        return resolution;
    }

    public void setResolution(IntegerFilter resolution) {
        this.resolution = resolution;
    }

    public ScanFormatFilter getFileFormat() {
        return fileFormat;
    }

    public Optional<ScanFormatFilter> optionalFileFormat() {
        return Optional.ofNullable(fileFormat);
    }

    public ScanFormatFilter fileFormat() {
        if (fileFormat == null) {
            setFileFormat(new ScanFormatFilter());
        }
        return fileFormat;
    }

    public void setFileFormat(ScanFormatFilter fileFormat) {
        this.fileFormat = fileFormat;
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

    public LongFilter getScannerConfigId() {
        return scannerConfigId;
    }

    public Optional<LongFilter> optionalScannerConfigId() {
        return Optional.ofNullable(scannerConfigId);
    }

    public LongFilter scannerConfigId() {
        if (scannerConfigId == null) {
            setScannerConfigId(new LongFilter());
        }
        return scannerConfigId;
    }

    public void setScannerConfigId(LongFilter scannerConfigId) {
        this.scannerConfigId = scannerConfigId;
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
        final ScanJobCriteria that = (ScanJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(scannerConfigId, that.scannerConfigId) &&
            Objects.equals(batchId, that.batchId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(folderId, that.folderId) &&
            Objects.equals(pageCount, that.pageCount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(colorMode, that.colorMode) &&
            Objects.equals(resolution, that.resolution) &&
            Objects.equals(fileFormat, that.fileFormat) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(scannerConfigId, that.scannerConfigId) &&
            Objects.equals(batchId, that.batchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            scannerConfigId,
            batchId,
            documentTypeId,
            folderId,
            pageCount,
            status,
            colorMode,
            resolution,
            fileFormat,
            startDate,
            endDate,
            createdBy,
            createdDate,
            scannerConfigId,
            batchId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScanJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalScannerConfigId().map(f -> "scannerConfigId=" + f + ", ").orElse("") +
            optionalBatchId().map(f -> "batchId=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalFolderId().map(f -> "folderId=" + f + ", ").orElse("") +
            optionalPageCount().map(f -> "pageCount=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalColorMode().map(f -> "colorMode=" + f + ", ").orElse("") +
            optionalResolution().map(f -> "resolution=" + f + ", ").orElse("") +
            optionalFileFormat().map(f -> "fileFormat=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalScannerConfigId().map(f -> "scannerConfigId=" + f + ", ").orElse("") +
            optionalBatchId().map(f -> "batchId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
