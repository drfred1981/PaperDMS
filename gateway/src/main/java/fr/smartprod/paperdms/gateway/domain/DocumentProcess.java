package fr.smartprod.paperdms.gateway.domain;

import fr.smartprod.paperdms.gateway.domain.enumeration.WorkflowInstanceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DocumentProcess.
 */
@Entity
@Table(name = "document_process")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentprocess")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private WorkflowInstanceStatus status;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentSha256;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentProcess id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowInstanceStatus getStatus() {
        return this.status;
    }

    public DocumentProcess status(WorkflowInstanceStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WorkflowInstanceStatus status) {
        this.status = status;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public DocumentProcess documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public DocumentProcess documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentProcess)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentProcess) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentProcess{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            "}";
    }
}
