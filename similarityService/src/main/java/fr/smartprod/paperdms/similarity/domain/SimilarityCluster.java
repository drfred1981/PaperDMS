package fr.smartprod.paperdms.similarity.domain;

import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SimilarityCluster.
 */
@Entity
@Table(name = "similarity_cluster")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "similaritycluster")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimilarityCluster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimilarityAlgorithm algorithm;

    @Lob
    @Column(name = "centroid")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String centroid;

    @Column(name = "document_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer documentCount;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "avg_similarity")
    private Double avgSimilarity;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SimilarityCluster id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SimilarityCluster name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public SimilarityCluster description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SimilarityAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public SimilarityCluster algorithm(SimilarityAlgorithm algorithm) {
        this.setAlgorithm(algorithm);
        return this;
    }

    public void setAlgorithm(SimilarityAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getCentroid() {
        return this.centroid;
    }

    public SimilarityCluster centroid(String centroid) {
        this.setCentroid(centroid);
        return this;
    }

    public void setCentroid(String centroid) {
        this.centroid = centroid;
    }

    public Integer getDocumentCount() {
        return this.documentCount;
    }

    public SimilarityCluster documentCount(Integer documentCount) {
        this.setDocumentCount(documentCount);
        return this;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Double getAvgSimilarity() {
        return this.avgSimilarity;
    }

    public SimilarityCluster avgSimilarity(Double avgSimilarity) {
        this.setAvgSimilarity(avgSimilarity);
        return this;
    }

    public void setAvgSimilarity(Double avgSimilarity) {
        this.avgSimilarity = avgSimilarity;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SimilarityCluster createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public SimilarityCluster lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimilarityCluster)) {
            return false;
        }
        return getId() != null && getId().equals(((SimilarityCluster) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimilarityCluster{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", algorithm='" + getAlgorithm() + "'" +
            ", centroid='" + getCentroid() + "'" +
            ", documentCount=" + getDocumentCount() +
            ", avgSimilarity=" + getAvgSimilarity() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
