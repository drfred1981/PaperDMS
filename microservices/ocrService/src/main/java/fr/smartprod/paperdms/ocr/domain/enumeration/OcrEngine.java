package fr.smartprod.paperdms.ocr.domain.enumeration;

/**
 * The OcrEngine enumeration.
 */
public enum OcrEngine {
    TIKA_TESSERACT,
    TIKA_NATIVE,
    OPENAI_VISION,
    GOOGLE_VISION,
    AWS_TEXTRACT,
    AZURE_VISION,
    ANTHROPIC_CLAUDE,
    CUSTOM,
    HYBRID,
}
