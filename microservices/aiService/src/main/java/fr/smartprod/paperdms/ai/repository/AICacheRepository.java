package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AICache;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AICache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AICacheRepository extends JpaRepository<AICache, Long>, JpaSpecificationExecutor<AICache> {}
