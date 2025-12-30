package fr.smartprod.paperdms.pdftoimage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImageConversionBatch.
 */
@Entity
@Table(name = "image_conversion_batch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imageconversionbatch")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageConversionBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "batch_name", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String batchName;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConversionStatus status;

    @Min(value = 0)
    @Column(name = "total_conversions")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer totalConversions;

    @Min(value = 0)
    @Column(name = "completed_conversions")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer completedConversions;

    @Min(value = 0)
    @Column(name = "failed_conversions")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer failedConversions;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "total_processing_duration")
    private Long totalProcessingDuration;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "generatedImages", "batch" }, allowSetters = true)
    private Set<ImagePdfConversionRequest> conversions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageConversionBatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public ImageConversionBatch batchName(String batchName) {
        this.setBatchName(batchName);
        return this;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDescription() {
        return this.description;
    }

    public ImageConversionBatch description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ImageConversionBatch createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ConversionStatus getStatus() {
        return this.status;
    }

    public ImageConversionBatch status(ConversionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ConversionStatus status) {
        this.status = status;
    }

    public Integer getTotalConversions() {
        return this.totalConversions;
    }

    public ImageConversionBatch totalConversions(Integer totalConversions) {
        this.setTotalConversions(totalConversions);
        return this;
    }

    public void setTotalConversions(Integer totalConversions) {
        this.totalConversions = totalConversions;
    }

    public Integer getCompletedConversions() {
        return this.completedConversions;
    }

    public ImageConversionBatch completedConversions(Integer completedConversions) {
        this.setCompletedConversions(completedConversions);
        return this;
    }

    public void setCompletedConversions(Integer completedConversions) {
        this.completedConversions = completedConversions;
    }

    public Integer getFailedConversions() {
        return this.failedConversions;
    }

    public ImageConversionBatch failedConversions(Integer failedConversions) {
        this.setFailedConversions(failedConversions);
        return this;
    }

    public void setFailedConversions(Integer failedConversions) {
        this.failedConversions = failedConversions;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public ImageConversionBatch startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public ImageConversionBatch completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getTotalProcessingDuration() {
        return this.totalProcessingDuration;
    }

    public ImageConversionBatch totalProcessingDuration(Long totalProcessingDuration) {
        this.setTotalProcessingDuration(totalProcessingDuration);
        return this;
    }

    public void setTotalProcessingDuration(Long totalProcessingDuration) {
        this.totalProcessingDuration = totalProcessingDuration;
    }

    public Long getCreatedByUserId() {
        return this.createdByUserId;
    }

    public ImageConversionBatch createdByUserId(Long createdByUserId) {
        this.setCreatedByUserId(createdByUserId);
        return this;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Set<ImagePdfConversionRequest> getConversions() {
        return this.conversions;
    }

    public void setConversions(Set<ImagePdfConversionRequest> imagePdfConversionRequests) {
        if (this.conversions != null) {
            this.conversions.forEach(i -> i.setBatch(null));
        }
        if (imagePdfConversionRequests != null) {
            imagePdfConversionRequests.forEach(i -> i.setBatch(this));
        }
        this.conversions = imagePdfConversionRequests;
    }

    public ImageConversionBatch conversions(Set<ImagePdfConversionRequest> imagePdfConversionRequests) {
        this.setConversions(imagePdfConversionRequests);
        return this;
    }

    public ImageConversionBatch addConversions(ImagePdfConversionRequest imagePdfConversionRequest) {
        this.conversions.add(imagePdfConversionRequest);
        imagePdfConversionRequest.setBatch(this);
        return this;
    }

    public ImageConversionBatch removeConversions(ImagePdfConversionRequest imagePdfConversionRequest) {
        this.conversions.remove(imagePdfConversionRequest);
        imagePdfConversionRequest.setBatch(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageConversionBatch)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageConversionBatch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageConversionBatch{" +
            "id=" + getId() +
            ", batchName='" + getBatchName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalConversions=" + getTotalConversions() +
            ", completedConversions=" + getCompletedConversions() +
            ", failedConversions=" + getFailedConversions() +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", totalProcessingDuration=" + getTotalProcessingDuration() +
            ", createdByUserId=" + getCreatedByUserId() +
            "}";
    }
}
