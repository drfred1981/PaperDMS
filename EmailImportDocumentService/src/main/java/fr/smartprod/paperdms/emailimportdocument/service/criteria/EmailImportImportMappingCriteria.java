package fr.smartprod.paperdms.emailimportdocument.service.criteria;

import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.EmailField;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.MappingTransformation;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping} entity. This class is used
 * in {@link fr.smartprod.paperdms.emailimportdocument.web.rest.EmailImportImportMappingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-import-import-mappings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportImportMappingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EmailField
     */
    public static class EmailFieldFilter extends Filter<EmailField> {

        public EmailFieldFilter() {}

        public EmailFieldFilter(EmailFieldFilter filter) {
            super(filter);
        }

        @Override
        public EmailFieldFilter copy() {
            return new EmailFieldFilter(this);
        }
    }

    /**
     * Class for filtering MappingTransformation
     */
    public static class MappingTransformationFilter extends Filter<MappingTransformation> {

        public MappingTransformationFilter() {}

        public MappingTransformationFilter(MappingTransformationFilter filter) {
            super(filter);
        }

        @Override
        public MappingTransformationFilter copy() {
            return new MappingTransformationFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EmailFieldFilter emailField;

    private StringFilter documentField;

    private MappingTransformationFilter transformation;

    private BooleanFilter isRequired;

    private StringFilter defaultValue;

    private StringFilter validationRegex;

    private LongFilter ruleId;

    private Boolean distinct;

    public EmailImportImportMappingCriteria() {}

    public EmailImportImportMappingCriteria(EmailImportImportMappingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.emailField = other.optionalEmailField().map(EmailFieldFilter::copy).orElse(null);
        this.documentField = other.optionalDocumentField().map(StringFilter::copy).orElse(null);
        this.transformation = other.optionalTransformation().map(MappingTransformationFilter::copy).orElse(null);
        this.isRequired = other.optionalIsRequired().map(BooleanFilter::copy).orElse(null);
        this.defaultValue = other.optionalDefaultValue().map(StringFilter::copy).orElse(null);
        this.validationRegex = other.optionalValidationRegex().map(StringFilter::copy).orElse(null);
        this.ruleId = other.optionalRuleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmailImportImportMappingCriteria copy() {
        return new EmailImportImportMappingCriteria(this);
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

    public EmailFieldFilter getEmailField() {
        return emailField;
    }

    public Optional<EmailFieldFilter> optionalEmailField() {
        return Optional.ofNullable(emailField);
    }

    public EmailFieldFilter emailField() {
        if (emailField == null) {
            setEmailField(new EmailFieldFilter());
        }
        return emailField;
    }

    public void setEmailField(EmailFieldFilter emailField) {
        this.emailField = emailField;
    }

    public StringFilter getDocumentField() {
        return documentField;
    }

    public Optional<StringFilter> optionalDocumentField() {
        return Optional.ofNullable(documentField);
    }

    public StringFilter documentField() {
        if (documentField == null) {
            setDocumentField(new StringFilter());
        }
        return documentField;
    }

    public void setDocumentField(StringFilter documentField) {
        this.documentField = documentField;
    }

    public MappingTransformationFilter getTransformation() {
        return transformation;
    }

    public Optional<MappingTransformationFilter> optionalTransformation() {
        return Optional.ofNullable(transformation);
    }

    public MappingTransformationFilter transformation() {
        if (transformation == null) {
            setTransformation(new MappingTransformationFilter());
        }
        return transformation;
    }

    public void setTransformation(MappingTransformationFilter transformation) {
        this.transformation = transformation;
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

    public StringFilter getDefaultValue() {
        return defaultValue;
    }

    public Optional<StringFilter> optionalDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public StringFilter defaultValue() {
        if (defaultValue == null) {
            setDefaultValue(new StringFilter());
        }
        return defaultValue;
    }

    public void setDefaultValue(StringFilter defaultValue) {
        this.defaultValue = defaultValue;
    }

    public StringFilter getValidationRegex() {
        return validationRegex;
    }

    public Optional<StringFilter> optionalValidationRegex() {
        return Optional.ofNullable(validationRegex);
    }

    public StringFilter validationRegex() {
        if (validationRegex == null) {
            setValidationRegex(new StringFilter());
        }
        return validationRegex;
    }

    public void setValidationRegex(StringFilter validationRegex) {
        this.validationRegex = validationRegex;
    }

    public LongFilter getRuleId() {
        return ruleId;
    }

    public Optional<LongFilter> optionalRuleId() {
        return Optional.ofNullable(ruleId);
    }

    public LongFilter ruleId() {
        if (ruleId == null) {
            setRuleId(new LongFilter());
        }
        return ruleId;
    }

    public void setRuleId(LongFilter ruleId) {
        this.ruleId = ruleId;
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
        final EmailImportImportMappingCriteria that = (EmailImportImportMappingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(emailField, that.emailField) &&
            Objects.equals(documentField, that.documentField) &&
            Objects.equals(transformation, that.transformation) &&
            Objects.equals(isRequired, that.isRequired) &&
            Objects.equals(defaultValue, that.defaultValue) &&
            Objects.equals(validationRegex, that.validationRegex) &&
            Objects.equals(ruleId, that.ruleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, emailField, documentField, transformation, isRequired, defaultValue, validationRegex, ruleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportImportMappingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEmailField().map(f -> "emailField=" + f + ", ").orElse("") +
            optionalDocumentField().map(f -> "documentField=" + f + ", ").orElse("") +
            optionalTransformation().map(f -> "transformation=" + f + ", ").orElse("") +
            optionalIsRequired().map(f -> "isRequired=" + f + ", ").orElse("") +
            optionalDefaultValue().map(f -> "defaultValue=" + f + ", ").orElse("") +
            optionalValidationRegex().map(f -> "validationRegex=" + f + ", ").orElse("") +
            optionalRuleId().map(f -> "ruleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
