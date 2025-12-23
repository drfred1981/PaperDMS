package fr.smartprod.paperdms.ocr.service.dto;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ocr.domain.OcrResult} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrResultDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer pageNumber;

    @Size(max = 64)
    private String pageSha256;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @Size(max = 1000)
    private String s3ResultKey;

    @NotNull
    @Size(max = 255)
    private String s3Bucket;

    @Size(max = 1000)
    private String s3BoundingBoxKey;

    @Lob
    private String boundingBoxes;

    @Lob
    private String metadata;

    @Size(max = 10)
    private String language;

    private Integer wordCount;

    private OcrEngine ocrEngine;

    private Long processingTime;

    @Lob
    private String rawResponse;

    @Size(max = 1000)
    private String rawResponseS3Key;

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

    public String getPageSha256() {
        return pageSha256;
    }

    public void setPageSha256(String pageSha256) {
        this.pageSha256 = pageSha256;
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

    public String gets3Bucket() {
        return s3Bucket;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3BoundingBoxKey() {
        return s3BoundingBoxKey;
    }

    public void sets3BoundingBoxKey(String s3BoundingBoxKey) {
        this.s3BoundingBoxKey = s3BoundingBoxKey;
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

    public OcrEngine getOcrEngine() {
        return ocrEngine;
    }

    public void setOcrEngine(OcrEngine ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getRawResponseS3Key() {
        return rawResponseS3Key;
    }

    public void setRawResponseS3Key(String rawResponseS3Key) {
        this.rawResponseS3Key = rawResponseS3Key;
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
            ", job=" + getJob() +
            "}";
    }
}
