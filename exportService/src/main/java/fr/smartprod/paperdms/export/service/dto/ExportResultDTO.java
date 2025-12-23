package fr.smartprod.paperdms.export.service.dto;

import fr.smartprod.paperdms.export.domain.enumeration.ExportResultStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.export.domain.ExportResult} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportResultDTO implements Serializable {

    private Long id;

    @NotNull
    private Long exportJobId;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @Size(max = 500)
    private String originalFileName;

    @Size(max = 1000)
    private String exportedPath;

    @Size(max = 500)
    private String exportedFileName;

    @Size(max = 1000)
    private String s3ExportKey;

    private Long fileSize;

    private ExportResultStatus status;

    @Lob
    private String errorMessage;

    @NotNull
    private Instant exportedDate;

    @NotNull
    private ExportJobDTO exportJob;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExportJobId() {
        return exportJobId;
    }

    public void setExportJobId(Long exportJobId) {
        this.exportJobId = exportJobId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getExportedPath() {
        return exportedPath;
    }

    public void setExportedPath(String exportedPath) {
        this.exportedPath = exportedPath;
    }

    public String getExportedFileName() {
        return exportedFileName;
    }

    public void setExportedFileName(String exportedFileName) {
        this.exportedFileName = exportedFileName;
    }

    public String gets3ExportKey() {
        return s3ExportKey;
    }

    public void sets3ExportKey(String s3ExportKey) {
        this.s3ExportKey = s3ExportKey;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ExportResultStatus getStatus() {
        return status;
    }

    public void setStatus(ExportResultStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getExportedDate() {
        return exportedDate;
    }

    public void setExportedDate(Instant exportedDate) {
        this.exportedDate = exportedDate;
    }

    public ExportJobDTO getExportJob() {
        return exportJob;
    }

    public void setExportJob(ExportJobDTO exportJob) {
        this.exportJob = exportJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExportResultDTO)) {
            return false;
        }

        ExportResultDTO exportResultDTO = (ExportResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exportResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportResultDTO{" +
            "id=" + getId() +
            ", exportJobId=" + getExportJobId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", exportedPath='" + getExportedPath() + "'" +
            ", exportedFileName='" + getExportedFileName() + "'" +
            ", s3ExportKey='" + gets3ExportKey() + "'" +
            ", fileSize=" + getFileSize() +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", exportedDate='" + getExportedDate() + "'" +
            ", exportJob=" + getExportJob() +
            "}";
    }
}
