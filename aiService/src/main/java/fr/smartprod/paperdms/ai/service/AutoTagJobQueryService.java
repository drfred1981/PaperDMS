package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.AutoTagJob;
import fr.smartprod.paperdms.ai.repository.AutoTagJobRepository;
import fr.smartprod.paperdms.ai.service.criteria.AutoTagJobCriteria;
import fr.smartprod.paperdms.ai.service.dto.AutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.mapper.AutoTagJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AutoTagJob} entities in the database.
 * The main input is a {@link AutoTagJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AutoTagJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutoTagJobQueryService extends QueryService<AutoTagJob> {

    private static final Logger LOG = LoggerFactory.getLogger(AutoTagJobQueryService.class);

    private final AutoTagJobRepository autoTagJobRepository;

    private final AutoTagJobMapper autoTagJobMapper;

    public AutoTagJobQueryService(AutoTagJobRepository autoTagJobRepository, AutoTagJobMapper autoTagJobMapper) {
        this.autoTagJobRepository = autoTagJobRepository;
        this.autoTagJobMapper = autoTagJobMapper;
    }

    /**
     * Return a {@link Page} of {@link AutoTagJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutoTagJobDTO> findByCriteria(AutoTagJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutoTagJob> specification = createSpecification(criteria);
        return autoTagJobRepository.findAll(specification, page).map(autoTagJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutoTagJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AutoTagJob> specification = createSpecification(criteria);
        return autoTagJobRepository.count(specification);
    }

    /**
     * Function to convert {@link AutoTagJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutoTagJob> createSpecification(AutoTagJobCriteria criteria) {
        Specification<AutoTagJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AutoTagJob_.id),
                buildRangeSpecification(criteria.getDocumentId(), AutoTagJob_.documentId),
                buildStringSpecification(criteria.getDocumentSha256(), AutoTagJob_.documentSha256),
                buildStringSpecification(criteria.gets3Key(), AutoTagJob_.s3Key),
                buildStringSpecification(criteria.getExtractedTextSha256(), AutoTagJob_.extractedTextSha256),
                buildStringSpecification(criteria.getDetectedLanguage(), AutoTagJob_.detectedLanguage),
                buildRangeSpecification(criteria.getLanguageConfidence(), AutoTagJob_.languageConfidence),
                buildSpecification(criteria.getStatus(), AutoTagJob_.status),
                buildStringSpecification(criteria.getModelVersion(), AutoTagJob_.modelVersion),
                buildStringSpecification(criteria.getResultCacheKey(), AutoTagJob_.resultCacheKey),
                buildSpecification(criteria.getIsCached(), AutoTagJob_.isCached),
                buildRangeSpecification(criteria.getStartDate(), AutoTagJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), AutoTagJob_.endDate),
                buildRangeSpecification(criteria.getConfidence(), AutoTagJob_.confidence),
                buildRangeSpecification(criteria.getCreatedDate(), AutoTagJob_.createdDate)
            );
        }
        return specification;
    }
}
