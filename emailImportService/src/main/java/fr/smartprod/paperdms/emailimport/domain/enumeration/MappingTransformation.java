package fr.smartprod.paperdms.emailimport.domain.enumeration;

/**
 * The MappingTransformation enumeration.
 */
public enum MappingTransformation {
    NONE,
    UPPERCASE,
    LOWERCASE,
    TRIM,
    EXTRACT_DATE,
    EXTRACT_NUMBER,
    EXTRACT_EMAIL,
    REGEX_EXTRACT,
    REPLACE,
    SPLIT,
    CONCAT,
    CUSTOM,
}
