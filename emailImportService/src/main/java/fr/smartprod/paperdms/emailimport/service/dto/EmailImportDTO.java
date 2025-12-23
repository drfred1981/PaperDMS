package fr.smartprod.paperdms.emailimport.service.dto;

import fr.smartprod.paperdms.emailimport.domain.enumeration.ImportStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.emailimport.domain.EmailImport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportDTO implements Serializable {

    private Long id;

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

    private Long folderId;

    private Long documentTypeId;

    private Integer attachmentCount;

    private Integer documentsCreated;

    private Long appliedRuleId;

    @Lob
    private String errorMessage;

    @Lob
    private String metadata;

    private ImportRuleDTO appliedRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
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

    public Long getAppliedRuleId() {
        return appliedRuleId;
    }

    public void setAppliedRuleId(Long appliedRuleId) {
        this.appliedRuleId = appliedRuleId;
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

    public ImportRuleDTO getAppliedRule() {
        return appliedRule;
    }

    public void setAppliedRule(ImportRuleDTO appliedRule) {
        this.appliedRule = appliedRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImportDTO)) {
            return false;
        }

        EmailImportDTO emailImportDTO = (EmailImportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailImportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportDTO{" +
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
            ", appliedRule=" + getAppliedRule() +
            "}";
    }
}
