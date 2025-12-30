package fr.smartprod.paperdms.pdftoimage.repository;

import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImagePdfConversionRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImagePdfConversionRequestRepository
    extends JpaRepository<ImagePdfConversionRequest, Long>, JpaSpecificationExecutor<ImagePdfConversionRequest> {}
