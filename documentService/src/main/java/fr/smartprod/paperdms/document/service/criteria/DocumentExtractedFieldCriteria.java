package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.DocumentExtractedField} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentExtractedFieldResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-extracted-fields?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentExtractedFieldCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ExtractionMethod
     */
    public static class ExtractionMethodFilter extends Filter<ExtractionMethod> {

        public ExtractionMethodFilter() {}

        public ExtractionMethodFilter(ExtractionMethodFilter filter) {
            super(filter);
        }

        @Override
        public ExtractionMethodFilter copy() {
            return new ExtractionMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fieldKey;

    private DoubleFilter confidence;

    private ExtractionMethodFilter extractionMethod;

    private BooleanFilter isVerified;

    private InstantFilter extractedDate;

    private LongFilter documentId;

    private Boolean distinct;

    public DocumentExtractedFieldCriteria() {}

    public DocumentExtractedFieldCriteria(DocumentExtractedFieldCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fieldKey = other.optionalFieldKey().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.extractionMethod = other.optionalExtractionMethod().map(ExtractionMethodFilter::copy).orElse(null);
        this.isVerified = other.optionalIsVerified().map(BooleanFilter::copy).orElse(null);
        this.extractedDate = other.optionalExtractedDate().map(InstantFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentExtractedFieldCriteria copy() {
        return new DocumentExtractedFieldCriteria(this);
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

    public ExtractionMethodFilter getExtractionMethod() {
        return extractionMethod;
    }

    public Optional<ExtractionMethodFilter> optionalExtractionMethod() {
        return Optional.ofNullable(extractionMethod);
    }

    public ExtractionMethodFilter extractionMethod() {
        if (extractionMethod == null) {
            setExtractionMethod(new ExtractionMethodFilter());
        }
        return extractionMethod;
    }

    public void setExtractionMethod(ExtractionMethodFilter extractionMethod) {
        this.extractionMethod = extractionMethod;
    }

    public BooleanFilter getIsVerified() {
        return isVerified;
    }

    public Optional<BooleanFilter> optionalIsVerified() {
        return Optional.ofNullable(isVerified);
    }

    public BooleanFilter isVerified() {
        if (isVerified == null) {
            setIsVerified(new BooleanFilter());
        }
        return isVerified;
    }

    public void setIsVerified(BooleanFilter isVerified) {
        this.isVerified = isVerified;
    }

    public InstantFilter getExtractedDate() {
        return extractedDate;
    }

    public Optional<InstantFilter> optionalExtractedDate() {
        return Optional.ofNullable(extractedDate);
    }

    public InstantFilter extractedDate() {
        if (extractedDate == null) {
            setExtractedDate(new InstantFilter());
        }
        return extractedDate;
    }

    public void setExtractedDate(InstantFilter extractedDate) {
        this.extractedDate = extractedDate;
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
        final DocumentExtractedFieldCriteria that = (DocumentExtractedFieldCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fieldKey, that.fieldKey) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(extractionMethod, that.extractionMethod) &&
            Objects.equals(isVerified, that.isVerified) &&
            Objects.equals(extractedDate, that.extractedDate) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldKey, confidence, extractionMethod, isVerified, extractedDate, documentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentExtractedFieldCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFieldKey().map(f -> "fieldKey=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalExtractionMethod().map(f -> "extractionMethod=" + f + ", ").orElse("") +
            optionalIsVerified().map(f -> "isVerified=" + f + ", ").orElse("") +
            optionalExtractedDate().map(f -> "extractedDate=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
