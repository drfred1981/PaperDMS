package fr.smartprod.paperdms.emailimportdocument.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.ImportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailImportDocument.
 */
@Entity
@Table(name = "email_import_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "sha_256", length = 64, nullable = false)
    private String sha256;

    @NotNull
    @Size(max = 255)
    @Column(name = "from_email", length = 255, nullable = false)
    private String fromEmail;

    @NotNull
    @Size(max = 255)
    @Column(name = "to_email", length = 255, nullable = false)
    private String toEmail;

    @Size(max = 1000)
    @Column(name = "subject", length = 1000)
    private String subject;

    @Lob
    @Column(name = "body")
    private String body;

    @Lob
    @Column(name = "body_html")
    private String bodyHtml;

    @NotNull
    @Column(name = "received_date", nullable = false)
    private Instant receivedDate;

    @Column(name = "processed_date")
    private Instant processedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImportStatus status;

    @Column(name = "attachment_count")
    private Integer attachmentCount;

    @Column(name = "documents_created")
    private Integer documentsCreated;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64)
    private String documentSha256;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emailImportDocument")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emailImportDocument" }, allowSetters = true)
    private Set<EmailImportEmailAttachment> emailImportEmailAttachments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emailImportImportMappings", "emailImportDocuments" }, allowSetters = true)
    private EmailImportImportRule appliedRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailImportDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSha256() {
        return this.sha256;
    }

    public EmailImportDocument sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getFromEmail() {
        return this.fromEmail;
    }

    public EmailImportDocument fromEmail(String fromEmail) {
        this.setFromEmail(fromEmail);
        return this;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return this.toEmail;
    }

    public EmailImportDocument toEmail(String toEmail) {
        this.setToEmail(toEmail);
        return this;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return this.subject;
    }

    public EmailImportDocument subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public EmailImportDocument body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyHtml() {
        return this.bodyHtml;
    }

    public EmailImportDocument bodyHtml(String bodyHtml) {
        this.setBodyHtml(bodyHtml);
        return this;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public EmailImportDocument receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Instant getProcessedDate() {
        return this.processedDate;
    }

    public EmailImportDocument processedDate(Instant processedDate) {
        this.setProcessedDate(processedDate);
        return this;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    public ImportStatus getStatus() {
        return this.status;
    }

    public EmailImportDocument status(ImportStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public Integer getAttachmentCount() {
        return this.attachmentCount;
    }

    public EmailImportDocument attachmentCount(Integer attachmentCount) {
        this.setAttachmentCount(attachmentCount);
        return this;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public Integer getDocumentsCreated() {
        return this.documentsCreated;
    }

    public EmailImportDocument documentsCreated(Integer documentsCreated) {
        this.setDocumentsCreated(documentsCreated);
        return this;
    }

    public void setDocumentsCreated(Integer documentsCreated) {
        this.documentsCreated = documentsCreated;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public EmailImportDocument errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public EmailImportDocument metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public EmailImportDocument documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public Set<EmailImportEmailAttachment> getEmailImportEmailAttachments() {
        return this.emailImportEmailAttachments;
    }

    public void setEmailImportEmailAttachments(Set<EmailImportEmailAttachment> emailImportEmailAttachments) {
        if (this.emailImportEmailAttachments != null) {
            this.emailImportEmailAttachments.forEach(i -> i.setEmailImportDocument(null));
        }
        if (emailImportEmailAttachments != null) {
            emailImportEmailAttachments.forEach(i -> i.setEmailImportDocument(this));
        }
        this.emailImportEmailAttachments = emailImportEmailAttachments;
    }

    public EmailImportDocument emailImportEmailAttachments(Set<EmailImportEmailAttachment> emailImportEmailAttachments) {
        this.setEmailImportEmailAttachments(emailImportEmailAttachments);
        return this;
    }

    public EmailImportDocument addEmailImportEmailAttachments(EmailImportEmailAttachment emailImportEmailAttachment) {
        this.emailImportEmailAttachments.add(emailImportEmailAttachment);
        emailImportEmailAttachment.setEmailImportDocument(this);
        return this;
    }

    public EmailImportDocument removeEmailImportEmailAttachments(EmailImportEmailAttachment emailImportEmailAttachment) {
        this.emailImportEmailAttachments.remove(emailImportEmailAttachment);
        emailImportEmailAttachment.setEmailImportDocument(null);
        return this;
    }

    public EmailImportImportRule getAppliedRule() {
        return this.appliedRule;
    }

    public void setAppliedRule(EmailImportImportRule emailImportImportRule) {
        this.appliedRule = emailImportImportRule;
    }

    public EmailImportDocument appliedRule(EmailImportImportRule emailImportImportRule) {
        this.setAppliedRule(emailImportImportRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImportDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailImportDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportDocument{" +
            "id=" + getId() +
            ", sha256='" + getSha256() + "'" +
            ", fromEmail='" + getFromEmail() + "'" +
            ", toEmail='" + getToEmail() + "'" +
            ", subject='" + getSubject() + "'" +
            ", body='" + getBody() + "'" +
            ", bodyHtml='" + getBodyHtml() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", processedDate='" + getProcessedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", attachmentCount=" + getAttachmentCount() +
            ", documentsCreated=" + getDocumentsCreated() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", documentSha256='" + getDocumentSha256() + "'" +
            "}";
    }
}
