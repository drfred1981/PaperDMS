package fr.smartprod.paperdms.emailimport.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.emailimport.domain.ImportRule} entity. This class is used
 * in {@link fr.smartprod.paperdms.emailimport.web.rest.ImportRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /import-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImportRuleCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter priority;

    private BooleanFilter isActive;

    private LongFilter folderId;

    private LongFilter documentTypeId;

    private IntegerFilter matchCount;

    private InstantFilter lastMatchDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public ImportRuleCriteria() {}

    public ImportRuleCriteria(ImportRuleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(IntegerFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.folderId = other.optionalFolderId().map(LongFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.matchCount = other.optionalMatchCount().map(IntegerFilter::copy).orElse(null);
        this.lastMatchDate = other.optionalLastMatchDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImportRuleCriteria copy() {
        return new ImportRuleCriteria(this);
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

    public IntegerFilter getPriority() {
        return priority;
    }

    public Optional<IntegerFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public IntegerFilter priority() {
        if (priority == null) {
            setPriority(new IntegerFilter());
        }
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
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

    public LongFilter getFolderId() {
        return folderId;
    }

    public Optional<LongFilter> optionalFolderId() {
        return Optional.ofNullable(folderId);
    }

    public LongFilter folderId() {
        if (folderId == null) {
            setFolderId(new LongFilter());
        }
        return folderId;
    }

    public void setFolderId(LongFilter folderId) {
        this.folderId = folderId;
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

    public IntegerFilter getMatchCount() {
        return matchCount;
    }

    public Optional<IntegerFilter> optionalMatchCount() {
        return Optional.ofNullable(matchCount);
    }

    public IntegerFilter matchCount() {
        if (matchCount == null) {
            setMatchCount(new IntegerFilter());
        }
        return matchCount;
    }

    public void setMatchCount(IntegerFilter matchCount) {
        this.matchCount = matchCount;
    }

    public InstantFilter getLastMatchDate() {
        return lastMatchDate;
    }

    public Optional<InstantFilter> optionalLastMatchDate() {
        return Optional.ofNullable(lastMatchDate);
    }

    public InstantFilter lastMatchDate() {
        if (lastMatchDate == null) {
            setLastMatchDate(new InstantFilter());
        }
        return lastMatchDate;
    }

    public void setLastMatchDate(InstantFilter lastMatchDate) {
        this.lastMatchDate = lastMatchDate;
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
        final ImportRuleCriteria that = (ImportRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(folderId, that.folderId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(matchCount, that.matchCount) &&
            Objects.equals(lastMatchDate, that.lastMatchDate) &&
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
            priority,
            isActive,
            folderId,
            documentTypeId,
            matchCount,
            lastMatchDate,
            createdBy,
            createdDate,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImportRuleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalFolderId().map(f -> "folderId=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalMatchCount().map(f -> "matchCount=" + f + ", ").orElse("") +
            optionalLastMatchDate().map(f -> "lastMatchDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
