package fr.smartprod.paperdms.business.service.dto;

import fr.smartprod.paperdms.business.domain.enumeration.ManualStatus;
import fr.smartprod.paperdms.business.domain.enumeration.ManualType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.Manual} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 500)
    private String title;

    @NotNull
    private ManualType manualType;

    @NotNull
    @Size(max = 50)
    private String version;

    @NotNull
    @Size(max = 10)
    private String language;

    private LocalDate publicationDate;

    private Integer pageCount;

    @NotNull
    private ManualStatus status;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private Instant createdDate;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ManualType getManualType() {
        return manualType;
    }

    public void setManualType(ManualType manualType) {
        this.manualType = manualType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public ManualStatus getStatus() {
        return status;
    }

    public void setStatus(ManualStatus status) {
        this.status = status;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualDTO)) {
            return false;
        }

        ManualDTO manualDTO = (ManualDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manualDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", title='" + getTitle() + "'" +
            ", manualType='" + getManualType() + "'" +
            ", version='" + getVersion() + "'" +
            ", language='" + getLanguage() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", pageCount=" + getPageCount() +
            ", status='" + getStatus() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
