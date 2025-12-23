package fr.smartprod.paperdms.document.domain;

import fr.smartprod.paperdms.document.domain.enumeration.AuditAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentAudit.
 */
@Entity
@Table(name = "document_audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    private String documentSha256;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;

    @NotNull
    @Size(max = 50)
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Size(max = 45)
    @Column(name = "user_ip", length = 45)
    private String userIp;

    @NotNull
    @Column(name = "action_date", nullable = false)
    private Instant actionDate;

    @Lob
    @Column(name = "additional_info")
    private String additionalInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentAudit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public DocumentAudit documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public DocumentAudit documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public AuditAction getAction() {
        return this.action;
    }

    public DocumentAudit action(AuditAction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getUserId() {
        return this.userId;
    }

    public DocumentAudit userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public DocumentAudit userIp(String userIp) {
        this.setUserIp(userIp);
        return this;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public DocumentAudit actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public DocumentAudit additionalInfo(String additionalInfo) {
        this.setAdditionalInfo(additionalInfo);
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentAudit)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentAudit) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentAudit{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", action='" + getAction() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userIp='" + getUserIp() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            "}";
    }
}
