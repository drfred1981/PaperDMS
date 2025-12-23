package fr.smartprod.paperdms.gateway.service.dto;

import fr.smartprod.paperdms.gateway.domain.enumeration.WorkflowInstanceStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.gateway.domain.DocumentProcess} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentProcessDTO implements Serializable {

    private Long id;

    private WorkflowInstanceStatus status;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowInstanceStatus status) {
        this.status = status;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentProcessDTO)) {
            return false;
        }

        DocumentProcessDTO documentProcessDTO = (DocumentProcessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentProcessDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentProcessDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            "}";
    }
}
