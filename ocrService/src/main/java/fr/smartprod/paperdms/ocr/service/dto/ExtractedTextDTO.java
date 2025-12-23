package fr.smartprod.paperdms.ocr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ocr.domain.ExtractedText} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtractedTextDTO implements Serializable {

    private Long id;

    @Lob
    private String content;

    @Size(max = 64)
    private String contentSha256;

    @Size(max = 1000)
    private String s3ContentKey;

    @NotNull
    @Size(max = 255)
    private String s3Bucket;

    @NotNull
    private Integer pageNumber;

    @Size(max = 10)
    private String language;

    private Integer wordCount;

    private Boolean hasStructuredData;

    @Lob
    private String structuredData;

    @Size(max = 1000)
    private String structuredDataS3Key;

    @NotNull
    private Instant extractedDate;

    @NotNull
    private OcrJobDTO job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSha256() {
        return contentSha256;
    }

    public void setContentSha256(String contentSha256) {
        this.contentSha256 = contentSha256;
    }

    public String gets3ContentKey() {
        return s3ContentKey;
    }

    public void sets3ContentKey(String s3ContentKey) {
        this.s3ContentKey = s3ContentKey;
    }

    public String gets3Bucket() {
        return s3Bucket;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Boolean getHasStructuredData() {
        return hasStructuredData;
    }

    public void setHasStructuredData(Boolean hasStructuredData) {
        this.hasStructuredData = hasStructuredData;
    }

    public String getStructuredData() {
        return structuredData;
    }

    public void setStructuredData(String structuredData) {
        this.structuredData = structuredData;
    }

    public String getStructuredDataS3Key() {
        return structuredDataS3Key;
    }

    public void setStructuredDataS3Key(String structuredDataS3Key) {
        this.structuredDataS3Key = structuredDataS3Key;
    }

    public Instant getExtractedDate() {
        return extractedDate;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    public OcrJobDTO getJob() {
        return job;
    }

    public void setJob(OcrJobDTO job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtractedTextDTO)) {
            return false;
        }

        ExtractedTextDTO extractedTextDTO = (ExtractedTextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, extractedTextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtractedTextDTO{" +
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
            ", job=" + getJob() +
            "}";
    }
}
