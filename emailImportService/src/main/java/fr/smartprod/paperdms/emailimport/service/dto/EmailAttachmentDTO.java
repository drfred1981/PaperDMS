package fr.smartprod.paperdms.emailimport.service.dto;

import fr.smartprod.paperdms.emailimport.domain.enumeration.AttachmentStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.emailimport.domain.EmailAttachment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailAttachmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Long emailImportId;

    @NotNull
    @Size(max = 500)
    private String fileName;

    @NotNull
    private Long fileSize;

    @NotNull
    @Size(max = 100)
    private String mimeType;

    @NotNull
    @Size(max = 64)
    private String sha256;

    @Size(max = 1000)
    private String s3Key;

    private Long documentId;

    @NotNull
    private AttachmentStatus status;

    @Lob
    private String errorMessage;

    @NotNull
    private EmailImportDTO emailImport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmailImportId() {
        return emailImportId;
    }

    public void setEmailImportId(Long emailImportId) {
        this.emailImportId = emailImportId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public AttachmentStatus getStatus() {
        return status;
    }

    public void setStatus(AttachmentStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EmailImportDTO getEmailImport() {
        return emailImport;
    }

    public void setEmailImport(EmailImportDTO emailImport) {
        this.emailImport = emailImport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailAttachmentDTO)) {
            return false;
        }

        EmailAttachmentDTO emailAttachmentDTO = (EmailAttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailAttachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailAttachmentDTO{" +
            "id=" + getId() +
            ", emailImportId=" + getEmailImportId() +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", documentId=" + getDocumentId() +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", emailImport=" + getEmailImport() +
            "}";
    }
}
