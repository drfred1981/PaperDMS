package fr.smartprod.paperdms.ai.service.dto;

import fr.smartprod.paperdms.ai.domain.enumeration.AiJobStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ai.domain.CorrespondentExtraction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrespondentExtractionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @Lob
    private String extractedText;

    @NotNull
    @Size(max = 64)
    private String extractedTextSha256;

    @Size(max = 10)
    private String detectedLanguage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double languageConfidence;

    private AiJobStatus status;

    @Size(max = 128)
    private String resultCacheKey;

    @NotNull
    private Boolean isCached;

    @Size(max = 1000)
    private String resultS3Key;

    private Instant startDate;

    private Instant endDate;

    @Lob
    private String errorMessage;

    private Integer sendersCount;

    private Integer recipientsCount;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSha256() {
        return documentSha256;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public String getExtractedTextSha256() {
        return extractedTextSha256;
    }

    public void setExtractedTextSha256(String extractedTextSha256) {
        this.extractedTextSha256 = extractedTextSha256;
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getLanguageConfidence() {
        return languageConfidence;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public AiJobStatus getStatus() {
        return status;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public String getResultCacheKey() {
        return resultCacheKey;
    }

    public void setResultCacheKey(String resultCacheKey) {
        this.resultCacheKey = resultCacheKey;
    }

    public Boolean getIsCached() {
        return isCached;
    }

    public void setIsCached(Boolean isCached) {
        this.isCached = isCached;
    }

    public String getResultS3Key() {
        return resultS3Key;
    }

    public void setResultS3Key(String resultS3Key) {
        this.resultS3Key = resultS3Key;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getSendersCount() {
        return sendersCount;
    }

    public void setSendersCount(Integer sendersCount) {
        this.sendersCount = sendersCount;
    }

    public Integer getRecipientsCount() {
        return recipientsCount;
    }

    public void setRecipientsCount(Integer recipientsCount) {
        this.recipientsCount = recipientsCount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorrespondentExtractionDTO)) {
            return false;
        }

        CorrespondentExtractionDTO correspondentExtractionDTO = (CorrespondentExtractionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, correspondentExtractionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrespondentExtractionDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", extractedText='" + getExtractedText() + "'" +
            ", extractedTextSha256='" + getExtractedTextSha256() + "'" +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", languageConfidence=" + getLanguageConfidence() +
            ", status='" + getStatus() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", resultS3Key='" + getResultS3Key() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", sendersCount=" + getSendersCount() +
            ", recipientsCount=" + getRecipientsCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
