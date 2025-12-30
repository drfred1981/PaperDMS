package fr.smartprod.paperdms.pdftoimage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entite principale - Demande de conversion PDF vers image(s)
 */
@Entity
@Table(name = "image_pdf_conversion_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imagepdfconversionrequest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagePdfConversionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "source_document_id", nullable = false)
    private Long sourceDocumentId;

    @NotNull
    @Size(max = 255)
    @Column(name = "source_file_name", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sourceFileName;

    @NotNull
    @Size(max = 500)
    @Column(name = "source_pdf_s_3_key", length = 500, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sourcePdfS3Key;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "image_quality", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ImageQuality imageQuality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "image_format", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ImageFormat imageFormat;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conversion_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConversionType conversionType;

    @Min(value = 1)
    @Column(name = "start_page")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer startPage;

    @Min(value = 1)
    @Column(name = "end_page")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer endPage;

    @Min(value = 1)
    @Column(name = "total_pages")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer totalPages;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConversionStatus status;

    @Size(max = 2000)
    @Column(name = "error_message", length = 2000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorMessage;

    @NotNull
    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "processing_duration")
    private Long processingDuration;

    @Column(name = "total_images_size")
    private Long totalImagesSize;

    @Min(value = 0)
    @Column(name = "images_generated")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer imagesGenerated;

    @Min(value = 72)
    @Max(value = 1200)
    @Column(name = "dpi")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dpi;

    @Column(name = "requested_by_user_id")
    private Long requestedByUserId;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "priority")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer priority;

    @Lob
    @Column(name = "additional_options")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String additionalOptions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversionRequest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "conversionRequest" }, allowSetters = true)
    private Set<ImageGeneratedImage> generatedImages = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "conversions" }, allowSetters = true)
    private ImageConversionBatch batch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImagePdfConversionRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceDocumentId() {
        return this.sourceDocumentId;
    }

    public ImagePdfConversionRequest sourceDocumentId(Long sourceDocumentId) {
        this.setSourceDocumentId(sourceDocumentId);
        return this;
    }

    public void setSourceDocumentId(Long sourceDocumentId) {
        this.sourceDocumentId = sourceDocumentId;
    }

    public String getSourceFileName() {
        return this.sourceFileName;
    }

    public ImagePdfConversionRequest sourceFileName(String sourceFileName) {
        this.setSourceFileName(sourceFileName);
        return this;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public String getSourcePdfS3Key() {
        return this.sourcePdfS3Key;
    }

    public ImagePdfConversionRequest sourcePdfS3Key(String sourcePdfS3Key) {
        this.setSourcePdfS3Key(sourcePdfS3Key);
        return this;
    }

    public void setSourcePdfS3Key(String sourcePdfS3Key) {
        this.sourcePdfS3Key = sourcePdfS3Key;
    }

    public ImageQuality getImageQuality() {
        return this.imageQuality;
    }

    public ImagePdfConversionRequest imageQuality(ImageQuality imageQuality) {
        this.setImageQuality(imageQuality);
        return this;
    }

    public void setImageQuality(ImageQuality imageQuality) {
        this.imageQuality = imageQuality;
    }

    public ImageFormat getImageFormat() {
        return this.imageFormat;
    }

    public ImagePdfConversionRequest imageFormat(ImageFormat imageFormat) {
        this.setImageFormat(imageFormat);
        return this;
    }

    public void setImageFormat(ImageFormat imageFormat) {
        this.imageFormat = imageFormat;
    }

    public ConversionType getConversionType() {
        return this.conversionType;
    }

    public ImagePdfConversionRequest conversionType(ConversionType conversionType) {
        this.setConversionType(conversionType);
        return this;
    }

    public void setConversionType(ConversionType conversionType) {
        this.conversionType = conversionType;
    }

    public Integer getStartPage() {
        return this.startPage;
    }

    public ImagePdfConversionRequest startPage(Integer startPage) {
        this.setStartPage(startPage);
        return this;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return this.endPage;
    }

    public ImagePdfConversionRequest endPage(Integer endPage) {
        this.setEndPage(endPage);
        return this;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public ImagePdfConversionRequest totalPages(Integer totalPages) {
        this.setTotalPages(totalPages);
        return this;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public ConversionStatus getStatus() {
        return this.status;
    }

    public ImagePdfConversionRequest status(ConversionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ConversionStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ImagePdfConversionRequest errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getRequestedAt() {
        return this.requestedAt;
    }

    public ImagePdfConversionRequest requestedAt(Instant requestedAt) {
        this.setRequestedAt(requestedAt);
        return this;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public ImagePdfConversionRequest startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public ImagePdfConversionRequest completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getProcessingDuration() {
        return this.processingDuration;
    }

    public ImagePdfConversionRequest processingDuration(Long processingDuration) {
        this.setProcessingDuration(processingDuration);
        return this;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    public Long getTotalImagesSize() {
        return this.totalImagesSize;
    }

    public ImagePdfConversionRequest totalImagesSize(Long totalImagesSize) {
        this.setTotalImagesSize(totalImagesSize);
        return this;
    }

    public void setTotalImagesSize(Long totalImagesSize) {
        this.totalImagesSize = totalImagesSize;
    }

    public Integer getImagesGenerated() {
        return this.imagesGenerated;
    }

    public ImagePdfConversionRequest imagesGenerated(Integer imagesGenerated) {
        this.setImagesGenerated(imagesGenerated);
        return this;
    }

    public void setImagesGenerated(Integer imagesGenerated) {
        this.imagesGenerated = imagesGenerated;
    }

    public Integer getDpi() {
        return this.dpi;
    }

    public ImagePdfConversionRequest dpi(Integer dpi) {
        this.setDpi(dpi);
        return this;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public Long getRequestedByUserId() {
        return this.requestedByUserId;
    }

    public ImagePdfConversionRequest requestedByUserId(Long requestedByUserId) {
        this.setRequestedByUserId(requestedByUserId);
        return this;
    }

    public void setRequestedByUserId(Long requestedByUserId) {
        this.requestedByUserId = requestedByUserId;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public ImagePdfConversionRequest priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAdditionalOptions() {
        return this.additionalOptions;
    }

    public ImagePdfConversionRequest additionalOptions(String additionalOptions) {
        this.setAdditionalOptions(additionalOptions);
        return this;
    }

    public void setAdditionalOptions(String additionalOptions) {
        this.additionalOptions = additionalOptions;
    }

    public Set<ImageGeneratedImage> getGeneratedImages() {
        return this.generatedImages;
    }

    public void setGeneratedImages(Set<ImageGeneratedImage> imageGeneratedImages) {
        if (this.generatedImages != null) {
            this.generatedImages.forEach(i -> i.setConversionRequest(null));
        }
        if (imageGeneratedImages != null) {
            imageGeneratedImages.forEach(i -> i.setConversionRequest(this));
        }
        this.generatedImages = imageGeneratedImages;
    }

    public ImagePdfConversionRequest generatedImages(Set<ImageGeneratedImage> imageGeneratedImages) {
        this.setGeneratedImages(imageGeneratedImages);
        return this;
    }

    public ImagePdfConversionRequest addGeneratedImages(ImageGeneratedImage imageGeneratedImage) {
        this.generatedImages.add(imageGeneratedImage);
        imageGeneratedImage.setConversionRequest(this);
        return this;
    }

    public ImagePdfConversionRequest removeGeneratedImages(ImageGeneratedImage imageGeneratedImage) {
        this.generatedImages.remove(imageGeneratedImage);
        imageGeneratedImage.setConversionRequest(null);
        return this;
    }

    public ImageConversionBatch getBatch() {
        return this.batch;
    }

    public void setBatch(ImageConversionBatch imageConversionBatch) {
        this.batch = imageConversionBatch;
    }

    public ImagePdfConversionRequest batch(ImageConversionBatch imageConversionBatch) {
        this.setBatch(imageConversionBatch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagePdfConversionRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((ImagePdfConversionRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagePdfConversionRequest{" +
            "id=" + getId() +
            ", sourceDocumentId=" + getSourceDocumentId() +
            ", sourceFileName='" + getSourceFileName() + "'" +
            ", sourcePdfS3Key='" + getSourcePdfS3Key() + "'" +
            ", imageQuality='" + getImageQuality() + "'" +
            ", imageFormat='" + getImageFormat() + "'" +
            ", conversionType='" + getConversionType() + "'" +
            ", startPage=" + getStartPage() +
            ", endPage=" + getEndPage() +
            ", totalPages=" + getTotalPages() +
            ", status='" + getStatus() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", requestedAt='" + getRequestedAt() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", processingDuration=" + getProcessingDuration() +
            ", totalImagesSize=" + getTotalImagesSize() +
            ", imagesGenerated=" + getImagesGenerated() +
            ", dpi=" + getDpi() +
            ", requestedByUserId=" + getRequestedByUserId() +
            ", priority=" + getPriority() +
            ", additionalOptions='" + getAdditionalOptions() + "'" +
            "}";
    }
}
