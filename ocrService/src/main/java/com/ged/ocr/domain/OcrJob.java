package com.ged.ocr.domain;

import com.ged.ocr.domain.enumeration.OcrStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Travail OCR avec Apache Tika
 */
@Entity
@Table(name = "ocr_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OcrStatus status;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000, nullable = false)
    private String s3Key;

    @NotNull
    @Size(max = 255)
    @Column(name = "s_3_bucket", length = 255, nullable = false)
    private String s3Bucket;

    @Size(max = 10)
    @Column(name = "language", length = 10)
    private String language;

    @Size(max = 500)
    @Column(name = "tika_endpoint", length = 500)
    private String tikaEndpoint;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "page_count")
    private Integer pageCount;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "progress")
    private Integer progress;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "priority")
    private Integer priority;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private TikaConfiguration tikaConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OcrJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OcrStatus getStatus() {
        return this.status;
    }

    public OcrJob status(OcrStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OcrStatus status) {
        this.status = status;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public OcrJob documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public OcrJob s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3Bucket() {
        return this.s3Bucket;
    }

    public OcrJob s3Bucket(String s3Bucket) {
        this.sets3Bucket(s3Bucket);
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getLanguage() {
        return this.language;
    }

    public OcrJob language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTikaEndpoint() {
        return this.tikaEndpoint;
    }

    public OcrJob tikaEndpoint(String tikaEndpoint) {
        this.setTikaEndpoint(tikaEndpoint);
        return this;
    }

    public void setTikaEndpoint(String tikaEndpoint) {
        this.tikaEndpoint = tikaEndpoint;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public OcrJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public OcrJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public OcrJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public OcrJob pageCount(Integer pageCount) {
        this.setPageCount(pageCount);
        return this;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getProgress() {
        return this.progress;
    }

    public OcrJob progress(Integer progress) {
        this.setProgress(progress);
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public OcrJob retryCount(Integer retryCount) {
        this.setRetryCount(retryCount);
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public OcrJob priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public OcrJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public OcrJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public TikaConfiguration getTikaConfig() {
        return this.tikaConfig;
    }

    public void setTikaConfig(TikaConfiguration tikaConfiguration) {
        this.tikaConfig = tikaConfiguration;
    }

    public OcrJob tikaConfig(TikaConfiguration tikaConfiguration) {
        this.setTikaConfig(tikaConfiguration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrJob)) {
            return false;
        }
        return getId() != null && getId().equals(((OcrJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrJob{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", documentId=" + getDocumentId() +
            ", s3Key='" + gets3Key() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", language='" + getLanguage() + "'" +
            ", tikaEndpoint='" + getTikaEndpoint() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", pageCount=" + getPageCount() +
            ", progress=" + getProgress() +
            ", retryCount=" + getRetryCount() +
            ", priority=" + getPriority() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
