package fr.smartprod.paperdms.emailimport.repository;

import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImportRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImportRuleRepository extends JpaRepository<ImportRule, Long>, JpaSpecificationExecutor<ImportRule> {}
