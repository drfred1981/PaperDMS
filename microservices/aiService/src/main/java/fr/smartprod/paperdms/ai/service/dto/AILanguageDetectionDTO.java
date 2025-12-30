package fr.smartprod.paperdms.ai.service.dto;

import fr.smartprod.paperdms.ai.domain.enumeration.AILanguageDetectionMethod;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ai.domain.AILanguageDetection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AILanguageDetectionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @NotNull
    @Size(max = 10)
    private String detectedLanguage;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    private AILanguageDetectionMethod detectionMethod;

    @Lob
    private String alternativeLanguages;

    @Lob
    private String textSample;

    @Size(max = 128)
    private String resultCacheKey;

    @NotNull
    private Boolean isCached;

    @NotNull
    private Instant detectedDate;

    @Size(max = 50)
    private String modelVersion;

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

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public AILanguageDetectionMethod getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(AILanguageDetectionMethod detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getAlternativeLanguages() {
        return alternativeLanguages;
    }

    public void setAlternativeLanguages(String alternativeLanguages) {
        this.alternativeLanguages = alternativeLanguages;
    }

    public String getTextSample() {
        return textSample;
    }

    public void setTextSample(String textSample) {
        this.textSample = textSample;
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

    public Instant getDetectedDate() {
        return detectedDate;
    }

    public void setDetectedDate(Instant detectedDate) {
        this.detectedDate = detectedDate;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AILanguageDetectionDTO)) {
            return false;
        }

        AILanguageDetectionDTO aILanguageDetectionDTO = (AILanguageDetectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aILanguageDetectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AILanguageDetectionDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", confidence=" + getConfidence() +
            ", detectionMethod='" + getDetectionMethod() + "'" +
            ", alternativeLanguages='" + getAlternativeLanguages() + "'" +
            ", textSample='" + getTextSample() + "'" +
            ", resultCacheKey='" + getResultCacheKey() + "'" +
            ", isCached='" + getIsCached() + "'" +
            ", detectedDate='" + getDetectedDate() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            "}";
    }
}
