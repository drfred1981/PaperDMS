package fr.smartprod.paperdms.document.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.MetaTag} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.MetaTagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /meta-tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaTagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter color;

    private StringFilter description;

    private IntegerFilter usageCount;

    private BooleanFilter isSystem;

    private InstantFilter createdDate;

    private StringFilter createdBy;

    private LongFilter documentTagsId;

    private LongFilter metaMetaTagCategoryId;

    private Boolean distinct;

    public MetaTagCriteria() {}

    public MetaTagCriteria(MetaTagCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.usageCount = other.optionalUsageCount().map(IntegerFilter::copy).orElse(null);
        this.isSystem = other.optionalIsSystem().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.documentTagsId = other.optionalDocumentTagsId().map(LongFilter::copy).orElse(null);
        this.metaMetaTagCategoryId = other.optionalMetaMetaTagCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MetaTagCriteria copy() {
        return new MetaTagCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getUsageCount() {
        return usageCount;
    }

    public Optional<IntegerFilter> optionalUsageCount() {
        return Optional.ofNullable(usageCount);
    }

    public IntegerFilter usageCount() {
        if (usageCount == null) {
            setUsageCount(new IntegerFilter());
        }
        return usageCount;
    }

    public void setUsageCount(IntegerFilter usageCount) {
        this.usageCount = usageCount;
    }

    public BooleanFilter getIsSystem() {
        return isSystem;
    }

    public Optional<BooleanFilter> optionalIsSystem() {
        return Optional.ofNullable(isSystem);
    }

    public BooleanFilter isSystem() {
        if (isSystem == null) {
            setIsSystem(new BooleanFilter());
        }
        return isSystem;
    }

    public void setIsSystem(BooleanFilter isSystem) {
        this.isSystem = isSystem;
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

    public LongFilter getDocumentTagsId() {
        return documentTagsId;
    }

    public Optional<LongFilter> optionalDocumentTagsId() {
        return Optional.ofNullable(documentTagsId);
    }

    public LongFilter documentTagsId() {
        if (documentTagsId == null) {
            setDocumentTagsId(new LongFilter());
        }
        return documentTagsId;
    }

    public void setDocumentTagsId(LongFilter documentTagsId) {
        this.documentTagsId = documentTagsId;
    }

    public LongFilter getMetaMetaTagCategoryId() {
        return metaMetaTagCategoryId;
    }

    public Optional<LongFilter> optionalMetaMetaTagCategoryId() {
        return Optional.ofNullable(metaMetaTagCategoryId);
    }

    public LongFilter metaMetaTagCategoryId() {
        if (metaMetaTagCategoryId == null) {
            setMetaMetaTagCategoryId(new LongFilter());
        }
        return metaMetaTagCategoryId;
    }

    public void setMetaMetaTagCategoryId(LongFilter metaMetaTagCategoryId) {
        this.metaMetaTagCategoryId = metaMetaTagCategoryId;
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
        final MetaTagCriteria that = (MetaTagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(color, that.color) &&
            Objects.equals(description, that.description) &&
            Objects.equals(usageCount, that.usageCount) &&
            Objects.equals(isSystem, that.isSystem) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(documentTagsId, that.documentTagsId) &&
            Objects.equals(metaMetaTagCategoryId, that.metaMetaTagCategoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            color,
            description,
            usageCount,
            isSystem,
            createdDate,
            createdBy,
            documentTagsId,
            metaMetaTagCategoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaTagCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalUsageCount().map(f -> "usageCount=" + f + ", ").orElse("") +
            optionalIsSystem().map(f -> "isSystem=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalDocumentTagsId().map(f -> "documentTagsId=" + f + ", ").orElse("") +
            optionalMetaMetaTagCategoryId().map(f -> "metaMetaTagCategoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
