package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.domain.*; // for static metamodels
import fr.smartprod.paperdms.business.domain.Contract;
import fr.smartprod.paperdms.business.repository.ContractRepository;
import fr.smartprod.paperdms.business.repository.search.ContractSearchRepository;
import fr.smartprod.paperdms.business.service.criteria.ContractCriteria;
import fr.smartprod.paperdms.business.service.dto.ContractDTO;
import fr.smartprod.paperdms.business.service.mapper.ContractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Contract} entities in the database.
 * The main input is a {@link ContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractQueryService extends QueryService<Contract> {

    private static final Logger LOG = LoggerFactory.getLogger(ContractQueryService.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    private final ContractSearchRepository contractSearchRepository;

    public ContractQueryService(
        ContractRepository contractRepository,
        ContractMapper contractMapper,
        ContractSearchRepository contractSearchRepository
    ) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.contractSearchRepository = contractSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findByCriteria(ContractCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contract> specification = createSpecification(criteria);
        return contractRepository.findAll(specification, page).map(contractMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Contract> specification = createSpecification(criteria);
        return contractRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contract> createSpecification(ContractCriteria criteria) {
        Specification<Contract> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Contract_.id),
                buildRangeSpecification(criteria.getDocumentId(), Contract_.documentId),
                buildStringSpecification(criteria.getContractNumber(), Contract_.contractNumber),
                buildSpecification(criteria.getContractType(), Contract_.contractType),
                buildStringSpecification(criteria.getTitle(), Contract_.title),
                buildStringSpecification(criteria.getPartyA(), Contract_.partyA),
                buildStringSpecification(criteria.getPartyB(), Contract_.partyB),
                buildRangeSpecification(criteria.getStartDate(), Contract_.startDate),
                buildRangeSpecification(criteria.getEndDate(), Contract_.endDate),
                buildSpecification(criteria.getAutoRenew(), Contract_.autoRenew),
                buildRangeSpecification(criteria.getContractValue(), Contract_.contractValue),
                buildStringSpecification(criteria.getCurrency(), Contract_.currency),
                buildSpecification(criteria.getStatus(), Contract_.status),
                buildRangeSpecification(criteria.getCreatedDate(), Contract_.createdDate)
            );
        }
        return specification;
    }
}
