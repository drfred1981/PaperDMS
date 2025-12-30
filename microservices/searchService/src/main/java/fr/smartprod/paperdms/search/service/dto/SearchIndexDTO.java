package fr.smartprod.paperdms.search.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.search.domain.SearchIndex} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchIndexDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @Lob
    private String indexedContent;

    @Lob
    private String metadata;

    @Size(max = 2000)
    private String tags;

    @Size(max = 1000)
    private String correspondents;

    @Lob
    private String extractedEntities;

    @NotNull
    private Instant indexedDate;

    private Instant lastUpdated;

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

    public String getIndexedContent() {
        return indexedContent;
    }

    public void setIndexedContent(String indexedContent) {
        this.indexedContent = indexedContent;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCorrespondents() {
        return correspondents;
    }

    public void setCorrespondents(String correspondents) {
        this.correspondents = correspondents;
    }

    public String getExtractedEntities() {
        return extractedEntities;
    }

    public void setExtractedEntities(String extractedEntities) {
        this.extractedEntities = extractedEntities;
    }

    public Instant getIndexedDate() {
        return indexedDate;
    }

    public void setIndexedDate(Instant indexedDate) {
        this.indexedDate = indexedDate;
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
        if (!(o instanceof SearchIndexDTO)) {
            return false;
        }

        SearchIndexDTO searchIndexDTO = (SearchIndexDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchIndexDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchIndexDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", indexedContent='" + getIndexedContent() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", tags='" + getTags() + "'" +
            ", correspondents='" + getCorrespondents() + "'" +
            ", extractedEntities='" + getExtractedEntities() + "'" +
            ", indexedDate='" + getIndexedDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
