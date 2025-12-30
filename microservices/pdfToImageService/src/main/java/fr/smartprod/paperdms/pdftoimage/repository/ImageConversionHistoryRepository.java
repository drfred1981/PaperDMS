package fr.smartprod.paperdms.pdftoimage.repository;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImageConversionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageConversionHistoryRepository
    extends JpaRepository<ImageConversionHistory, Long>, JpaSpecificationExecutor<ImageConversionHistory> {}
