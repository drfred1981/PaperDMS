package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentRelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRelationRepository extends JpaRepository<DocumentRelation, Long>, JpaSpecificationExecutor<DocumentRelation> {}
