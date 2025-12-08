package com.ged.ai.service.dto;

import com.ged.ai.domain.enumeration.AiJobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ai.domain.CorrespondentExtraction} entity.
 */
@Schema(description = "Extraction de correspondants")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrespondentExtractionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @Lob
    private String extractedText;

    @NotNull
    private AiJobStatus status;

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

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public AiJobStatus getStatus() {
        return status;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
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
            ", extractedText='" + getExtractedText() + "'" +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", sendersCount=" + getSendersCount() +
            ", recipientsCount=" + getRecipientsCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
