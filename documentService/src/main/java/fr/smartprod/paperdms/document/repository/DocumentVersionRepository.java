package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentVersion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {}
