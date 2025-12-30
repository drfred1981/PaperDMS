package fr.smartprod.paperdms.pdftoimage.repository;

import fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImageGeneratedImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageGeneratedImageRepository
    extends JpaRepository<ImageGeneratedImage, Long>, JpaSpecificationExecutor<ImageGeneratedImage> {}
