package fr.smartprod.paperdms.monitoring.domain.enumeration;

/**
 * The MaintenanceType enumeration.
 */
public enum MaintenanceType {
    CLEANUP_DELETED,
    REBUILD_INDEX,
    OPTIMIZE_STORAGE,
    VERIFY_CHECKSUMS,
    EXPIRE_TEMP_FILES,
    PRUNE_AUDIT_LOGS,
    UPDATE_STATISTICS,
    VACUUM_DATABASE,
    ROTATE_LOGS,
    BACKUP_DATABASE,
}
