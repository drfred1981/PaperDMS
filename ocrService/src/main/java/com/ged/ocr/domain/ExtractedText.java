package com.ged.ocr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Texte extrait du document
 */
@Entity
@Table(name = "extracted_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "extractedtext")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtractedText implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String content;

    @NotNull
    @Column(name = "page_number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pageNumber;

    @Size(max = 10)
    @Column(name = "language", length = 10)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String language;

    @Column(name = "word_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer wordCount;

    @Column(name = "has_structured_data")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasStructuredData;

    @Lob
    @Column(name = "structured_data")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String structuredData;

    @NotNull
    @Column(name = "extracted_date", nullable = false)
    private Instant extractedDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tikaConfig" }, allowSetters = true)
    private OcrJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExtractedText id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public ExtractedText content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public ExtractedText pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getLanguage() {
        return this.language;
    }

    public ExtractedText language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getWordCount() {
        return this.wordCount;
    }

    public ExtractedText wordCount(Integer wordCount) {
        this.setWordCount(wordCount);
        return this;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Boolean getHasStructuredData() {
        return this.hasStructuredData;
    }

    public ExtractedText hasStructuredData(Boolean hasStructuredData) {
        this.setHasStructuredData(hasStructuredData);
        return this;
    }

    public void setHasStructuredData(Boolean hasStructuredData) {
        this.hasStructuredData = hasStructuredData;
    }

    public String getStructuredData() {
        return this.structuredData;
    }

    public ExtractedText structuredData(String structuredData) {
        this.setStructuredData(structuredData);
        return this;
    }

    public void setStructuredData(String structuredData) {
        this.structuredData = structuredData;
    }

    public Instant getExtractedDate() {
        return this.extractedDate;
    }

    public ExtractedText extractedDate(Instant extractedDate) {
        this.setExtractedDate(extractedDate);
        return this;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    public OcrJob getJob() {
        return this.job;
    }

    public void setJob(OcrJob ocrJob) {
        this.job = ocrJob;
    }

    public ExtractedText job(OcrJob ocrJob) {
        this.setJob(ocrJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtractedText)) {
            return false;
        }
        return getId() != null && getId().equals(((ExtractedText) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtractedText{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", pageNumber=" + getPageNumber() +
            ", language='" + getLanguage() + "'" +
            ", wordCount=" + getWordCount() +
            ", hasStructuredData='" + getHasStructuredData() + "'" +
            ", structuredData='" + getStructuredData() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            "}";
    }
}
