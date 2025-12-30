package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.PermissionType;
import fr.smartprod.paperdms.document.domain.enumeration.PrincipalType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentPermission} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentPermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentPermissionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PrincipalType
     */
    public static class PrincipalTypeFilter extends Filter<PrincipalType> {

        public PrincipalTypeFilter() {}

        public PrincipalTypeFilter(PrincipalTypeFilter filter) {
            super(filter);
        }

        @Override
        public PrincipalTypeFilter copy() {
            return new PrincipalTypeFilter(this);
        }
    }

    /**
     * Class for filtering PermissionType
     */
    public static class PermissionTypeFilter extends Filter<PermissionType> {

        public PermissionTypeFilter() {}

        public PermissionTypeFilter(PermissionTypeFilter filter) {
            super(filter);
        }

        @Override
        public PermissionTypeFilter copy() {
            return new PermissionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PrincipalTypeFilter principalType;

    private StringFilter principalId;

    private PermissionTypeFilter permission;

    private BooleanFilter canDelegate;

    private StringFilter grantedBy;

    private InstantFilter grantedDate;

    private LongFilter documentId;

    private LongFilter metaPermissionGroupId;

    private Boolean distinct;

    public DocumentPermissionCriteria() {}

    public DocumentPermissionCriteria(DocumentPermissionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.principalType = other.optionalPrincipalType().map(PrincipalTypeFilter::copy).orElse(null);
        this.principalId = other.optionalPrincipalId().map(StringFilter::copy).orElse(null);
        this.permission = other.optionalPermission().map(PermissionTypeFilter::copy).orElse(null);
        this.canDelegate = other.optionalCanDelegate().map(BooleanFilter::copy).orElse(null);
        this.grantedBy = other.optionalGrantedBy().map(StringFilter::copy).orElse(null);
        this.grantedDate = other.optionalGrantedDate().map(InstantFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.metaPermissionGroupId = other.optionalMetaPermissionGroupId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentPermissionCriteria copy() {
        return new DocumentPermissionCriteria(this);
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

    public PrincipalTypeFilter getPrincipalType() {
        return principalType;
    }

    public Optional<PrincipalTypeFilter> optionalPrincipalType() {
        return Optional.ofNullable(principalType);
    }

    public PrincipalTypeFilter principalType() {
        if (principalType == null) {
            setPrincipalType(new PrincipalTypeFilter());
        }
        return principalType;
    }

    public void setPrincipalType(PrincipalTypeFilter principalType) {
        this.principalType = principalType;
    }

    public StringFilter getPrincipalId() {
        return principalId;
    }

    public Optional<StringFilter> optionalPrincipalId() {
        return Optional.ofNullable(principalId);
    }

    public StringFilter principalId() {
        if (principalId == null) {
            setPrincipalId(new StringFilter());
        }
        return principalId;
    }

    public void setPrincipalId(StringFilter principalId) {
        this.principalId = principalId;
    }

    public PermissionTypeFilter getPermission() {
        return permission;
    }

    public Optional<PermissionTypeFilter> optionalPermission() {
        return Optional.ofNullable(permission);
    }

    public PermissionTypeFilter permission() {
        if (permission == null) {
            setPermission(new PermissionTypeFilter());
        }
        return permission;
    }

    public void setPermission(PermissionTypeFilter permission) {
        this.permission = permission;
    }

    public BooleanFilter getCanDelegate() {
        return canDelegate;
    }

    public Optional<BooleanFilter> optionalCanDelegate() {
        return Optional.ofNullable(canDelegate);
    }

    public BooleanFilter canDelegate() {
        if (canDelegate == null) {
            setCanDelegate(new BooleanFilter());
        }
        return canDelegate;
    }

    public void setCanDelegate(BooleanFilter canDelegate) {
        this.canDelegate = canDelegate;
    }

    public StringFilter getGrantedBy() {
        return grantedBy;
    }

    public Optional<StringFilter> optionalGrantedBy() {
        return Optional.ofNullable(grantedBy);
    }

    public StringFilter grantedBy() {
        if (grantedBy == null) {
            setGrantedBy(new StringFilter());
        }
        return grantedBy;
    }

    public void setGrantedBy(StringFilter grantedBy) {
        this.grantedBy = grantedBy;
    }

    public InstantFilter getGrantedDate() {
        return grantedDate;
    }

    public Optional<InstantFilter> optionalGrantedDate() {
        return Optional.ofNullable(grantedDate);
    }

    public InstantFilter grantedDate() {
        if (grantedDate == null) {
            setGrantedDate(new InstantFilter());
        }
        return grantedDate;
    }

    public void setGrantedDate(InstantFilter grantedDate) {
        this.grantedDate = grantedDate;
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

    public LongFilter getMetaPermissionGroupId() {
        return metaPermissionGroupId;
    }

    public Optional<LongFilter> optionalMetaPermissionGroupId() {
        return Optional.ofNullable(metaPermissionGroupId);
    }

    public LongFilter metaPermissionGroupId() {
        if (metaPermissionGroupId == null) {
            setMetaPermissionGroupId(new LongFilter());
        }
        return metaPermissionGroupId;
    }

    public void setMetaPermissionGroupId(LongFilter metaPermissionGroupId) {
        this.metaPermissionGroupId = metaPermissionGroupId;
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
        final DocumentPermissionCriteria that = (DocumentPermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(principalType, that.principalType) &&
            Objects.equals(principalId, that.principalId) &&
            Objects.equals(permission, that.permission) &&
            Objects.equals(canDelegate, that.canDelegate) &&
            Objects.equals(grantedBy, that.grantedBy) &&
            Objects.equals(grantedDate, that.grantedDate) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(metaPermissionGroupId, that.metaPermissionGroupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            principalType,
            principalId,
            permission,
            canDelegate,
            grantedBy,
            grantedDate,
            documentId,
            metaPermissionGroupId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentPermissionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrincipalType().map(f -> "principalType=" + f + ", ").orElse("") +
            optionalPrincipalId().map(f -> "principalId=" + f + ", ").orElse("") +
            optionalPermission().map(f -> "permission=" + f + ", ").orElse("") +
            optionalCanDelegate().map(f -> "canDelegate=" + f + ", ").orElse("") +
            optionalGrantedBy().map(f -> "grantedBy=" + f + ", ").orElse("") +
            optionalGrantedDate().map(f -> "grantedDate=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalMetaPermissionGroupId().map(f -> "metaPermissionGroupId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
