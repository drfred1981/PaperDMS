package fr.smartprod.paperdms.notification.domain.enumeration;

/**
 * The NotificationType enumeration.
 */
public enum NotificationType {
    DOCUMENT_UPLOADED,
    DOCUMENT_PROCESSED,
    DOCUMENT_SHARED,
    WORKFLOW_ASSIGNED,
    WORKFLOW_APPROVED,
    WORKFLOW_REJECTED,
    WORKFLOW_COMPLETED,
    OCR_COMPLETED,
    AI_TAG_SUGGESTED,
    SIMILAR_DOCUMENT_FOUND,
    SYSTEM_ALERT,
    CUSTOM,
}
