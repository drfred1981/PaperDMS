package fr.smartprod.paperdms.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Event published when a document upload operation occurs.
 * This event contains all relevant information about the uploaded document.
 */
public class DocumentUploadEvent extends DocumentEvent {
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("fileSize")
    private Long fileSize;
    
    @JsonProperty("mimeType")
    private String mimeType;
    
    @JsonProperty("s3Key")
    private String s3Key;
    
    @JsonProperty("s3Bucket")
    private String s3Bucket;
    
    @JsonProperty("s3Region")
    private String s3Region;
    
    @JsonProperty("s3Etag")
    private String s3Etag;
    
    @JsonProperty("folderId")
    private Long folderId;
    
    @JsonProperty("documentTypeId")
    private Long documentTypeId;
    
    @JsonProperty("pageCount")
    private Integer pageCount;

    /**
     * Default constructor.
     */
    public DocumentUploadEvent() {
        super();
    }

    /**
     * Constructor with required fields.
     *
     * @param eventType The type of upload event
     * @param documentId The ID of the uploaded document
     * @param sourceService The service that uploaded the document
     */
    public DocumentUploadEvent(DocumentEventType eventType, Long documentId, String sourceService) {
        super(eventType, documentId, sourceService);
    }

    // Getters and Setters

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

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getS3Region() {
        return s3Region;
    }

    public void setS3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public String getS3Etag() {
        return s3Etag;
    }

    public void setS3Etag(String s3Etag) {
        this.s3Etag = s3Etag;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "DocumentUploadEvent{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", mimeType='" + mimeType + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", s3Bucket='" + s3Bucket + '\'' +
                ", s3Region='" + s3Region + '\'' +
                ", folderId=" + folderId +
                ", documentTypeId=" + documentTypeId +
                ", pageCount=" + pageCount +
                "} " + super.toString();
    }
}
