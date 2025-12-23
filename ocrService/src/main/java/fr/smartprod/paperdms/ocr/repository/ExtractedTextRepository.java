package fr.smartprod.paperdms.ocr.repository;

import fr.smartprod.paperdms.ocr.domain.ExtractedText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExtractedText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtractedTextRepository extends JpaRepository<ExtractedText, Long> {}
