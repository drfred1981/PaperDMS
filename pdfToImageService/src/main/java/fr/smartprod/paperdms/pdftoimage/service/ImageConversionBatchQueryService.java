package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.*; // for static metamodels
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionBatchRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionBatchSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionBatchCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionBatchDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionBatchMapper;
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
 * Service for executing complex queries for {@link ImageConversionBatch} entities in the database.
 * The main input is a {@link ImageConversionBatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImageConversionBatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageConversionBatchQueryService extends QueryService<ImageConversionBatch> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionBatchQueryService.class);

    private final ImageConversionBatchRepository imageConversionBatchRepository;

    private final ImageConversionBatchMapper imageConversionBatchMapper;

    private final ImageConversionBatchSearchRepository imageConversionBatchSearchRepository;

    public ImageConversionBatchQueryService(
        ImageConversionBatchRepository imageConversionBatchRepository,
        ImageConversionBatchMapper imageConversionBatchMapper,
        ImageConversionBatchSearchRepository imageConversionBatchSearchRepository
    ) {
        this.imageConversionBatchRepository = imageConversionBatchRepository;
        this.imageConversionBatchMapper = imageConversionBatchMapper;
        this.imageConversionBatchSearchRepository = imageConversionBatchSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ImageConversionBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionBatchDTO> findByCriteria(ImageConversionBatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageConversionBatch> specification = createSpecification(criteria);
        return imageConversionBatchRepository.findAll(specification, page).map(imageConversionBatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageConversionBatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImageConversionBatch> specification = createSpecification(criteria);
        return imageConversionBatchRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageConversionBatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageConversionBatch> createSpecification(ImageConversionBatchCriteria criteria) {
        Specification<ImageConversionBatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImageConversionBatch_.id),
                buildStringSpecification(criteria.getBatchName(), ImageConversionBatch_.batchName),
                buildStringSpecification(criteria.getDescription(), ImageConversionBatch_.description),
                buildRangeSpecification(criteria.getCreatedAt(), ImageConversionBatch_.createdAt),
                buildSpecification(criteria.getStatus(), ImageConversionBatch_.status),
                buildRangeSpecification(criteria.getTotalConversions(), ImageConversionBatch_.totalConversions),
                buildRangeSpecification(criteria.getCompletedConversions(), ImageConversionBatch_.completedConversions),
                buildRangeSpecification(criteria.getFailedConversions(), ImageConversionBatch_.failedConversions),
                buildRangeSpecification(criteria.getStartedAt(), ImageConversionBatch_.startedAt),
                buildRangeSpecification(criteria.getCompletedAt(), ImageConversionBatch_.completedAt),
                buildRangeSpecification(criteria.getTotalProcessingDuration(), ImageConversionBatch_.totalProcessingDuration),
                buildRangeSpecification(criteria.getCreatedByUserId(), ImageConversionBatch_.createdByUserId),
                buildSpecification(criteria.getConversionsId(), root ->
                    root.join(ImageConversionBatch_.conversions, JoinType.LEFT).get(ImagePdfConversionRequest_.id)
                )
            );
        }
        return specification;
    }
}
