package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AiCache;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AiCache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AiCacheRepository extends JpaRepository<AiCache, Long> {}
