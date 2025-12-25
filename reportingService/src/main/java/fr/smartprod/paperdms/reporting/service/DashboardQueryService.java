package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.*; // for static metamodels
import fr.smartprod.paperdms.reporting.domain.Dashboard;
import fr.smartprod.paperdms.reporting.repository.DashboardRepository;
import fr.smartprod.paperdms.reporting.service.criteria.DashboardCriteria;
import fr.smartprod.paperdms.reporting.service.dto.DashboardDTO;
import fr.smartprod.paperdms.reporting.service.mapper.DashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Dashboard} entities in the database.
 * The main input is a {@link DashboardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DashboardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DashboardQueryService extends QueryService<Dashboard> {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardQueryService.class);

    private final DashboardRepository dashboardRepository;

    private final DashboardMapper dashboardMapper;

    public DashboardQueryService(DashboardRepository dashboardRepository, DashboardMapper dashboardMapper) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardMapper = dashboardMapper;
    }

    /**
     * Return a {@link Page} of {@link DashboardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DashboardDTO> findByCriteria(DashboardCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dashboard> specification = createSpecification(criteria);
        return dashboardRepository.findAll(specification, page).map(dashboardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DashboardCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Dashboard> specification = createSpecification(criteria);
        return dashboardRepository.count(specification);
    }

    /**
     * Function to convert {@link DashboardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dashboard> createSpecification(DashboardCriteria criteria) {
        Specification<Dashboard> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Dashboard_.id),
                buildStringSpecification(criteria.getName(), Dashboard_.name),
                buildStringSpecification(criteria.getUserId(), Dashboard_.userId),
                buildSpecification(criteria.getIsPublic(), Dashboard_.isPublic),
                buildRangeSpecification(criteria.getRefreshInterval(), Dashboard_.refreshInterval),
                buildSpecification(criteria.getIsDefault(), Dashboard_.isDefault),
                buildRangeSpecification(criteria.getCreatedDate(), Dashboard_.createdDate)
            );
        }
        return specification;
    }
}
