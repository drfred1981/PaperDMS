package fr.smartprod.paperdms.pdftoimage.service.dto;

import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage} entity.
 */
@Schema(description = "Entité - Image générée depuis un PDF")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageGeneratedImageDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer pageNumber;

    @NotNull
    @Size(max = 255)
    private String fileName;

    @NotNull
    @Size(max = 500)
    private String s3Key;

    @Size(max = 1000)
    private String preSignedUrl;

    private Instant urlExpiresAt;

    @NotNull
    private ImageFormat format;

    @NotNull
    private ImageQuality quality;

    @NotNull
    @Min(value = 1)
    private Integer width;

    @NotNull
    @Min(value = 1)
    private Integer height;

    @NotNull
    @Min(value = 0L)
    private Long fileSize;

    @NotNull
    @Min(value = 72)
    private Integer dpi;

    @Size(max = 64)
    private String sha256Hash;

    @NotNull
    private Instant generatedAt;

    @Lob
    private String metadata;

    @NotNull
    private ImagePdfConversionRequestDTO conversionRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String gets3Key() {
        return s3Key;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getPreSignedUrl() {
        return preSignedUrl;
    }

    public void setPreSignedUrl(String preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public Instant getUrlExpiresAt() {
        return urlExpiresAt;
    }

    public void setUrlExpiresAt(Instant urlExpiresAt) {
        this.urlExpiresAt = urlExpiresAt;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public void setFormat(ImageFormat format) {
        this.format = format;
    }

    public ImageQuality getQuality() {
        return quality;
    }

    public void setQuality(ImageQuality quality) {
        this.quality = quality;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDpi() {
        return dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public String getSha256Hash() {
        return sha256Hash;
    }

    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public ImagePdfConversionRequestDTO getConversionRequest() {
        return conversionRequest;
    }

    public void setConversionRequest(ImagePdfConversionRequestDTO conversionRequest) {
        this.conversionRequest = conversionRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageGeneratedImageDTO)) {
            return false;
        }

        ImageGeneratedImageDTO imageGeneratedImageDTO = (ImageGeneratedImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageGeneratedImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageGeneratedImageDTO{" +
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
            ", conversionRequest=" + getConversionRequest() +
            "}";
    }
}
