package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentTypeField} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentTypeFieldResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-type-fields?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTypeFieldCriteria implements Serializable, Criteria {

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

    private StringFilter fieldKey;

    private StringFilter fieldLabel;

    private MetadataTypeFilter dataType;

    private BooleanFilter isRequired;

    private BooleanFilter isSearchable;

    private InstantFilter createdDate;

    private LongFilter documentTypeId;

    private Boolean distinct;

    public DocumentTypeFieldCriteria() {}

    public DocumentTypeFieldCriteria(DocumentTypeFieldCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fieldKey = other.optionalFieldKey().map(StringFilter::copy).orElse(null);
        this.fieldLabel = other.optionalFieldLabel().map(StringFilter::copy).orElse(null);
        this.dataType = other.optionalDataType().map(MetadataTypeFilter::copy).orElse(null);
        this.isRequired = other.optionalIsRequired().map(BooleanFilter::copy).orElse(null);
        this.isSearchable = other.optionalIsSearchable().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentTypeFieldCriteria copy() {
        return new DocumentTypeFieldCriteria(this);
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

    public StringFilter getFieldKey() {
        return fieldKey;
    }

    public Optional<StringFilter> optionalFieldKey() {
        return Optional.ofNullable(fieldKey);
    }

    public StringFilter fieldKey() {
        if (fieldKey == null) {
            setFieldKey(new StringFilter());
        }
        return fieldKey;
    }

    public void setFieldKey(StringFilter fieldKey) {
        this.fieldKey = fieldKey;
    }

    public StringFilter getFieldLabel() {
        return fieldLabel;
    }

    public Optional<StringFilter> optionalFieldLabel() {
        return Optional.ofNullable(fieldLabel);
    }

    public StringFilter fieldLabel() {
        if (fieldLabel == null) {
            setFieldLabel(new StringFilter());
        }
        return fieldLabel;
    }

    public void setFieldLabel(StringFilter fieldLabel) {
        this.fieldLabel = fieldLabel;
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

    public BooleanFilter getIsRequired() {
        return isRequired;
    }

    public Optional<BooleanFilter> optionalIsRequired() {
        return Optional.ofNullable(isRequired);
    }

    public BooleanFilter isRequired() {
        if (isRequired == null) {
            setIsRequired(new BooleanFilter());
        }
        return isRequired;
    }

    public void setIsRequired(BooleanFilter isRequired) {
        this.isRequired = isRequired;
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
        final DocumentTypeFieldCriteria that = (DocumentTypeFieldCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fieldKey, that.fieldKey) &&
            Objects.equals(fieldLabel, that.fieldLabel) &&
            Objects.equals(dataType, that.dataType) &&
            Objects.equals(isRequired, that.isRequired) &&
            Objects.equals(isSearchable, that.isSearchable) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldKey, fieldLabel, dataType, isRequired, isSearchable, createdDate, documentTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTypeFieldCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFieldKey().map(f -> "fieldKey=" + f + ", ").orElse("") +
            optionalFieldLabel().map(f -> "fieldLabel=" + f + ", ").orElse("") +
            optionalDataType().map(f -> "dataType=" + f + ", ").orElse("") +
            optionalIsRequired().map(f -> "isRequired=" + f + ", ").orElse("") +
            optionalIsSearchable().map(f -> "isSearchable=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
