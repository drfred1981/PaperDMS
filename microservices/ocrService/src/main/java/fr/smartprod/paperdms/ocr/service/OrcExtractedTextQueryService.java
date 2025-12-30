package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.*; // for static metamodels
import fr.smartprod.paperdms.ocr.domain.OrcExtractedText;
import fr.smartprod.paperdms.ocr.repository.OrcExtractedTextRepository;
import fr.smartprod.paperdms.ocr.repository.search.OrcExtractedTextSearchRepository;
import fr.smartprod.paperdms.ocr.service.criteria.OrcExtractedTextCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OrcExtractedTextDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OrcExtractedTextMapper;
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
 * Service for executing complex queries for {@link OrcExtractedText} entities in the database.
 * The main input is a {@link OrcExtractedTextCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrcExtractedTextDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrcExtractedTextQueryService extends QueryService<OrcExtractedText> {

    private static final Logger LOG = LoggerFactory.getLogger(OrcExtractedTextQueryService.class);

    private final OrcExtractedTextRepository orcExtractedTextRepository;

    private final OrcExtractedTextMapper orcExtractedTextMapper;

    private final OrcExtractedTextSearchRepository orcExtractedTextSearchRepository;

    public OrcExtractedTextQueryService(
        OrcExtractedTextRepository orcExtractedTextRepository,
        OrcExtractedTextMapper orcExtractedTextMapper,
        OrcExtractedTextSearchRepository orcExtractedTextSearchRepository
    ) {
        this.orcExtractedTextRepository = orcExtractedTextRepository;
        this.orcExtractedTextMapper = orcExtractedTextMapper;
        this.orcExtractedTextSearchRepository = orcExtractedTextSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link OrcExtractedTextDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrcExtractedTextDTO> findByCriteria(OrcExtractedTextCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrcExtractedText> specification = createSpecification(criteria);
        return orcExtractedTextRepository.findAll(specification, page).map(orcExtractedTextMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrcExtractedTextCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrcExtractedText> specification = createSpecification(criteria);
        return orcExtractedTextRepository.count(specification);
    }

    /**
     * Function to convert {@link OrcExtractedTextCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrcExtractedText> createSpecification(OrcExtractedTextCriteria criteria) {
        Specification<OrcExtractedText> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), OrcExtractedText_.id),
                buildStringSpecification(criteria.getContentSha256(), OrcExtractedText_.contentSha256),
                buildStringSpecification(criteria.gets3ContentKey(), OrcExtractedText_.s3ContentKey),
                buildStringSpecification(criteria.gets3Bucket(), OrcExtractedText_.s3Bucket),
                buildRangeSpecification(criteria.getPageNumber(), OrcExtractedText_.pageNumber),
                buildStringSpecification(criteria.getLanguage(), OrcExtractedText_.language),
                buildRangeSpecification(criteria.getWordCount(), OrcExtractedText_.wordCount),
                buildSpecification(criteria.getHasStructuredData(), OrcExtractedText_.hasStructuredData),
                buildStringSpecification(criteria.getStructuredDataS3Key(), OrcExtractedText_.structuredDataS3Key),
                buildRangeSpecification(criteria.getExtractedDate(), OrcExtractedText_.extractedDate),
                buildSpecification(criteria.getJobId(), root -> root.join(OrcExtractedText_.job, JoinType.LEFT).get(OcrJob_.id))
            );
        }
        return specification;
    }
}
