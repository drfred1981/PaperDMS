package fr.smartprod.paperdms.similarity.service.dto;

import fr.smartprod.paperdms.similarity.domain.enumeration.FingerprintType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.similarity.domain.DocumentFingerprint} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentFingerprintDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    private FingerprintType fingerprintType;

    @Lob
    private String fingerprint;

    @Lob
    private String vectorEmbedding;

    @Lob
    private String metadata;

    @NotNull
    private Instant computedDate;

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

    public FingerprintType getFingerprintType() {
        return fingerprintType;
    }

    public void setFingerprintType(FingerprintType fingerprintType) {
        this.fingerprintType = fingerprintType;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getVectorEmbedding() {
        return vectorEmbedding;
    }

    public void setVectorEmbedding(String vectorEmbedding) {
        this.vectorEmbedding = vectorEmbedding;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getComputedDate() {
        return computedDate;
    }

    public void setComputedDate(Instant computedDate) {
        this.computedDate = computedDate;
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
        if (!(o instanceof DocumentFingerprintDTO)) {
            return false;
        }

        DocumentFingerprintDTO documentFingerprintDTO = (DocumentFingerprintDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentFingerprintDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentFingerprintDTO{" +
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
