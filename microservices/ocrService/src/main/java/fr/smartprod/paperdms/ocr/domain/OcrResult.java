package fr.smartprod.paperdms.ocr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OcrResult.
 */
@Entity
@Table(name = "ocr_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ocrresult")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "page_number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pageNumber;

    @Size(max = 64)
    @Column(name = "page_sha_256", length = 64)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pageSha256;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @Size(max = 1000)
    @Column(name = "s_3_result_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3ResultKey;

    @NotNull
    @Size(max = 255)
    @Column(name = "s_3_bucket", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Bucket;

    @Size(max = 1000)
    @Column(name = "s_3_bounding_box_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3BoundingBoxKey;

    @Lob
    @Column(name = "bounding_boxes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String boundingBoxes;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String metadata;

    @Size(max = 10)
    @Column(name = "language", length = 10)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String language;

    @Column(name = "word_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer wordCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "ocr_engine")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private OcrEngine ocrEngine;

    @Column(name = "processing_time")
    private Long processingTime;

    @Lob
    @Column(name = "raw_response")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rawResponse;

    @Size(max = 1000)
    @Column(name = "raw_response_s_3_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rawResponseS3Key;

    @NotNull
    @Column(name = "processed_date", nullable = false)
    private Instant processedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "results", "extracteds" }, allowSetters = true)
    private OcrJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OcrResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public OcrResult pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSha256() {
        return this.pageSha256;
    }

    public OcrResult pageSha256(String pageSha256) {
        this.setPageSha256(pageSha256);
        return this;
    }

    public void setPageSha256(String pageSha256) {
        this.pageSha256 = pageSha256;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public OcrResult confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String gets3ResultKey() {
        return this.s3ResultKey;
    }

    public OcrResult s3ResultKey(String s3ResultKey) {
        this.sets3ResultKey(s3ResultKey);
        return this;
    }

    public void sets3ResultKey(String s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public String gets3Bucket() {
        return this.s3Bucket;
    }

    public OcrResult s3Bucket(String s3Bucket) {
        this.sets3Bucket(s3Bucket);
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3BoundingBoxKey() {
        return this.s3BoundingBoxKey;
    }

    public OcrResult s3BoundingBoxKey(String s3BoundingBoxKey) {
        this.sets3BoundingBoxKey(s3BoundingBoxKey);
        return this;
    }

    public void sets3BoundingBoxKey(String s3BoundingBoxKey) {
        this.s3BoundingBoxKey = s3BoundingBoxKey;
    }

    public String getBoundingBoxes() {
        return this.boundingBoxes;
    }

    public OcrResult boundingBoxes(String boundingBoxes) {
        this.setBoundingBoxes(boundingBoxes);
        return this;
    }

    public void setBoundingBoxes(String boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public OcrResult metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getLanguage() {
        return this.language;
    }

    public OcrResult language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getWordCount() {
        return this.wordCount;
    }

    public OcrResult wordCount(Integer wordCount) {
        this.setWordCount(wordCount);
        return this;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public OcrEngine getOcrEngine() {
        return this.ocrEngine;
    }

    public OcrResult ocrEngine(OcrEngine ocrEngine) {
        this.setOcrEngine(ocrEngine);
        return this;
    }

    public void setOcrEngine(OcrEngine ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public Long getProcessingTime() {
        return this.processingTime;
    }

    public OcrResult processingTime(Long processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getRawResponse() {
        return this.rawResponse;
    }

    public OcrResult rawResponse(String rawResponse) {
        this.setRawResponse(rawResponse);
        return this;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getRawResponseS3Key() {
        return this.rawResponseS3Key;
    }

    public OcrResult rawResponseS3Key(String rawResponseS3Key) {
        this.setRawResponseS3Key(rawResponseS3Key);
        return this;
    }

    public void setRawResponseS3Key(String rawResponseS3Key) {
        this.rawResponseS3Key = rawResponseS3Key;
    }

    public Instant getProcessedDate() {
        return this.processedDate;
    }

    public OcrResult processedDate(Instant processedDate) {
        this.setProcessedDate(processedDate);
        return this;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
    }

    public OcrJob getJob() {
        return this.job;
    }

    public void setJob(OcrJob ocrJob) {
        this.job = ocrJob;
    }

    public OcrResult job(OcrJob ocrJob) {
        this.setJob(ocrJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrResult)) {
            return false;
        }
        return getId() != null && getId().equals(((OcrResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrResult{" +
            "id=" + getId() +
            ", pageNumber=" + getPageNumber() +
            ", pageSha256='" + getPageSha256() + "'" +
            ", confidence=" + getConfidence() +
            ", s3ResultKey='" + gets3ResultKey() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", s3BoundingBoxKey='" + gets3BoundingBoxKey() + "'" +
            ", boundingBoxes='" + getBoundingBoxes() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", language='" + getLanguage() + "'" +
            ", wordCount=" + getWordCount() +
            ", ocrEngine='" + getOcrEngine() + "'" +
            ", processingTime=" + getProcessingTime() +
            ", rawResponse='" + getRawResponse() + "'" +
            ", rawResponseS3Key='" + getRawResponseS3Key() + "'" +
            ", processedDate='" + getProcessedDate() + "'" +
            "}";
    }
}
