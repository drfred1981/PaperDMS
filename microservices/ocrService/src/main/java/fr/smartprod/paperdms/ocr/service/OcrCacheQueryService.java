package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.*; // for static metamodels
import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrCacheSearchRepository;
import fr.smartprod.paperdms.ocr.service.criteria.OcrCacheCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrCacheMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OcrCache} entities in the database.
 * The main input is a {@link OcrCacheCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OcrCacheDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OcrCacheQueryService extends QueryService<OcrCache> {

    private static final Logger LOG = LoggerFactory.getLogger(OcrCacheQueryService.class);

    private final OcrCacheRepository ocrCacheRepository;

    private final OcrCacheMapper ocrCacheMapper;

    private final OcrCacheSearchRepository ocrCacheSearchRepository;

    public OcrCacheQueryService(
        OcrCacheRepository ocrCacheRepository,
        OcrCacheMapper ocrCacheMapper,
        OcrCacheSearchRepository ocrCacheSearchRepository
    ) {
        this.ocrCacheRepository = ocrCacheRepository;
        this.ocrCacheMapper = ocrCacheMapper;
        this.ocrCacheSearchRepository = ocrCacheSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link OcrCacheDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrCacheDTO> findByCriteria(OcrCacheCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OcrCache> specification = createSpecification(criteria);
        return ocrCacheRepository.findAll(specification, page).map(ocrCacheMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OcrCacheCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OcrCache> specification = createSpecification(criteria);
        return ocrCacheRepository.count(specification);
    }

    /**
     * Function to convert {@link OcrCacheCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OcrCache> createSpecification(OcrCacheCriteria criteria) {
        Specification<OcrCache> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), OcrCache_.id),
                buildStringSpecification(criteria.getDocumentSha256(), OcrCache_.documentSha256),
                buildSpecification(criteria.getOcrEngine(), OcrCache_.ocrEngine),
                buildStringSpecification(criteria.getLanguage(), OcrCache_.language),
                buildRangeSpecification(criteria.getPageCount(), OcrCache_.pageCount),
                buildRangeSpecification(criteria.getTotalConfidence(), OcrCache_.totalConfidence),
                buildStringSpecification(criteria.gets3ResultKey(), OcrCache_.s3ResultKey),
                buildStringSpecification(criteria.gets3Bucket(), OcrCache_.s3Bucket),
                buildStringSpecification(criteria.getOrcExtractedTextS3Key(), OcrCache_.orcExtractedTextS3Key),
                buildRangeSpecification(criteria.getHits(), OcrCache_.hits),
                buildRangeSpecification(criteria.getLastAccessDate(), OcrCache_.lastAccessDate),
                buildRangeSpecification(criteria.getCreatedDate(), OcrCache_.createdDate),
                buildRangeSpecification(criteria.getExpirationDate(), OcrCache_.expirationDate)
            );
        }
        return specification;
    }
}
