package fr.smartprod.paperdms.export.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.export.domain.enumeration.ExportResultStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExportResult.
 */
@Entity
@Table(name = "export_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExportResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    private String documentSha256;

    @Size(max = 500)
    @Column(name = "original_file_name", length = 500)
    private String originalFileName;

    @Size(max = 1000)
    @Column(name = "exported_path", length = 1000)
    private String exportedPath;

    @Size(max = 500)
    @Column(name = "exported_file_name", length = 500)
    private String exportedFileName;

    @Size(max = 1000)
    @Column(name = "s_3_export_key", length = 1000)
    private String s3ExportKey;

    @Column(name = "file_size")
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExportResultStatus status;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @NotNull
    @Column(name = "exported_date", nullable = false)
    private Instant exportedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "exportResults", "exportPattern" }, allowSetters = true)
    private ExportJob exportJob;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExportResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public ExportResult documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public ExportResult originalFileName(String originalFileName) {
        this.setOriginalFileName(originalFileName);
        return this;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getExportedPath() {
        return this.exportedPath;
    }

    public ExportResult exportedPath(String exportedPath) {
        this.setExportedPath(exportedPath);
        return this;
    }

    public void setExportedPath(String exportedPath) {
        this.exportedPath = exportedPath;
    }

    public String getExportedFileName() {
        return this.exportedFileName;
    }

    public ExportResult exportedFileName(String exportedFileName) {
        this.setExportedFileName(exportedFileName);
        return this;
    }

    public void setExportedFileName(String exportedFileName) {
        this.exportedFileName = exportedFileName;
    }

    public String gets3ExportKey() {
        return this.s3ExportKey;
    }

    public ExportResult s3ExportKey(String s3ExportKey) {
        this.sets3ExportKey(s3ExportKey);
        return this;
    }

    public void sets3ExportKey(String s3ExportKey) {
        this.s3ExportKey = s3ExportKey;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public ExportResult fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ExportResultStatus getStatus() {
        return this.status;
    }

    public ExportResult status(ExportResultStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ExportResultStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ExportResult errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getExportedDate() {
        return this.exportedDate;
    }

    public ExportResult exportedDate(Instant exportedDate) {
        this.setExportedDate(exportedDate);
        return this;
    }

    public void setExportedDate(Instant exportedDate) {
        this.exportedDate = exportedDate;
    }

    public ExportJob getExportJob() {
        return this.exportJob;
    }

    public void setExportJob(ExportJob exportJob) {
        this.exportJob = exportJob;
    }

    public ExportResult exportJob(ExportJob exportJob) {
        this.setExportJob(exportJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExportResult)) {
            return false;
        }
        return getId() != null && getId().equals(((ExportResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExportResult{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", exportedPath='" + getExportedPath() + "'" +
            ", exportedFileName='" + getExportedFileName() + "'" +
            ", s3ExportKey='" + gets3ExportKey() + "'" +
            ", fileSize=" + getFileSize() +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", exportedDate='" + getExportedDate() + "'" +
            "}";
    }
}
