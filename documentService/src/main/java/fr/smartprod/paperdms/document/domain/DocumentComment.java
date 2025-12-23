package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentComment.
 */
@Entity
@Table(name = "document_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentComment implements Serializable {

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

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "page_number")
    private Integer pageNumber;

    @NotNull
    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved;

    @NotNull
    @Size(max = 50)
    @Column(name = "author_id", length = 50, nullable = false)
    private String authorId;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "replies", "parentComment" }, allowSetters = true)
    private Set<DocumentComment> replies = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "replies", "parentComment" }, allowSetters = true)
    private DocumentComment parentComment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public DocumentComment documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getContent() {
        return this.content;
    }

    public DocumentComment content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public DocumentComment pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Boolean getIsResolved() {
        return this.isResolved;
    }

    public DocumentComment isResolved(Boolean isResolved) {
        this.setIsResolved(isResolved);
        return this;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public DocumentComment authorId(String authorId) {
        this.setAuthorId(authorId);
        return this;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public DocumentComment createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<DocumentComment> getReplies() {
        return this.replies;
    }

    public void setReplies(Set<DocumentComment> documentComments) {
        if (this.replies != null) {
            this.replies.forEach(i -> i.setParentComment(null));
        }
        if (documentComments != null) {
            documentComments.forEach(i -> i.setParentComment(this));
        }
        this.replies = documentComments;
    }

    public DocumentComment replies(Set<DocumentComment> documentComments) {
        this.setReplies(documentComments);
        return this;
    }

    public DocumentComment addReplies(DocumentComment documentComment) {
        this.replies.add(documentComment);
        documentComment.setParentComment(this);
        return this;
    }

    public DocumentComment removeReplies(DocumentComment documentComment) {
        this.replies.remove(documentComment);
        documentComment.setParentComment(null);
        return this;
    }

    public DocumentComment getParentComment() {
        return this.parentComment;
    }

    public void setParentComment(DocumentComment documentComment) {
        this.parentComment = documentComment;
    }

    public DocumentComment parentComment(DocumentComment documentComment) {
        this.setParentComment(documentComment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentComment)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentComment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentComment{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", content='" + getContent() + "'" +
            ", pageNumber=" + getPageNumber() +
            ", isResolved='" + getIsResolved() + "'" +
            ", authorId='" + getAuthorId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
