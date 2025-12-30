package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long>, JpaSpecificationExecutor<DocumentTemplate> {}
