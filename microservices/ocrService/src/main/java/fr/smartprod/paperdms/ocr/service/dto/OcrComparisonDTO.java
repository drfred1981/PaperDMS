package fr.smartprod.paperdms.ocr.service.dto;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ocr.domain.OcrComparison} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrComparisonDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String documentSha256;

    @NotNull
    private Integer pageNumber;

    @Lob
    private String tikaText;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double tikaConfidence;

    @Lob
    private String aiText;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double aiConfidence;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double similarity;

    @Lob
    private String differences;

    @Size(max = 1000)
    private String differencesS3Key;

    private OcrEngine selectedEngine;

    @Size(max = 50)
    private String selectedBy;

    private Instant selectedDate;

    @NotNull
    private Instant comparisonDate;

    @Lob
    private String metadata;

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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getTikaText() {
        return tikaText;
    }

    public void setTikaText(String tikaText) {
        this.tikaText = tikaText;
    }

    public Double getTikaConfidence() {
        return tikaConfidence;
    }

    public void setTikaConfidence(Double tikaConfidence) {
        this.tikaConfidence = tikaConfidence;
    }

    public String getAiText() {
        return aiText;
    }

    public void setAiText(String aiText) {
        this.aiText = aiText;
    }

    public Double getAiConfidence() {
        return aiConfidence;
    }

    public void setAiConfidence(Double aiConfidence) {
        this.aiConfidence = aiConfidence;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String getDifferences() {
        return differences;
    }

    public void setDifferences(String differences) {
        this.differences = differences;
    }

    public String getDifferencesS3Key() {
        return differencesS3Key;
    }

    public void setDifferencesS3Key(String differencesS3Key) {
        this.differencesS3Key = differencesS3Key;
    }

    public OcrEngine getSelectedEngine() {
        return selectedEngine;
    }

    public void setSelectedEngine(OcrEngine selectedEngine) {
        this.selectedEngine = selectedEngine;
    }

    public String getSelectedBy() {
        return selectedBy;
    }

    public void setSelectedBy(String selectedBy) {
        this.selectedBy = selectedBy;
    }

    public Instant getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Instant selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Instant getComparisonDate() {
        return comparisonDate;
    }

    public void setComparisonDate(Instant comparisonDate) {
        this.comparisonDate = comparisonDate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrComparisonDTO)) {
            return false;
        }

        OcrComparisonDTO ocrComparisonDTO = (OcrComparisonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ocrComparisonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrComparisonDTO{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", pageNumber=" + getPageNumber() +
            ", tikaText='" + getTikaText() + "'" +
            ", tikaConfidence=" + getTikaConfidence() +
            ", aiText='" + getAiText() + "'" +
            ", aiConfidence=" + getAiConfidence() +
            ", similarity=" + getSimilarity() +
            ", differences='" + getDifferences() + "'" +
            ", differencesS3Key='" + getDifferencesS3Key() + "'" +
            ", selectedEngine='" + getSelectedEngine() + "'" +
            ", selectedBy='" + getSelectedBy() + "'" +
            ", selectedDate='" + getSelectedDate() + "'" +
            ", comparisonDate='" + getComparisonDate() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
