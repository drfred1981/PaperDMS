package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.RelationType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentRelation} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentRelationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-relations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentRelationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RelationType
     */
    public static class RelationTypeFilter extends Filter<RelationType> {

        public RelationTypeFilter() {}

        public RelationTypeFilter(RelationTypeFilter filter) {
            super(filter);
        }

        @Override
        public RelationTypeFilter copy() {
            return new RelationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter sourceDocumentId;

    private LongFilter targetDocumentId;

    private RelationTypeFilter relationType;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public DocumentRelationCriteria() {}

    public DocumentRelationCriteria(DocumentRelationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sourceDocumentId = other.optionalSourceDocumentId().map(LongFilter::copy).orElse(null);
        this.targetDocumentId = other.optionalTargetDocumentId().map(LongFilter::copy).orElse(null);
        this.relationType = other.optionalRelationType().map(RelationTypeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentRelationCriteria copy() {
        return new DocumentRelationCriteria(this);
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

    public LongFilter getSourceDocumentId() {
        return sourceDocumentId;
    }

    public Optional<LongFilter> optionalSourceDocumentId() {
        return Optional.ofNullable(sourceDocumentId);
    }

    public LongFilter sourceDocumentId() {
        if (sourceDocumentId == null) {
            setSourceDocumentId(new LongFilter());
        }
        return sourceDocumentId;
    }

    public void setSourceDocumentId(LongFilter sourceDocumentId) {
        this.sourceDocumentId = sourceDocumentId;
    }

    public LongFilter getTargetDocumentId() {
        return targetDocumentId;
    }

    public Optional<LongFilter> optionalTargetDocumentId() {
        return Optional.ofNullable(targetDocumentId);
    }

    public LongFilter targetDocumentId() {
        if (targetDocumentId == null) {
            setTargetDocumentId(new LongFilter());
        }
        return targetDocumentId;
    }

    public void setTargetDocumentId(LongFilter targetDocumentId) {
        this.targetDocumentId = targetDocumentId;
    }

    public RelationTypeFilter getRelationType() {
        return relationType;
    }

    public Optional<RelationTypeFilter> optionalRelationType() {
        return Optional.ofNullable(relationType);
    }

    public RelationTypeFilter relationType() {
        if (relationType == null) {
            setRelationType(new RelationTypeFilter());
        }
        return relationType;
    }

    public void setRelationType(RelationTypeFilter relationType) {
        this.relationType = relationType;
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
        final DocumentRelationCriteria that = (DocumentRelationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sourceDocumentId, that.sourceDocumentId) &&
            Objects.equals(targetDocumentId, that.targetDocumentId) &&
            Objects.equals(relationType, that.relationType) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceDocumentId, targetDocumentId, relationType, createdBy, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentRelationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSourceDocumentId().map(f -> "sourceDocumentId=" + f + ", ").orElse("") +
            optionalTargetDocumentId().map(f -> "targetDocumentId=" + f + ", ").orElse("") +
            optionalRelationType().map(f -> "relationType=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
