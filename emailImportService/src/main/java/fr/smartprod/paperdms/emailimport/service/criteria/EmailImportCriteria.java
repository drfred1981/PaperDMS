package fr.smartprod.paperdms.emailimport.service.criteria;

import fr.smartprod.paperdms.emailimport.domain.enumeration.ImportStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.emailimport.domain.EmailImport} entity. This class is used
 * in {@link fr.smartprod.paperdms.emailimport.web.rest.EmailImportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-imports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ImportStatus
     */
    public static class ImportStatusFilter extends Filter<ImportStatus> {

        public ImportStatusFilter() {}

        public ImportStatusFilter(ImportStatusFilter filter) {
            super(filter);
        }

        @Override
        public ImportStatusFilter copy() {
            return new ImportStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fromEmail;

    private StringFilter toEmail;

    private StringFilter subject;

    private InstantFilter receivedDate;

    private InstantFilter processedDate;

    private ImportStatusFilter status;

    private LongFilter folderId;

    private LongFilter documentTypeId;

    private IntegerFilter attachmentCount;

    private IntegerFilter documentsCreated;

    private LongFilter appliedRuleId;

    private LongFilter appliedRuleId;

    private Boolean distinct;

    public EmailImportCriteria() {}

    public EmailImportCriteria(EmailImportCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromEmail = other.optionalFromEmail().map(StringFilter::copy).orElse(null);
        this.toEmail = other.optionalToEmail().map(StringFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(StringFilter::copy).orElse(null);
        this.receivedDate = other.optionalReceivedDate().map(InstantFilter::copy).orElse(null);
        this.processedDate = other.optionalProcessedDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ImportStatusFilter::copy).orElse(null);
        this.folderId = other.optionalFolderId().map(LongFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.attachmentCount = other.optionalAttachmentCount().map(IntegerFilter::copy).orElse(null);
        this.documentsCreated = other.optionalDocumentsCreated().map(IntegerFilter::copy).orElse(null);
        this.appliedRuleId = other.optionalAppliedRuleId().map(LongFilter::copy).orElse(null);
        this.appliedRuleId = other.optionalAppliedRuleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmailImportCriteria copy() {
        return new EmailImportCriteria(this);
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

    public StringFilter getFromEmail() {
        return fromEmail;
    }

    public Optional<StringFilter> optionalFromEmail() {
        return Optional.ofNullable(fromEmail);
    }

    public StringFilter fromEmail() {
        if (fromEmail == null) {
            setFromEmail(new StringFilter());
        }
        return fromEmail;
    }

    public void setFromEmail(StringFilter fromEmail) {
        this.fromEmail = fromEmail;
    }

    public StringFilter getToEmail() {
        return toEmail;
    }

    public Optional<StringFilter> optionalToEmail() {
        return Optional.ofNullable(toEmail);
    }

    public StringFilter toEmail() {
        if (toEmail == null) {
            setToEmail(new StringFilter());
        }
        return toEmail;
    }

    public void setToEmail(StringFilter toEmail) {
        this.toEmail = toEmail;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public Optional<StringFilter> optionalSubject() {
        return Optional.ofNullable(subject);
    }

    public StringFilter subject() {
        if (subject == null) {
            setSubject(new StringFilter());
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public InstantFilter getReceivedDate() {
        return receivedDate;
    }

    public Optional<InstantFilter> optionalReceivedDate() {
        return Optional.ofNullable(receivedDate);
    }

    public InstantFilter receivedDate() {
        if (receivedDate == null) {
            setReceivedDate(new InstantFilter());
        }
        return receivedDate;
    }

    public void setReceivedDate(InstantFilter receivedDate) {
        this.receivedDate = receivedDate;
    }

    public InstantFilter getProcessedDate() {
        return processedDate;
    }

    public Optional<InstantFilter> optionalProcessedDate() {
        return Optional.ofNullable(processedDate);
    }

    public InstantFilter processedDate() {
        if (processedDate == null) {
            setProcessedDate(new InstantFilter());
        }
        return processedDate;
    }

    public void setProcessedDate(InstantFilter processedDate) {
        this.processedDate = processedDate;
    }

    public ImportStatusFilter getStatus() {
        return status;
    }

    public Optional<ImportStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ImportStatusFilter status() {
        if (status == null) {
            setStatus(new ImportStatusFilter());
        }
        return status;
    }

    public void setStatus(ImportStatusFilter status) {
        this.status = status;
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

    public IntegerFilter getAttachmentCount() {
        return attachmentCount;
    }

    public Optional<IntegerFilter> optionalAttachmentCount() {
        return Optional.ofNullable(attachmentCount);
    }

    public IntegerFilter attachmentCount() {
        if (attachmentCount == null) {
            setAttachmentCount(new IntegerFilter());
        }
        return attachmentCount;
    }

    public void setAttachmentCount(IntegerFilter attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public IntegerFilter getDocumentsCreated() {
        return documentsCreated;
    }

    public Optional<IntegerFilter> optionalDocumentsCreated() {
        return Optional.ofNullable(documentsCreated);
    }

    public IntegerFilter documentsCreated() {
        if (documentsCreated == null) {
            setDocumentsCreated(new IntegerFilter());
        }
        return documentsCreated;
    }

    public void setDocumentsCreated(IntegerFilter documentsCreated) {
        this.documentsCreated = documentsCreated;
    }

    public LongFilter getAppliedRuleId() {
        return appliedRuleId;
    }

    public Optional<LongFilter> optionalAppliedRuleId() {
        return Optional.ofNullable(appliedRuleId);
    }

    public LongFilter appliedRuleId() {
        if (appliedRuleId == null) {
            setAppliedRuleId(new LongFilter());
        }
        return appliedRuleId;
    }

    public void setAppliedRuleId(LongFilter appliedRuleId) {
        this.appliedRuleId = appliedRuleId;
    }

    public LongFilter getAppliedRuleId() {
        return appliedRuleId;
    }

    public Optional<LongFilter> optionalAppliedRuleId() {
        return Optional.ofNullable(appliedRuleId);
    }

    public LongFilter appliedRuleId() {
        if (appliedRuleId == null) {
            setAppliedRuleId(new LongFilter());
        }
        return appliedRuleId;
    }

    public void setAppliedRuleId(LongFilter appliedRuleId) {
        this.appliedRuleId = appliedRuleId;
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
        final EmailImportCriteria that = (EmailImportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromEmail, that.fromEmail) &&
            Objects.equals(toEmail, that.toEmail) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(receivedDate, that.receivedDate) &&
            Objects.equals(processedDate, that.processedDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(folderId, that.folderId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(attachmentCount, that.attachmentCount) &&
            Objects.equals(documentsCreated, that.documentsCreated) &&
            Objects.equals(appliedRuleId, that.appliedRuleId) &&
            Objects.equals(appliedRuleId, that.appliedRuleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fromEmail,
            toEmail,
            subject,
            receivedDate,
            processedDate,
            status,
            folderId,
            documentTypeId,
            attachmentCount,
            documentsCreated,
            appliedRuleId,
            appliedRuleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromEmail().map(f -> "fromEmail=" + f + ", ").orElse("") +
            optionalToEmail().map(f -> "toEmail=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalReceivedDate().map(f -> "receivedDate=" + f + ", ").orElse("") +
            optionalProcessedDate().map(f -> "processedDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalFolderId().map(f -> "folderId=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalAttachmentCount().map(f -> "attachmentCount=" + f + ", ").orElse("") +
            optionalDocumentsCreated().map(f -> "documentsCreated=" + f + ", ").orElse("") +
            optionalAppliedRuleId().map(f -> "appliedRuleId=" + f + ", ").orElse("") +
            optionalAppliedRuleId().map(f -> "appliedRuleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
