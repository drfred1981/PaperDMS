package fr.smartprod.paperdms.ocr.domain;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OcrCache.
 */
@Entity
@Table(name = "ocr_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false, unique = true)
    private String documentSha256;

    @Enumerated(EnumType.STRING)
    @Column(name = "ocr_engine")
    private OcrEngine ocrEngine;

    @Size(max = 10)
    @Column(name = "language", length = 10)
    private String language;

    @NotNull
    @Column(name = "page_count", nullable = false)
    private Integer pageCount;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "total_confidence")
    private Double totalConfidence;

    @NotNull
    @Size(max = 1000)
    @Column(name = "s_3_result_key", length = 1000, nullable = false)
    private String s3ResultKey;

    @NotNull
    @Size(max = 255)
    @Column(name = "s_3_bucket", length = 255, nullable = false)
    private String s3Bucket;

    @Size(max = 1000)
    @Column(name = "extracted_text_s_3_key", length = 1000)
    private String extractedTextS3Key;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Column(name = "hits")
    private Integer hits;

    @Column(name = "last_access_date")
    private Instant lastAccessDate;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OcrCache id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public OcrCache documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public OcrEngine getOcrEngine() {
        return this.ocrEngine;
    }

    public OcrCache ocrEngine(OcrEngine ocrEngine) {
        this.setOcrEngine(ocrEngine);
        return this;
    }

    public void setOcrEngine(OcrEngine ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public String getLanguage() {
        return this.language;
    }

    public OcrCache language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public OcrCache pageCount(Integer pageCount) {
        this.setPageCount(pageCount);
        return this;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Double getTotalConfidence() {
        return this.totalConfidence;
    }

    public OcrCache totalConfidence(Double totalConfidence) {
        this.setTotalConfidence(totalConfidence);
        return this;
    }

    public void setTotalConfidence(Double totalConfidence) {
        this.totalConfidence = totalConfidence;
    }

    public String gets3ResultKey() {
        return this.s3ResultKey;
    }

    public OcrCache s3ResultKey(String s3ResultKey) {
        this.sets3ResultKey(s3ResultKey);
        return this;
    }

    public void sets3ResultKey(String s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public String gets3Bucket() {
        return this.s3Bucket;
    }

    public OcrCache s3Bucket(String s3Bucket) {
        this.sets3Bucket(s3Bucket);
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getExtractedTextS3Key() {
        return this.extractedTextS3Key;
    }

    public OcrCache extractedTextS3Key(String extractedTextS3Key) {
        this.setExtractedTextS3Key(extractedTextS3Key);
        return this;
    }

    public void setExtractedTextS3Key(String extractedTextS3Key) {
        this.extractedTextS3Key = extractedTextS3Key;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public OcrCache metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getHits() {
        return this.hits;
    }

    public OcrCache hits(Integer hits) {
        this.setHits(hits);
        return this;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Instant getLastAccessDate() {
        return this.lastAccessDate;
    }

    public OcrCache lastAccessDate(Instant lastAccessDate) {
        this.setLastAccessDate(lastAccessDate);
        return this;
    }

    public void setLastAccessDate(Instant lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public OcrCache createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public OcrCache expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrCache)) {
            return false;
        }
        return getId() != null && getId().equals(((OcrCache) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrCache{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", ocrEngine='" + getOcrEngine() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageCount=" + getPageCount() +
            ", totalConfidence=" + getTotalConfidence() +
            ", s3ResultKey='" + gets3ResultKey() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", extractedTextS3Key='" + getExtractedTextS3Key() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", hits=" + getHits() +
            ", lastAccessDate='" + getLastAccessDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
