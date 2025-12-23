package fr.smartprod.paperdms.scan.service.dto;

import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.scan.domain.ScanJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScanJobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @NotNull
    private Long scannerConfigId;

    private Long batchId;

    private Long documentTypeId;

    private Long folderId;

    private Integer pageCount;

    @NotNull
    private ScanStatus status;

    private ColorMode colorMode;

    private Integer resolution;

    private ScanFormat fileFormat;

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
    private ScannerConfigurationDTO scannerConfig;

    private ScanBatchDTO batch;

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

    public Long getScannerConfigId() {
        return scannerConfigId;
    }

    public void setScannerConfigId(Long scannerConfigId) {
        this.scannerConfigId = scannerConfigId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public ScanStatus getStatus() {
        return status;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public ScanFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(ScanFormat fileFormat) {
        this.fileFormat = fileFormat;
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

    public ScannerConfigurationDTO getScannerConfig() {
        return scannerConfig;
    }

    public void setScannerConfig(ScannerConfigurationDTO scannerConfig) {
        this.scannerConfig = scannerConfig;
    }

    public ScanBatchDTO getBatch() {
        return batch;
    }

    public void setBatch(ScanBatchDTO batch) {
        this.batch = batch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScanJobDTO)) {
            return false;
        }

        ScanJobDTO scanJobDTO = (ScanJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scanJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScanJobDTO{" +
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
            ", scannerConfig=" + getScannerConfig() +
            ", batch=" + getBatch() +
            "}";
    }
}
