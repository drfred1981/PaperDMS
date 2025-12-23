package fr.smartprod.paperdms.document.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentVersion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentVersionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer versionNumber;

    @NotNull
    @Size(max = 64)
    private String sha256;

    @NotNull
    @Size(max = 1000)
    private String s3Key;

    @NotNull
    private Long fileSize;

    @NotNull
    private Instant uploadDate;

    @NotNull
    private Boolean isActive;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private DocumentDTO document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentVersionDTO)) {
            return false;
        }

        DocumentVersionDTO documentVersionDTO = (DocumentVersionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentVersionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentVersionDTO{" +
            "id=" + getId() +
            ", versionNumber=" + getVersionNumber() +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", fileSize=" + getFileSize() +
            ", uploadDate='" + getUploadDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", document=" + getDocument() +
            "}";
    }
}
