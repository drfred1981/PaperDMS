package fr.smartprod.paperdms.scan.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.scan.domain.ScannedPage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScannedPageDTO implements Serializable {

    private Long id;

    @NotNull
    private Long scanJobId;

    @NotNull
    private Integer pageNumber;

    @NotNull
    @Size(max = 64)
    private String sha256;

    @NotNull
    @Size(max = 1000)
    private String s3Key;

    @Size(max = 1000)
    private String s3PreviewKey;

    private Long fileSize;

    private Integer width;

    private Integer height;

    private Integer dpi;

    private Long documentId;

    @NotNull
    private Instant scannedDate;

    @NotNull
    private ScanJobDTO scanJob;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScanJobId() {
        return scanJobId;
    }

    public void setScanJobId(Long scanJobId) {
        this.scanJobId = scanJobId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String gets3Key() {
        return s3Key;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3PreviewKey() {
        return s3PreviewKey;
    }

    public void sets3PreviewKey(String s3PreviewKey) {
        this.s3PreviewKey = s3PreviewKey;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDpi() {
        return dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Instant getScannedDate() {
        return scannedDate;
    }

    public void setScannedDate(Instant scannedDate) {
        this.scannedDate = scannedDate;
    }

    public ScanJobDTO getScanJob() {
        return scanJob;
    }

    public void setScanJob(ScanJobDTO scanJob) {
        this.scanJob = scanJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScannedPageDTO)) {
            return false;
        }

        ScannedPageDTO scannedPageDTO = (ScannedPageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scannedPageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScannedPageDTO{" +
            "id=" + getId() +
            ", scanJobId=" + getScanJobId() +
            ", pageNumber=" + getPageNumber() +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", s3PreviewKey='" + gets3PreviewKey() + "'" +
            ", fileSize=" + getFileSize() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", dpi=" + getDpi() +
            ", documentId=" + getDocumentId() +
            ", scannedDate='" + getScannedDate() + "'" +
            ", scanJob=" + getScanJob() +
            "}";
    }
}
