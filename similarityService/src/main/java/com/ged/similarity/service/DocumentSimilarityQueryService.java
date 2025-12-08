package com.ged.similarity.service;

import com.ged.similarity.domain.*; // for static metamodels
import com.ged.similarity.domain.DocumentSimilarity;
import com.ged.similarity.repository.DocumentSimilarityRepository;
import com.ged.similarity.service.criteria.DocumentSimilarityCriteria;
import com.ged.similarity.service.dto.DocumentSimilarityDTO;
import com.ged.similarity.service.mapper.DocumentSimilarityMapper;
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
 * Service for executing complex queries for {@link DocumentSimilarity} entities in the database.
 * The main input is a {@link DocumentSimilarityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentSimilarityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentSimilarityQueryService extends QueryService<DocumentSimilarity> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSimilarityQueryService.class);

    private final DocumentSimilarityRepository documentSimilarityRepository;

    private final DocumentSimilarityMapper documentSimilarityMapper;

    public DocumentSimilarityQueryService(
        DocumentSimilarityRepository documentSimilarityRepository,
        DocumentSimilarityMapper documentSimilarityMapper
    ) {
        this.documentSimilarityRepository = documentSimilarityRepository;
        this.documentSimilarityMapper = documentSimilarityMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentSimilarityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentSimilarityDTO> findByCriteria(DocumentSimilarityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentSimilarity> specification = createSpecification(criteria);
        return documentSimilarityRepository.findAll(specification, page).map(documentSimilarityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentSimilarityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DocumentSimilarity> specification = createSpecification(criteria);
        return documentSimilarityRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentSimilarityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentSimilarity> createSpecification(DocumentSimilarityCriteria criteria) {
        Specification<DocumentSimilarity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DocumentSimilarity_.id),
                buildRangeSpecification(criteria.getDocumentId1(), DocumentSimilarity_.documentId1),
                buildRangeSpecification(criteria.getDocumentId2(), DocumentSimilarity_.documentId2),
                buildRangeSpecification(criteria.getSimilarityScore(), DocumentSimilarity_.similarityScore),
                buildSpecification(criteria.getAlgorithm(), DocumentSimilarity_.algorithm),
                buildRangeSpecification(criteria.getComputedDate(), DocumentSimilarity_.computedDate),
                buildSpecification(criteria.getIsRelevant(), DocumentSimilarity_.isRelevant),
                buildStringSpecification(criteria.getReviewedBy(), DocumentSimilarity_.reviewedBy),
                buildRangeSpecification(criteria.getReviewedDate(), DocumentSimilarity_.reviewedDate),
                buildSpecification(criteria.getJobId(), root -> root.join(DocumentSimilarity_.job, JoinType.LEFT).get(SimilarityJob_.id))
            );
        }
        return specification;
    }
}
