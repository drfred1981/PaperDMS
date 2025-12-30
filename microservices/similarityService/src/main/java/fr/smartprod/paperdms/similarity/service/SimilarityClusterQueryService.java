package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.*; // for static metamodels
import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import fr.smartprod.paperdms.similarity.repository.SimilarityClusterRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityClusterSearchRepository;
import fr.smartprod.paperdms.similarity.service.criteria.SimilarityClusterCriteria;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityClusterDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityClusterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SimilarityCluster} entities in the database.
 * The main input is a {@link SimilarityClusterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SimilarityClusterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SimilarityClusterQueryService extends QueryService<SimilarityCluster> {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityClusterQueryService.class);

    private final SimilarityClusterRepository similarityClusterRepository;

    private final SimilarityClusterMapper similarityClusterMapper;

    private final SimilarityClusterSearchRepository similarityClusterSearchRepository;

    public SimilarityClusterQueryService(
        SimilarityClusterRepository similarityClusterRepository,
        SimilarityClusterMapper similarityClusterMapper,
        SimilarityClusterSearchRepository similarityClusterSearchRepository
    ) {
        this.similarityClusterRepository = similarityClusterRepository;
        this.similarityClusterMapper = similarityClusterMapper;
        this.similarityClusterSearchRepository = similarityClusterSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SimilarityClusterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityClusterDTO> findByCriteria(SimilarityClusterCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SimilarityCluster> specification = createSpecification(criteria);
        return similarityClusterRepository.findAll(specification, page).map(similarityClusterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SimilarityClusterCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SimilarityCluster> specification = createSpecification(criteria);
        return similarityClusterRepository.count(specification);
    }

    /**
     * Function to convert {@link SimilarityClusterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SimilarityCluster> createSpecification(SimilarityClusterCriteria criteria) {
        Specification<SimilarityCluster> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SimilarityCluster_.id),
                buildStringSpecification(criteria.getName(), SimilarityCluster_.name),
                buildSpecification(criteria.getAlgorithm(), SimilarityCluster_.algorithm),
                buildRangeSpecification(criteria.getDocumentCount(), SimilarityCluster_.documentCount),
                buildRangeSpecification(criteria.getAvgSimilarity(), SimilarityCluster_.avgSimilarity),
                buildRangeSpecification(criteria.getCreatedDate(), SimilarityCluster_.createdDate),
                buildRangeSpecification(criteria.getLastUpdated(), SimilarityCluster_.lastUpdated)
            );
        }
        return specification;
    }
}
