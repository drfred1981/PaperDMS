package fr.smartprod.paperdms.export.service.dto;

import fr.smartprod.paperdms.export.domain.enumeration.ExportFormat;
import fr.smartprod.paperdms.export.domain.enumeration.ExportStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.export.domain.ExportJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportJobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @Lob
    private String documentQuery;

    @NotNull
    private Long exportPatternId;

    @NotNull
    private ExportFormat exportFormat;

    @NotNull
    private Boolean includeMetadata;

    @NotNull
    private Boolean includeVersions;

    private Boolean includeComments;

    private Boolean includeAuditTrail;

    @Size(max = 1000)
    private String s3ExportKey;

    private Long exportSize;

    private Integer documentCount;

    private Integer filesGenerated;

    @NotNull
    private ExportStatus status;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    @NotNull
    private ExportPatternDTO exportPattern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentQuery() {
        return documentQuery;
    }

    public void setDocumentQuery(String documentQuery) {
        this.documentQuery = documentQuery;
    }

    public Long getExportPatternId() {
        return exportPatternId;
    }

    public void setExportPatternId(Long exportPatternId) {
        this.exportPatternId = exportPatternId;
    }

    public ExportFormat getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(ExportFormat exportFormat) {
        this.exportFormat = exportFormat;
    }

    public Boolean getIncludeMetadata() {
        return includeMetadata;
    }

    public void setIncludeMetadata(Boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public Boolean getIncludeVersions() {
        return includeVersions;
    }

    public void setIncludeVersions(Boolean includeVersions) {
        this.includeVersions = includeVersions;
    }

    public Boolean getIncludeComments() {
        return includeComments;
    }

    public void setIncludeComments(Boolean includeComments) {
        this.includeComments = includeComments;
    }

    public Boolean getIncludeAuditTrail() {
        return includeAuditTrail;
    }

    public void setIncludeAuditTrail(Boolean includeAuditTrail) {
        this.includeAuditTrail = includeAuditTrail;
    }

    public String gets3ExportKey() {
        return s3ExportKey;
    }

    public void sets3ExportKey(String s3ExportKey) {
        this.s3ExportKey = s3ExportKey;
    }

    public Long getExportSize() {
        return exportSize;
    }

    public void setExportSize(Long exportSize) {
        this.exportSize = exportSize;
    }

    public Integer getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Integer getFilesGenerated() {
        return filesGenerated;
    }

    public void setFilesGenerated(Integer filesGenerated) {
        this.filesGenerated = filesGenerated;
    }

    public ExportStatus getStatus() {
        return status;
    }

    public void setStatus(ExportStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public ExportPatternDTO getExportPattern() {
        return exportPattern;
    }

    public void setExportPattern(ExportPatternDTO exportPattern) {
        this.exportPattern = exportPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExportJobDTO)) {
            return false;
        }

        ExportJobDTO exportJobDTO = (ExportJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exportJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportJobDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentQuery='" + getDocumentQuery() + "'" +
            ", exportPatternId=" + getExportPatternId() +
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
            ", exportPattern=" + getExportPattern() +
            "}";
    }
}
