package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentAuditRepository extends JpaRepository<DocumentAudit, Long>, JpaSpecificationExecutor<DocumentAudit> {}
