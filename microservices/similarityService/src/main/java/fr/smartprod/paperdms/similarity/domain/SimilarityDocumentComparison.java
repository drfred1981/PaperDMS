package fr.smartprod.paperdms.similarity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SimilarityDocumentComparison.
 */
@Entity
@Table(name = "similarity_document_comparison")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "similaritydocumentcomparison")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityDocumentComparison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "source_document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sourceDocumentSha256;

    @NotNull
    @Size(max = 64)
    @Column(name = "target_document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String targetDocumentSha256;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "similarity_score", nullable = false)
    private Double similarityScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimilarityAlgorithm algorithm;

    @Lob
    @Column(name = "features")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String features;

    @NotNull
    @Column(name = "computed_date", nullable = false)
    private Instant computedDate;

    @Column(name = "is_relevant")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isRelevant;

    @Size(max = 50)
    @Column(name = "reviewed_by", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String reviewedBy;

    @Column(name = "reviewed_date")
    private Instant reviewedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "similarities" }, allowSetters = true)
    private SimilarityJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SimilarityDocumentComparison id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceDocumentSha256() {
        return this.sourceDocumentSha256;
    }

    public SimilarityDocumentComparison sourceDocumentSha256(String sourceDocumentSha256) {
        this.setSourceDocumentSha256(sourceDocumentSha256);
        return this;
    }

    public void setSourceDocumentSha256(String sourceDocumentSha256) {
        this.sourceDocumentSha256 = sourceDocumentSha256;
    }

    public String getTargetDocumentSha256() {
        return this.targetDocumentSha256;
    }

    public SimilarityDocumentComparison targetDocumentSha256(String targetDocumentSha256) {
        this.setTargetDocumentSha256(targetDocumentSha256);
        return this;
    }

    public void setTargetDocumentSha256(String targetDocumentSha256) {
        this.targetDocumentSha256 = targetDocumentSha256;
    }

    public Double getSimilarityScore() {
        return this.similarityScore;
    }

    public SimilarityDocumentComparison similarityScore(Double similarityScore) {
        this.setSimilarityScore(similarityScore);
        return this;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public SimilarityDocumentComparison algorithm(SimilarityAlgorithm algorithm) {
        this.setAlgorithm(algorithm);
        return this;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getFeatures() {
        return this.features;
    }

    public SimilarityDocumentComparison features(String features) {
        this.setFeatures(features);
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Instant getComputedDate() {
        return this.computedDate;
    }

    public SimilarityDocumentComparison computedDate(Instant computedDate) {
        this.setComputedDate(computedDate);
        return this;
    }

    public void setComputedDate(Instant computedDate) {
        this.computedDate = computedDate;
    }

    public Boolean getIsRelevant() {
        return this.isRelevant;
    }

    public SimilarityDocumentComparison isRelevant(Boolean isRelevant) {
        this.setIsRelevant(isRelevant);
        return this;
    }

    public void setIsRelevant(Boolean isRelevant) {
        this.isRelevant = isRelevant;
    }

    public String getReviewedBy() {
        return this.reviewedBy;
    }

    public SimilarityDocumentComparison reviewedBy(String reviewedBy) {
        this.setReviewedBy(reviewedBy);
        return this;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Instant getReviewedDate() {
        return this.reviewedDate;
    }

    public SimilarityDocumentComparison reviewedDate(Instant reviewedDate) {
        this.setReviewedDate(reviewedDate);
        return this;
    }

    public void setReviewedDate(Instant reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public SimilarityJob getJob() {
        return this.job;
    }

    public void setJob(SimilarityJob similarityJob) {
        this.job = similarityJob;
    }

    public SimilarityDocumentComparison job(SimilarityJob similarityJob) {
        this.setJob(similarityJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimilarityDocumentComparison)) {
            return false;
        }
        return getId() != null && getId().equals(((SimilarityDocumentComparison) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityDocumentComparison{" +
            "id=" + getId() +
            ", sourceDocumentSha256='" + getSourceDocumentSha256() + "'" +
            ", targetDocumentSha256='" + getTargetDocumentSha256() + "'" +
            ", similarityScore=" + getSimilarityScore() +
            ", algorithm='" + getAlgorithm() + "'" +
            ", features='" + getFeatures() + "'" +
            ", computedDate='" + getComputedDate() + "'" +
            ", isRelevant='" + getIsRelevant() + "'" +
            ", reviewedBy='" + getReviewedBy() + "'" +
            ", reviewedDate='" + getReviewedDate() + "'" +
            "}";
    }
}
