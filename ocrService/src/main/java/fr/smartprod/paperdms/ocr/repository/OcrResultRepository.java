package fr.smartprod.paperdms.ocr.repository;

import fr.smartprod.paperdms.ocr.domain.OcrResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OcrResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcrResultRepository extends JpaRepository<OcrResult, Long> {}
