package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.domain.*; // for static metamodels
import fr.smartprod.paperdms.business.domain.BankStatement;
import fr.smartprod.paperdms.business.repository.BankStatementRepository;
import fr.smartprod.paperdms.business.service.criteria.BankStatementCriteria;
import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
import fr.smartprod.paperdms.business.service.mapper.BankStatementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BankStatement} entities in the database.
 * The main input is a {@link BankStatementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BankStatementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankStatementQueryService extends QueryService<BankStatement> {

    private static final Logger LOG = LoggerFactory.getLogger(BankStatementQueryService.class);

    private final BankStatementRepository bankStatementRepository;

    private final BankStatementMapper bankStatementMapper;

    public BankStatementQueryService(BankStatementRepository bankStatementRepository, BankStatementMapper bankStatementMapper) {
        this.bankStatementRepository = bankStatementRepository;
        this.bankStatementMapper = bankStatementMapper;
    }

    /**
     * Return a {@link Page} of {@link BankStatementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankStatementDTO> findByCriteria(BankStatementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankStatement> specification = createSpecification(criteria);
        return bankStatementRepository.findAll(specification, page).map(bankStatementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankStatementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<BankStatement> specification = createSpecification(criteria);
        return bankStatementRepository.count(specification);
    }

    /**
     * Function to convert {@link BankStatementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankStatement> createSpecification(BankStatementCriteria criteria) {
        Specification<BankStatement> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), BankStatement_.id),
                buildRangeSpecification(criteria.getDocumentId(), BankStatement_.documentId),
                buildStringSpecification(criteria.getAccountNumber(), BankStatement_.accountNumber),
                buildStringSpecification(criteria.getBankName(), BankStatement_.bankName),
                buildRangeSpecification(criteria.getStatementDate(), BankStatement_.statementDate),
                buildRangeSpecification(criteria.getStatementPeriodStart(), BankStatement_.statementPeriodStart),
                buildRangeSpecification(criteria.getStatementPeriodEnd(), BankStatement_.statementPeriodEnd),
                buildRangeSpecification(criteria.getOpeningBalance(), BankStatement_.openingBalance),
                buildRangeSpecification(criteria.getClosingBalance(), BankStatement_.closingBalance),
                buildStringSpecification(criteria.getCurrency(), BankStatement_.currency),
                buildSpecification(criteria.getStatus(), BankStatement_.status),
                buildSpecification(criteria.getIsReconciled(), BankStatement_.isReconciled),
                buildRangeSpecification(criteria.getCreatedDate(), BankStatement_.createdDate)
            );
        }
        return specification;
    }
}
