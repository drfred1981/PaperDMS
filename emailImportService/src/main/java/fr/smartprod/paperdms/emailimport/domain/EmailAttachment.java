package fr.smartprod.paperdms.emailimport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.emailimport.domain.enumeration.AttachmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailAttachment.
 */
@Entity
@Table(name = "email_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email_import_id", nullable = false)
    private Long emailImportId;

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

    @Column(name = "document_id")
    private Long documentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttachmentStatus status;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "appliedRule" }, allowSetters = true)
    private EmailImport emailImport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmailImportId() {
        return this.emailImportId;
    }

    public EmailAttachment emailImportId(Long emailImportId) {
        this.setEmailImportId(emailImportId);
        return this;
    }

    public void setEmailImportId(Long emailImportId) {
        this.emailImportId = emailImportId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public EmailAttachment fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public EmailAttachment fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public EmailAttachment mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSha256() {
        return this.sha256;
    }

    public EmailAttachment sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public EmailAttachment s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public EmailAttachment documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public AttachmentStatus getStatus() {
        return this.status;
    }

    public EmailAttachment status(AttachmentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AttachmentStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public EmailAttachment errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EmailImport getEmailImport() {
        return this.emailImport;
    }

    public void setEmailImport(EmailImport emailImport) {
        this.emailImport = emailImport;
    }

    public EmailAttachment emailImport(EmailImport emailImport) {
        this.setEmailImport(emailImport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailAttachment{" +
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
            "}";
    }
}
