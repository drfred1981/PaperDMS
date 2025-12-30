package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AITagPrediction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AITagPrediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AITagPredictionRepository extends JpaRepository<AITagPrediction, Long>, JpaSpecificationExecutor<AITagPrediction> {}
