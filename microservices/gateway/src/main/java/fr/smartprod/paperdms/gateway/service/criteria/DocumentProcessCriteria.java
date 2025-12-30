package fr.smartprod.paperdms.gateway.service.criteria;

import fr.smartprod.paperdms.gateway.domain.enumeration.WorkflowInstanceStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.gateway.domain.DocumentProcess} entity. This class is used
 * in {@link fr.smartprod.paperdms.gateway.web.rest.DocumentProcessResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-processes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentProcessCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkflowInstanceStatus
     */
    public static class WorkflowInstanceStatusFilter extends Filter<WorkflowInstanceStatus> {

        public WorkflowInstanceStatusFilter() {}

        public WorkflowInstanceStatusFilter(WorkflowInstanceStatusFilter filter) {
            super(filter);
        }

        @Override
        public WorkflowInstanceStatusFilter copy() {
            return new WorkflowInstanceStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private WorkflowInstanceStatusFilter status;

    private StringFilter documentSha256;

    private Boolean distinct;

    public DocumentProcessCriteria() {}

    public DocumentProcessCriteria(DocumentProcessCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(WorkflowInstanceStatusFilter::copy).orElse(null);
        this.documentSha256 = other.optionalDocumentSha256().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentProcessCriteria copy() {
        return new DocumentProcessCriteria(this);
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

    public WorkflowInstanceStatusFilter getStatus() {
        return status;
    }

    public Optional<WorkflowInstanceStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public WorkflowInstanceStatusFilter status() {
        if (status == null) {
            setStatus(new WorkflowInstanceStatusFilter());
        }
        return status;
    }

    public void setStatus(WorkflowInstanceStatusFilter status) {
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
        final DocumentProcessCriteria that = (DocumentProcessCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(documentSha256, that.documentSha256) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, documentSha256, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentProcessCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDocumentSha256().map(f -> "documentSha256=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
