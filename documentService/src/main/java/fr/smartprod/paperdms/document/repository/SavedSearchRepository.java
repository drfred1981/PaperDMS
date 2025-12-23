package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.SavedSearch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SavedSearch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SavedSearchRepository extends JpaRepository<SavedSearch, Long>, JpaSpecificationExecutor<SavedSearch> {}
