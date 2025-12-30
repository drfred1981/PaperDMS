package fr.smartprod.paperdms.pdftoimage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entité - Image générée depuis un PDF
 */
@Entity
@Table(name = "image_generated_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "imagegeneratedimage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageGeneratedImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "page_number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pageNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_name", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fileName;

    @NotNull
    @Size(max = 500)
    @Column(name = "s_3_key", length = 500, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Key;

    @Size(max = 1000)
    @Column(name = "pre_signed_url", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String preSignedUrl;

    @Column(name = "url_expires_at")
    private Instant urlExpiresAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ImageFormat format;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "quality", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ImageQuality quality;

    @NotNull
    @Min(value = 1)
    @Column(name = "width", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer width;

    @NotNull
    @Min(value = 1)
    @Column(name = "height", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer height;

    @NotNull
    @Min(value = 0L)
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @NotNull
    @Min(value = 72)
    @Column(name = "dpi", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dpi;

    @Size(max = 64)
    @Column(name = "sha_256_hash", length = 64)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sha256Hash;

    @NotNull
    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String metadata;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "generatedImages", "batch" }, allowSetters = true)
    private ImagePdfConversionRequest conversionRequest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImageGeneratedImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public ImageGeneratedImage pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFileName() {
        return this.fileName;
    }

    public ImageGeneratedImage fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public ImageGeneratedImage s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getPreSignedUrl() {
        return this.preSignedUrl;
    }

    public ImageGeneratedImage preSignedUrl(String preSignedUrl) {
        this.setPreSignedUrl(preSignedUrl);
        return this;
    }

    public void setPreSignedUrl(String preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public Instant getUrlExpiresAt() {
        return this.urlExpiresAt;
    }

    public ImageGeneratedImage urlExpiresAt(Instant urlExpiresAt) {
        this.setUrlExpiresAt(urlExpiresAt);
        return this;
    }

    public void setUrlExpiresAt(Instant urlExpiresAt) {
        this.urlExpiresAt = urlExpiresAt;
    }

    public ImageFormat getFormat() {
        return this.format;
    }

    public ImageGeneratedImage format(ImageFormat format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(ImageFormat format) {
        this.format = format;
    }

    public ImageQuality getQuality() {
        return this.quality;
    }

    public ImageGeneratedImage quality(ImageQuality quality) {
        this.setQuality(quality);
        return this;
    }

    public void setQuality(ImageQuality quality) {
        this.quality = quality;
    }

    public Integer getWidth() {
        return this.width;
    }

    public ImageGeneratedImage width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public ImageGeneratedImage height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public ImageGeneratedImage fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDpi() {
        return this.dpi;
    }

    public ImageGeneratedImage dpi(Integer dpi) {
        this.setDpi(dpi);
        return this;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public String getSha256Hash() {
        return this.sha256Hash;
    }

    public ImageGeneratedImage sha256Hash(String sha256Hash) {
        this.setSha256Hash(sha256Hash);
        return this;
    }

    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    public Instant getGeneratedAt() {
        return this.generatedAt;
    }

    public ImageGeneratedImage generatedAt(Instant generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public ImageGeneratedImage metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public ImagePdfConversionRequest getConversionRequest() {
        return this.conversionRequest;
    }

    public void setConversionRequest(ImagePdfConversionRequest imagePdfConversionRequest) {
        this.conversionRequest = imagePdfConversionRequest;
    }

    public ImageGeneratedImage conversionRequest(ImagePdfConversionRequest imagePdfConversionRequest) {
        this.setConversionRequest(imagePdfConversionRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageGeneratedImage)) {
            return false;
        }
        return getId() != null && getId().equals(((ImageGeneratedImage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageGeneratedImage{" +
            "id=" + getId() +
            ", pageNumber=" + getPageNumber() +
            ", fileName='" + getFileName() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", preSignedUrl='" + getPreSignedUrl() + "'" +
            ", urlExpiresAt='" + getUrlExpiresAt() + "'" +
            ", format='" + getFormat() + "'" +
            ", quality='" + getQuality() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", fileSize=" + getFileSize() +
            ", dpi=" + getDpi() +
            ", sha256Hash='" + getSha256Hash() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
