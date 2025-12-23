package fr.smartprod.paperdms.business.service.criteria;

import fr.smartprod.paperdms.business.domain.enumeration.ManualStatus;
import fr.smartprod.paperdms.business.domain.enumeration.ManualType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.business.domain.Manual} entity. This class is used
 * in {@link fr.smartprod.paperdms.business.web.rest.ManualResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /manuals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ManualType
     */
    public static class ManualTypeFilter extends Filter<ManualType> {

        public ManualTypeFilter() {}

        public ManualTypeFilter(ManualTypeFilter filter) {
            super(filter);
        }

        @Override
        public ManualTypeFilter copy() {
            return new ManualTypeFilter(this);
        }
    }

    /**
     * Class for filtering ManualStatus
     */
    public static class ManualStatusFilter extends Filter<ManualStatus> {

        public ManualStatusFilter() {}

        public ManualStatusFilter(ManualStatusFilter filter) {
            super(filter);
        }

        @Override
        public ManualStatusFilter copy() {
            return new ManualStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private StringFilter title;

    private ManualTypeFilter manualType;

    private StringFilter version;

    private StringFilter language;

    private LocalDateFilter publicationDate;

    private IntegerFilter pageCount;

    private ManualStatusFilter status;

    private BooleanFilter isPublic;

    private InstantFilter createdDate;

    private Boolean distinct;

    public ManualCriteria() {}

    public ManualCriteria(ManualCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.manualType = other.optionalManualType().map(ManualTypeFilter::copy).orElse(null);
        this.version = other.optionalVersion().map(StringFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(StringFilter::copy).orElse(null);
        this.publicationDate = other.optionalPublicationDate().map(LocalDateFilter::copy).orElse(null);
        this.pageCount = other.optionalPageCount().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ManualStatusFilter::copy).orElse(null);
        this.isPublic = other.optionalIsPublic().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ManualCriteria copy() {
        return new ManualCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public ManualTypeFilter getManualType() {
        return manualType;
    }

    public Optional<ManualTypeFilter> optionalManualType() {
        return Optional.ofNullable(manualType);
    }

    public ManualTypeFilter manualType() {
        if (manualType == null) {
            setManualType(new ManualTypeFilter());
        }
        return manualType;
    }

    public void setManualType(ManualTypeFilter manualType) {
        this.manualType = manualType;
    }

    public StringFilter getVersion() {
        return version;
    }

    public Optional<StringFilter> optionalVersion() {
        return Optional.ofNullable(version);
    }

    public StringFilter version() {
        if (version == null) {
            setVersion(new StringFilter());
        }
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public Optional<StringFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public StringFilter language() {
        if (language == null) {
            setLanguage(new StringFilter());
        }
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public LocalDateFilter getPublicationDate() {
        return publicationDate;
    }

    public Optional<LocalDateFilter> optionalPublicationDate() {
        return Optional.ofNullable(publicationDate);
    }

    public LocalDateFilter publicationDate() {
        if (publicationDate == null) {
            setPublicationDate(new LocalDateFilter());
        }
        return publicationDate;
    }

    public void setPublicationDate(LocalDateFilter publicationDate) {
        this.publicationDate = publicationDate;
    }

    public IntegerFilter getPageCount() {
        return pageCount;
    }

    public Optional<IntegerFilter> optionalPageCount() {
        return Optional.ofNullable(pageCount);
    }

    public IntegerFilter pageCount() {
        if (pageCount == null) {
            setPageCount(new IntegerFilter());
        }
        return pageCount;
    }

    public void setPageCount(IntegerFilter pageCount) {
        this.pageCount = pageCount;
    }

    public ManualStatusFilter getStatus() {
        return status;
    }

    public Optional<ManualStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ManualStatusFilter status() {
        if (status == null) {
            setStatus(new ManualStatusFilter());
        }
        return status;
    }

    public void setStatus(ManualStatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public Optional<BooleanFilter> optionalIsPublic() {
        return Optional.ofNullable(isPublic);
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            setIsPublic(new BooleanFilter());
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
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
        final ManualCriteria that = (ManualCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(manualType, that.manualType) &&
            Objects.equals(version, that.version) &&
            Objects.equals(language, that.language) &&
            Objects.equals(publicationDate, that.publicationDate) &&
            Objects.equals(pageCount, that.pageCount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            title,
            manualType,
            version,
            language,
            publicationDate,
            pageCount,
            status,
            isPublic,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalManualType().map(f -> "manualType=" + f + ", ").orElse("") +
            optionalVersion().map(f -> "version=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalPublicationDate().map(f -> "publicationDate=" + f + ", ").orElse("") +
            optionalPageCount().map(f -> "pageCount=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIsPublic().map(f -> "isPublic=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
