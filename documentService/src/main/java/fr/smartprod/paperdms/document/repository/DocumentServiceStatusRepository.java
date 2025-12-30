package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentServiceStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentServiceStatusRepository
    extends JpaRepository<DocumentServiceStatus, Long>, JpaSpecificationExecutor<DocumentServiceStatus> {}
