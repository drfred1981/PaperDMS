package fr.smartprod.paperdms.emailimportdocument.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.AttachmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailImportEmailAttachment.
 */
@Entity
@Table(name = "email_import_email_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportEmailAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "file_name", length = 500, nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @NotNull
    @Size(max = 100)
    @Column(name = "mime_type", length = 100, nullable = false)
    private String mimeType;

    @NotNull
    @Size(max = 64)
    @Column(name = "sha_256", length = 64, nullable = false)
    private String sha256;

    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000)
    private String s3Key;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttachmentStatus status;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64)
    private String documentSha256;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emailImportEmailAttachments", "appliedRule" }, allowSetters = true)
    private EmailImportDocument emailImportDocument;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailImportEmailAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public EmailImportEmailAttachment fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public EmailImportEmailAttachment fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public EmailImportEmailAttachment mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSha256() {
        return this.sha256;
    }

    public EmailImportEmailAttachment sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public EmailImportEmailAttachment s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public AttachmentStatus getStatus() {
        return this.status;
    }

    public EmailImportEmailAttachment status(AttachmentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AttachmentStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public EmailImportEmailAttachment errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public EmailImportEmailAttachment documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public EmailImportDocument getEmailImportDocument() {
        return this.emailImportDocument;
    }

    public void setEmailImportDocument(EmailImportDocument emailImportDocument) {
        this.emailImportDocument = emailImportDocument;
    }

    public EmailImportEmailAttachment emailImportDocument(EmailImportDocument emailImportDocument) {
        this.setEmailImportDocument(emailImportDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImportEmailAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailImportEmailAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportEmailAttachment{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", documentSha256='" + getDocumentSha256() + "'" +
            "}";
    }
}
