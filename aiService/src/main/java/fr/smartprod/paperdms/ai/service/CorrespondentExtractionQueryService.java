package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.*; // for static metamodels
import fr.smartprod.paperdms.ai.domain.CorrespondentExtraction;
import fr.smartprod.paperdms.ai.repository.CorrespondentExtractionRepository;
import fr.smartprod.paperdms.ai.service.criteria.CorrespondentExtractionCriteria;
import fr.smartprod.paperdms.ai.service.dto.CorrespondentExtractionDTO;
import fr.smartprod.paperdms.ai.service.mapper.CorrespondentExtractionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CorrespondentExtraction} entities in the database.
 * The main input is a {@link CorrespondentExtractionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CorrespondentExtractionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorrespondentExtractionQueryService extends QueryService<CorrespondentExtraction> {

    private static final Logger LOG = LoggerFactory.getLogger(CorrespondentExtractionQueryService.class);

    private final CorrespondentExtractionRepository correspondentExtractionRepository;

    private final CorrespondentExtractionMapper correspondentExtractionMapper;

    public CorrespondentExtractionQueryService(
        CorrespondentExtractionRepository correspondentExtractionRepository,
        CorrespondentExtractionMapper correspondentExtractionMapper
    ) {
        this.correspondentExtractionRepository = correspondentExtractionRepository;
        this.correspondentExtractionMapper = correspondentExtractionMapper;
    }

    /**
     * Return a {@link Page} of {@link CorrespondentExtractionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorrespondentExtractionDTO> findByCriteria(CorrespondentExtractionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CorrespondentExtraction> specification = createSpecification(criteria);
        return correspondentExtractionRepository.findAll(specification, page).map(correspondentExtractionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorrespondentExtractionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CorrespondentExtraction> specification = createSpecification(criteria);
        return correspondentExtractionRepository.count(specification);
    }

    /**
     * Function to convert {@link CorrespondentExtractionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CorrespondentExtraction> createSpecification(CorrespondentExtractionCriteria criteria) {
        Specification<CorrespondentExtraction> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CorrespondentExtraction_.id),
                buildRangeSpecification(criteria.getDocumentId(), CorrespondentExtraction_.documentId),
                buildStringSpecification(criteria.getDocumentSha256(), CorrespondentExtraction_.documentSha256),
                buildStringSpecification(criteria.getExtractedTextSha256(), CorrespondentExtraction_.extractedTextSha256),
                buildStringSpecification(criteria.getDetectedLanguage(), CorrespondentExtraction_.detectedLanguage),
                buildRangeSpecification(criteria.getLanguageConfidence(), CorrespondentExtraction_.languageConfidence),
                buildSpecification(criteria.getStatus(), CorrespondentExtraction_.status),
                buildStringSpecification(criteria.getResultCacheKey(), CorrespondentExtraction_.resultCacheKey),
                buildSpecification(criteria.getIsCached(), CorrespondentExtraction_.isCached),
                buildStringSpecification(criteria.getResultS3Key(), CorrespondentExtraction_.resultS3Key),
                buildRangeSpecification(criteria.getStartDate(), CorrespondentExtraction_.startDate),
                buildRangeSpecification(criteria.getEndDate(), CorrespondentExtraction_.endDate),
                buildRangeSpecification(criteria.getSendersCount(), CorrespondentExtraction_.sendersCount),
                buildRangeSpecification(criteria.getRecipientsCount(), CorrespondentExtraction_.recipientsCount),
                buildRangeSpecification(criteria.getCreatedDate(), CorrespondentExtraction_.createdDate)
            );
        }
        return specification;
    }
}
