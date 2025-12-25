package fr.smartprod.paperdms.emailimport.domain;

import fr.smartprod.paperdms.emailimport.domain.enumeration.ImportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailImport.
 */
@Entity
@Table(name = "email_import")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

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

    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Column(name = "attachment_count")
    private Integer attachmentCount;

    @Column(name = "documents_created")
    private Integer documentsCreated;

    @Column(name = "applied_rule_id")
    private Long appliedRuleId;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    private ImportRule appliedRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailImport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromEmail() {
        return this.fromEmail;
    }

    public EmailImport fromEmail(String fromEmail) {
        this.setFromEmail(fromEmail);
        return this;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return this.toEmail;
    }

    public EmailImport toEmail(String toEmail) {
        this.setToEmail(toEmail);
        return this;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return this.subject;
    }

    public EmailImport subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public EmailImport body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyHtml() {
        return this.bodyHtml;
    }

    public EmailImport bodyHtml(String bodyHtml) {
        this.setBodyHtml(bodyHtml);
        return this;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public EmailImport receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Instant getProcessedDate() {
        return this.processedDate;
    }

    public EmailImport processedDate(Instant processedDate) {
        this.setProcessedDate(processedDate);
        return this;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    public ImportStatus getStatus() {
        return this.status;
    }

    public EmailImport status(ImportStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public EmailImport folderId(Long folderId) {
        this.setFolderId(folderId);
        return this;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getDocumentTypeId() {
        return this.documentTypeId;
    }

    public EmailImport documentTypeId(Long documentTypeId) {
        this.setDocumentTypeId(documentTypeId);
        return this;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Integer getAttachmentCount() {
        return this.attachmentCount;
    }

    public EmailImport attachmentCount(Integer attachmentCount) {
        this.setAttachmentCount(attachmentCount);
        return this;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public Integer getDocumentsCreated() {
        return this.documentsCreated;
    }

    public EmailImport documentsCreated(Integer documentsCreated) {
        this.setDocumentsCreated(documentsCreated);
        return this;
    }

    public void setDocumentsCreated(Integer documentsCreated) {
        this.documentsCreated = documentsCreated;
    }

    public Long getAppliedRuleId() {
        return this.appliedRuleId;
    }

    public EmailImport appliedRuleId(Long appliedRuleId) {
        this.setAppliedRuleId(appliedRuleId);
        return this;
    }

    public void setAppliedRuleId(Long appliedRuleId) {
        this.appliedRuleId = appliedRuleId;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public EmailImport errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public EmailImport metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public ImportRule getAppliedRule() {
        return this.appliedRule;
    }

    public void setAppliedRule(ImportRule importRule) {
        this.appliedRule = importRule;
    }

    public EmailImport appliedRule(ImportRule importRule) {
        this.setAppliedRule(importRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImport)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailImport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImport{" +
            "id=" + getId() +
            ", fromEmail='" + getFromEmail() + "'" +
            ", toEmail='" + getToEmail() + "'" +
            ", subject='" + getSubject() + "'" +
            ", body='" + getBody() + "'" +
            ", bodyHtml='" + getBodyHtml() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", processedDate='" + getProcessedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", folderId=" + getFolderId() +
            ", documentTypeId=" + getDocumentTypeId() +
            ", attachmentCount=" + getAttachmentCount() +
            ", documentsCreated=" + getDocumentsCreated() +
            ", appliedRuleId=" + getAppliedRuleId() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
