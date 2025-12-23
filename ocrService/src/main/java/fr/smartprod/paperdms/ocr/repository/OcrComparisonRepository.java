package fr.smartprod.paperdms.ocr.repository;

import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OcrComparison entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcrComparisonRepository extends JpaRepository<OcrComparison, Long> {}
