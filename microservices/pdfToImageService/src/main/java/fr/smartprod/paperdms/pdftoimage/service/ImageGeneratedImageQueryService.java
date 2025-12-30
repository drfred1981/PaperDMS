package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.*; // for static metamodels
import fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage;
import fr.smartprod.paperdms.pdftoimage.repository.ImageGeneratedImageRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageGeneratedImageSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageGeneratedImageCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageGeneratedImageDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageGeneratedImageMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ImageGeneratedImage} entities in the database.
 * The main input is a {@link ImageGeneratedImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImageGeneratedImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageGeneratedImageQueryService extends QueryService<ImageGeneratedImage> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageGeneratedImageQueryService.class);

    private final ImageGeneratedImageRepository imageGeneratedImageRepository;

    private final ImageGeneratedImageMapper imageGeneratedImageMapper;

    private final ImageGeneratedImageSearchRepository imageGeneratedImageSearchRepository;

    public ImageGeneratedImageQueryService(
        ImageGeneratedImageRepository imageGeneratedImageRepository,
        ImageGeneratedImageMapper imageGeneratedImageMapper,
        ImageGeneratedImageSearchRepository imageGeneratedImageSearchRepository
    ) {
        this.imageGeneratedImageRepository = imageGeneratedImageRepository;
        this.imageGeneratedImageMapper = imageGeneratedImageMapper;
        this.imageGeneratedImageSearchRepository = imageGeneratedImageSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ImageGeneratedImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageGeneratedImageDTO> findByCriteria(ImageGeneratedImageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageGeneratedImage> specification = createSpecification(criteria);
        return imageGeneratedImageRepository.findAll(specification, page).map(imageGeneratedImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageGeneratedImageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImageGeneratedImage> specification = createSpecification(criteria);
        return imageGeneratedImageRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageGeneratedImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageGeneratedImage> createSpecification(ImageGeneratedImageCriteria criteria) {
        Specification<ImageGeneratedImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImageGeneratedImage_.id),
                buildRangeSpecification(criteria.getPageNumber(), ImageGeneratedImage_.pageNumber),
                buildStringSpecification(criteria.getFileName(), ImageGeneratedImage_.fileName),
                buildStringSpecification(criteria.gets3Key(), ImageGeneratedImage_.s3Key),
                buildStringSpecification(criteria.getPreSignedUrl(), ImageGeneratedImage_.preSignedUrl),
                buildRangeSpecification(criteria.getUrlExpiresAt(), ImageGeneratedImage_.urlExpiresAt),
                buildSpecification(criteria.getFormat(), ImageGeneratedImage_.format),
                buildSpecification(criteria.getQuality(), ImageGeneratedImage_.quality),
                buildRangeSpecification(criteria.getWidth(), ImageGeneratedImage_.width),
                buildRangeSpecification(criteria.getHeight(), ImageGeneratedImage_.height),
                buildRangeSpecification(criteria.getFileSize(), ImageGeneratedImage_.fileSize),
                buildRangeSpecification(criteria.getDpi(), ImageGeneratedImage_.dpi),
                buildStringSpecification(criteria.getSha256Hash(), ImageGeneratedImage_.sha256Hash),
                buildRangeSpecification(criteria.getGeneratedAt(), ImageGeneratedImage_.generatedAt),
                buildSpecification(criteria.getConversionRequestId(), root ->
                    root.join(ImageGeneratedImage_.conversionRequest, JoinType.LEFT).get(ImagePdfConversionRequest_.id)
                )
            );
        }
        return specification;
    }
}
