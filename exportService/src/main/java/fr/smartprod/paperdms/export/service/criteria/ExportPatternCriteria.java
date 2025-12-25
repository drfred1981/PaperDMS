package fr.smartprod.paperdms.export.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.export.domain.ExportPattern} entity. This class is used
 * in {@link fr.smartprod.paperdms.export.web.rest.ExportPatternResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /export-patterns?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportPatternCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter pathTemplate;

    private StringFilter fileNameTemplate;

    private BooleanFilter isSystem;

    private BooleanFilter isActive;

    private IntegerFilter usageCount;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public ExportPatternCriteria() {}

    public ExportPatternCriteria(ExportPatternCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.pathTemplate = other.optionalPathTemplate().map(StringFilter::copy).orElse(null);
        this.fileNameTemplate = other.optionalFileNameTemplate().map(StringFilter::copy).orElse(null);
        this.isSystem = other.optionalIsSystem().map(BooleanFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.usageCount = other.optionalUsageCount().map(IntegerFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExportPatternCriteria copy() {
        return new ExportPatternCriteria(this);
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

    public StringFilter getPathTemplate() {
        return pathTemplate;
    }

    public Optional<StringFilter> optionalPathTemplate() {
        return Optional.ofNullable(pathTemplate);
    }

    public StringFilter pathTemplate() {
        if (pathTemplate == null) {
            setPathTemplate(new StringFilter());
        }
        return pathTemplate;
    }

    public void setPathTemplate(StringFilter pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public StringFilter getFileNameTemplate() {
        return fileNameTemplate;
    }

    public Optional<StringFilter> optionalFileNameTemplate() {
        return Optional.ofNullable(fileNameTemplate);
    }

    public StringFilter fileNameTemplate() {
        if (fileNameTemplate == null) {
            setFileNameTemplate(new StringFilter());
        }
        return fileNameTemplate;
    }

    public void setFileNameTemplate(StringFilter fileNameTemplate) {
        this.fileNameTemplate = fileNameTemplate;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        final ExportPatternCriteria that = (ExportPatternCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(pathTemplate, that.pathTemplate) &&
            Objects.equals(fileNameTemplate, that.fileNameTemplate) &&
            Objects.equals(isSystem, that.isSystem) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(usageCount, that.usageCount) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            pathTemplate,
            fileNameTemplate,
            isSystem,
            isActive,
            usageCount,
            createdBy,
            createdDate,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportPatternCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPathTemplate().map(f -> "pathTemplate=" + f + ", ").orElse("") +
            optionalFileNameTemplate().map(f -> "fileNameTemplate=" + f + ", ").orElse("") +
            optionalIsSystem().map(f -> "isSystem=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalUsageCount().map(f -> "usageCount=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
