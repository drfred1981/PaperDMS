package fr.smartprod.paperdms.ocr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExtractedText.
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

    @Size(max = 64)
    @Column(name = "content_sha_256", length = 64)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contentSha256;

    @Size(max = 1000)
    @Column(name = "s_3_content_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3ContentKey;

    @NotNull
    @Size(max = 255)
    @Column(name = "s_3_bucket", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Bucket;

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

    @Size(max = 1000)
    @Column(name = "structured_data_s_3_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String structuredDataS3Key;

    @NotNull
    @Column(name = "extracted_date", nullable = false)
    private Instant extractedDate;

    @ManyToOne(optional = false)
    @NotNull
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

    public String getContentSha256() {
        return this.contentSha256;
    }

    public ExtractedText contentSha256(String contentSha256) {
        this.setContentSha256(contentSha256);
        return this;
    }

    public void setContentSha256(String contentSha256) {
        this.contentSha256 = contentSha256;
    }

    public String gets3ContentKey() {
        return this.s3ContentKey;
    }

    public ExtractedText s3ContentKey(String s3ContentKey) {
        this.sets3ContentKey(s3ContentKey);
        return this;
    }

    public void sets3ContentKey(String s3ContentKey) {
        this.s3ContentKey = s3ContentKey;
    }

    public String gets3Bucket() {
        return this.s3Bucket;
    }

    public ExtractedText s3Bucket(String s3Bucket) {
        this.sets3Bucket(s3Bucket);
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
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

    public String getStructuredDataS3Key() {
        return this.structuredDataS3Key;
    }

    public ExtractedText structuredDataS3Key(String structuredDataS3Key) {
        this.setStructuredDataS3Key(structuredDataS3Key);
        return this;
    }

    public void setStructuredDataS3Key(String structuredDataS3Key) {
        this.structuredDataS3Key = structuredDataS3Key;
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
            ", contentSha256='" + getContentSha256() + "'" +
            ", s3ContentKey='" + gets3ContentKey() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", pageNumber=" + getPageNumber() +
            ", language='" + getLanguage() + "'" +
            ", wordCount=" + getWordCount() +
            ", hasStructuredData='" + getHasStructuredData() + "'" +
            ", structuredData='" + getStructuredData() + "'" +
            ", structuredDataS3Key='" + getStructuredDataS3Key() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            "}";
    }
}
