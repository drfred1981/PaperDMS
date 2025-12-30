package fr.smartprod.paperdms.archive.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArchiveDocument.
 */
@Entity
@Table(name = "archive_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArchiveDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    private String documentSha256;

    @Size(max = 1000)
    @Column(name = "original_path", length = 1000)
    private String originalPath;

    @Size(max = 1000)
    @Column(name = "archive_path", length = 1000)
    private String archivePath;

    @Column(name = "file_size")
    private Long fileSize;

    @NotNull
    @Column(name = "added_date", nullable = false)
    private Instant addedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "archivesDocuments" }, allowSetters = true)
    private ArchiveJob archiveJob;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArchiveDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public ArchiveDocument documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getOriginalPath() {
        return this.originalPath;
    }

    public ArchiveDocument originalPath(String originalPath) {
        this.setOriginalPath(originalPath);
        return this;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getArchivePath() {
        return this.archivePath;
    }

    public ArchiveDocument archivePath(String archivePath) {
        this.setArchivePath(archivePath);
        return this;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public ArchiveDocument fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getAddedDate() {
        return this.addedDate;
    }

    public ArchiveDocument addedDate(Instant addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public ArchiveJob getArchiveJob() {
        return this.archiveJob;
    }

    public void setArchiveJob(ArchiveJob archiveJob) {
        this.archiveJob = archiveJob;
    }

    public ArchiveDocument archiveJob(ArchiveJob archiveJob) {
        this.setArchiveJob(archiveJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchiveDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((ArchiveDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveDocument{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", originalPath='" + getOriginalPath() + "'" +
            ", archivePath='" + getArchivePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", addedDate='" + getAddedDate() + "'" +
            "}";
    }
}
