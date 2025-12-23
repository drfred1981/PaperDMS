package fr.smartprod.paperdms.common.event;

/**
 * Service processing status for document workflow.
 * Corresponds to the JDL ServiceStatus enum.
 */
public enum ServiceStatus {
    NOT_APPLICABLE,
    PENDING,
    QUEUED,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED,
    SKIPPED,
    RETRYING,
    PARTIALLY_COMPLETED
}
