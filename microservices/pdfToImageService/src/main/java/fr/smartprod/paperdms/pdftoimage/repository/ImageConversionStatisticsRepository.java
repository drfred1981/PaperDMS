package fr.smartprod.paperdms.pdftoimage.repository;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImageConversionStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageConversionStatisticsRepository
    extends JpaRepository<ImageConversionStatistics, Long>, JpaSpecificationExecutor<ImageConversionStatistics> {}
