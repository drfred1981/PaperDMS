package fr.smartprod.paperdms.archive.domain;

import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveFormat;
import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArchiveJob.
 */
@Entity
@Table(name = "archive_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArchiveJob implements Serializable {

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

    @Lob
    @Column(name = "document_query", nullable = false)
    private String documentQuery;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "archive_format", nullable = false)
    private ArchiveFormat archiveFormat;

    @Min(value = 0)
    @Max(value = 9)
    @Column(name = "compression_level")
    private Integer compressionLevel;

    @NotNull
    @Column(name = "encryption_enabled", nullable = false)
    private Boolean encryptionEnabled;

    @Size(max = 50)
    @Column(name = "encryption_algorithm", length = 50)
    private String encryptionAlgorithm;

    @Size(max = 255)
    @Column(name = "password", length = 255)
    private String password;

    @Size(max = 1000)
    @Column(name = "s_3_archive_key", length = 1000)
    private String s3ArchiveKey;

    @Size(max = 64)
    @Column(name = "archive_sha_256", length = 64)
    private String archiveSha256;

    @Column(name = "archive_size")
    private Long archiveSize;

    @Column(name = "document_count")
    private Integer documentCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ArchiveStatus status;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArchiveJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ArchiveJob name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ArchiveJob description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentQuery() {
        return this.documentQuery;
    }

    public ArchiveJob documentQuery(String documentQuery) {
        this.setDocumentQuery(documentQuery);
        return this;
    }

    public void setDocumentQuery(String documentQuery) {
        this.documentQuery = documentQuery;
    }

    public ArchiveFormat getArchiveFormat() {
        return this.archiveFormat;
    }

    public ArchiveJob archiveFormat(ArchiveFormat archiveFormat) {
        this.setArchiveFormat(archiveFormat);
        return this;
    }

    public void setArchiveFormat(ArchiveFormat archiveFormat) {
        this.archiveFormat = archiveFormat;
    }

    public Integer getCompressionLevel() {
        return this.compressionLevel;
    }

    public ArchiveJob compressionLevel(Integer compressionLevel) {
        this.setCompressionLevel(compressionLevel);
        return this;
    }

    public void setCompressionLevel(Integer compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public Boolean getEncryptionEnabled() {
        return this.encryptionEnabled;
    }

    public ArchiveJob encryptionEnabled(Boolean encryptionEnabled) {
        this.setEncryptionEnabled(encryptionEnabled);
        return this;
    }

    public void setEncryptionEnabled(Boolean encryptionEnabled) {
        this.encryptionEnabled = encryptionEnabled;
    }

    public String getEncryptionAlgorithm() {
        return this.encryptionAlgorithm;
    }

    public ArchiveJob encryptionAlgorithm(String encryptionAlgorithm) {
        this.setEncryptionAlgorithm(encryptionAlgorithm);
        return this;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getPassword() {
        return this.password;
    }

    public ArchiveJob password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String gets3ArchiveKey() {
        return this.s3ArchiveKey;
    }

    public ArchiveJob s3ArchiveKey(String s3ArchiveKey) {
        this.sets3ArchiveKey(s3ArchiveKey);
        return this;
    }

    public void sets3ArchiveKey(String s3ArchiveKey) {
        this.s3ArchiveKey = s3ArchiveKey;
    }

    public String getArchiveSha256() {
        return this.archiveSha256;
    }

    public ArchiveJob archiveSha256(String archiveSha256) {
        this.setArchiveSha256(archiveSha256);
        return this;
    }

    public void setArchiveSha256(String archiveSha256) {
        this.archiveSha256 = archiveSha256;
    }

    public Long getArchiveSize() {
        return this.archiveSize;
    }

    public ArchiveJob archiveSize(Long archiveSize) {
        this.setArchiveSize(archiveSize);
        return this;
    }

    public void setArchiveSize(Long archiveSize) {
        this.archiveSize = archiveSize;
    }

    public Integer getDocumentCount() {
        return this.documentCount;
    }

    public ArchiveJob documentCount(Integer documentCount) {
        this.setDocumentCount(documentCount);
        return this;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public ArchiveStatus getStatus() {
        return this.status;
    }

    public ArchiveJob status(ArchiveStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ArchiveStatus status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public ArchiveJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public ArchiveJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ArchiveJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ArchiveJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ArchiveJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchiveJob)) {
            return false;
        }
        return getId() != null && getId().equals(((ArchiveJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveJob{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentQuery='" + getDocumentQuery() + "'" +
            ", archiveFormat='" + getArchiveFormat() + "'" +
            ", compressionLevel=" + getCompressionLevel() +
            ", encryptionEnabled='" + getEncryptionEnabled() + "'" +
            ", encryptionAlgorithm='" + getEncryptionAlgorithm() + "'" +
            ", password='" + getPassword() + "'" +
            ", s3ArchiveKey='" + gets3ArchiveKey() + "'" +
            ", archiveSha256='" + getArchiveSha256() + "'" +
            ", archiveSize=" + getArchiveSize() +
            ", documentCount=" + getDocumentCount() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
