package fr.smartprod.paperdms.scan.domain;

import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ScanJob.
 */
@Entity
@Table(name = "scan_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScanJob implements Serializable {

    @Serial
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

    @NotNull
    @Column(name = "scanner_config_id", nullable = false)
    private Long scannerConfigId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "page_count")
    private Integer pageCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScanStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_mode")
    private ColorMode colorMode;

    @Column(name = "resolution")
    private Integer resolution;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_format")
    private ScanFormat fileFormat;

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

    @ManyToOne(optional = false)
    @NotNull
    private ScannerConfiguration scannerConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    private ScanBatch batch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScanJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ScanJob name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ScanJob description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getScannerConfigId() {
        return this.scannerConfigId;
    }

    public ScanJob scannerConfigId(Long scannerConfigId) {
        this.setScannerConfigId(scannerConfigId);
        return this;
    }

    public void setScannerConfigId(Long scannerConfigId) {
        this.scannerConfigId = scannerConfigId;
    }

    public Long getBatchId() {
        return this.batchId;
    }

    public ScanJob batchId(Long batchId) {
        this.setBatchId(batchId);
        return this;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getDocumentTypeId() {
        return this.documentTypeId;
    }

    public ScanJob documentTypeId(Long documentTypeId) {
        this.setDocumentTypeId(documentTypeId);
        return this;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public ScanJob folderId(Long folderId) {
        this.setFolderId(folderId);
        return this;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public ScanJob pageCount(Integer pageCount) {
        this.setPageCount(pageCount);
        return this;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public ScanStatus getStatus() {
        return this.status;
    }

    public ScanJob status(ScanStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }

    public ColorMode getColorMode() {
        return this.colorMode;
    }

    public ScanJob colorMode(ColorMode colorMode) {
        this.setColorMode(colorMode);
        return this;
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

    public Integer getResolution() {
        return this.resolution;
    }

    public ScanJob resolution(Integer resolution) {
        this.setResolution(resolution);
        return this;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public ScanFormat getFileFormat() {
        return this.fileFormat;
    }

    public ScanJob fileFormat(ScanFormat fileFormat) {
        this.setFileFormat(fileFormat);
        return this;
    }

    public void setFileFormat(ScanFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public ScanJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public ScanJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ScanJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ScanJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ScanJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public ScannerConfiguration getScannerConfig() {
        return this.scannerConfig;
    }

    public void setScannerConfig(ScannerConfiguration scannerConfiguration) {
        this.scannerConfig = scannerConfiguration;
    }

    public ScanJob scannerConfig(ScannerConfiguration scannerConfiguration) {
        this.setScannerConfig(scannerConfiguration);
        return this;
    }

    public ScanBatch getBatch() {
        return this.batch;
    }

    public void setBatch(ScanBatch scanBatch) {
        this.batch = scanBatch;
    }

    public ScanJob batch(ScanBatch scanBatch) {
        this.setBatch(scanBatch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScanJob)) {
            return false;
        }
        return getId() != null && getId().equals(((ScanJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScanJob{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", scannerConfigId=" + getScannerConfigId() +
            ", batchId=" + getBatchId() +
            ", documentTypeId=" + getDocumentTypeId() +
            ", folderId=" + getFolderId() +
            ", pageCount=" + getPageCount() +
            ", status='" + getStatus() + "'" +
            ", colorMode='" + getColorMode() + "'" +
            ", resolution=" + getResolution() +
            ", fileFormat='" + getFileFormat() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
