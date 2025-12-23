package fr.smartprod.paperdms.document.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.DocumentStatistics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentStatisticsDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    private Integer viewsTotal;

    private Integer downloadsTotal;

    private Integer uniqueViewers;

    @NotNull
    private Instant lastUpdated;

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

    public Integer getViewsTotal() {
        return viewsTotal;
    }

    public void setViewsTotal(Integer viewsTotal) {
        this.viewsTotal = viewsTotal;
    }

    public Integer getDownloadsTotal() {
        return downloadsTotal;
    }

    public void setDownloadsTotal(Integer downloadsTotal) {
        this.downloadsTotal = downloadsTotal;
    }

    public Integer getUniqueViewers() {
        return uniqueViewers;
    }

    public void setUniqueViewers(Integer uniqueViewers) {
        this.uniqueViewers = uniqueViewers;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentStatisticsDTO)) {
            return false;
        }

        DocumentStatisticsDTO documentStatisticsDTO = (DocumentStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentStatisticsDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", viewsTotal=" + getViewsTotal() +
            ", downloadsTotal=" + getDownloadsTotal() +
            ", uniqueViewers=" + getUniqueViewers() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
