package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AIAutoTagJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AIAutoTagJobRepository extends JpaRepository<AIAutoTagJob, Long>, JpaSpecificationExecutor<AIAutoTagJob> {}
