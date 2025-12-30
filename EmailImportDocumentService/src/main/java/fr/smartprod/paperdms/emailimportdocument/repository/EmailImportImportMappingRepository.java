package fr.smartprod.paperdms.emailimportdocument.repository;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailImportImportMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailImportImportMappingRepository
    extends JpaRepository<EmailImportImportMapping, Long>, JpaSpecificationExecutor<EmailImportImportMapping> {}
