package fr.smartprod.paperdms.emailimport.service.dto;

import fr.smartprod.paperdms.emailimport.domain.enumeration.EmailField;
import fr.smartprod.paperdms.emailimport.domain.enumeration.MappingTransformation;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.emailimport.domain.ImportMapping} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImportMappingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long ruleId;

    @NotNull
    private EmailField emailField;

    @NotNull
    @Size(max = 100)
    private String documentField;

    private MappingTransformation transformation;

    @Lob
    private String transformationConfig;

    @NotNull
    private Boolean isRequired;

    @Size(max = 500)
    private String defaultValue;

    @Size(max = 500)
    private String validationRegex;

    @NotNull
    private ImportRuleDTO rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public EmailField getEmailField() {
        return emailField;
    }

    public void setEmailField(EmailField emailField) {
        this.emailField = emailField;
    }

    public String getDocumentField() {
        return documentField;
    }

    public void setDocumentField(String documentField) {
        this.documentField = documentField;
    }

    public MappingTransformation getTransformation() {
        return transformation;
    }

    public void setTransformation(MappingTransformation transformation) {
        this.transformation = transformation;
    }

    public String getTransformationConfig() {
        return transformationConfig;
    }

    public void setTransformationConfig(String transformationConfig) {
        this.transformationConfig = transformationConfig;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValidationRegex() {
        return validationRegex;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    public ImportRuleDTO getRule() {
        return rule;
    }

    public void setRule(ImportRuleDTO rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImportMappingDTO)) {
            return false;
        }

        ImportMappingDTO importMappingDTO = (ImportMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, importMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImportMappingDTO{" +
            "id=" + getId() +
            ", ruleId=" + getRuleId() +
            ", emailField='" + getEmailField() + "'" +
            ", documentField='" + getDocumentField() + "'" +
            ", transformation='" + getTransformation() + "'" +
            ", transformationConfig='" + getTransformationConfig() + "'" +
            ", isRequired='" + getIsRequired() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", validationRegex='" + getValidationRegex() + "'" +
            ", rule=" + getRule() +
            "}";
    }
}
