package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentMetadata;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, Long>, JpaSpecificationExecutor<DocumentMetadata> {}
