package fr.smartprod.paperdms.document.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.Document} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter fileName;

    private LongFilter fileSize;

    private StringFilter mimeType;

    private StringFilter sha256;

    private StringFilter s3Key;

    private StringFilter s3Bucket;

    private StringFilter s3Region;

    private StringFilter s3Etag;

    private StringFilter thumbnailS3Key;

    private StringFilter thumbnailSha256;

    private StringFilter webpPreviewS3Key;

    private StringFilter webpPreviewSha256;

    private InstantFilter uploadDate;

    private BooleanFilter isPublic;

    private IntegerFilter downloadCount;

    private IntegerFilter viewCount;

    private StringFilter detectedLanguage;

    private StringFilter manualLanguage;

    private DoubleFilter languageConfidence;

    private IntegerFilter pageCount;

    private InstantFilter createdDate;

    private StringFilter createdBy;

    private LongFilter folderId;

    private LongFilter documentTypeId;

    private Boolean distinct;

    public DocumentCriteria() {}

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.fileName = other.optionalFileName().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.mimeType = other.optionalMimeType().map(StringFilter::copy).orElse(null);
        this.sha256 = other.optionalSha256().map(StringFilter::copy).orElse(null);
        this.s3Key = other.optionals3Key().map(StringFilter::copy).orElse(null);
        this.s3Bucket = other.optionals3Bucket().map(StringFilter::copy).orElse(null);
        this.s3Region = other.optionals3Region().map(StringFilter::copy).orElse(null);
        this.s3Etag = other.optionals3Etag().map(StringFilter::copy).orElse(null);
        this.thumbnailS3Key = other.optionalThumbnailS3Key().map(StringFilter::copy).orElse(null);
        this.thumbnailSha256 = other.optionalThumbnailSha256().map(StringFilter::copy).orElse(null);
        this.webpPreviewS3Key = other.optionalWebpPreviewS3Key().map(StringFilter::copy).orElse(null);
        this.webpPreviewSha256 = other.optionalWebpPreviewSha256().map(StringFilter::copy).orElse(null);
        this.uploadDate = other.optionalUploadDate().map(InstantFilter::copy).orElse(null);
        this.isPublic = other.optionalIsPublic().map(BooleanFilter::copy).orElse(null);
        this.downloadCount = other.optionalDownloadCount().map(IntegerFilter::copy).orElse(null);
        this.viewCount = other.optionalViewCount().map(IntegerFilter::copy).orElse(null);
        this.detectedLanguage = other.optionalDetectedLanguage().map(StringFilter::copy).orElse(null);
        this.manualLanguage = other.optionalManualLanguage().map(StringFilter::copy).orElse(null);
        this.languageConfidence = other.optionalLanguageConfidence().map(DoubleFilter::copy).orElse(null);
        this.pageCount = other.optionalPageCount().map(IntegerFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.folderId = other.optionalFolderId().map(LongFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public Optional<StringFilter> optionalFileName() {
        return Optional.ofNullable(fileName);
    }

    public StringFilter fileName() {
        if (fileName == null) {
            setFileName(new StringFilter());
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public StringFilter getMimeType() {
        return mimeType;
    }

    public Optional<StringFilter> optionalMimeType() {
        return Optional.ofNullable(mimeType);
    }

    public StringFilter mimeType() {
        if (mimeType == null) {
            setMimeType(new StringFilter());
        }
        return mimeType;
    }

    public void setMimeType(StringFilter mimeType) {
        this.mimeType = mimeType;
    }

    public StringFilter getSha256() {
        return sha256;
    }

    public Optional<StringFilter> optionalSha256() {
        return Optional.ofNullable(sha256);
    }

    public StringFilter sha256() {
        if (sha256 == null) {
            setSha256(new StringFilter());
        }
        return sha256;
    }

    public void setSha256(StringFilter sha256) {
        this.sha256 = sha256;
    }

    public StringFilter gets3Key() {
        return s3Key;
    }

    public Optional<StringFilter> optionals3Key() {
        return Optional.ofNullable(s3Key);
    }

    public StringFilter s3Key() {
        if (s3Key == null) {
            sets3Key(new StringFilter());
        }
        return s3Key;
    }

    public void sets3Key(StringFilter s3Key) {
        this.s3Key = s3Key;
    }

    public StringFilter gets3Bucket() {
        return s3Bucket;
    }

    public Optional<StringFilter> optionals3Bucket() {
        return Optional.ofNullable(s3Bucket);
    }

    public StringFilter s3Bucket() {
        if (s3Bucket == null) {
            sets3Bucket(new StringFilter());
        }
        return s3Bucket;
    }

    public void sets3Bucket(StringFilter s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public StringFilter gets3Region() {
        return s3Region;
    }

    public Optional<StringFilter> optionals3Region() {
        return Optional.ofNullable(s3Region);
    }

    public StringFilter s3Region() {
        if (s3Region == null) {
            sets3Region(new StringFilter());
        }
        return s3Region;
    }

    public void sets3Region(StringFilter s3Region) {
        this.s3Region = s3Region;
    }

    public StringFilter gets3Etag() {
        return s3Etag;
    }

    public Optional<StringFilter> optionals3Etag() {
        return Optional.ofNullable(s3Etag);
    }

    public StringFilter s3Etag() {
        if (s3Etag == null) {
            sets3Etag(new StringFilter());
        }
        return s3Etag;
    }

    public void sets3Etag(StringFilter s3Etag) {
        this.s3Etag = s3Etag;
    }

    public StringFilter getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public Optional<StringFilter> optionalThumbnailS3Key() {
        return Optional.ofNullable(thumbnailS3Key);
    }

    public StringFilter thumbnailS3Key() {
        if (thumbnailS3Key == null) {
            setThumbnailS3Key(new StringFilter());
        }
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(StringFilter thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public StringFilter getThumbnailSha256() {
        return thumbnailSha256;
    }

    public Optional<StringFilter> optionalThumbnailSha256() {
        return Optional.ofNullable(thumbnailSha256);
    }

    public StringFilter thumbnailSha256() {
        if (thumbnailSha256 == null) {
            setThumbnailSha256(new StringFilter());
        }
        return thumbnailSha256;
    }

    public void setThumbnailSha256(StringFilter thumbnailSha256) {
        this.thumbnailSha256 = thumbnailSha256;
    }

    public StringFilter getWebpPreviewS3Key() {
        return webpPreviewS3Key;
    }

    public Optional<StringFilter> optionalWebpPreviewS3Key() {
        return Optional.ofNullable(webpPreviewS3Key);
    }

    public StringFilter webpPreviewS3Key() {
        if (webpPreviewS3Key == null) {
            setWebpPreviewS3Key(new StringFilter());
        }
        return webpPreviewS3Key;
    }

    public void setWebpPreviewS3Key(StringFilter webpPreviewS3Key) {
        this.webpPreviewS3Key = webpPreviewS3Key;
    }

    public StringFilter getWebpPreviewSha256() {
        return webpPreviewSha256;
    }

    public Optional<StringFilter> optionalWebpPreviewSha256() {
        return Optional.ofNullable(webpPreviewSha256);
    }

    public StringFilter webpPreviewSha256() {
        if (webpPreviewSha256 == null) {
            setWebpPreviewSha256(new StringFilter());
        }
        return webpPreviewSha256;
    }

    public void setWebpPreviewSha256(StringFilter webpPreviewSha256) {
        this.webpPreviewSha256 = webpPreviewSha256;
    }

    public InstantFilter getUploadDate() {
        return uploadDate;
    }

    public Optional<InstantFilter> optionalUploadDate() {
        return Optional.ofNullable(uploadDate);
    }

    public InstantFilter uploadDate() {
        if (uploadDate == null) {
            setUploadDate(new InstantFilter());
        }
        return uploadDate;
    }

    public void setUploadDate(InstantFilter uploadDate) {
        this.uploadDate = uploadDate;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public Optional<BooleanFilter> optionalIsPublic() {
        return Optional.ofNullable(isPublic);
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            setIsPublic(new BooleanFilter());
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public IntegerFilter getDownloadCount() {
        return downloadCount;
    }

    public Optional<IntegerFilter> optionalDownloadCount() {
        return Optional.ofNullable(downloadCount);
    }

    public IntegerFilter downloadCount() {
        if (downloadCount == null) {
            setDownloadCount(new IntegerFilter());
        }
        return downloadCount;
    }

    public void setDownloadCount(IntegerFilter downloadCount) {
        this.downloadCount = downloadCount;
    }

    public IntegerFilter getViewCount() {
        return viewCount;
    }

    public Optional<IntegerFilter> optionalViewCount() {
        return Optional.ofNullable(viewCount);
    }

    public IntegerFilter viewCount() {
        if (viewCount == null) {
            setViewCount(new IntegerFilter());
        }
        return viewCount;
    }

    public void setViewCount(IntegerFilter viewCount) {
        this.viewCount = viewCount;
    }

    public StringFilter getDetectedLanguage() {
        return detectedLanguage;
    }

    public Optional<StringFilter> optionalDetectedLanguage() {
        return Optional.ofNullable(detectedLanguage);
    }

    public StringFilter detectedLanguage() {
        if (detectedLanguage == null) {
            setDetectedLanguage(new StringFilter());
        }
        return detectedLanguage;
    }

    public void setDetectedLanguage(StringFilter detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public StringFilter getManualLanguage() {
        return manualLanguage;
    }

    public Optional<StringFilter> optionalManualLanguage() {
        return Optional.ofNullable(manualLanguage);
    }

    public StringFilter manualLanguage() {
        if (manualLanguage == null) {
            setManualLanguage(new StringFilter());
        }
        return manualLanguage;
    }

    public void setManualLanguage(StringFilter manualLanguage) {
        this.manualLanguage = manualLanguage;
    }

    public DoubleFilter getLanguageConfidence() {
        return languageConfidence;
    }

    public Optional<DoubleFilter> optionalLanguageConfidence() {
        return Optional.ofNullable(languageConfidence);
    }

    public DoubleFilter languageConfidence() {
        if (languageConfidence == null) {
            setLanguageConfidence(new DoubleFilter());
        }
        return languageConfidence;
    }

    public void setLanguageConfidence(DoubleFilter languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public IntegerFilter getPageCount() {
        return pageCount;
    }

    public Optional<IntegerFilter> optionalPageCount() {
        return Optional.ofNullable(pageCount);
    }

    public IntegerFilter pageCount() {
        if (pageCount == null) {
            setPageCount(new IntegerFilter());
        }
        return pageCount;
    }

    public void setPageCount(IntegerFilter pageCount) {
        this.pageCount = pageCount;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public LongFilter getFolderId() {
        return folderId;
    }

    public Optional<LongFilter> optionalFolderId() {
        return Optional.ofNullable(folderId);
    }

    public LongFilter folderId() {
        if (folderId == null) {
            setFolderId(new LongFilter());
        }
        return folderId;
    }

    public void setFolderId(LongFilter folderId) {
        this.folderId = folderId;
    }

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public Optional<LongFilter> optionalDocumentTypeId() {
        return Optional.ofNullable(documentTypeId);
    }

    public LongFilter documentTypeId() {
        if (documentTypeId == null) {
            setDocumentTypeId(new LongFilter());
        }
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentCriteria that = (DocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(mimeType, that.mimeType) &&
            Objects.equals(sha256, that.sha256) &&
            Objects.equals(s3Key, that.s3Key) &&
            Objects.equals(s3Bucket, that.s3Bucket) &&
            Objects.equals(s3Region, that.s3Region) &&
            Objects.equals(s3Etag, that.s3Etag) &&
            Objects.equals(thumbnailS3Key, that.thumbnailS3Key) &&
            Objects.equals(thumbnailSha256, that.thumbnailSha256) &&
            Objects.equals(webpPreviewS3Key, that.webpPreviewS3Key) &&
            Objects.equals(webpPreviewSha256, that.webpPreviewSha256) &&
            Objects.equals(uploadDate, that.uploadDate) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(downloadCount, that.downloadCount) &&
            Objects.equals(viewCount, that.viewCount) &&
            Objects.equals(detectedLanguage, that.detectedLanguage) &&
            Objects.equals(manualLanguage, that.manualLanguage) &&
            Objects.equals(languageConfidence, that.languageConfidence) &&
            Objects.equals(pageCount, that.pageCount) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(folderId, that.folderId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            fileName,
            fileSize,
            mimeType,
            sha256,
            s3Key,
            s3Bucket,
            s3Region,
            s3Etag,
            thumbnailS3Key,
            thumbnailSha256,
            webpPreviewS3Key,
            webpPreviewSha256,
            uploadDate,
            isPublic,
            downloadCount,
            viewCount,
            detectedLanguage,
            manualLanguage,
            languageConfidence,
            pageCount,
            createdDate,
            createdBy,
            folderId,
            documentTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalFileName().map(f -> "fileName=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalMimeType().map(f -> "mimeType=" + f + ", ").orElse("") +
            optionalSha256().map(f -> "sha256=" + f + ", ").orElse("") +
            optionals3Key().map(f -> "s3Key=" + f + ", ").orElse("") +
            optionals3Bucket().map(f -> "s3Bucket=" + f + ", ").orElse("") +
            optionals3Region().map(f -> "s3Region=" + f + ", ").orElse("") +
            optionals3Etag().map(f -> "s3Etag=" + f + ", ").orElse("") +
            optionalThumbnailS3Key().map(f -> "thumbnailS3Key=" + f + ", ").orElse("") +
            optionalThumbnailSha256().map(f -> "thumbnailSha256=" + f + ", ").orElse("") +
            optionalWebpPreviewS3Key().map(f -> "webpPreviewS3Key=" + f + ", ").orElse("") +
            optionalWebpPreviewSha256().map(f -> "webpPreviewSha256=" + f + ", ").orElse("") +
            optionalUploadDate().map(f -> "uploadDate=" + f + ", ").orElse("") +
            optionalIsPublic().map(f -> "isPublic=" + f + ", ").orElse("") +
            optionalDownloadCount().map(f -> "downloadCount=" + f + ", ").orElse("") +
            optionalViewCount().map(f -> "viewCount=" + f + ", ").orElse("") +
            optionalDetectedLanguage().map(f -> "detectedLanguage=" + f + ", ").orElse("") +
            optionalManualLanguage().map(f -> "manualLanguage=" + f + ", ").orElse("") +
            optionalLanguageConfidence().map(f -> "languageConfidence=" + f + ", ").orElse("") +
            optionalPageCount().map(f -> "pageCount=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalFolderId().map(f -> "folderId=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
