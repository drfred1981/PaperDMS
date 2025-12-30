package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AICorrespondentPrediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AICorrespondentPredictionRepository
    extends JpaRepository<AICorrespondentPrediction, Long>, JpaSpecificationExecutor<AICorrespondentPrediction> {}
