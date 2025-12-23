package fr.smartprod.paperdms.similarity.domain;

import fr.smartprod.paperdms.similarity.domain.enumeration.FingerprintType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentFingerprint.
 */
@Entity
@Table(name = "document_fingerprint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentfingerprint")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentFingerprint implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false, unique = true)
    private Long documentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fingerprint_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private FingerprintType fingerprintType;

    @Lob
    @Column(name = "fingerprint", nullable = false)
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String fingerprint;

    @Lob
    @Column(name = "vector_embedding")
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String vectorEmbedding;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String metadata;

    @NotNull
    @Column(name = "computed_date", nullable = false)
    private Instant computedDate;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentFingerprint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public DocumentFingerprint documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public FingerprintType getFingerprintType() {
        return this.fingerprintType;
    }

    public DocumentFingerprint fingerprintType(FingerprintType fingerprintType) {
        this.setFingerprintType(fingerprintType);
        return this;
    }

    public void setFingerprintType(FingerprintType fingerprintType) {
        this.fingerprintType = fingerprintType;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public DocumentFingerprint fingerprint(String fingerprint) {
        this.setFingerprint(fingerprint);
        return this;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getVectorEmbedding() {
        return this.vectorEmbedding;
    }

    public DocumentFingerprint vectorEmbedding(String vectorEmbedding) {
        this.setVectorEmbedding(vectorEmbedding);
        return this;
    }

    public void setVectorEmbedding(String vectorEmbedding) {
        this.vectorEmbedding = vectorEmbedding;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public DocumentFingerprint metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getComputedDate() {
        return this.computedDate;
    }

    public DocumentFingerprint computedDate(Instant computedDate) {
        this.setComputedDate(computedDate);
        return this;
    }

    public void setComputedDate(Instant computedDate) {
        this.computedDate = computedDate;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public DocumentFingerprint lastUpdated(Instant lastUpdated) {
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
        if (!(o instanceof DocumentFingerprint)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentFingerprint) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentFingerprint{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", fingerprintType='" + getFingerprintType() + "'" +
            ", fingerprint='" + getFingerprint() + "'" +
            ", vectorEmbedding='" + getVectorEmbedding() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", computedDate='" + getComputedDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
