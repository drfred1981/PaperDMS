package fr.smartprod.paperdms.document.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentComment} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentCommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter pageNumber;

    private BooleanFilter isResolved;

    private StringFilter authorId;

    private InstantFilter createdDate;

    private LongFilter repliesId;

    private LongFilter documentId;

    private LongFilter parentCommentId;

    private Boolean distinct;

    public DocumentCommentCriteria() {}

    public DocumentCommentCriteria(DocumentCommentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.pageNumber = other.optionalPageNumber().map(IntegerFilter::copy).orElse(null);
        this.isResolved = other.optionalIsResolved().map(BooleanFilter::copy).orElse(null);
        this.authorId = other.optionalAuthorId().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.repliesId = other.optionalRepliesId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.parentCommentId = other.optionalParentCommentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentCommentCriteria copy() {
        return new DocumentCommentCriteria(this);
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

    public IntegerFilter getPageNumber() {
        return pageNumber;
    }

    public Optional<IntegerFilter> optionalPageNumber() {
        return Optional.ofNullable(pageNumber);
    }

    public IntegerFilter pageNumber() {
        if (pageNumber == null) {
            setPageNumber(new IntegerFilter());
        }
        return pageNumber;
    }

    public void setPageNumber(IntegerFilter pageNumber) {
        this.pageNumber = pageNumber;
    }

    public BooleanFilter getIsResolved() {
        return isResolved;
    }

    public Optional<BooleanFilter> optionalIsResolved() {
        return Optional.ofNullable(isResolved);
    }

    public BooleanFilter isResolved() {
        if (isResolved == null) {
            setIsResolved(new BooleanFilter());
        }
        return isResolved;
    }

    public void setIsResolved(BooleanFilter isResolved) {
        this.isResolved = isResolved;
    }

    public StringFilter getAuthorId() {
        return authorId;
    }

    public Optional<StringFilter> optionalAuthorId() {
        return Optional.ofNullable(authorId);
    }

    public StringFilter authorId() {
        if (authorId == null) {
            setAuthorId(new StringFilter());
        }
        return authorId;
    }

    public void setAuthorId(StringFilter authorId) {
        this.authorId = authorId;
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

    public LongFilter getRepliesId() {
        return repliesId;
    }

    public Optional<LongFilter> optionalRepliesId() {
        return Optional.ofNullable(repliesId);
    }

    public LongFilter repliesId() {
        if (repliesId == null) {
            setRepliesId(new LongFilter());
        }
        return repliesId;
    }

    public void setRepliesId(LongFilter repliesId) {
        this.repliesId = repliesId;
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

    public LongFilter getParentCommentId() {
        return parentCommentId;
    }

    public Optional<LongFilter> optionalParentCommentId() {
        return Optional.ofNullable(parentCommentId);
    }

    public LongFilter parentCommentId() {
        if (parentCommentId == null) {
            setParentCommentId(new LongFilter());
        }
        return parentCommentId;
    }

    public void setParentCommentId(LongFilter parentCommentId) {
        this.parentCommentId = parentCommentId;
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
        final DocumentCommentCriteria that = (DocumentCommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(isResolved, that.isResolved) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(repliesId, that.repliesId) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(parentCommentId, that.parentCommentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageNumber, isResolved, authorId, createdDate, repliesId, documentId, parentCommentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCommentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPageNumber().map(f -> "pageNumber=" + f + ", ").orElse("") +
            optionalIsResolved().map(f -> "isResolved=" + f + ", ").orElse("") +
            optionalAuthorId().map(f -> "authorId=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalRepliesId().map(f -> "repliesId=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalParentCommentId().map(f -> "parentCommentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
