package fr.smartprod.paperdms.monitoring.domain.enumeration;

/**
 * The AlertType enumeration.
 */
public enum AlertType {
    DOCUMENT_EXPIRATION,
    STORAGE_QUOTA,
    UNUSUAL_ACTIVITY,
    COMPLIANCE_VIOLATION,
    WORKFLOW_DELAYED,
    FAILED_OCR,
    MISSING_METADATA,
    DUPLICATE_DETECTED,
    PERMISSION_CHANGED,
    SERVICE_DOWN,
    HIGH_ERROR_RATE,
    CONTRACT_EXPIRATION,
    INVOICE_OVERDUE,
    SYSTEM_OVERLOAD,
}
