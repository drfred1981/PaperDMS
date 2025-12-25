package fr.smartprod.paperdms.export.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExportPattern.
 */
@Entity
@Table(name = "export_pattern")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Size(max = 1000)
    @Column(name = "path_template", length = 1000, nullable = false)
    private String pathTemplate;

    @NotNull
    @Size(max = 500)
    @Column(name = "file_name_template", length = 500, nullable = false)
    private String fileNameTemplate;

    @Lob
    @Column(name = "variables")
    private String variables;

    @Lob
    @Column(name = "examples")
    private String examples;

    @NotNull
    @Column(name = "is_system", nullable = false)
    private Boolean isSystem;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "usage_count")
    private Integer usageCount;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExportPattern id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ExportPattern name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ExportPattern description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathTemplate() {
        return this.pathTemplate;
    }

    public ExportPattern pathTemplate(String pathTemplate) {
        this.setPathTemplate(pathTemplate);
        return this;
    }

    public void setPathTemplate(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public String getFileNameTemplate() {
        return this.fileNameTemplate;
    }

    public ExportPattern fileNameTemplate(String fileNameTemplate) {
        this.setFileNameTemplate(fileNameTemplate);
        return this;
    }

    public void setFileNameTemplate(String fileNameTemplate) {
        this.fileNameTemplate = fileNameTemplate;
    }

    public String getVariables() {
        return this.variables;
    }

    public ExportPattern variables(String variables) {
        this.setVariables(variables);
        return this;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getExamples() {
        return this.examples;
    }

    public ExportPattern examples(String examples) {
        this.setExamples(examples);
        return this;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public ExportPattern isSystem(Boolean isSystem) {
        this.setIsSystem(isSystem);
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ExportPattern isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getUsageCount() {
        return this.usageCount;
    }

    public ExportPattern usageCount(Integer usageCount) {
        this.setUsageCount(usageCount);
        return this;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ExportPattern createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ExportPattern createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ExportPattern lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExportPattern)) {
            return false;
        }
        return getId() != null && getId().equals(((ExportPattern) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportPattern{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pathTemplate='" + getPathTemplate() + "'" +
            ", fileNameTemplate='" + getFileNameTemplate() + "'" +
            ", variables='" + getVariables() + "'" +
            ", examples='" + getExamples() + "'" +
            ", isSystem='" + getIsSystem() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", usageCount=" + getUsageCount() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
