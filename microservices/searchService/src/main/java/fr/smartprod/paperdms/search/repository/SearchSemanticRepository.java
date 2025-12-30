package fr.smartprod.paperdms.search.repository;

import fr.smartprod.paperdms.search.domain.SearchSemantic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchSemantic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchSemanticRepository extends JpaRepository<SearchSemantic, Long>, JpaSpecificationExecutor<SearchSemantic> {}
