package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.AuditAction;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentAudit} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentAuditResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-audits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentAuditCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AuditAction
     */
    public static class AuditActionFilter extends Filter<AuditAction> {

        public AuditActionFilter() {}

        public AuditActionFilter(AuditActionFilter filter) {
            super(filter);
        }

        @Override
        public AuditActionFilter copy() {
            return new AuditActionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentSha256;

    private AuditActionFilter action;

    private StringFilter userId;

    private StringFilter userIp;

    private InstantFilter actionDate;

    private LongFilter documentId;

    private Boolean distinct;

    public DocumentAuditCriteria() {}

    public DocumentAuditCriteria(DocumentAuditCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.action = other.optionalAction().map(AuditActionFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.userIp = other.optionalUserIp().map(StringFilter::copy).orElse(null);
        this.actionDate = other.optionalActionDate().map(InstantFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentAuditCriteria copy() {
        return new DocumentAuditCriteria(this);
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

    public AuditActionFilter getAction() {
        return action;
    }

    public Optional<AuditActionFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public AuditActionFilter action() {
        if (action == null) {
            setAction(new AuditActionFilter());
        }
        return action;
    }

    public void setAction(AuditActionFilter action) {
        this.action = action;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public StringFilter getUserIp() {
        return userIp;
    }

    public Optional<StringFilter> optionalUserIp() {
        return Optional.ofNullable(userIp);
    }

    public StringFilter userIp() {
        if (userIp == null) {
            setUserIp(new StringFilter());
        }
        return userIp;
    }

    public void setUserIp(StringFilter userIp) {
        this.userIp = userIp;
    }

    public InstantFilter getActionDate() {
        return actionDate;
    }

    public Optional<InstantFilter> optionalActionDate() {
        return Optional.ofNullable(actionDate);
    }

    public InstantFilter actionDate() {
        if (actionDate == null) {
            setActionDate(new InstantFilter());
        }
        return actionDate;
    }

    public void setActionDate(InstantFilter actionDate) {
        this.actionDate = actionDate;
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
        final DocumentAuditCriteria that = (DocumentAuditCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(action, that.action) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userIp, that.userIp) &&
            Objects.equals(actionDate, that.actionDate) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentSha256, action, userId, userIp, actionDate, documentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentAuditCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalUserIp().map(f -> "userIp=" + f + ", ").orElse("") +
            optionalActionDate().map(f -> "actionDate=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
