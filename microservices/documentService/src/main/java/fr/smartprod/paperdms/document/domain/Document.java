package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "title", length = 500, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @NotNull
    @Size(max = 500)
    @Column(name = "file_name", length = 500, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fileName;

    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @NotNull
    @Size(max = 100)
    @Column(name = "mime_type", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mimeType;

    @NotNull
    @Size(max = 64)
    @Column(name = "sha_256", length = 64, nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sha256;

    @NotNull
    @Size(max = 1000)
    @Column(name = "s_3_key", length = 1000, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Key;

    @NotNull
    @Size(max = 255)
    @Column(name = "s_3_bucket", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Bucket;

    @Size(max = 50)
    @Column(name = "s_3_region", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3Region;

    @Size(max = 100)
    @Column(name = "s_3_e_meta_tag", length = 100)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String s3EMetaTag;

    @Size(max = 1000)
    @Column(name = "thumbnail_s_3_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String thumbnailS3Key;

    @Size(max = 64)
    @Column(name = "thumbnail_sha_256", length = 64)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String thumbnailSha256;

    @Size(max = 1000)
    @Column(name = "webp_preview_s_3_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String webpPreviewS3Key;

    @Size(max = 64)
    @Column(name = "webp_preview_sha_256", length = 64)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String webpPreviewSha256;

    @NotNull
    @Column(name = "upload_date", nullable = false)
    private Instant uploadDate;

    @NotNull
    @Column(name = "is_public", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPublic;

    @Column(name = "download_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer downloadCount;

    @Column(name = "view_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer viewCount;

    @Size(max = 10)
    @Column(name = "detected_language", length = 10)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String detectedLanguage;

    @Size(max = 10)
    @Column(name = "manual_language", length = 10)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String manualLanguage;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "language_confidence")
    private Double languageConfidence;

    @Column(name = "page_count")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pageCount;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentVersion> documentVersions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document", "metaTag" }, allowSetters = true)
    private Set<DocumentTag> documentTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentServiceStatus> statuses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentExtractedField> documentExtractedFields = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document", "metaPermissionGroup" }, allowSetters = true)
    private Set<DocumentPermission> permissions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentAudit> audits = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "replies", "document", "parentComment" }, allowSetters = true)
    private Set<DocumentComment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentMetadata> metadatas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    private Set<DocumentStatistics> statistics = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "documents", "documentTemplates", "fields" }, allowSetters = true)
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "documents", "parent" }, allowSetters = true)
    private MetaFolder folder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Document title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Document fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public Document fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Document mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSha256() {
        return this.sha256;
    }

    public Document sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String gets3Key() {
        return this.s3Key;
    }

    public Document s3Key(String s3Key) {
        this.sets3Key(s3Key);
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3Bucket() {
        return this.s3Bucket;
    }

    public Document s3Bucket(String s3Bucket) {
        this.sets3Bucket(s3Bucket);
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3Region() {
        return this.s3Region;
    }

    public Document s3Region(String s3Region) {
        this.sets3Region(s3Region);
        return this;
    }

    public void sets3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public String gets3EMetaTag() {
        return this.s3EMetaTag;
    }

    public Document s3EMetaTag(String s3EMetaTag) {
        this.sets3EMetaTag(s3EMetaTag);
        return this;
    }

    public void sets3EMetaTag(String s3EMetaTag) {
        this.s3EMetaTag = s3EMetaTag;
    }

    public String getThumbnailS3Key() {
        return this.thumbnailS3Key;
    }

    public Document thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public String getThumbnailSha256() {
        return this.thumbnailSha256;
    }

    public Document thumbnailSha256(String thumbnailSha256) {
        this.setThumbnailSha256(thumbnailSha256);
        return this;
    }

    public void setThumbnailSha256(String thumbnailSha256) {
        this.thumbnailSha256 = thumbnailSha256;
    }

    public String getWebpPreviewS3Key() {
        return this.webpPreviewS3Key;
    }

    public Document webpPreviewS3Key(String webpPreviewS3Key) {
        this.setWebpPreviewS3Key(webpPreviewS3Key);
        return this;
    }

    public void setWebpPreviewS3Key(String webpPreviewS3Key) {
        this.webpPreviewS3Key = webpPreviewS3Key;
    }

    public String getWebpPreviewSha256() {
        return this.webpPreviewSha256;
    }

    public Document webpPreviewSha256(String webpPreviewSha256) {
        this.setWebpPreviewSha256(webpPreviewSha256);
        return this;
    }

    public void setWebpPreviewSha256(String webpPreviewSha256) {
        this.webpPreviewSha256 = webpPreviewSha256;
    }

    public Instant getUploadDate() {
        return this.uploadDate;
    }

    public Document uploadDate(Instant uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public Document isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getDownloadCount() {
        return this.downloadCount;
    }

    public Document downloadCount(Integer downloadCount) {
        this.setDownloadCount(downloadCount);
        return this;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getViewCount() {
        return this.viewCount;
    }

    public Document viewCount(Integer viewCount) {
        this.setViewCount(viewCount);
        return this;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getDetectedLanguage() {
        return this.detectedLanguage;
    }

    public Document detectedLanguage(String detectedLanguage) {
        this.setDetectedLanguage(detectedLanguage);
        return this;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public String getManualLanguage() {
        return this.manualLanguage;
    }

    public Document manualLanguage(String manualLanguage) {
        this.setManualLanguage(manualLanguage);
        return this;
    }

    public void setManualLanguage(String manualLanguage) {
        this.manualLanguage = manualLanguage;
    }

    public Double getLanguageConfidence() {
        return this.languageConfidence;
    }

    public Document languageConfidence(Double languageConfidence) {
        this.setLanguageConfidence(languageConfidence);
        return this;
    }

    public void setLanguageConfidence(Double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public Document pageCount(Integer pageCount) {
        this.setPageCount(pageCount);
        return this;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Document createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Document createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<DocumentVersion> getDocumentVersions() {
        return this.documentVersions;
    }

    public void setDocumentVersions(Set<DocumentVersion> documentVersions) {
        if (this.documentVersions != null) {
            this.documentVersions.forEach(i -> i.setDocument(null));
        }
        if (documentVersions != null) {
            documentVersions.forEach(i -> i.setDocument(this));
        }
        this.documentVersions = documentVersions;
    }

    public Document documentVersions(Set<DocumentVersion> documentVersions) {
        this.setDocumentVersions(documentVersions);
        return this;
    }

    public Document addDocumentVersions(DocumentVersion documentVersion) {
        this.documentVersions.add(documentVersion);
        documentVersion.setDocument(this);
        return this;
    }

    public Document removeDocumentVersions(DocumentVersion documentVersion) {
        this.documentVersions.remove(documentVersion);
        documentVersion.setDocument(null);
        return this;
    }

    public Set<DocumentTag> getDocumentTags() {
        return this.documentTags;
    }

    public void setDocumentTags(Set<DocumentTag> documentTags) {
        if (this.documentTags != null) {
            this.documentTags.forEach(i -> i.setDocument(null));
        }
        if (documentTags != null) {
            documentTags.forEach(i -> i.setDocument(this));
        }
        this.documentTags = documentTags;
    }

    public Document documentTags(Set<DocumentTag> documentTags) {
        this.setDocumentTags(documentTags);
        return this;
    }

    public Document addDocumentTags(DocumentTag documentTag) {
        this.documentTags.add(documentTag);
        documentTag.setDocument(this);
        return this;
    }

    public Document removeDocumentTags(DocumentTag documentTag) {
        this.documentTags.remove(documentTag);
        documentTag.setDocument(null);
        return this;
    }

    public Set<DocumentServiceStatus> getStatuses() {
        return this.statuses;
    }

    public void setStatuses(Set<DocumentServiceStatus> documentServiceStatuses) {
        if (this.statuses != null) {
            this.statuses.forEach(i -> i.setDocument(null));
        }
        if (documentServiceStatuses != null) {
            documentServiceStatuses.forEach(i -> i.setDocument(this));
        }
        this.statuses = documentServiceStatuses;
    }

    public Document statuses(Set<DocumentServiceStatus> documentServiceStatuses) {
        this.setStatuses(documentServiceStatuses);
        return this;
    }

    public Document addStatuses(DocumentServiceStatus documentServiceStatus) {
        this.statuses.add(documentServiceStatus);
        documentServiceStatus.setDocument(this);
        return this;
    }

    public Document removeStatuses(DocumentServiceStatus documentServiceStatus) {
        this.statuses.remove(documentServiceStatus);
        documentServiceStatus.setDocument(null);
        return this;
    }

    public Set<DocumentExtractedField> getDocumentExtractedFields() {
        return this.documentExtractedFields;
    }

    public void setDocumentExtractedFields(Set<DocumentExtractedField> documentExtractedFields) {
        if (this.documentExtractedFields != null) {
            this.documentExtractedFields.forEach(i -> i.setDocument(null));
        }
        if (documentExtractedFields != null) {
            documentExtractedFields.forEach(i -> i.setDocument(this));
        }
        this.documentExtractedFields = documentExtractedFields;
    }

    public Document documentExtractedFields(Set<DocumentExtractedField> documentExtractedFields) {
        this.setDocumentExtractedFields(documentExtractedFields);
        return this;
    }

    public Document addDocumentExtractedFields(DocumentExtractedField documentExtractedField) {
        this.documentExtractedFields.add(documentExtractedField);
        documentExtractedField.setDocument(this);
        return this;
    }

    public Document removeDocumentExtractedFields(DocumentExtractedField documentExtractedField) {
        this.documentExtractedFields.remove(documentExtractedField);
        documentExtractedField.setDocument(null);
        return this;
    }

    public Set<DocumentPermission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<DocumentPermission> documentPermissions) {
        if (this.permissions != null) {
            this.permissions.forEach(i -> i.setDocument(null));
        }
        if (documentPermissions != null) {
            documentPermissions.forEach(i -> i.setDocument(this));
        }
        this.permissions = documentPermissions;
    }

    public Document permissions(Set<DocumentPermission> documentPermissions) {
        this.setPermissions(documentPermissions);
        return this;
    }

    public Document addPermissions(DocumentPermission documentPermission) {
        this.permissions.add(documentPermission);
        documentPermission.setDocument(this);
        return this;
    }

    public Document removePermissions(DocumentPermission documentPermission) {
        this.permissions.remove(documentPermission);
        documentPermission.setDocument(null);
        return this;
    }

    public Set<DocumentAudit> getAudits() {
        return this.audits;
    }

    public void setAudits(Set<DocumentAudit> documentAudits) {
        if (this.audits != null) {
            this.audits.forEach(i -> i.setDocument(null));
        }
        if (documentAudits != null) {
            documentAudits.forEach(i -> i.setDocument(this));
        }
        this.audits = documentAudits;
    }

    public Document audits(Set<DocumentAudit> documentAudits) {
        this.setAudits(documentAudits);
        return this;
    }

    public Document addAudits(DocumentAudit documentAudit) {
        this.audits.add(documentAudit);
        documentAudit.setDocument(this);
        return this;
    }

    public Document removeAudits(DocumentAudit documentAudit) {
        this.audits.remove(documentAudit);
        documentAudit.setDocument(null);
        return this;
    }

    public Set<DocumentComment> getComments() {
        return this.comments;
    }

    public void setComments(Set<DocumentComment> documentComments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setDocument(null));
        }
        if (documentComments != null) {
            documentComments.forEach(i -> i.setDocument(this));
        }
        this.comments = documentComments;
    }

    public Document comments(Set<DocumentComment> documentComments) {
        this.setComments(documentComments);
        return this;
    }

    public Document addComments(DocumentComment documentComment) {
        this.comments.add(documentComment);
        documentComment.setDocument(this);
        return this;
    }

    public Document removeComments(DocumentComment documentComment) {
        this.comments.remove(documentComment);
        documentComment.setDocument(null);
        return this;
    }

    public Set<DocumentMetadata> getMetadatas() {
        return this.metadatas;
    }

    public void setMetadatas(Set<DocumentMetadata> documentMetadata) {
        if (this.metadatas != null) {
            this.metadatas.forEach(i -> i.setDocument(null));
        }
        if (documentMetadata != null) {
            documentMetadata.forEach(i -> i.setDocument(this));
        }
        this.metadatas = documentMetadata;
    }

    public Document metadatas(Set<DocumentMetadata> documentMetadata) {
        this.setMetadatas(documentMetadata);
        return this;
    }

    public Document addMetadatas(DocumentMetadata documentMetadata) {
        this.metadatas.add(documentMetadata);
        documentMetadata.setDocument(this);
        return this;
    }

    public Document removeMetadatas(DocumentMetadata documentMetadata) {
        this.metadatas.remove(documentMetadata);
        documentMetadata.setDocument(null);
        return this;
    }

    public Set<DocumentStatistics> getStatistics() {
        return this.statistics;
    }

    public void setStatistics(Set<DocumentStatistics> documentStatistics) {
        if (this.statistics != null) {
            this.statistics.forEach(i -> i.setDocument(null));
        }
        if (documentStatistics != null) {
            documentStatistics.forEach(i -> i.setDocument(this));
        }
        this.statistics = documentStatistics;
    }

    public Document statistics(Set<DocumentStatistics> documentStatistics) {
        this.setStatistics(documentStatistics);
        return this;
    }

    public Document addStatistics(DocumentStatistics documentStatistics) {
        this.statistics.add(documentStatistics);
        documentStatistics.setDocument(this);
        return this;
    }

    public Document removeStatistics(DocumentStatistics documentStatistics) {
        this.statistics.remove(documentStatistics);
        documentStatistics.setDocument(null);
        return this;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Document documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public MetaFolder getFolder() {
        return this.folder;
    }

    public void setFolder(MetaFolder metaFolder) {
        this.folder = metaFolder;
    }

    public Document folder(MetaFolder metaFolder) {
        this.setFolder(metaFolder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return getId() != null && getId().equals(((Document) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", s3Key='" + gets3Key() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", s3Region='" + gets3Region() + "'" +
            ", s3EMetaTag='" + gets3EMetaTag() + "'" +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", thumbnailSha256='" + getThumbnailSha256() + "'" +
            ", webpPreviewS3Key='" + getWebpPreviewS3Key() + "'" +
            ", webpPreviewSha256='" + getWebpPreviewSha256() + "'" +
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
            "}";
    }
}
