package fr.smartprod.paperdms.emailimport.repository;

import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailImport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailImportRepository extends JpaRepository<EmailImport, Long>, JpaSpecificationExecutor<EmailImport> {}
