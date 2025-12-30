package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.*; // for static metamodels
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionStatisticsRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionStatisticsSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionStatisticsCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionStatisticsDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionStatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ImageConversionStatistics} entities in the database.
 * The main input is a {@link ImageConversionStatisticsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImageConversionStatisticsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageConversionStatisticsQueryService extends QueryService<ImageConversionStatistics> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionStatisticsQueryService.class);

    private final ImageConversionStatisticsRepository imageConversionStatisticsRepository;

    private final ImageConversionStatisticsMapper imageConversionStatisticsMapper;

    private final ImageConversionStatisticsSearchRepository imageConversionStatisticsSearchRepository;

    public ImageConversionStatisticsQueryService(
        ImageConversionStatisticsRepository imageConversionStatisticsRepository,
        ImageConversionStatisticsMapper imageConversionStatisticsMapper,
        ImageConversionStatisticsSearchRepository imageConversionStatisticsSearchRepository
    ) {
        this.imageConversionStatisticsRepository = imageConversionStatisticsRepository;
        this.imageConversionStatisticsMapper = imageConversionStatisticsMapper;
        this.imageConversionStatisticsSearchRepository = imageConversionStatisticsSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ImageConversionStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionStatisticsDTO> findByCriteria(ImageConversionStatisticsCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageConversionStatistics> specification = createSpecification(criteria);
        return imageConversionStatisticsRepository.findAll(specification, page).map(imageConversionStatisticsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageConversionStatisticsCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImageConversionStatistics> specification = createSpecification(criteria);
        return imageConversionStatisticsRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageConversionStatisticsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageConversionStatistics> createSpecification(ImageConversionStatisticsCriteria criteria) {
        Specification<ImageConversionStatistics> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImageConversionStatistics_.id),
                buildRangeSpecification(criteria.getStatisticsDate(), ImageConversionStatistics_.statisticsDate),
                buildRangeSpecification(criteria.getTotalConversions(), ImageConversionStatistics_.totalConversions),
                buildRangeSpecification(criteria.getSuccessfulConversions(), ImageConversionStatistics_.successfulConversions),
                buildRangeSpecification(criteria.getFailedConversions(), ImageConversionStatistics_.failedConversions),
                buildRangeSpecification(criteria.getTotalPagesConverted(), ImageConversionStatistics_.totalPagesConverted),
                buildRangeSpecification(criteria.getTotalImagesGenerated(), ImageConversionStatistics_.totalImagesGenerated),
                buildRangeSpecification(criteria.getTotalImagesSize(), ImageConversionStatistics_.totalImagesSize),
                buildRangeSpecification(criteria.getAverageProcessingDuration(), ImageConversionStatistics_.averageProcessingDuration),
                buildRangeSpecification(criteria.getMaxProcessingDuration(), ImageConversionStatistics_.maxProcessingDuration),
                buildRangeSpecification(criteria.getMinProcessingDuration(), ImageConversionStatistics_.minProcessingDuration),
                buildRangeSpecification(criteria.getCalculatedAt(), ImageConversionStatistics_.calculatedAt)
            );
        }
        return specification;
    }
}
