package com.ged.ocr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ocr.domain.OcrResult} entity.
 */
@Schema(description = "R�sultat OCR par page")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrResultDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer pageNumber;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @Size(max = 1000)
    private String s3ResultKey;

    @Lob
    private String boundingBoxes;

    @Lob
    private String metadata;

    @Size(max = 10)
    private String language;

    private Integer wordCount;

    @NotNull
    private Instant processedDate;

    @NotNull
    private OcrJobDTO job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String gets3ResultKey() {
        return s3ResultKey;
    }

    public void sets3ResultKey(String s3ResultKey) {
        this.s3ResultKey = s3ResultKey;
    }

    public String getBoundingBoxes() {
        return boundingBoxes;
    }

    public void setBoundingBoxes(String boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
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

    public Instant getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Instant processedDate) {
        this.processedDate = processedDate;
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
        if (!(o instanceof OcrResultDTO)) {
            return false;
        }

        OcrResultDTO ocrResultDTO = (OcrResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ocrResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrResultDTO{" +
            "id=" + getId() +
            ", pageNumber=" + getPageNumber() +
            ", confidence=" + getConfidence() +
            ", s3ResultKey='" + gets3ResultKey() + "'" +
            ", boundingBoxes='" + getBoundingBoxes() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", language='" + getLanguage() + "'" +
            ", wordCount=" + getWordCount() +
            ", processedDate='" + getProcessedDate() + "'" +
            ", job=" + getJob() +
            "}";
    }
}
