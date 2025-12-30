package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.AuditAction;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentAudit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentAuditDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @NotNull
    private AuditAction action;

    @NotNull
    @Size(max = 50)
    private String userId;

    @Size(max = 45)
    private String userIp;

    @NotNull
    private Instant actionDate;

    @Lob
    private String additionalInfo;

    private DocumentDTO document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Instant getActionDate() {
        return actionDate;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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
        if (!(o instanceof DocumentAuditDTO)) {
            return false;
        }

        DocumentAuditDTO documentAuditDTO = (DocumentAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentAuditDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", action='" + getAction() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userIp='" + getUserIp() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            ", document=" + getDocument() +
            "}";
    }
}
