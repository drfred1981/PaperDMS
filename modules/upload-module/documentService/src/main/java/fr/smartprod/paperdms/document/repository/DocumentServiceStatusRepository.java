package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for DocumentServiceStatus entity.
 * Provides database-level locking to prevent concurrent processing of documents.
 */
@Repository
public interface DocumentServiceStatusRepository extends JpaRepository<DocumentServiceStatus, Long> {

    /**
     * Find service status with pessimistic write lock.
     * This ensures that only one transaction can update the status at a time.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @return Optional DocumentServiceStatus with lock acquired
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT dss FROM DocumentServiceStatus dss WHERE dss.documentId = :documentId AND dss.serviceType = :serviceType")
    Optional<DocumentServiceStatus> findByDocumentIdAndServiceTypeWithLock(
        @Param("documentId") Long documentId,
        @Param("serviceType") ServiceType serviceType
    );

    /**
     * Find service status without locking.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @return Optional DocumentServiceStatus
     */
    Optional<DocumentServiceStatus> findByDocumentIdAndServiceType(Long documentId, ServiceType serviceType);
}
