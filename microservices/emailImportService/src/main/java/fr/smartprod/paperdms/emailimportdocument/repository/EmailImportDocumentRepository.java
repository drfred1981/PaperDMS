package fr.smartprod.paperdms.emailimportdocument.repository;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailImportDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailImportDocumentRepository
    extends JpaRepository<EmailImportDocument, Long>, JpaSpecificationExecutor<EmailImportDocument> {}
