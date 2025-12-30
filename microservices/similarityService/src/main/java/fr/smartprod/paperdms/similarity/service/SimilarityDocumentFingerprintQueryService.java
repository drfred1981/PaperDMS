package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.*; // for static metamodels
import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentFingerprintRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityDocumentFingerprintSearchRepository;
import fr.smartprod.paperdms.similarity.service.criteria.SimilarityDocumentFingerprintCriteria;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentFingerprintDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityDocumentFingerprintMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SimilarityDocumentFingerprint} entities in the database.
 * The main input is a {@link SimilarityDocumentFingerprintCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SimilarityDocumentFingerprintDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimilarityDocumentFingerprintQueryService extends QueryService<SimilarityDocumentFingerprint> {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDocumentFingerprintQueryService.class);

    private final SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository;

    private final SimilarityDocumentFingerprintMapper similarityDocumentFingerprintMapper;

    private final SimilarityDocumentFingerprintSearchRepository similarityDocumentFingerprintSearchRepository;

    public SimilarityDocumentFingerprintQueryService(
        SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository,
        SimilarityDocumentFingerprintMapper similarityDocumentFingerprintMapper,
        SimilarityDocumentFingerprintSearchRepository similarityDocumentFingerprintSearchRepository
    ) {
        this.similarityDocumentFingerprintRepository = similarityDocumentFingerprintRepository;
        this.similarityDocumentFingerprintMapper = similarityDocumentFingerprintMapper;
        this.similarityDocumentFingerprintSearchRepository = similarityDocumentFingerprintSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SimilarityDocumentFingerprintDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityDocumentFingerprintDTO> findByCriteria(SimilarityDocumentFingerprintCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SimilarityDocumentFingerprint> specification = createSpecification(criteria);
        return similarityDocumentFingerprintRepository.findAll(specification, page).map(similarityDocumentFingerprintMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SimilarityDocumentFingerprintCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SimilarityDocumentFingerprint> specification = createSpecification(criteria);
        return similarityDocumentFingerprintRepository.count(specification);
    }

    /**
     * Function to convert {@link SimilarityDocumentFingerprintCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SimilarityDocumentFingerprint> createSpecification(SimilarityDocumentFingerprintCriteria criteria) {
        Specification<SimilarityDocumentFingerprint> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SimilarityDocumentFingerprint_.id),
                buildSpecification(criteria.getFingerprintType(), SimilarityDocumentFingerprint_.fingerprintType),
                buildRangeSpecification(criteria.getComputedDate(), SimilarityDocumentFingerprint_.computedDate),
                buildRangeSpecification(criteria.getLastUpdated(), SimilarityDocumentFingerprint_.lastUpdated)
            );
        }
        return specification;
    }
}
