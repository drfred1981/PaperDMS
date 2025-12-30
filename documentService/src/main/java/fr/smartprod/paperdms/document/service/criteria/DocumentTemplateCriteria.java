package fr.smartprod.paperdms.document.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentTemplate} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentTemplateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-templates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTemplateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter templateSha256;

    private StringFilter templateS3Key;

    private BooleanFilter isActive;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter documentTypeId;

    private Boolean distinct;

    public DocumentTemplateCriteria() {}

    public DocumentTemplateCriteria(DocumentTemplateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.templateSha256 = other.optionalTemplateSha256().map(StringFilter::copy).orElse(null);
        this.templateS3Key = other.optionalTemplateS3Key().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentTemplateCriteria copy() {
        return new DocumentTemplateCriteria(this);
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

    public StringFilter getTemplateSha256() {
        return templateSha256;
    }

    public Optional<StringFilter> optionalTemplateSha256() {
        return Optional.ofNullable(templateSha256);
    }

    public StringFilter templateSha256() {
        if (templateSha256 == null) {
            setTemplateSha256(new StringFilter());
        }
        return templateSha256;
    }

    public void setTemplateSha256(StringFilter templateSha256) {
        this.templateSha256 = templateSha256;
    }

    public StringFilter getTemplateS3Key() {
        return templateS3Key;
    }

    public Optional<StringFilter> optionalTemplateS3Key() {
        return Optional.ofNullable(templateS3Key);
    }

    public StringFilter templateS3Key() {
        if (templateS3Key == null) {
            setTemplateS3Key(new StringFilter());
        }
        return templateS3Key;
    }

    public void setTemplateS3Key(StringFilter templateS3Key) {
        this.templateS3Key = templateS3Key;
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
        final DocumentTemplateCriteria that = (DocumentTemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(templateSha256, that.templateSha256) &&
            Objects.equals(templateS3Key, that.templateS3Key) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, templateSha256, templateS3Key, isActive, createdBy, createdDate, documentTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTemplateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalTemplateSha256().map(f -> "templateSha256=" + f + ", ").orElse("") +
            optionalTemplateS3Key().map(f -> "templateS3Key=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
