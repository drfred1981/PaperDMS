package com.ged.ocr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * R�sultat OCR par page
 */
@Entity
@Table(name = "ocr_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Integer pageNumber;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @Size(max = 1000)
    @Column(name = "s_3_result_key", length = 1000)
    private String s3ResultKey;

    @Lob
    @Column(name = "bounding_boxes")
    private String boundingBoxes;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Size(max = 10)
    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "word_count")
    private Integer wordCount;

    @NotNull
    @Column(name = "processed_date", nullable = false)
    private Instant processedDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tikaConfig" }, allowSetters = true)
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
            ", confidence=" + getConfidence() +
            ", s3ResultKey='" + gets3ResultKey() + "'" +
            ", boundingBoxes='" + getBoundingBoxes() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", language='" + getLanguage() + "'" +
            ", wordCount=" + getWordCount() +
            ", processedDate='" + getProcessedDate() + "'" +
            "}";
    }
}
