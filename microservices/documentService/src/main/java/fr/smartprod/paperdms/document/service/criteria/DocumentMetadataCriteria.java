package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentMetadata} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentMetadataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-metadata?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentMetadataCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MetadataType
     */
    public static class MetadataTypeFilter extends Filter<MetadataType> {

        public MetadataTypeFilter() {}

        public MetadataTypeFilter(MetadataTypeFilter filter) {
            super(filter);
        }

        @Override
        public MetadataTypeFilter copy() {
            return new MetadataTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private MetadataTypeFilter dataType;

    private BooleanFilter isSearchable;

    private InstantFilter createdDate;

    private LongFilter documentId;

    private Boolean distinct;

    public DocumentMetadataCriteria() {}

    public DocumentMetadataCriteria(DocumentMetadataCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.key = other.optionalKey().map(StringFilter::copy).orElse(null);
        this.dataType = other.optionalDataType().map(MetadataTypeFilter::copy).orElse(null);
        this.isSearchable = other.optionalIsSearchable().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentMetadataCriteria copy() {
        return new DocumentMetadataCriteria(this);
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

    public StringFilter getKey() {
        return key;
    }

    public Optional<StringFilter> optionalKey() {
        return Optional.ofNullable(key);
    }

    public StringFilter key() {
        if (key == null) {
            setKey(new StringFilter());
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public MetadataTypeFilter getDataType() {
        return dataType;
    }

    public Optional<MetadataTypeFilter> optionalDataType() {
        return Optional.ofNullable(dataType);
    }

    public MetadataTypeFilter dataType() {
        if (dataType == null) {
            setDataType(new MetadataTypeFilter());
        }
        return dataType;
    }

    public void setDataType(MetadataTypeFilter dataType) {
        this.dataType = dataType;
    }

    public BooleanFilter getIsSearchable() {
        return isSearchable;
    }

    public Optional<BooleanFilter> optionalIsSearchable() {
        return Optional.ofNullable(isSearchable);
    }

    public BooleanFilter isSearchable() {
        if (isSearchable == null) {
            setIsSearchable(new BooleanFilter());
        }
        return isSearchable;
    }

    public void setIsSearchable(BooleanFilter isSearchable) {
        this.isSearchable = isSearchable;
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
        final DocumentMetadataCriteria that = (DocumentMetadataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(dataType, that.dataType) &&
            Objects.equals(isSearchable, that.isSearchable) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, dataType, isSearchable, createdDate, documentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentMetadataCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKey().map(f -> "key=" + f + ", ").orElse("") +
            optionalDataType().map(f -> "dataType=" + f + ", ").orElse("") +
            optionalIsSearchable().map(f -> "isSearchable=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
