package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentExtractedField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentExtractedField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentExtractedFieldRepository
    extends JpaRepository<DocumentExtractedField, Long>, JpaSpecificationExecutor<DocumentExtractedField> {}
