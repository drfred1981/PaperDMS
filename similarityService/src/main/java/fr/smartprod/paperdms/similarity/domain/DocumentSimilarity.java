package fr.smartprod.paperdms.similarity.domain;

import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentSimilarity.
 */
@Entity
@Table(name = "document_similarity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentSimilarity implements Serializable {

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
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "similarity_score", nullable = false)
    private Double similarityScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm")
    private SimilarityAlgorithm algorithm;

    @Lob
    @Column(name = "features")
    private String features;

    @NotNull
    @Column(name = "computed_date", nullable = false)
    private Instant computedDate;

    @Column(name = "is_relevant")
    private Boolean isRelevant;

    @Size(max = 50)
    @Column(name = "reviewed_by", length = 50)
    private String reviewedBy;

    @Column(name = "reviewed_date")
    private Instant reviewedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private SimilarityJob job;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentSimilarity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId1() {
        return this.documentId1;
    }

    public DocumentSimilarity documentId1(Long documentId1) {
        this.setDocumentId1(documentId1);
        return this;
    }

    public void setDocumentId1(Long documentId1) {
        this.documentId1 = documentId1;
    }

    public Long getDocumentId2() {
        return this.documentId2;
    }

    public DocumentSimilarity documentId2(Long documentId2) {
        this.setDocumentId2(documentId2);
        return this;
    }

    public void setDocumentId2(Long documentId2) {
        this.documentId2 = documentId2;
    }

    public Double getSimilarityScore() {
        return this.similarityScore;
    }

    public DocumentSimilarity similarityScore(Double similarityScore) {
        this.setSimilarityScore(similarityScore);
        return this;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public DocumentSimilarity algorithm(SimilarityAlgorithm algorithm) {
        this.setAlgorithm(algorithm);
        return this;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getFeatures() {
        return this.features;
    }

    public DocumentSimilarity features(String features) {
        this.setFeatures(features);
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Instant getComputedDate() {
        return this.computedDate;
    }

    public DocumentSimilarity computedDate(Instant computedDate) {
        this.setComputedDate(computedDate);
        return this;
    }

    public void setComputedDate(Instant computedDate) {
        this.computedDate = computedDate;
    }

    public Boolean getIsRelevant() {
        return this.isRelevant;
    }

    public DocumentSimilarity isRelevant(Boolean isRelevant) {
        this.setIsRelevant(isRelevant);
        return this;
    }

    public void setIsRelevant(Boolean isRelevant) {
        this.isRelevant = isRelevant;
    }

    public String getReviewedBy() {
        return this.reviewedBy;
    }

    public DocumentSimilarity reviewedBy(String reviewedBy) {
        this.setReviewedBy(reviewedBy);
        return this;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Instant getReviewedDate() {
        return this.reviewedDate;
    }

    public DocumentSimilarity reviewedDate(Instant reviewedDate) {
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

    public DocumentSimilarity job(SimilarityJob similarityJob) {
        this.setJob(similarityJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentSimilarity)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentSimilarity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSimilarity{" +
            "id=" + getId() +
            ", documentId1=" + getDocumentId1() +
            ", documentId2=" + getDocumentId2() +
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
