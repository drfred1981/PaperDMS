package fr.smartprod.paperdms.archive.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.archive.domain.ArchiveDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArchiveDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @Size(max = 1000)
    private String originalPath;

    @Size(max = 1000)
    private String archivePath;

    private Long fileSize;

    @NotNull
    private Instant addedDate;

    private ArchiveJobDTO archiveJob;

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

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getArchivePath() {
        return archivePath;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public ArchiveJobDTO getArchiveJob() {
        return archiveJob;
    }

    public void setArchiveJob(ArchiveJobDTO archiveJob) {
        this.archiveJob = archiveJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchiveDocumentDTO)) {
            return false;
        }

        ArchiveDocumentDTO archiveDocumentDTO = (ArchiveDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, archiveDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveDocumentDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", originalPath='" + getOriginalPath() + "'" +
            ", archivePath='" + getArchivePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", addedDate='" + getAddedDate() + "'" +
            ", archiveJob=" + getArchiveJob() +
            "}";
    }
}
