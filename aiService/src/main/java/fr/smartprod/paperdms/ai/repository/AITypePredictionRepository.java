package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AITypePrediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AITypePredictionRepository extends JpaRepository<AITypePrediction, Long>, JpaSpecificationExecutor<AITypePrediction> {}
