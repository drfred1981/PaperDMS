package fr.smartprod.paperdms.emailimportdocument.service.criteria;

import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.AttachmentStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment} entity. This class is used
 * in {@link fr.smartprod.paperdms.emailimportdocument.web.rest.EmailImportEmailAttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-import-email-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportEmailAttachmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AttachmentStatus
     */
    public static class AttachmentStatusFilter extends Filter<AttachmentStatus> {

        public AttachmentStatusFilter() {}

        public AttachmentStatusFilter(AttachmentStatusFilter filter) {
            super(filter);
        }

        @Override
        public AttachmentStatusFilter copy() {
            return new AttachmentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private LongFilter fileSize;

    private StringFilter mimeType;

    private StringFilter sha256;

    private StringFilter s3Key;

    private AttachmentStatusFilter status;

    private StringFilter documentSha256;

    private LongFilter emailImportDocumentId;

    private Boolean distinct;

    public EmailImportEmailAttachmentCriteria() {}

    public EmailImportEmailAttachmentCriteria(EmailImportEmailAttachmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fileName = other.optionalFileName().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.mimeType = other.optionalMimeType().map(StringFilter::copy).orElse(null);
        this.sha256 = other.optionalSha256().map(StringFilter::copy).orElse(null);
        this.s3Key = other.optionals3Key().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AttachmentStatusFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.emailImportDocumentId = other.optionalEmailImportDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmailImportEmailAttachmentCriteria copy() {
        return new EmailImportEmailAttachmentCriteria(this);
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

    public StringFilter getMimeType() {
        return mimeType;
    }

    public Optional<StringFilter> optionalMimeType() {
        return Optional.ofNullable(mimeType);
    }

    public StringFilter mimeType() {
        if (mimeType == null) {
            setMimeType(new StringFilter());
        }
        return mimeType;
    }

    public void setMimeType(StringFilter mimeType) {
        this.mimeType = mimeType;
    }

    public StringFilter getSha256() {
        return sha256;
    }

    public Optional<StringFilter> optionalSha256() {
        return Optional.ofNullable(sha256);
    }

    public StringFilter sha256() {
        if (sha256 == null) {
            setSha256(new StringFilter());
        }
        return sha256;
    }

    public void setSha256(StringFilter sha256) {
        this.sha256 = sha256;
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

    public AttachmentStatusFilter getStatus() {
        return status;
    }

    public Optional<AttachmentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AttachmentStatusFilter status() {
        if (status == null) {
            setStatus(new AttachmentStatusFilter());
        }
        return status;
    }

    public void setStatus(AttachmentStatusFilter status) {
        this.status = status;
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

    public LongFilter getEmailImportDocumentId() {
        return emailImportDocumentId;
    }

    public Optional<LongFilter> optionalEmailImportDocumentId() {
        return Optional.ofNullable(emailImportDocumentId);
    }

    public LongFilter emailImportDocumentId() {
        if (emailImportDocumentId == null) {
            setEmailImportDocumentId(new LongFilter());
        }
        return emailImportDocumentId;
    }

    public void setEmailImportDocumentId(LongFilter emailImportDocumentId) {
        this.emailImportDocumentId = emailImportDocumentId;
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
        final EmailImportEmailAttachmentCriteria that = (EmailImportEmailAttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(mimeType, that.mimeType) &&
            Objects.equals(sha256, that.sha256) &&
            Objects.equals(s3Key, that.s3Key) &&
            Objects.equals(status, that.status) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(emailImportDocumentId, that.emailImportDocumentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, fileSize, mimeType, sha256, s3Key, status, documentSha256, emailImportDocumentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportEmailAttachmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFileName().map(f -> "fileName=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalMimeType().map(f -> "mimeType=" + f + ", ").orElse("") +
            optionalSha256().map(f -> "sha256=" + f + ", ").orElse("") +
            optionals3Key().map(f -> "s3Key=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalEmailImportDocumentId().map(f -> "emailImportDocumentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
