package fr.smartprod.paperdms.business.domain.enumeration;

/**
 * The InvoiceStatus enumeration.
 */
public enum InvoiceStatus {
    DRAFT,
    PENDING_APPROVAL,
    APPROVED,
    SENT,
    PAID,
    PARTIALLY_PAID,
    OVERDUE,
    CANCELLED,
    DISPUTED,
}
