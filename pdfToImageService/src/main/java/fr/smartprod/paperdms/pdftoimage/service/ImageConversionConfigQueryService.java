package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.*; // for static metamodels
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionConfigRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionConfigSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionConfigCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionConfigDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ImageConversionConfig} entities in the database.
 * The main input is a {@link ImageConversionConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImageConversionConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageConversionConfigQueryService extends QueryService<ImageConversionConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionConfigQueryService.class);

    private final ImageConversionConfigRepository imageConversionConfigRepository;

    private final ImageConversionConfigMapper imageConversionConfigMapper;

    private final ImageConversionConfigSearchRepository imageConversionConfigSearchRepository;

    public ImageConversionConfigQueryService(
        ImageConversionConfigRepository imageConversionConfigRepository,
        ImageConversionConfigMapper imageConversionConfigMapper,
        ImageConversionConfigSearchRepository imageConversionConfigSearchRepository
    ) {
        this.imageConversionConfigRepository = imageConversionConfigRepository;
        this.imageConversionConfigMapper = imageConversionConfigMapper;
        this.imageConversionConfigSearchRepository = imageConversionConfigSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ImageConversionConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionConfigDTO> findByCriteria(ImageConversionConfigCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImageConversionConfig> specification = createSpecification(criteria);
        return imageConversionConfigRepository.findAll(specification, page).map(imageConversionConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageConversionConfigCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImageConversionConfig> specification = createSpecification(criteria);
        return imageConversionConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link ImageConversionConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImageConversionConfig> createSpecification(ImageConversionConfigCriteria criteria) {
        Specification<ImageConversionConfig> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImageConversionConfig_.id),
                buildStringSpecification(criteria.getConfigName(), ImageConversionConfig_.configName),
                buildStringSpecification(criteria.getDescription(), ImageConversionConfig_.description),
                buildSpecification(criteria.getDefaultQuality(), ImageConversionConfig_.defaultQuality),
                buildSpecification(criteria.getDefaultFormat(), ImageConversionConfig_.defaultFormat),
                buildRangeSpecification(criteria.getDefaultDpi(), ImageConversionConfig_.defaultDpi),
                buildSpecification(criteria.getDefaultConversionType(), ImageConversionConfig_.defaultConversionType),
                buildRangeSpecification(criteria.getDefaultPriority(), ImageConversionConfig_.defaultPriority),
                buildSpecification(criteria.getIsActive(), ImageConversionConfig_.isActive),
                buildSpecification(criteria.getIsDefault(), ImageConversionConfig_.isDefault),
                buildRangeSpecification(criteria.getCreatedAt(), ImageConversionConfig_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), ImageConversionConfig_.updatedAt)
            );
        }
        return specification;
    }
}
