package fr.smartprod.paperdms.search.repository;

import fr.smartprod.paperdms.search.domain.SearchQuery;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchQuery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long>, JpaSpecificationExecutor<SearchQuery> {}
