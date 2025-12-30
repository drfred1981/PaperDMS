package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.*; // for static metamodels
import fr.smartprod.paperdms.transform.domain.TransformRedactionJob;
import fr.smartprod.paperdms.transform.repository.TransformRedactionJobRepository;
import fr.smartprod.paperdms.transform.service.criteria.TransformRedactionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformRedactionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformRedactionJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransformRedactionJob} entities in the database.
 * The main input is a {@link TransformRedactionJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransformRedactionJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransformRedactionJobQueryService extends QueryService<TransformRedactionJob> {

    private static final Logger LOG = LoggerFactory.getLogger(TransformRedactionJobQueryService.class);

    private final TransformRedactionJobRepository transformRedactionJobRepository;

    private final TransformRedactionJobMapper transformRedactionJobMapper;

    public TransformRedactionJobQueryService(
        TransformRedactionJobRepository transformRedactionJobRepository,
        TransformRedactionJobMapper transformRedactionJobMapper
    ) {
        this.transformRedactionJobRepository = transformRedactionJobRepository;
        this.transformRedactionJobMapper = transformRedactionJobMapper;
    }

    /**
     * Return a {@link Page} of {@link TransformRedactionJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransformRedactionJobDTO> findByCriteria(TransformRedactionJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransformRedactionJob> specification = createSpecification(criteria);
        return transformRedactionJobRepository.findAll(specification, page).map(transformRedactionJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransformRedactionJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransformRedactionJob> specification = createSpecification(criteria);
        return transformRedactionJobRepository.count(specification);
    }

    /**
     * Function to convert {@link TransformRedactionJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransformRedactionJob> createSpecification(TransformRedactionJobCriteria criteria) {
        Specification<TransformRedactionJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TransformRedactionJob_.id),
                buildStringSpecification(criteria.getDocumentSha256(), TransformRedactionJob_.documentSha256),
                buildSpecification(criteria.getRedactionType(), TransformRedactionJob_.redactionType),
                buildStringSpecification(criteria.getRedactionColor(), TransformRedactionJob_.redactionColor),
                buildStringSpecification(criteria.getReplaceWith(), TransformRedactionJob_.replaceWith),
                buildStringSpecification(criteria.getOutputS3Key(), TransformRedactionJob_.outputS3Key),
                buildStringSpecification(criteria.getOutputDocumentSha256(), TransformRedactionJob_.outputDocumentSha256),
                buildSpecification(criteria.getStatus(), TransformRedactionJob_.status),
                buildRangeSpecification(criteria.getStartDate(), TransformRedactionJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), TransformRedactionJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), TransformRedactionJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), TransformRedactionJob_.createdDate)
            );
        }
        return specification;
    }
}
