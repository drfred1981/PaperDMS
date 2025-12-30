package fr.smartprod.paperdms.ocr.repository;

import fr.smartprod.paperdms.ocr.domain.OrcExtractedText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrcExtractedText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrcExtractedTextRepository extends JpaRepository<OrcExtractedText, Long>, JpaSpecificationExecutor<OrcExtractedText> {}
