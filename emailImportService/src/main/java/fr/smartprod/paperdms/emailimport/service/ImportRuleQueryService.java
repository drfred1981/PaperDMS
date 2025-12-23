package fr.smartprod.paperdms.emailimport.service;

import fr.smartprod.paperdms.emailimport.domain.*; // for static metamodels
import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.repository.ImportRuleRepository;
import fr.smartprod.paperdms.emailimport.service.criteria.ImportRuleCriteria;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.ImportRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ImportRule} entities in the database.
 * The main input is a {@link ImportRuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ImportRuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImportRuleQueryService extends QueryService<ImportRule> {

    private static final Logger LOG = LoggerFactory.getLogger(ImportRuleQueryService.class);

    private final ImportRuleRepository importRuleRepository;

    private final ImportRuleMapper importRuleMapper;

    public ImportRuleQueryService(ImportRuleRepository importRuleRepository, ImportRuleMapper importRuleMapper) {
        this.importRuleRepository = importRuleRepository;
        this.importRuleMapper = importRuleMapper;
    }

    /**
     * Return a {@link Page} of {@link ImportRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImportRuleDTO> findByCriteria(ImportRuleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImportRule> specification = createSpecification(criteria);
        return importRuleRepository.findAll(specification, page).map(importRuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImportRuleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ImportRule> specification = createSpecification(criteria);
        return importRuleRepository.count(specification);
    }

    /**
     * Function to convert {@link ImportRuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImportRule> createSpecification(ImportRuleCriteria criteria) {
        Specification<ImportRule> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ImportRule_.id),
                buildStringSpecification(criteria.getName(), ImportRule_.name),
                buildRangeSpecification(criteria.getPriority(), ImportRule_.priority),
                buildSpecification(criteria.getIsActive(), ImportRule_.isActive),
                buildRangeSpecification(criteria.getFolderId(), ImportRule_.folderId),
                buildRangeSpecification(criteria.getDocumentTypeId(), ImportRule_.documentTypeId),
                buildRangeSpecification(criteria.getMatchCount(), ImportRule_.matchCount),
                buildRangeSpecification(criteria.getLastMatchDate(), ImportRule_.lastMatchDate),
                buildStringSpecification(criteria.getCreatedBy(), ImportRule_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ImportRule_.createdDate),
                buildRangeSpecification(criteria.getLastModifiedDate(), ImportRule_.lastModifiedDate)
            );
        }
        return specification;
    }
}
