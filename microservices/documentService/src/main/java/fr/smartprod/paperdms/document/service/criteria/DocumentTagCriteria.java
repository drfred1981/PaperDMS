package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.MetaTagSource;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentTag} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentTagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MetaTagSource
     */
    public static class MetaTagSourceFilter extends Filter<MetaTagSource> {

        public MetaTagSourceFilter() {}

        public MetaTagSourceFilter(MetaTagSourceFilter filter) {
            super(filter);
        }

        @Override
        public MetaTagSourceFilter copy() {
            return new MetaTagSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter assignedDate;

    private StringFilter assignedBy;

    private DoubleFilter confidence;

    private BooleanFilter isAutoMetaTagged;

    private MetaTagSourceFilter source;

    private LongFilter documentId;

    private LongFilter metaTagId;

    private Boolean distinct;

    public DocumentTagCriteria() {}

    public DocumentTagCriteria(DocumentTagCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.assignedDate = other.optionalAssignedDate().map(InstantFilter::copy).orElse(null);
        this.assignedBy = other.optionalAssignedBy().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.isAutoMetaTagged = other.optionalIsAutoMetaTagged().map(BooleanFilter::copy).orElse(null);
        this.source = other.optionalSource().map(MetaTagSourceFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.metaTagId = other.optionalMetaTagId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentTagCriteria copy() {
        return new DocumentTagCriteria(this);
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

    public InstantFilter getAssignedDate() {
        return assignedDate;
    }

    public Optional<InstantFilter> optionalAssignedDate() {
        return Optional.ofNullable(assignedDate);
    }

    public InstantFilter assignedDate() {
        if (assignedDate == null) {
            setAssignedDate(new InstantFilter());
        }
        return assignedDate;
    }

    public void setAssignedDate(InstantFilter assignedDate) {
        this.assignedDate = assignedDate;
    }

    public StringFilter getAssignedBy() {
        return assignedBy;
    }

    public Optional<StringFilter> optionalAssignedBy() {
        return Optional.ofNullable(assignedBy);
    }

    public StringFilter assignedBy() {
        if (assignedBy == null) {
            setAssignedBy(new StringFilter());
        }
        return assignedBy;
    }

    public void setAssignedBy(StringFilter assignedBy) {
        this.assignedBy = assignedBy;
    }

    public DoubleFilter getConfidence() {
        return confidence;
    }

    public Optional<DoubleFilter> optionalConfidence() {
        return Optional.ofNullable(confidence);
    }

    public DoubleFilter confidence() {
        if (confidence == null) {
            setConfidence(new DoubleFilter());
        }
        return confidence;
    }

    public void setConfidence(DoubleFilter confidence) {
        this.confidence = confidence;
    }

    public BooleanFilter getIsAutoMetaTagged() {
        return isAutoMetaTagged;
    }

    public Optional<BooleanFilter> optionalIsAutoMetaTagged() {
        return Optional.ofNullable(isAutoMetaTagged);
    }

    public BooleanFilter isAutoMetaTagged() {
        if (isAutoMetaTagged == null) {
            setIsAutoMetaTagged(new BooleanFilter());
        }
        return isAutoMetaTagged;
    }

    public void setIsAutoMetaTagged(BooleanFilter isAutoMetaTagged) {
        this.isAutoMetaTagged = isAutoMetaTagged;
    }

    public MetaTagSourceFilter getSource() {
        return source;
    }

    public Optional<MetaTagSourceFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public MetaTagSourceFilter source() {
        if (source == null) {
            setSource(new MetaTagSourceFilter());
        }
        return source;
    }

    public void setSource(MetaTagSourceFilter source) {
        this.source = source;
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

    public LongFilter getMetaTagId() {
        return metaTagId;
    }

    public Optional<LongFilter> optionalMetaTagId() {
        return Optional.ofNullable(metaTagId);
    }

    public LongFilter metaTagId() {
        if (metaTagId == null) {
            setMetaTagId(new LongFilter());
        }
        return metaTagId;
    }

    public void setMetaTagId(LongFilter metaTagId) {
        this.metaTagId = metaTagId;
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
        final DocumentTagCriteria that = (DocumentTagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assignedDate, that.assignedDate) &&
            Objects.equals(assignedBy, that.assignedBy) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(isAutoMetaTagged, that.isAutoMetaTagged) &&
            Objects.equals(source, that.source) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(metaTagId, that.metaTagId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedDate, assignedBy, confidence, isAutoMetaTagged, source, documentId, metaTagId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTagCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAssignedDate().map(f -> "assignedDate=" + f + ", ").orElse("") +
            optionalAssignedBy().map(f -> "assignedBy=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalIsAutoMetaTagged().map(f -> "isAutoMetaTagged=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalMetaTagId().map(f -> "metaTagId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
