package fr.smartprod.paperdms.archive.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.archive.domain.ArchiveDocument} entity. This class is used
 * in {@link fr.smartprod.paperdms.archive.web.rest.ArchiveDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /archive-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArchiveDocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentSha256;

    private StringFilter originalPath;

    private StringFilter archivePath;

    private LongFilter fileSize;

    private InstantFilter addedDate;

    private LongFilter archiveJobId;

    private Boolean distinct;

    public ArchiveDocumentCriteria() {}

    public ArchiveDocumentCriteria(ArchiveDocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.originalPath = other.optionalOriginalPath().map(StringFilter::copy).orElse(null);
        this.archivePath = other.optionalArchivePath().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.addedDate = other.optionalAddedDate().map(InstantFilter::copy).orElse(null);
        this.archiveJobId = other.optionalArchiveJobId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ArchiveDocumentCriteria copy() {
        return new ArchiveDocumentCriteria(this);
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

    public StringFilter getOriginalPath() {
        return originalPath;
    }

    public Optional<StringFilter> optionalOriginalPath() {
        return Optional.ofNullable(originalPath);
    }

    public StringFilter originalPath() {
        if (originalPath == null) {
            setOriginalPath(new StringFilter());
        }
        return originalPath;
    }

    public void setOriginalPath(StringFilter originalPath) {
        this.originalPath = originalPath;
    }

    public StringFilter getArchivePath() {
        return archivePath;
    }

    public Optional<StringFilter> optionalArchivePath() {
        return Optional.ofNullable(archivePath);
    }

    public StringFilter archivePath() {
        if (archivePath == null) {
            setArchivePath(new StringFilter());
        }
        return archivePath;
    }

    public void setArchivePath(StringFilter archivePath) {
        this.archivePath = archivePath;
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

    public InstantFilter getAddedDate() {
        return addedDate;
    }

    public Optional<InstantFilter> optionalAddedDate() {
        return Optional.ofNullable(addedDate);
    }

    public InstantFilter addedDate() {
        if (addedDate == null) {
            setAddedDate(new InstantFilter());
        }
        return addedDate;
    }

    public void setAddedDate(InstantFilter addedDate) {
        this.addedDate = addedDate;
    }

    public LongFilter getArchiveJobId() {
        return archiveJobId;
    }

    public Optional<LongFilter> optionalArchiveJobId() {
        return Optional.ofNullable(archiveJobId);
    }

    public LongFilter archiveJobId() {
        if (archiveJobId == null) {
            setArchiveJobId(new LongFilter());
        }
        return archiveJobId;
    }

    public void setArchiveJobId(LongFilter archiveJobId) {
        this.archiveJobId = archiveJobId;
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
        final ArchiveDocumentCriteria that = (ArchiveDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(originalPath, that.originalPath) &&
            Objects.equals(archivePath, that.archivePath) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(archiveJobId, that.archiveJobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentSha256, originalPath, archivePath, fileSize, addedDate, archiveJobId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveDocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalOriginalPath().map(f -> "originalPath=" + f + ", ").orElse("") +
            optionalArchivePath().map(f -> "archivePath=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalAddedDate().map(f -> "addedDate=" + f + ", ").orElse("") +
            optionalArchiveJobId().map(f -> "archiveJobId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
