package fr.smartprod.paperdms.ocr.repository;

import fr.smartprod.paperdms.ocr.domain.OcrCache;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OcrCache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcrCacheRepository extends JpaRepository<OcrCache, Long>, JpaSpecificationExecutor<OcrCache> {}
