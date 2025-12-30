package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.MetaSavedSearch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetaSavedSearch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaSavedSearchRepository extends JpaRepository<MetaSavedSearch, Long>, JpaSpecificationExecutor<MetaSavedSearch> {}
