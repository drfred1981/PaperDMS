package fr.smartprod.paperdms.ocr.service.dto;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ocr.domain.OcrCache} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrCacheDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    private OcrEngine ocrEngine;

    @Size(max = 10)
    private String language;

    @NotNull
    private Integer pageCount;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double totalConfidence;

    @NotNull
    @Size(max = 1000)
    private String s3ResultKey;

    @NotNull
    @Size(max = 255)
    private String s3Bucket;

    @Size(max = 1000)
    private String orcExtractedTextS3Key;

    @Lob
    private String metadata;

    private Integer hits;

    private Instant lastAccessDate;

    @NotNull
    private Instant createdDate;

    private Instant expirationDate;

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

    public OcrEngine getOcrEngine() {
        return ocrEngine;
    }

    public void setOcrEngine(OcrEngine ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Double getTotalConfidence() {
        return totalConfidence;
    }

    public void setTotalConfidence(Double totalConfidence) {
        this.totalConfidence = totalConfidence;
    }

    public String gets3ResultKey() {
        return s3ResultKey;
    }

    public void sets3ResultKey(String s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public String gets3Bucket() {
        return s3Bucket;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getOrcExtractedTextS3Key() {
        return orcExtractedTextS3Key;
    }

    public void setOrcExtractedTextS3Key(String orcExtractedTextS3Key) {
        this.orcExtractedTextS3Key = orcExtractedTextS3Key;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Instant getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Instant lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrCacheDTO)) {
            return false;
        }

        OcrCacheDTO ocrCacheDTO = (OcrCacheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ocrCacheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrCacheDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", ocrEngine='" + getOcrEngine() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageCount=" + getPageCount() +
            ", totalConfidence=" + getTotalConfidence() +
            ", s3ResultKey='" + gets3ResultKey() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", orcExtractedTextS3Key='" + getOrcExtractedTextS3Key() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", hits=" + getHits() +
            ", lastAccessDate='" + getLastAccessDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
