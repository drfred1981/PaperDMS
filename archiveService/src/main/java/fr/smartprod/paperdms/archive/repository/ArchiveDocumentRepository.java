package fr.smartprod.paperdms.archive.repository;

import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArchiveDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchiveDocumentRepository extends JpaRepository<ArchiveDocument, Long>, JpaSpecificationExecutor<ArchiveDocument> {}
