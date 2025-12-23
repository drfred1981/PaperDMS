package fr.smartprod.paperdms.emailimport.domain;

import fr.smartprod.paperdms.emailimport.domain.enumeration.EmailField;
import fr.smartprod.paperdms.emailimport.domain.enumeration.MappingTransformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImportMapping.
 */
@Entity
@Table(name = "import_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImportMapping implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "email_field", nullable = false)
    private EmailField emailField;

    @NotNull
    @Size(max = 100)
    @Column(name = "document_field", length = 100, nullable = false)
    private String documentField;

    @Enumerated(EnumType.STRING)
    @Column(name = "transformation")
    private MappingTransformation transformation;

    @Lob
    @Column(name = "transformation_config")
    private String transformationConfig;

    @NotNull
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @Size(max = 500)
    @Column(name = "default_value", length = 500)
    private String defaultValue;

    @Size(max = 500)
    @Column(name = "validation_regex", length = 500)
    private String validationRegex;

    @ManyToOne(optional = false)
    @NotNull
    private ImportRule rule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImportMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return this.ruleId;
    }

    public ImportMapping ruleId(Long ruleId) {
        this.setRuleId(ruleId);
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public EmailField getEmailField() {
        return this.emailField;
    }

    public ImportMapping emailField(EmailField emailField) {
        this.setEmailField(emailField);
        return this;
    }

    public void setEmailField(EmailField emailField) {
        this.emailField = emailField;
    }

    public String getDocumentField() {
        return this.documentField;
    }

    public ImportMapping documentField(String documentField) {
        this.setDocumentField(documentField);
        return this;
    }

    public void setDocumentField(String documentField) {
        this.documentField = documentField;
    }

    public MappingTransformation getTransformation() {
        return this.transformation;
    }

    public ImportMapping transformation(MappingTransformation transformation) {
        this.setTransformation(transformation);
        return this;
    }

    public void setTransformation(MappingTransformation transformation) {
        this.transformation = transformation;
    }

    public String getTransformationConfig() {
        return this.transformationConfig;
    }

    public ImportMapping transformationConfig(String transformationConfig) {
        this.setTransformationConfig(transformationConfig);
        return this;
    }

    public void setTransformationConfig(String transformationConfig) {
        this.transformationConfig = transformationConfig;
    }

    public Boolean getIsRequired() {
        return this.isRequired;
    }

    public ImportMapping isRequired(Boolean isRequired) {
        this.setIsRequired(isRequired);
        return this;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public ImportMapping defaultValue(String defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValidationRegex() {
        return this.validationRegex;
    }

    public ImportMapping validationRegex(String validationRegex) {
        this.setValidationRegex(validationRegex);
        return this;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    public ImportRule getRule() {
        return this.rule;
    }

    public void setRule(ImportRule importRule) {
        this.rule = importRule;
    }

    public ImportMapping rule(ImportRule importRule) {
        this.setRule(importRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImportMapping)) {
            return false;
        }
        return getId() != null && getId().equals(((ImportMapping) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImportMapping{" +
            "id=" + getId() +
            ", ruleId=" + getRuleId() +
            ", emailField='" + getEmailField() + "'" +
            ", documentField='" + getDocumentField() + "'" +
            ", transformation='" + getTransformation() + "'" +
            ", transformationConfig='" + getTransformationConfig() + "'" +
            ", isRequired='" + getIsRequired() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", validationRegex='" + getValidationRegex() + "'" +
            "}";
    }
}
