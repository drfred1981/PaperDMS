package fr.smartprod.paperdms.search.repository;

import fr.smartprod.paperdms.search.domain.SearchFacet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchFacet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchFacetRepository extends JpaRepository<SearchFacet, Long> {}
