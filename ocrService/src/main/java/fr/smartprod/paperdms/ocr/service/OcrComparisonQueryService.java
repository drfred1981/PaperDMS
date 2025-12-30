package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.*; // for static metamodels
import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrComparisonSearchRepository;
import fr.smartprod.paperdms.ocr.service.criteria.OcrComparisonCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrComparisonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OcrComparison} entities in the database.
 * The main input is a {@link OcrComparisonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OcrComparisonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OcrComparisonQueryService extends QueryService<OcrComparison> {

    private static final Logger LOG = LoggerFactory.getLogger(OcrComparisonQueryService.class);

    private final OcrComparisonRepository ocrComparisonRepository;

    private final OcrComparisonMapper ocrComparisonMapper;

    private final OcrComparisonSearchRepository ocrComparisonSearchRepository;

    public OcrComparisonQueryService(
        OcrComparisonRepository ocrComparisonRepository,
        OcrComparisonMapper ocrComparisonMapper,
        OcrComparisonSearchRepository ocrComparisonSearchRepository
    ) {
        this.ocrComparisonRepository = ocrComparisonRepository;
        this.ocrComparisonMapper = ocrComparisonMapper;
        this.ocrComparisonSearchRepository = ocrComparisonSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link OcrComparisonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrComparisonDTO> findByCriteria(OcrComparisonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OcrComparison> specification = createSpecification(criteria);
        return ocrComparisonRepository.findAll(specification, page).map(ocrComparisonMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OcrComparisonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OcrComparison> specification = createSpecification(criteria);
        return ocrComparisonRepository.count(specification);
    }

    /**
     * Function to convert {@link OcrComparisonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OcrComparison> createSpecification(OcrComparisonCriteria criteria) {
        Specification<OcrComparison> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), OcrComparison_.id),
                buildStringSpecification(criteria.getDocumentSha256(), OcrComparison_.documentSha256),
                buildRangeSpecification(criteria.getPageNumber(), OcrComparison_.pageNumber),
                buildRangeSpecification(criteria.getTikaConfidence(), OcrComparison_.tikaConfidence),
                buildRangeSpecification(criteria.getAiConfidence(), OcrComparison_.aiConfidence),
                buildRangeSpecification(criteria.getSimilarity(), OcrComparison_.similarity),
                buildStringSpecification(criteria.getDifferencesS3Key(), OcrComparison_.differencesS3Key),
                buildSpecification(criteria.getSelectedEngine(), OcrComparison_.selectedEngine),
                buildStringSpecification(criteria.getSelectedBy(), OcrComparison_.selectedBy),
                buildRangeSpecification(criteria.getSelectedDate(), OcrComparison_.selectedDate),
                buildRangeSpecification(criteria.getComparisonDate(), OcrComparison_.comparisonDate)
            );
        }
        return specification;
    }
}
