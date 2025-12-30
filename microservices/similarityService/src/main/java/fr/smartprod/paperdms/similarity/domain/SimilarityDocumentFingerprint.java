package fr.smartprod.paperdms.similarity.domain;

import fr.smartprod.paperdms.similarity.domain.enumeration.FingerprintType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SimilarityDocumentFingerprint.
 */
@Entity
@Table(name = "similarity_document_fingerprint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "similaritydocumentfingerprint")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityDocumentFingerprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "fingerprint_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private FingerprintType fingerprintType;

    @Lob
    @Column(name = "fingerprint", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fingerprint;

    @Lob
    @Column(name = "vector_embedding")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vectorEmbedding;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
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

    public SimilarityDocumentFingerprint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FingerprintType getFingerprintType() {
        return this.fingerprintType;
    }

    public SimilarityDocumentFingerprint fingerprintType(FingerprintType fingerprintType) {
        this.setFingerprintType(fingerprintType);
        return this;
    }

    public void setFingerprintType(FingerprintType fingerprintType) {
        this.fingerprintType = fingerprintType;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public SimilarityDocumentFingerprint fingerprint(String fingerprint) {
        this.setFingerprint(fingerprint);
        return this;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getVectorEmbedding() {
        return this.vectorEmbedding;
    }

    public SimilarityDocumentFingerprint vectorEmbedding(String vectorEmbedding) {
        this.setVectorEmbedding(vectorEmbedding);
        return this;
    }

    public void setVectorEmbedding(String vectorEmbedding) {
        this.vectorEmbedding = vectorEmbedding;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public SimilarityDocumentFingerprint metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getComputedDate() {
        return this.computedDate;
    }

    public SimilarityDocumentFingerprint computedDate(Instant computedDate) {
        this.setComputedDate(computedDate);
        return this;
    }

    public void setComputedDate(Instant computedDate) {
        this.computedDate = computedDate;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public SimilarityDocumentFingerprint lastUpdated(Instant lastUpdated) {
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
        if (!(o instanceof SimilarityDocumentFingerprint)) {
            return false;
        }
        return getId() != null && getId().equals(((SimilarityDocumentFingerprint) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityDocumentFingerprint{" +
            "id=" + getId() +
            ", fingerprintType='" + getFingerprintType() + "'" +
            ", fingerprint='" + getFingerprint() + "'" +
            ", vectorEmbedding='" + getVectorEmbedding() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", computedDate='" + getComputedDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
