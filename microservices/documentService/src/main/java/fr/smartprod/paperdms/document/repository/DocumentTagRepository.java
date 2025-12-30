package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTagRepository extends JpaRepository<DocumentTag, Long>, JpaSpecificationExecutor<DocumentTag> {}
