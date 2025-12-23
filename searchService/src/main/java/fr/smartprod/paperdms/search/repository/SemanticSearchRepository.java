package fr.smartprod.paperdms.search.repository;

import fr.smartprod.paperdms.search.domain.SemanticSearch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SemanticSearch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemanticSearchRepository extends JpaRepository<SemanticSearch, Long>, JpaSpecificationExecutor<SemanticSearch> {}
