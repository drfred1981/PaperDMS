package fr.smartprod.paperdms.archive.service.dto;

import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveFormat;
import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.archive.domain.ArchiveJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArchiveJobDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @Lob
    private String documentQuery;

    @NotNull
    private ArchiveFormat archiveFormat;

    @Min(value = 0)
    @Max(value = 9)
    private Integer compressionLevel;

    @NotNull
    private Boolean encryptionEnabled;

    @Size(max = 50)
    private String encryptionAlgorithm;

    @Size(max = 255)
    private String password;

    @Size(max = 1000)
    private String s3ArchiveKey;

    @Size(max = 64)
    private String archiveSha256;

    private Long archiveSize;

    private Integer documentCount;

    @NotNull
    private ArchiveStatus status;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

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

    public ArchiveFormat getArchiveFormat() {
        return archiveFormat;
    }

    public void setArchiveFormat(ArchiveFormat archiveFormat) {
        this.archiveFormat = archiveFormat;
    }

    public Integer getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(Integer compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public Boolean getEncryptionEnabled() {
        return encryptionEnabled;
    }

    public void setEncryptionEnabled(Boolean encryptionEnabled) {
        this.encryptionEnabled = encryptionEnabled;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String gets3ArchiveKey() {
        return s3ArchiveKey;
    }

    public void sets3ArchiveKey(String s3ArchiveKey) {
        this.s3ArchiveKey = s3ArchiveKey;
    }

    public String getArchiveSha256() {
        return archiveSha256;
    }

    public void setArchiveSha256(String archiveSha256) {
        this.archiveSha256 = archiveSha256;
    }

    public Long getArchiveSize() {
        return archiveSize;
    }

    public void setArchiveSize(Long archiveSize) {
        this.archiveSize = archiveSize;
    }

    public Integer getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public ArchiveStatus getStatus() {
        return status;
    }

    public void setStatus(ArchiveStatus status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchiveJobDTO)) {
            return false;
        }

        ArchiveJobDTO archiveJobDTO = (ArchiveJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, archiveJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveJobDTO{" +
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
