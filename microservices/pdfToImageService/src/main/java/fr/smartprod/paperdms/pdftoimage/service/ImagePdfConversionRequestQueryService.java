package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.*; // for static metamodels
import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import fr.smartprod.paperdms.pdftoimage.repository.ImagePdfConversionRequestRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImagePdfConversionRequestSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImagePdfConversionRequestCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImagePdfConversionRequestDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImagePdfConversionRequestMapper;
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
 * Service for executing complex queries for {@link ImagePdfConversionRequest} entities in the database.
 * The main input is a {@link ImagePdfConversionRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImagePdfConversionRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImagePdfConversionRequestQueryService extends QueryService<ImagePdfConversionRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(ImagePdfConversionRequestQueryService.class);

    private final ImagePdfConversionRequestRepository imagePdfConversionRequestRepository;

    private final ImagePdfConversionRequestMapper imagePdfConversionRequestMapper;

    private final ImagePdfConversionRequestSearchRepository imagePdfConversionRequestSearchRepository;

    public ImagePdfConversionRequestQueryService(
        ImagePdfConversionRequestRepository imagePdfConversionRequestRepository,
        ImagePdfConversionRequestMapper imagePdfConversionRequestMapper,
        ImagePdfConversionRequestSearchRepository imagePdfConversionRequestSearchRepository
    ) {
        this.imagePdfConversionRequestRepository = imagePdfConversionRequestRepository;
        this.imagePdfConversionRequestMapper = imagePdfConversionRequestMapper;
        this.imagePdfConversionRequestSearchRepository = imagePdfConversionRequestSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ImagePdfConversionRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagePdfConversionRequestDTO> findByCriteria(ImagePdfConversionRequestCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImagePdfConversionRequest> specification = createSpecification(criteria);
        return imagePdfConversionRequestRepository.findAll(specification, page).map(imagePdfConversionRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImagePdfConversionRequestCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImagePdfConversionRequest> specification = createSpecification(criteria);
        return imagePdfConversionRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link ImagePdfConversionRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImagePdfConversionRequest> createSpecification(ImagePdfConversionRequestCriteria criteria) {
        Specification<ImagePdfConversionRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImagePdfConversionRequest_.id),
                buildRangeSpecification(criteria.getSourceDocumentId(), ImagePdfConversionRequest_.sourceDocumentId),
                buildStringSpecification(criteria.getSourceFileName(), ImagePdfConversionRequest_.sourceFileName),
                buildStringSpecification(criteria.getSourcePdfS3Key(), ImagePdfConversionRequest_.sourcePdfS3Key),
                buildSpecification(criteria.getImageQuality(), ImagePdfConversionRequest_.imageQuality),
                buildSpecification(criteria.getImageFormat(), ImagePdfConversionRequest_.imageFormat),
                buildSpecification(criteria.getConversionType(), ImagePdfConversionRequest_.conversionType),
                buildRangeSpecification(criteria.getStartPage(), ImagePdfConversionRequest_.startPage),
                buildRangeSpecification(criteria.getEndPage(), ImagePdfConversionRequest_.endPage),
                buildRangeSpecification(criteria.getTotalPages(), ImagePdfConversionRequest_.totalPages),
                buildSpecification(criteria.getStatus(), ImagePdfConversionRequest_.status),
                buildStringSpecification(criteria.getErrorMessage(), ImagePdfConversionRequest_.errorMessage),
                buildRangeSpecification(criteria.getRequestedAt(), ImagePdfConversionRequest_.requestedAt),
                buildRangeSpecification(criteria.getStartedAt(), ImagePdfConversionRequest_.startedAt),
                buildRangeSpecification(criteria.getCompletedAt(), ImagePdfConversionRequest_.completedAt),
                buildRangeSpecification(criteria.getProcessingDuration(), ImagePdfConversionRequest_.processingDuration),
                buildRangeSpecification(criteria.getTotalImagesSize(), ImagePdfConversionRequest_.totalImagesSize),
                buildRangeSpecification(criteria.getImagesGenerated(), ImagePdfConversionRequest_.imagesGenerated),
                buildRangeSpecification(criteria.getDpi(), ImagePdfConversionRequest_.dpi),
                buildRangeSpecification(criteria.getRequestedByUserId(), ImagePdfConversionRequest_.requestedByUserId),
                buildRangeSpecification(criteria.getPriority(), ImagePdfConversionRequest_.priority),
                buildSpecification(criteria.getGeneratedImagesId(), root ->
                    root.join(ImagePdfConversionRequest_.generatedImages, JoinType.LEFT).get(ImageGeneratedImage_.id)
                ),
                buildSpecification(criteria.getBatchId(), root ->
                    root.join(ImagePdfConversionRequest_.batch, JoinType.LEFT).get(ImageConversionBatch_.id)
                )
            );
        }
        return specification;
    }
}
