package fr.smartprod.paperdms.common.event;

/**
 * Service types for document processing status tracking.
 * Corresponds to the JDL ServiceType enum.
 */
public enum ServiceType {
    OCR_SERVICE,
    AI_SERVICE,
    WORKFLOW_SERVICE,
    SEARCH_SERVICE,
    TRANSFORM_SERVICE,
    ARCHIVE_SERVICE,
    EXPORT_SERVICE,
    SIMILARITY_SERVICE,
    BUSINESS_SERVICE,
    SCAN_SERVICE,
    EMAIL_IMPORT_SERVICE,
    REPORTING_SERVICE,
    MONITORING_SERVICE
}
