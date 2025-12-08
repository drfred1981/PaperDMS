package com.ged.ocr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ocr.domain.ExtractedText} entity.
 */
@Schema(description = "Texte extrait du document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtractedTextDTO implements Serializable {

    private Long id;

    @Lob
    private String content;

    @NotNull
    private Integer pageNumber;

    @Size(max = 10)
    private String language;

    private Integer wordCount;

    private Boolean hasStructuredData;

    @Lob
    private String structuredData;

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
            ", pageNumber=" + getPageNumber() +
            ", language='" + getLanguage() + "'" +
            ", wordCount=" + getWordCount() +
            ", hasStructuredData='" + getHasStructuredData() + "'" +
            ", structuredData='" + getStructuredData() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            ", job=" + getJob() +
            "}";
    }
}
