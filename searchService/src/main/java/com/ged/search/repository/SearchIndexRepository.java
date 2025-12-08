package com.ged.search.repository;

import com.ged.search.domain.SearchIndex;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchIndexRepository extends JpaRepository<SearchIndex, Long> {}
