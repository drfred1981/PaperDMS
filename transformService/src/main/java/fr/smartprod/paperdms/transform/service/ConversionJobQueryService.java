package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.ConversionJob;
import fr.smartprod.paperdms.transform.repository.ConversionJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.ConversionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.ConversionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.ConversionJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ConversionJob} entities in the database.
 * The main input is a {@link ConversionJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConversionJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConversionJobQueryService extends QueryService<ConversionJob> {

    private static final Logger LOG = LoggerFactory.getLogger(ConversionJobQueryService.class);

    private final ConversionJobRepository conversionJobRepository;

    private final ConversionJobMapper conversionJobMapper;

    public ConversionJobQueryService(ConversionJobRepository conversionJobRepository, ConversionJobMapper conversionJobMapper) {
        this.conversionJobRepository = conversionJobRepository;
        this.conversionJobMapper = conversionJobMapper;
    }

    /**
     * Return a {@link Page} of {@link ConversionJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversionJobDTO> findByCriteria(ConversionJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConversionJob> specification = createSpecification(criteria);
        return conversionJobRepository.findAll(specification, page).map(conversionJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConversionJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ConversionJob> specification = createSpecification(criteria);
        return conversionJobRepository.count(specification);
    }

    /**
     * Function to convert {@link ConversionJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConversionJob> createSpecification(ConversionJobCriteria criteria) {
        Specification<ConversionJob> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ConversionJob_.id),
                buildRangeSpecification(criteria.getDocumentId(), ConversionJob_.documentId),
                buildStringSpecification(criteria.getDocumentSha256(), ConversionJob_.documentSha256),
                buildStringSpecification(criteria.getSourceFormat(), ConversionJob_.sourceFormat),
                buildStringSpecification(criteria.getTargetFormat(), ConversionJob_.targetFormat),
                buildStringSpecification(criteria.getConversionEngine(), ConversionJob_.conversionEngine),
                buildStringSpecification(criteria.getOutputS3Key(), ConversionJob_.outputS3Key),
                buildRangeSpecification(criteria.getOutputDocumentId(), ConversionJob_.outputDocumentId),
                buildSpecification(criteria.getStatus(), ConversionJob_.status),
                buildRangeSpecification(criteria.getStartDate(), ConversionJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), ConversionJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), ConversionJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ConversionJob_.createdDate)
            );
        }
        return specification;
    }
}
