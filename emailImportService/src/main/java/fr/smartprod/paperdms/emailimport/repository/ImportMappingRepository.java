package fr.smartprod.paperdms.emailimport.repository;

import fr.smartprod.paperdms.emailimport.domain.ImportMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImportMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImportMappingRepository extends JpaRepository<ImportMapping, Long> {}
