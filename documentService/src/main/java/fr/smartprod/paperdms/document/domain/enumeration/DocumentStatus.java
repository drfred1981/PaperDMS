package fr.smartprod.paperdms.document.domain.enumeration;

/**
 * PaperDMS - Toutes les �num�rations (MODIFI�)
 * Fichier 2/20 - � inclure dans chaque fichier de service qui en a besoin
 * MODIFICATION: Ajout des statuts par service
 */
public enum DocumentStatus {
    UPLOADING,
    UPLOADED,
    ACTIVE,
    ARCHIVED,
    DELETED,
    ERROR,
}
