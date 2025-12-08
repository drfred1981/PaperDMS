package com.ged.ai.service;

import com.ged.ai.domain.*; // for static metamodels
import com.ged.ai.domain.Correspondent;
import com.ged.ai.repository.CorrespondentRepository;
import com.ged.ai.repository.search.CorrespondentSearchRepository;
import com.ged.ai.service.criteria.CorrespondentCriteria;
import com.ged.ai.service.dto.CorrespondentDTO;
import com.ged.ai.service.mapper.CorrespondentMapper;
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
 * Service for executing complex queries for {@link Correspondent} entities in the database.
 * The main input is a {@link CorrespondentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CorrespondentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorrespondentQueryService extends QueryService<Correspondent> {

    private static final Logger LOG = LoggerFactory.getLogger(CorrespondentQueryService.class);

    private final CorrespondentRepository correspondentRepository;

    private final CorrespondentMapper correspondentMapper;

    private final CorrespondentSearchRepository correspondentSearchRepository;

    public CorrespondentQueryService(
        CorrespondentRepository correspondentRepository,
        CorrespondentMapper correspondentMapper,
        CorrespondentSearchRepository correspondentSearchRepository
    ) {
        this.correspondentRepository = correspondentRepository;
        this.correspondentMapper = correspondentMapper;
        this.correspondentSearchRepository = correspondentSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link CorrespondentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorrespondentDTO> findByCriteria(CorrespondentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Correspondent> specification = createSpecification(criteria);
        return correspondentRepository.findAll(specification, page).map(correspondentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorrespondentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Correspondent> specification = createSpecification(criteria);
        return correspondentRepository.count(specification);
    }

    /**
     * Function to convert {@link CorrespondentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Correspondent> createSpecification(CorrespondentCriteria criteria) {
        Specification<Correspondent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Correspondent_.id),
                buildStringSpecification(criteria.getName(), Correspondent_.name),
                buildStringSpecification(criteria.getEmail(), Correspondent_.email),
                buildStringSpecification(criteria.getPhone(), Correspondent_.phone),
                buildStringSpecification(criteria.getCompany(), Correspondent_.company),
                buildSpecification(criteria.getType(), Correspondent_.type),
                buildSpecification(criteria.getRole(), Correspondent_.role),
                buildRangeSpecification(criteria.getConfidence(), Correspondent_.confidence),
                buildSpecification(criteria.getIsVerified(), Correspondent_.isVerified),
                buildStringSpecification(criteria.getVerifiedBy(), Correspondent_.verifiedBy),
                buildRangeSpecification(criteria.getVerifiedDate(), Correspondent_.verifiedDate),
                buildRangeSpecification(criteria.getExtractedDate(), Correspondent_.extractedDate),
                buildSpecification(criteria.getExtractionId(), root ->
                    root.join(Correspondent_.extraction, JoinType.LEFT).get(CorrespondentExtraction_.id)
                )
            );
        }
        return specification;
    }
}
