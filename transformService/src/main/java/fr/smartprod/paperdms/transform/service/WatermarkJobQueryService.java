package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.WatermarkJob;
import fr.smartprod.paperdms.transform.repository.WatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.WatermarkJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.WatermarkJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.WatermarkJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WatermarkJob} entities in the database.
 * The main input is a {@link WatermarkJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WatermarkJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WatermarkJobQueryService extends QueryService<WatermarkJob> {

    private static final Logger LOG = LoggerFactory.getLogger(WatermarkJobQueryService.class);

    private final WatermarkJobRepository watermarkJobRepository;

    private final WatermarkJobMapper watermarkJobMapper;

    public WatermarkJobQueryService(WatermarkJobRepository watermarkJobRepository, WatermarkJobMapper watermarkJobMapper) {
        this.watermarkJobRepository = watermarkJobRepository;
        this.watermarkJobMapper = watermarkJobMapper;
    }

    /**
     * Return a {@link Page} of {@link WatermarkJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WatermarkJobDTO> findByCriteria(WatermarkJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WatermarkJob> specification = createSpecification(criteria);
        return watermarkJobRepository.findAll(specification, page).map(watermarkJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WatermarkJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WatermarkJob> specification = createSpecification(criteria);
        return watermarkJobRepository.count(specification);
    }

    /**
     * Function to convert {@link WatermarkJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WatermarkJob> createSpecification(WatermarkJobCriteria criteria) {
        Specification<WatermarkJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), WatermarkJob_.id),
                buildRangeSpecification(criteria.getDocumentId(), WatermarkJob_.documentId),
                buildSpecification(criteria.getWatermarkType(), WatermarkJob_.watermarkType),
                buildStringSpecification(criteria.getWatermarkText(), WatermarkJob_.watermarkText),
                buildStringSpecification(criteria.getWatermarkImageS3Key(), WatermarkJob_.watermarkImageS3Key),
                buildSpecification(criteria.getPosition(), WatermarkJob_.position),
                buildRangeSpecification(criteria.getOpacity(), WatermarkJob_.opacity),
                buildRangeSpecification(criteria.getFontSize(), WatermarkJob_.fontSize),
                buildStringSpecification(criteria.getColor(), WatermarkJob_.color),
                buildRangeSpecification(criteria.getRotation(), WatermarkJob_.rotation),
                buildSpecification(criteria.getTiled(), WatermarkJob_.tiled),
                buildStringSpecification(criteria.getOutputS3Key(), WatermarkJob_.outputS3Key),
                buildRangeSpecification(criteria.getOutputDocumentId(), WatermarkJob_.outputDocumentId),
                buildSpecification(criteria.getStatus(), WatermarkJob_.status),
                buildRangeSpecification(criteria.getStartDate(), WatermarkJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), WatermarkJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), WatermarkJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), WatermarkJob_.createdDate)
            );
        }
        return specification;
    }
}
