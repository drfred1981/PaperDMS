package fr.smartprod.paperdms.export.service.criteria;

import fr.smartprod.paperdms.export.domain.enumeration.ExportFormat;
import fr.smartprod.paperdms.export.domain.enumeration.ExportStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.export.domain.ExportJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.export.web.rest.ExportJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /export-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ExportFormat
     */
    public static class ExportFormatFilter extends Filter<ExportFormat> {

        public ExportFormatFilter() {}

        public ExportFormatFilter(ExportFormatFilter filter) {
            super(filter);
        }

        @Override
        public ExportFormatFilter copy() {
            return new ExportFormatFilter(this);
        }
    }

    /**
     * Class for filtering ExportStatus
     */
    public static class ExportStatusFilter extends Filter<ExportStatus> {

        public ExportStatusFilter() {}

        public ExportStatusFilter(ExportStatusFilter filter) {
            super(filter);
        }

        @Override
        public ExportStatusFilter copy() {
            return new ExportStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ExportFormatFilter exportFormat;

    private BooleanFilter includeMetadata;

    private BooleanFilter includeVersions;

    private BooleanFilter includeComments;

    private BooleanFilter includeAuditTrail;

    private StringFilter s3ExportKey;

    private LongFilter exportSize;

    private IntegerFilter documentCount;

    private IntegerFilter filesGenerated;

    private ExportStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter exportResultsId;

    private LongFilter exportPatternId;

    private Boolean distinct;

    public ExportJobCriteria() {}

    public ExportJobCriteria(ExportJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.exportFormat = other.optionalExportFormat().map(ExportFormatFilter::copy).orElse(null);
        this.includeMetadata = other.optionalIncludeMetadata().map(BooleanFilter::copy).orElse(null);
        this.includeVersions = other.optionalIncludeVersions().map(BooleanFilter::copy).orElse(null);
        this.includeComments = other.optionalIncludeComments().map(BooleanFilter::copy).orElse(null);
        this.includeAuditTrail = other.optionalIncludeAuditTrail().map(BooleanFilter::copy).orElse(null);
        this.s3ExportKey = other.optionals3ExportKey().map(StringFilter::copy).orElse(null);
        this.exportSize = other.optionalExportSize().map(LongFilter::copy).orElse(null);
        this.documentCount = other.optionalDocumentCount().map(IntegerFilter::copy).orElse(null);
        this.filesGenerated = other.optionalFilesGenerated().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ExportStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.exportResultsId = other.optionalExportResultsId().map(LongFilter::copy).orElse(null);
        this.exportPatternId = other.optionalExportPatternId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExportJobCriteria copy() {
        return new ExportJobCriteria(this);
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

    public ExportFormatFilter getExportFormat() {
        return exportFormat;
    }

    public Optional<ExportFormatFilter> optionalExportFormat() {
        return Optional.ofNullable(exportFormat);
    }

    public ExportFormatFilter exportFormat() {
        if (exportFormat == null) {
            setExportFormat(new ExportFormatFilter());
        }
        return exportFormat;
    }

    public void setExportFormat(ExportFormatFilter exportFormat) {
        this.exportFormat = exportFormat;
    }

    public BooleanFilter getIncludeMetadata() {
        return includeMetadata;
    }

    public Optional<BooleanFilter> optionalIncludeMetadata() {
        return Optional.ofNullable(includeMetadata);
    }

    public BooleanFilter includeMetadata() {
        if (includeMetadata == null) {
            setIncludeMetadata(new BooleanFilter());
        }
        return includeMetadata;
    }

    public void setIncludeMetadata(BooleanFilter includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public BooleanFilter getIncludeVersions() {
        return includeVersions;
    }

    public Optional<BooleanFilter> optionalIncludeVersions() {
        return Optional.ofNullable(includeVersions);
    }

    public BooleanFilter includeVersions() {
        if (includeVersions == null) {
            setIncludeVersions(new BooleanFilter());
        }
        return includeVersions;
    }

    public void setIncludeVersions(BooleanFilter includeVersions) {
        this.includeVersions = includeVersions;
    }

    public BooleanFilter getIncludeComments() {
        return includeComments;
    }

    public Optional<BooleanFilter> optionalIncludeComments() {
        return Optional.ofNullable(includeComments);
    }

    public BooleanFilter includeComments() {
        if (includeComments == null) {
            setIncludeComments(new BooleanFilter());
        }
        return includeComments;
    }

    public void setIncludeComments(BooleanFilter includeComments) {
        this.includeComments = includeComments;
    }

    public BooleanFilter getIncludeAuditTrail() {
        return includeAuditTrail;
    }

    public Optional<BooleanFilter> optionalIncludeAuditTrail() {
        return Optional.ofNullable(includeAuditTrail);
    }

    public BooleanFilter includeAuditTrail() {
        if (includeAuditTrail == null) {
            setIncludeAuditTrail(new BooleanFilter());
        }
        return includeAuditTrail;
    }

    public void setIncludeAuditTrail(BooleanFilter includeAuditTrail) {
        this.includeAuditTrail = includeAuditTrail;
    }

    public StringFilter gets3ExportKey() {
        return s3ExportKey;
    }

    public Optional<StringFilter> optionals3ExportKey() {
        return Optional.ofNullable(s3ExportKey);
    }

    public StringFilter s3ExportKey() {
        if (s3ExportKey == null) {
            sets3ExportKey(new StringFilter());
        }
        return s3ExportKey;
    }

    public void sets3ExportKey(StringFilter s3ExportKey) {
        this.s3ExportKey = s3ExportKey;
    }

    public LongFilter getExportSize() {
        return exportSize;
    }

    public Optional<LongFilter> optionalExportSize() {
        return Optional.ofNullable(exportSize);
    }

    public LongFilter exportSize() {
        if (exportSize == null) {
            setExportSize(new LongFilter());
        }
        return exportSize;
    }

    public void setExportSize(LongFilter exportSize) {
        this.exportSize = exportSize;
    }

    public IntegerFilter getDocumentCount() {
        return documentCount;
    }

    public Optional<IntegerFilter> optionalDocumentCount() {
        return Optional.ofNullable(documentCount);
    }

    public IntegerFilter documentCount() {
        if (documentCount == null) {
            setDocumentCount(new IntegerFilter());
        }
        return documentCount;
    }

    public void setDocumentCount(IntegerFilter documentCount) {
        this.documentCount = documentCount;
    }

    public IntegerFilter getFilesGenerated() {
        return filesGenerated;
    }

    public Optional<IntegerFilter> optionalFilesGenerated() {
        return Optional.ofNullable(filesGenerated);
    }

    public IntegerFilter filesGenerated() {
        if (filesGenerated == null) {
            setFilesGenerated(new IntegerFilter());
        }
        return filesGenerated;
    }

    public void setFilesGenerated(IntegerFilter filesGenerated) {
        this.filesGenerated = filesGenerated;
    }

    public ExportStatusFilter getStatus() {
        return status;
    }

    public Optional<ExportStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ExportStatusFilter status() {
        if (status == null) {
            setStatus(new ExportStatusFilter());
        }
        return status;
    }

    public void setStatus(ExportStatusFilter status) {
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

    public LongFilter getExportResultsId() {
        return exportResultsId;
    }

    public Optional<LongFilter> optionalExportResultsId() {
        return Optional.ofNullable(exportResultsId);
    }

    public LongFilter exportResultsId() {
        if (exportResultsId == null) {
            setExportResultsId(new LongFilter());
        }
        return exportResultsId;
    }

    public void setExportResultsId(LongFilter exportResultsId) {
        this.exportResultsId = exportResultsId;
    }

    public LongFilter getExportPatternId() {
        return exportPatternId;
    }

    public Optional<LongFilter> optionalExportPatternId() {
        return Optional.ofNullable(exportPatternId);
    }

    public LongFilter exportPatternId() {
        if (exportPatternId == null) {
            setExportPatternId(new LongFilter());
        }
        return exportPatternId;
    }

    public void setExportPatternId(LongFilter exportPatternId) {
        this.exportPatternId = exportPatternId;
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
        final ExportJobCriteria that = (ExportJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(exportFormat, that.exportFormat) &&
            Objects.equals(includeMetadata, that.includeMetadata) &&
            Objects.equals(includeVersions, that.includeVersions) &&
            Objects.equals(includeComments, that.includeComments) &&
            Objects.equals(includeAuditTrail, that.includeAuditTrail) &&
            Objects.equals(s3ExportKey, that.s3ExportKey) &&
            Objects.equals(exportSize, that.exportSize) &&
            Objects.equals(documentCount, that.documentCount) &&
            Objects.equals(filesGenerated, that.filesGenerated) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(exportResultsId, that.exportResultsId) &&
            Objects.equals(exportPatternId, that.exportPatternId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            exportFormat,
            includeMetadata,
            includeVersions,
            includeComments,
            includeAuditTrail,
            s3ExportKey,
            exportSize,
            documentCount,
            filesGenerated,
            status,
            startDate,
            endDate,
            createdBy,
            createdDate,
            exportResultsId,
            exportPatternId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalExportFormat().map(f -> "exportFormat=" + f + ", ").orElse("") +
            optionalIncludeMetadata().map(f -> "includeMetadata=" + f + ", ").orElse("") +
            optionalIncludeVersions().map(f -> "includeVersions=" + f + ", ").orElse("") +
            optionalIncludeComments().map(f -> "includeComments=" + f + ", ").orElse("") +
            optionalIncludeAuditTrail().map(f -> "includeAuditTrail=" + f + ", ").orElse("") +
            optionals3ExportKey().map(f -> "s3ExportKey=" + f + ", ").orElse("") +
            optionalExportSize().map(f -> "exportSize=" + f + ", ").orElse("") +
            optionalDocumentCount().map(f -> "documentCount=" + f + ", ").orElse("") +
            optionalFilesGenerated().map(f -> "filesGenerated=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalExportResultsId().map(f -> "exportResultsId=" + f + ", ").orElse("") +
            optionalExportPatternId().map(f -> "exportPatternId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
