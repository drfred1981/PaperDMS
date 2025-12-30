package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.*; // for static metamodels
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionHistoryRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionHistorySearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionHistoryCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionHistoryDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ImageConversionHistory} entities in the database.
 * The main input is a {@link ImageConversionHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImageConversionHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageConversionHistoryQueryService extends QueryService<ImageConversionHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionHistoryQueryService.class);

    private final ImageConversionHistoryRepository imageConversionHistoryRepository;

    private final ImageConversionHistoryMapper imageConversionHistoryMapper;

    private final ImageConversionHistorySearchRepository imageConversionHistorySearchRepository;

    public ImageConversionHistoryQueryService(
        ImageConversionHistoryRepository imageConversionHistoryRepository,
        ImageConversionHistoryMapper imageConversionHistoryMapper,
        ImageConversionHistorySearchRepository imageConversionHistorySearchRepository
    ) {
        this.imageConversionHistoryRepository = imageConversionHistoryRepository;
        this.imageConversionHistoryMapper = imageConversionHistoryMapper;
        this.imageConversionHistorySearchRepository = imageConversionHistorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ImageConversionHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionHistoryDTO> findByCriteria(ImageConversionHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageConversionHistory> specification = createSpecification(criteria);
        return imageConversionHistoryRepository.findAll(specification, page).map(imageConversionHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageConversionHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImageConversionHistory> specification = createSpecification(criteria);
        return imageConversionHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageConversionHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageConversionHistory> createSpecification(ImageConversionHistoryCriteria criteria) {
        Specification<ImageConversionHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImageConversionHistory_.id),
                buildRangeSpecification(criteria.getOriginalRequestId(), ImageConversionHistory_.originalRequestId),
                buildRangeSpecification(criteria.getArchivedAt(), ImageConversionHistory_.archivedAt),
                buildRangeSpecification(criteria.getImagesCount(), ImageConversionHistory_.imagesCount),
                buildRangeSpecification(criteria.getTotalSize(), ImageConversionHistory_.totalSize),
                buildSpecification(criteria.getFinalStatus(), ImageConversionHistory_.finalStatus),
                buildRangeSpecification(criteria.getProcessingDuration(), ImageConversionHistory_.processingDuration)
            );
        }
        return specification;
    }
}
