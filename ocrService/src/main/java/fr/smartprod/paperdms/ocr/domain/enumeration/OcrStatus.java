package fr.smartprod.paperdms.ocr.domain.enumeration;

/**
 * OCR Service - 7 Entit�s
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
