package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AILanguageDetection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AILanguageDetectionRepository
    extends JpaRepository<AILanguageDetection, Long>, JpaSpecificationExecutor<AILanguageDetection> {}
