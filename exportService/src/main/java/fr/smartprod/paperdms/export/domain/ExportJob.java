package fr.smartprod.paperdms.export.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.export.domain.enumeration.ExportFormat;
import fr.smartprod.paperdms.export.domain.enumeration.ExportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExportJob.
 */
@Entity
@Table(name = "export_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "document_query", nullable = false)
    private String documentQuery;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "export_format", nullable = false)
    private ExportFormat exportFormat;

    @NotNull
    @Column(name = "include_metadata", nullable = false)
    private Boolean includeMetadata;

    @NotNull
    @Column(name = "include_versions", nullable = false)
    private Boolean includeVersions;

    @Column(name = "include_comments")
    private Boolean includeComments;

    @Column(name = "include_audit_trail")
    private Boolean includeAuditTrail;

    @Size(max = 1000)
    @Column(name = "s_3_export_key", length = 1000)
    private String s3ExportKey;

    @Column(name = "export_size")
    private Long exportSize;

    @Column(name = "document_count")
    private Integer documentCount;

    @Column(name = "files_generated")
    private Integer filesGenerated;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExportStatus status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exportJob")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exportJob" }, allowSetters = true)
    private Set<ExportResult> exportResults = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "exportJobs" }, allowSetters = true)
    private ExportPattern exportPattern;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExportJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ExportJob name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ExportJob description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentQuery() {
        return this.documentQuery;
    }

    public ExportJob documentQuery(String documentQuery) {
        this.setDocumentQuery(documentQuery);
        return this;
    }

    public void setDocumentQuery(String documentQuery) {
        this.documentQuery = documentQuery;
    }

    public ExportFormat getExportFormat() {
        return this.exportFormat;
    }

    public ExportJob exportFormat(ExportFormat exportFormat) {
        this.setExportFormat(exportFormat);
        return this;
    }

    public void setExportFormat(ExportFormat exportFormat) {
        this.exportFormat = exportFormat;
    }

    public Boolean getIncludeMetadata() {
        return this.includeMetadata;
    }

    public ExportJob includeMetadata(Boolean includeMetadata) {
        this.setIncludeMetadata(includeMetadata);
        return this;
    }

    public void setIncludeMetadata(Boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public Boolean getIncludeVersions() {
        return this.includeVersions;
    }

    public ExportJob includeVersions(Boolean includeVersions) {
        this.setIncludeVersions(includeVersions);
        return this;
    }

    public void setIncludeVersions(Boolean includeVersions) {
        this.includeVersions = includeVersions;
    }

    public Boolean getIncludeComments() {
        return this.includeComments;
    }

    public ExportJob includeComments(Boolean includeComments) {
        this.setIncludeComments(includeComments);
        return this;
    }

    public void setIncludeComments(Boolean includeComments) {
        this.includeComments = includeComments;
    }

    public Boolean getIncludeAuditTrail() {
        return this.includeAuditTrail;
    }

    public ExportJob includeAuditTrail(Boolean includeAuditTrail) {
        this.setIncludeAuditTrail(includeAuditTrail);
        return this;
    }

    public void setIncludeAuditTrail(Boolean includeAuditTrail) {
        this.includeAuditTrail = includeAuditTrail;
    }

    public String gets3ExportKey() {
        return this.s3ExportKey;
    }

    public ExportJob s3ExportKey(String s3ExportKey) {
        this.sets3ExportKey(s3ExportKey);
        return this;
    }

    public void sets3ExportKey(String s3ExportKey) {
        this.s3ExportKey = s3ExportKey;
    }

    public Long getExportSize() {
        return this.exportSize;
    }

    public ExportJob exportSize(Long exportSize) {
        this.setExportSize(exportSize);
        return this;
    }

    public void setExportSize(Long exportSize) {
        this.exportSize = exportSize;
    }

    public Integer getDocumentCount() {
        return this.documentCount;
    }

    public ExportJob documentCount(Integer documentCount) {
        this.setDocumentCount(documentCount);
        return this;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Integer getFilesGenerated() {
        return this.filesGenerated;
    }

    public ExportJob filesGenerated(Integer filesGenerated) {
        this.setFilesGenerated(filesGenerated);
        return this;
    }

    public void setFilesGenerated(Integer filesGenerated) {
        this.filesGenerated = filesGenerated;
    }

    public ExportStatus getStatus() {
        return this.status;
    }

    public ExportJob status(ExportStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ExportStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public ExportJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public ExportJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ExportJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ExportJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ExportJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ExportResult> getExportResults() {
        return this.exportResults;
    }

    public void setExportResults(Set<ExportResult> exportResults) {
        if (this.exportResults != null) {
            this.exportResults.forEach(i -> i.setExportJob(null));
        }
        if (exportResults != null) {
            exportResults.forEach(i -> i.setExportJob(this));
        }
        this.exportResults = exportResults;
    }

    public ExportJob exportResults(Set<ExportResult> exportResults) {
        this.setExportResults(exportResults);
        return this;
    }

    public ExportJob addExportResults(ExportResult exportResult) {
        this.exportResults.add(exportResult);
        exportResult.setExportJob(this);
        return this;
    }

    public ExportJob removeExportResults(ExportResult exportResult) {
        this.exportResults.remove(exportResult);
        exportResult.setExportJob(null);
        return this;
    }

    public ExportPattern getExportPattern() {
        return this.exportPattern;
    }

    public void setExportPattern(ExportPattern exportPattern) {
        this.exportPattern = exportPattern;
    }

    public ExportJob exportPattern(ExportPattern exportPattern) {
        this.setExportPattern(exportPattern);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExportJob)) {
            return false;
        }
        return getId() != null && getId().equals(((ExportJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportJob{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentQuery='" + getDocumentQuery() + "'" +
            ", exportFormat='" + getExportFormat() + "'" +
            ", includeMetadata='" + getIncludeMetadata() + "'" +
            ", includeVersions='" + getIncludeVersions() + "'" +
            ", includeComments='" + getIncludeComments() + "'" +
            ", includeAuditTrail='" + getIncludeAuditTrail() + "'" +
            ", s3ExportKey='" + gets3ExportKey() + "'" +
            ", exportSize=" + getExportSize() +
            ", documentCount=" + getDocumentCount() +
            ", filesGenerated=" + getFilesGenerated() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
