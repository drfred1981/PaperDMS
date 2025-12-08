package com.ged.ocr.repository;

import com.ged.ocr.domain.OcrJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OcrJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OcrJobRepository extends JpaRepository<OcrJob, Long>, JpaSpecificationExecutor<OcrJob> {}
