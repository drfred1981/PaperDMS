package fr.smartprod.paperdms.search.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SearchIndex.
 */
@Entity
@Table(name = "search_index")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "searchindex")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchIndex implements Serializable {

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
    @Column(name = "indexed_content", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String indexedContent;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String metadata;

    @Size(max = 2000)
    @Column(name = "tags", length = 2000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tags;

    @Size(max = 1000)
    @Column(name = "correspondents", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String correspondents;

    @Lob
    @Column(name = "extracted_entities")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String extractedEntities;

    @NotNull
    @Column(name = "indexed_date", nullable = false)
    private Instant indexedDate;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchIndex id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public SearchIndex documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getIndexedContent() {
        return this.indexedContent;
    }

    public SearchIndex indexedContent(String indexedContent) {
        this.setIndexedContent(indexedContent);
        return this;
    }

    public void setIndexedContent(String indexedContent) {
        this.indexedContent = indexedContent;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public SearchIndex metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getTags() {
        return this.tags;
    }

    public SearchIndex tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCorrespondents() {
        return this.correspondents;
    }

    public SearchIndex correspondents(String correspondents) {
        this.setCorrespondents(correspondents);
        return this;
    }

    public void setCorrespondents(String correspondents) {
        this.correspondents = correspondents;
    }

    public String getExtractedEntities() {
        return this.extractedEntities;
    }

    public SearchIndex extractedEntities(String extractedEntities) {
        this.setExtractedEntities(extractedEntities);
        return this;
    }

    public void setExtractedEntities(String extractedEntities) {
        this.extractedEntities = extractedEntities;
    }

    public Instant getIndexedDate() {
        return this.indexedDate;
    }

    public SearchIndex indexedDate(Instant indexedDate) {
        this.setIndexedDate(indexedDate);
        return this;
    }

    public void setIndexedDate(Instant indexedDate) {
        this.indexedDate = indexedDate;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public SearchIndex lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchIndex)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchIndex) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchIndex{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
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
