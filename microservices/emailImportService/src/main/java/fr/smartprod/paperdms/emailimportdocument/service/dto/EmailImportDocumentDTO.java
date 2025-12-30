package fr.smartprod.paperdms.emailimportdocument.service.dto;

import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.ImportStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String sha256;

    @NotNull
    @Size(max = 255)
    private String fromEmail;

    @NotNull
    @Size(max = 255)
    private String toEmail;

    @Size(max = 1000)
    private String subject;

    @Lob
    private String body;

    @Lob
    private String bodyHtml;

    @NotNull
    private Instant receivedDate;

    private Instant processedDate;

    @NotNull
    private ImportStatus status;

    private Integer attachmentCount;

    private Integer documentsCreated;

    @Lob
    private String errorMessage;

    @Lob
    private String metadata;

    @Size(max = 64)
    private String documentSha256;

    private EmailImportImportRuleDTO appliedRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Instant getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public Integer getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public Integer getDocumentsCreated() {
        return documentsCreated;
    }

    public void setDocumentsCreated(Integer documentsCreated) {
        this.documentsCreated = documentsCreated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public EmailImportImportRuleDTO getAppliedRule() {
        return appliedRule;
    }

    public void setAppliedRule(EmailImportImportRuleDTO appliedRule) {
        this.appliedRule = appliedRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImportDocumentDTO)) {
            return false;
        }

        EmailImportDocumentDTO emailImportDocumentDTO = (EmailImportDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailImportDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportDocumentDTO{" +
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
            ", appliedRule=" + getAppliedRule() +
            "}";
    }
}
