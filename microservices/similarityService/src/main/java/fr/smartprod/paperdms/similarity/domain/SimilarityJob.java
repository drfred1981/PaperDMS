package fr.smartprod.paperdms.similarity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.similarity.domain.enumeration.AiJobStatus;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityScope;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SimilarityJob.
 */
@Entity
@Table(name = "similarity_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "similarityjob")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentSha256;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AiJobStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimilarityAlgorithm algorithm;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimilarityScope scope;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "min_similarity_threshold")
    private Double minSimilarityThreshold;

    @Column(name = "matches_found")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer matchesFound;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Lob
    @Column(name = "error_message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorMessage;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Set<SimilarityDocumentComparison> similarities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SimilarityJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public SimilarityJob documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public AiJobStatus getStatus() {
        return this.status;
    }

    public SimilarityJob status(AiJobStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AiJobStatus status) {
        this.status = status;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public SimilarityJob algorithm(SimilarityAlgorithm algorithm) {
        this.setAlgorithm(algorithm);
        return this;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public SimilarityScope getScope() {
        return this.scope;
    }

    public SimilarityJob scope(SimilarityScope scope) {
        this.setScope(scope);
        return this;
    }

    public void setScope(SimilarityScope scope) {
        this.scope = scope;
    }

    public Double getMinSimilarityThreshold() {
        return this.minSimilarityThreshold;
    }

    public SimilarityJob minSimilarityThreshold(Double minSimilarityThreshold) {
        this.setMinSimilarityThreshold(minSimilarityThreshold);
        return this;
    }

    public void setMinSimilarityThreshold(Double minSimilarityThreshold) {
        this.minSimilarityThreshold = minSimilarityThreshold;
    }

    public Integer getMatchesFound() {
        return this.matchesFound;
    }

    public SimilarityJob matchesFound(Integer matchesFound) {
        this.setMatchesFound(matchesFound);
        return this;
    }

    public void setMatchesFound(Integer matchesFound) {
        this.matchesFound = matchesFound;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public SimilarityJob startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public SimilarityJob endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public SimilarityJob errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SimilarityJob createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SimilarityJob createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<SimilarityDocumentComparison> getSimilarities() {
        return this.similarities;
    }

    public void setSimilarities(Set<SimilarityDocumentComparison> similarityDocumentComparisons) {
        if (this.similarities != null) {
            this.similarities.forEach(i -> i.setJob(null));
        }
        if (similarityDocumentComparisons != null) {
            similarityDocumentComparisons.forEach(i -> i.setJob(this));
        }
        this.similarities = similarityDocumentComparisons;
    }

    public SimilarityJob similarities(Set<SimilarityDocumentComparison> similarityDocumentComparisons) {
        this.setSimilarities(similarityDocumentComparisons);
        return this;
    }

    public SimilarityJob addSimilarities(SimilarityDocumentComparison similarityDocumentComparison) {
        this.similarities.add(similarityDocumentComparison);
        similarityDocumentComparison.setJob(this);
        return this;
    }

    public SimilarityJob removeSimilarities(SimilarityDocumentComparison similarityDocumentComparison) {
        this.similarities.remove(similarityDocumentComparison);
        similarityDocumentComparison.setJob(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimilarityJob)) {
            return false;
        }
        return getId() != null && getId().equals(((SimilarityJob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityJob{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", status='" + getStatus() + "'" +
            ", algorithm='" + getAlgorithm() + "'" +
            ", scope='" + getScope() + "'" +
            ", minSimilarityThreshold=" + getMinSimilarityThreshold() +
            ", matchesFound=" + getMatchesFound() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
