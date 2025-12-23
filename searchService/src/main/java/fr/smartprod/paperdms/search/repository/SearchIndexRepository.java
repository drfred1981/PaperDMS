package fr.smartprod.paperdms.search.repository;

import fr.smartprod.paperdms.search.domain.SearchIndex;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchIndexRepository extends JpaRepository<SearchIndex, Long> {}
