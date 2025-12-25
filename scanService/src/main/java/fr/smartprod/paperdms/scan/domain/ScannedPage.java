package fr.smartprod.paperdms.scan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ScannedPage.
 */
@Entity
@Table(name = "scanned_page")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScannedPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "scan_job_id", nullable = false)
    private Long scanJobId;

    @NotNull
    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @NotNull
    @Size(max = 64)
    @Column(name = "sha_256", length = 64, nullable = false)
    private String sha256;

    @NotNull
    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000, nullable = false)
    private String s3Key;

    @Size(max = 1000)
    @Column(name = "s_3_preview_key", length = 1000)
    private String s3PreviewKey;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "dpi")
    private Integer dpi;

    @Column(name = "document_id")
    private Long documentId;

    @NotNull
    @Column(name = "scanned_date", nullable = false)
    private Instant scannedDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "scannerConfig", "batch" }, allowSetters = true)
    private ScanJob scanJob;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScannedPage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScanJobId() {
        return this.scanJobId;
    }

    public ScannedPage scanJobId(Long scanJobId) {
        this.setScanJobId(scanJobId);
        return this;
    }

    public void setScanJobId(Long scanJobId) {
        this.scanJobId = scanJobId;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public ScannedPage pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSha256() {
        return this.sha256;
    }

    public ScannedPage sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public ScannedPage s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3PreviewKey() {
        return this.s3PreviewKey;
    }

    public ScannedPage s3PreviewKey(String s3PreviewKey) {
        this.sets3PreviewKey(s3PreviewKey);
        return this;
    }

    public void sets3PreviewKey(String s3PreviewKey) {
        this.s3PreviewKey = s3PreviewKey;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public ScannedPage fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getWidth() {
        return this.width;
    }

    public ScannedPage width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public ScannedPage height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDpi() {
        return this.dpi;
    }

    public ScannedPage dpi(Integer dpi) {
        this.setDpi(dpi);
        return this;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public ScannedPage documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Instant getScannedDate() {
        return this.scannedDate;
    }

    public ScannedPage scannedDate(Instant scannedDate) {
        this.setScannedDate(scannedDate);
        return this;
    }

    public void setScannedDate(Instant scannedDate) {
        this.scannedDate = scannedDate;
    }

    public ScanJob getScanJob() {
        return this.scanJob;
    }

    public void setScanJob(ScanJob scanJob) {
        this.scanJob = scanJob;
    }

    public ScannedPage scanJob(ScanJob scanJob) {
        this.setScanJob(scanJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScannedPage)) {
            return false;
        }
        return getId() != null && getId().equals(((ScannedPage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScannedPage{" +
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
            "}";
    }
}
