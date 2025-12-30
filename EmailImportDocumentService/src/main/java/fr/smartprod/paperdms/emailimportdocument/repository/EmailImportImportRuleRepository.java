package fr.smartprod.paperdms.emailimportdocument.repository;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailImportImportRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailImportImportRuleRepository
    extends JpaRepository<EmailImportImportRule, Long>, JpaSpecificationExecutor<EmailImportImportRule> {}
