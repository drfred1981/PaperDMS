package fr.smartprod.paperdms.ai.repository;

import fr.smartprod.paperdms.ai.domain.CorrespondentExtraction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CorrespondentExtraction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrespondentExtractionRepository
    extends JpaRepository<CorrespondentExtraction, Long>, JpaSpecificationExecutor<CorrespondentExtraction> {}
