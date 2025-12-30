package fr.smartprod.paperdms.pdftoimage.repository;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImageConversionBatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageConversionBatchRepository
    extends JpaRepository<ImageConversionBatch, Long>, JpaSpecificationExecutor<ImageConversionBatch> {}
