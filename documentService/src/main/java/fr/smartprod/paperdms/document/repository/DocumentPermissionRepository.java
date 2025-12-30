package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentPermission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentPermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentPermissionRepository
    extends JpaRepository<DocumentPermission, Long>, JpaSpecificationExecutor<DocumentPermission> {}
