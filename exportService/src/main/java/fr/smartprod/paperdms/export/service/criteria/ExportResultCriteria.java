package fr.smartprod.paperdms.export.service.criteria;

import fr.smartprod.paperdms.export.domain.enumeration.ExportResultStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.export.domain.ExportResult} entity. This class is used
 * in {@link fr.smartprod.paperdms.export.web.rest.ExportResultResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /export-results?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportResultCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ExportResultStatus
     */
    public static class ExportResultStatusFilter extends Filter<ExportResultStatus> {

        public ExportResultStatusFilter() {}

        public ExportResultStatusFilter(ExportResultStatusFilter filter) {
            super(filter);
        }

        @Override
        public ExportResultStatusFilter copy() {
            return new ExportResultStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentSha256;

    private StringFilter originalFileName;

    private StringFilter exportedPath;

    private StringFilter exportedFileName;

    private StringFilter s3ExportKey;

    private LongFilter fileSize;

    private ExportResultStatusFilter status;

    private InstantFilter exportedDate;

    private LongFilter exportJobId;

    private Boolean distinct;

    public ExportResultCriteria() {}

    public ExportResultCriteria(ExportResultCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.originalFileName = other.optionalOriginalFileName().map(StringFilter::copy).orElse(null);
        this.exportedPath = other.optionalExportedPath().map(StringFilter::copy).orElse(null);
        this.exportedFileName = other.optionalExportedFileName().map(StringFilter::copy).orElse(null);
        this.s3ExportKey = other.optionals3ExportKey().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ExportResultStatusFilter::copy).orElse(null);
        this.exportedDate = other.optionalExportedDate().map(InstantFilter::copy).orElse(null);
        this.exportJobId = other.optionalExportJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExportResultCriteria copy() {
        return new ExportResultCriteria(this);
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

    public StringFilter getOriginalFileName() {
        return originalFileName;
    }

    public Optional<StringFilter> optionalOriginalFileName() {
        return Optional.ofNullable(originalFileName);
    }

    public StringFilter originalFileName() {
        if (originalFileName == null) {
            setOriginalFileName(new StringFilter());
        }
        return originalFileName;
    }

    public void setOriginalFileName(StringFilter originalFileName) {
        this.originalFileName = originalFileName;
    }

    public StringFilter getExportedPath() {
        return exportedPath;
    }

    public Optional<StringFilter> optionalExportedPath() {
        return Optional.ofNullable(exportedPath);
    }

    public StringFilter exportedPath() {
        if (exportedPath == null) {
            setExportedPath(new StringFilter());
        }
        return exportedPath;
    }

    public void setExportedPath(StringFilter exportedPath) {
        this.exportedPath = exportedPath;
    }

    public StringFilter getExportedFileName() {
        return exportedFileName;
    }

    public Optional<StringFilter> optionalExportedFileName() {
        return Optional.ofNullable(exportedFileName);
    }

    public StringFilter exportedFileName() {
        if (exportedFileName == null) {
            setExportedFileName(new StringFilter());
        }
        return exportedFileName;
    }

    public void setExportedFileName(StringFilter exportedFileName) {
        this.exportedFileName = exportedFileName;
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

    public ExportResultStatusFilter getStatus() {
        return status;
    }

    public Optional<ExportResultStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ExportResultStatusFilter status() {
        if (status == null) {
            setStatus(new ExportResultStatusFilter());
        }
        return status;
    }

    public void setStatus(ExportResultStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getExportedDate() {
        return exportedDate;
    }

    public Optional<InstantFilter> optionalExportedDate() {
        return Optional.ofNullable(exportedDate);
    }

    public InstantFilter exportedDate() {
        if (exportedDate == null) {
            setExportedDate(new InstantFilter());
        }
        return exportedDate;
    }

    public void setExportedDate(InstantFilter exportedDate) {
        this.exportedDate = exportedDate;
    }

    public LongFilter getExportJobId() {
        return exportJobId;
    }

    public Optional<LongFilter> optionalExportJobId() {
        return Optional.ofNullable(exportJobId);
    }

    public LongFilter exportJobId() {
        if (exportJobId == null) {
            setExportJobId(new LongFilter());
        }
        return exportJobId;
    }

    public void setExportJobId(LongFilter exportJobId) {
        this.exportJobId = exportJobId;
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
        final ExportResultCriteria that = (ExportResultCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(originalFileName, that.originalFileName) &&
            Objects.equals(exportedPath, that.exportedPath) &&
            Objects.equals(exportedFileName, that.exportedFileName) &&
            Objects.equals(s3ExportKey, that.s3ExportKey) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(status, that.status) &&
            Objects.equals(exportedDate, that.exportedDate) &&
            Objects.equals(exportJobId, that.exportJobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentSha256,
            originalFileName,
            exportedPath,
            exportedFileName,
            s3ExportKey,
            fileSize,
            status,
            exportedDate,
            exportJobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportResultCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalOriginalFileName().map(f -> "originalFileName=" + f + ", ").orElse("") +
            optionalExportedPath().map(f -> "exportedPath=" + f + ", ").orElse("") +
            optionalExportedFileName().map(f -> "exportedFileName=" + f + ", ").orElse("") +
            optionals3ExportKey().map(f -> "s3ExportKey=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalExportedDate().map(f -> "exportedDate=" + f + ", ").orElse("") +
            optionalExportJobId().map(f -> "exportJobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
