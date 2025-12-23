package fr.smartprod.paperdms.transform.service.dto;

import fr.smartprod.paperdms.transform.domain.enumeration.ComparisonType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.transform.domain.ComparisonJob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComparisonJobDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId1;

    @NotNull
    private Long documentId2;

    @NotNull
    private ComparisonType comparisonType;

    @Lob
    private String differences;

    private Integer differenceCount;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Double similarityPercentage;

    @Size(max = 1000)
    private String diffReportS3Key;

    @Size(max = 1000)
    private String diffVisualS3Key;

    @NotNull
    private TransformStatus status;

    @NotNull
    private Instant comparedDate;

    @NotNull
    @Size(max = 50)
    private String comparedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId1() {
        return documentId1;
    }

    public void setDocumentId1(Long documentId1) {
        this.documentId1 = documentId1;
    }

    public Long getDocumentId2() {
        return documentId2;
    }

    public void setDocumentId2(Long documentId2) {
        this.documentId2 = documentId2;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getDifferences() {
        return differences;
    }

    public void setDifferences(String differences) {
        this.differences = differences;
    }

    public Integer getDifferenceCount() {
        return differenceCount;
    }

    public void setDifferenceCount(Integer differenceCount) {
        this.differenceCount = differenceCount;
    }

    public Double getSimilarityPercentage() {
        return similarityPercentage;
    }

    public void setSimilarityPercentage(Double similarityPercentage) {
        this.similarityPercentage = similarityPercentage;
    }

    public String getDiffReportS3Key() {
        return diffReportS3Key;
    }

    public void setDiffReportS3Key(String diffReportS3Key) {
        this.diffReportS3Key = diffReportS3Key;
    }

    public String getDiffVisualS3Key() {
        return diffVisualS3Key;
    }

    public void setDiffVisualS3Key(String diffVisualS3Key) {
        this.diffVisualS3Key = diffVisualS3Key;
    }

    public TransformStatus getStatus() {
        return status;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getComparedDate() {
        return comparedDate;
    }

    public void setComparedDate(Instant comparedDate) {
        this.comparedDate = comparedDate;
    }

    public String getComparedBy() {
        return comparedBy;
    }

    public void setComparedBy(String comparedBy) {
        this.comparedBy = comparedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComparisonJobDTO)) {
            return false;
        }

        ComparisonJobDTO comparisonJobDTO = (ComparisonJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, comparisonJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComparisonJobDTO{" +
            "id=" + getId() +
            ", documentId1=" + getDocumentId1() +
            ", documentId2=" + getDocumentId2() +
            ", comparisonType='" + getComparisonType() + "'" +
            ", differences='" + getDifferences() + "'" +
            ", differenceCount=" + getDifferenceCount() +
            ", similarityPercentage=" + getSimilarityPercentage() +
            ", diffReportS3Key='" + getDiffReportS3Key() + "'" +
            ", diffVisualS3Key='" + getDiffVisualS3Key() + "'" +
            ", status='" + getStatus() + "'" +
            ", comparedDate='" + getComparedDate() + "'" +
            ", comparedBy='" + getComparedBy() + "'" +
            "}";
    }
}
