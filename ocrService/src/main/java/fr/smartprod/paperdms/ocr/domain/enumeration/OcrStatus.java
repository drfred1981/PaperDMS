package fr.smartprod.paperdms.ocr.domain.enumeration;

/**
 * The OcrStatus enumeration.
 */
public enum OcrStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED,
    RETRYING,
    COMPARING,
}
