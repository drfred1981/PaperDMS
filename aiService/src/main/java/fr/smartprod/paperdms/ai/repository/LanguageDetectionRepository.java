package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.LanguageDetection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LanguageDetection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageDetectionRepository extends JpaRepository<LanguageDetection, Long> {}
