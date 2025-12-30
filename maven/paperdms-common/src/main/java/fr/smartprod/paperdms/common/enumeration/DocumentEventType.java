package fr.smartprod.paperdms.common.enumeration;

/**
 * Event types for document lifecycle tracking across microservices.
 * These events are published to Kafka topics and consumed by various services.
 */
public enum DocumentEventType {
    /**
     * Document upload has been initiated
     */
    DOCUMENT_UPLOAD_STARTED,
    
    /**
     * Document has been successfully uploaded to S3
     */
    DOCUMENT_UPLOADED,
    
    /**
     * Document upload has failed
     */
    DOCUMENT_UPLOAD_FAILED,
    
    /**
     * Document is ready for OCR processing
     */
    DOCUMENT_READY_FOR_OCR,
    
    /**
     * OCR processing has been completed
     */
    DOCUMENT_OCR_COMPLETED,
    
    /**
     * OCR processing has failed
     */
    DOCUMENT_OCR_FAILED,
    
    /**
     * Document is ready for AI tagging
     */
    DOCUMENT_READY_FOR_AI_TAGGING,
    
    /**
     * AI tagging has been completed
     */
    DOCUMENT_AI_TAGGING_COMPLETED,
    
    /**
     * AI tagging has failed
     */
    DOCUMENT_AI_TAGGING_FAILED,
    
    /**
     * Document has been indexed in Elasticsearch
     */
    DOCUMENT_INDEXED,
    
    /**
     * Document indexing has failed
     */
    DOCUMENT_INDEXING_FAILED,
    
    /**
     * WebP preview has been generated
     */
    DOCUMENT_PREVIEW_GENERATED,
    
    /**
     * WebP preview generation has failed
     */
    DOCUMENT_PREVIEW_FAILED,
    
    /**
     * Thumbnail has been generated
     */
    DOCUMENT_THUMBNAIL_GENERATED,
    
    /**
     * Thumbnail generation has failed
     */
    DOCUMENT_THUMBNAIL_FAILED,
    
    /**
     * Document processing is fully completed
     */
    DOCUMENT_PROCESSING_COMPLETED,
    
    /**
     * Document has been deleted
     */
    DOCUMENT_DELETED,
    
    /**
     * Service status has changed for a document
     */
    DOCUMENT_SERVICE_STATUS_CHANGED
}
