package fr.smartprod.paperdms.transform.domain;

import fr.smartprod.paperdms.transform.domain.enumeration.ComparisonType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ComparisonJob.
 */
@Entity
@Table(name = "comparison_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComparisonJob implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id_1", nullable = false)
    private Long documentId1;

    @NotNull
    @Column(name = "document_id_2", nullable = false)
    private Long documentId2;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "comparison_type", nullable = false)
    private ComparisonType comparisonType;

    @Lob
    @Column(name = "differences")
    private String differences;

    @Column(name = "difference_count")
    private Integer differenceCount;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "similarity_percentage")
    private Double similarityPercentage;

    @Size(max = 1000)
    @Column(name = "diff_report_s_3_key", length = 1000)
    private String diffReportS3Key;

    @Size(max = 1000)
    @Column(name = "diff_visual_s_3_key", length = 1000)
    private String diffVisualS3Key;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransformStatus status;

    @NotNull
    @Column(name = "compared_date", nullable = false)
    private Instant comparedDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "compared_by", length = 50, nullable = false)
    private String comparedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ComparisonJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId1() {
        return this.documentId1;
    }

    public ComparisonJob documentId1(Long documentId1) {
        this.setDocumentId1(documentId1);
        return this;
    }

    public void setDocumentId1(Long documentId1) {
        this.documentId1 = documentId1;
    }

    public Long getDocumentId2() {
        return this.documentId2;
    }

    public ComparisonJob documentId2(Long documentId2) {
        this.setDocumentId2(documentId2);
        return this;
    }

    public void setDocumentId2(Long documentId2) {
        this.documentId2 = documentId2;
    }

    public ComparisonType getComparisonType() {
        return this.comparisonType;
    }

    public ComparisonJob comparisonType(ComparisonType comparisonType) {
        this.setComparisonType(comparisonType);
        return this;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getDifferences() {
        return this.differences;
    }

    public ComparisonJob differences(String differences) {
        this.setDifferences(differences);
        return this;
    }

    public void setDifferences(String differences) {
        this.differences = differences;
    }

    public Integer getDifferenceCount() {
        return this.differenceCount;
    }

    public ComparisonJob differenceCount(Integer differenceCount) {
        this.setDifferenceCount(differenceCount);
        return this;
    }

    public void setDifferenceCount(Integer differenceCount) {
        this.differenceCount = differenceCount;
    }

    public Double getSimilarityPercentage() {
        return this.similarityPercentage;
    }

    public ComparisonJob similarityPercentage(Double similarityPercentage) {
        this.setSimilarityPercentage(similarityPercentage);
        return this;
    }

    public void setSimilarityPercentage(Double similarityPercentage) {
        this.similarityPercentage = similarityPercentage;
    }

    public String getDiffReportS3Key() {
        return this.diffReportS3Key;
    }

    public ComparisonJob diffReportS3Key(String diffReportS3Key) {
        this.setDiffReportS3Key(diffReportS3Key);
        return this;
    }

    public void setDiffReportS3Key(String diffReportS3Key) {
        this.diffReportS3Key = diffReportS3Key;
    }

    public String getDiffVisualS3Key() {
        return this.diffVisualS3Key;
    }

    public ComparisonJob diffVisualS3Key(String diffVisualS3Key) {
        this.setDiffVisualS3Key(diffVisualS3Key);
        return this;
    }

    public void setDiffVisualS3Key(String diffVisualS3Key) {
        this.diffVisualS3Key = diffVisualS3Key;
    }

    public TransformStatus getStatus() {
        return this.status;
    }

    public ComparisonJob status(TransformStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransformStatus status) {
        this.status = status;
    }

    public Instant getComparedDate() {
        return this.comparedDate;
    }

    public ComparisonJob comparedDate(Instant comparedDate) {
        this.setComparedDate(comparedDate);
        return this;
    }

    public void setComparedDate(Instant comparedDate) {
        this.comparedDate = comparedDate;
    }

    public String getComparedBy() {
        return this.comparedBy;
    }

    public ComparisonJob comparedBy(String comparedBy) {
        this.setComparedBy(comparedBy);
        return this;
    }

    public void setComparedBy(String comparedBy) {
        this.comparedBy = comparedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComparisonJob)) {
            return false;
        }
        return getId() != null && getId().equals(((ComparisonJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComparisonJob{" +
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
