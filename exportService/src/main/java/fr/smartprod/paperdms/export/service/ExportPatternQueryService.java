package fr.smartprod.paperdms.export.service;

import fr.smartprod.paperdms.export.domain.*; // for static metamodels
import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.repository.ExportPatternRepository;
import fr.smartprod.paperdms.export.service.criteria.ExportPatternCriteria;
import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportPatternMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExportPattern} entities in the database.
 * The main input is a {@link ExportPatternCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExportPatternDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExportPatternQueryService extends QueryService<ExportPattern> {

    private static final Logger LOG = LoggerFactory.getLogger(ExportPatternQueryService.class);

    private final ExportPatternRepository exportPatternRepository;

    private final ExportPatternMapper exportPatternMapper;

    public ExportPatternQueryService(ExportPatternRepository exportPatternRepository, ExportPatternMapper exportPatternMapper) {
        this.exportPatternRepository = exportPatternRepository;
        this.exportPatternMapper = exportPatternMapper;
    }

    /**
     * Return a {@link Page} of {@link ExportPatternDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExportPatternDTO> findByCriteria(ExportPatternCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExportPattern> specification = createSpecification(criteria);
        return exportPatternRepository.findAll(specification, page).map(exportPatternMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExportPatternCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExportPattern> specification = createSpecification(criteria);
        return exportPatternRepository.count(specification);
    }

    /**
     * Function to convert {@link ExportPatternCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExportPattern> createSpecification(ExportPatternCriteria criteria) {
        Specification<ExportPattern> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExportPattern_.id),
                buildStringSpecification(criteria.getName(), ExportPattern_.name),
                buildStringSpecification(criteria.getPathTemplate(), ExportPattern_.pathTemplate),
                buildStringSpecification(criteria.getFileNameTemplate(), ExportPattern_.fileNameTemplate),
                buildSpecification(criteria.getIsSystem(), ExportPattern_.isSystem),
                buildSpecification(criteria.getIsActive(), ExportPattern_.isActive),
                buildRangeSpecification(criteria.getUsageCount(), ExportPattern_.usageCount),
                buildStringSpecification(criteria.getCreatedBy(), ExportPattern_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ExportPattern_.createdDate),
                buildRangeSpecification(criteria.getLastModifiedDate(), ExportPattern_.lastModifiedDate)
            );
        }
        return specification;
    }
}
