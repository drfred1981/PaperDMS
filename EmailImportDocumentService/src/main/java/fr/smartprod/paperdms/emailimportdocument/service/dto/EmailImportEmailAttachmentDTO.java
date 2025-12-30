package fr.smartprod.paperdms.emailimportdocument.service.dto;

import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.AttachmentStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportEmailAttachmentDTO implements Serializable {

    private Long id;

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

    @NotNull
    private AttachmentStatus status;

    @Lob
    private String errorMessage;

    @Size(max = 64)
    private String documentSha256;

    private EmailImportDocumentDTO emailImportDocument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public EmailImportDocumentDTO getEmailImportDocument() {
        return emailImportDocument;
    }

    public void setEmailImportDocument(EmailImportDocumentDTO emailImportDocument) {
        this.emailImportDocument = emailImportDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImportEmailAttachmentDTO)) {
            return false;
        }

        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = (EmailImportEmailAttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailImportEmailAttachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportEmailAttachmentDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", emailImportDocument=" + getEmailImportDocument() +
            "}";
    }
}
