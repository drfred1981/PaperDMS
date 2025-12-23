package fr.smartprod.paperdms.document.service.dto;

import fr.smartprod.paperdms.document.domain.enumeration.DocumentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.document.domain.Document} entity.
 */
@Schema(
    description = "Document Service - 22 Entit�s (MODIFI�)\nMODIFICATION: Ajout de DocumentServiceStatus pour tracker l'�tat par service"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String title;

    @NotNull
    @Size(max = 500)
    private String fileName;

    @NotNull
    private Long fileSize;

    @NotNull
    @Size(max = 100)
    private String mimeType;

    @NotNull
    @Size(max = 64)
    private String sha256;

    @NotNull
    @Size(max = 1000)
    private String s3Key;

    @NotNull
    @Size(max = 255)
    private String s3Bucket;

    @Size(max = 50)
    private String s3Region;

    @Size(max = 100)
    private String s3Etag;

    @Size(max = 1000)
    private String thumbnailS3Key;

    @Size(max = 64)
    private String thumbnailSha256;

    @Size(max = 1000)
    private String webpPreviewS3Key;

    @Size(max = 64)
    private String webpPreviewSha256;

    @NotNull
    private DocumentStatus status;

    @NotNull
    private Instant uploadDate;

    @NotNull
    private Boolean isPublic;

    private Integer downloadCount;

    private Integer viewCount;

    @Size(max = 10)
    private String detectedLanguage;

    @Size(max = 10)
    private String manualLanguage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double languageConfidence;

    private Integer pageCount;

    @NotNull
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    private FolderDTO folder;

    @NotNull
    private DocumentTypeDTO documentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String gets3Key() {
        return s3Key;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3Bucket() {
        return s3Bucket;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3Region() {
        return s3Region;
    }

    public void sets3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public String gets3Etag() {
        return s3Etag;
    }

    public void sets3Etag(String s3Etag) {
        this.s3Etag = s3Etag;
    }

    public String getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public String getThumbnailSha256() {
        return thumbnailSha256;
    }

    public void setThumbnailSha256(String thumbnailSha256) {
        this.thumbnailSha256 = thumbnailSha256;
    }

    public String getWebpPreviewS3Key() {
        return webpPreviewS3Key;
    }

    public void setWebpPreviewS3Key(String webpPreviewS3Key) {
        this.webpPreviewS3Key = webpPreviewS3Key;
    }

    public String getWebpPreviewSha256() {
        return webpPreviewSha256;
    }

    public void setWebpPreviewSha256(String webpPreviewSha256) {
        this.webpPreviewSha256 = webpPreviewSha256;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public String getManualLanguage() {
        return manualLanguage;
    }

    public void setManualLanguage(String manualLanguage) {
        this.manualLanguage = manualLanguage;
    }

    public Double getLanguageConfidence() {
        return languageConfidence;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public FolderDTO getFolder() {
        return folder;
    }

    public void setFolder(FolderDTO folder) {
        this.folder = folder;
    }

    public DocumentTypeDTO getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeDTO documentType) {
        this.documentType = documentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", s3Region='" + gets3Region() + "'" +
            ", s3Etag='" + gets3Etag() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", thumbnailSha256='" + getThumbnailSha256() + "'" +
            ", webpPreviewS3Key='" + getWebpPreviewS3Key() + "'" +
            ", webpPreviewSha256='" + getWebpPreviewSha256() + "'" +
            ", status='" + getStatus() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", downloadCount=" + getDownloadCount() +
            ", viewCount=" + getViewCount() +
            ", detectedLanguage='" + getDetectedLanguage() + "'" +
            ", manualLanguage='" + getManualLanguage() + "'" +
            ", languageConfidence=" + getLanguageConfidence() +
            ", pageCount=" + getPageCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", folder=" + getFolder() +
            ", documentType=" + getDocumentType() +
            "}";
    }
}
