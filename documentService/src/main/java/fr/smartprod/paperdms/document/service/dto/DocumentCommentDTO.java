package fr.smartprod.paperdms.document.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentComment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCommentDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @Lob
    private String content;

    private Integer pageNumber;

    @NotNull
    private Boolean isResolved;

    @NotNull
    @Size(max = 50)
    private String authorId;

    @NotNull
    private Instant createdDate;

    private DocumentCommentDTO parentComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public DocumentCommentDTO getParentComment() {
        return parentComment;
    }

    public void setParentComment(DocumentCommentDTO parentComment) {
        this.parentComment = parentComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentCommentDTO)) {
            return false;
        }

        DocumentCommentDTO documentCommentDTO = (DocumentCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCommentDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", content='" + getContent() + "'" +
            ", pageNumber=" + getPageNumber() +
            ", isResolved='" + getIsResolved() + "'" +
            ", authorId='" + getAuthorId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", parentComment=" + getParentComment() +
            "}";
    }
}
