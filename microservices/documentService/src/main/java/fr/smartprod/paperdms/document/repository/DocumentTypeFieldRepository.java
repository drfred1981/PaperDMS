package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentTypeField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTypeFieldRepository extends JpaRepository<DocumentTypeField, Long>, JpaSpecificationExecutor<DocumentTypeField> {}
