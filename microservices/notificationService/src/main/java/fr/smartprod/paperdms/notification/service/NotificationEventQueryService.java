package fr.smartprod.paperdms.notification.service;

import fr.smartprod.paperdms.notification.domain.*; // for static metamodels
import fr.smartprod.paperdms.notification.domain.NotificationEvent;
import fr.smartprod.paperdms.notification.repository.NotificationEventRepository;
import fr.smartprod.paperdms.notification.service.criteria.NotificationEventCriteria;
import fr.smartprod.paperdms.notification.service.dto.NotificationEventDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link NotificationEvent} entities in the database.
 * The main input is a {@link NotificationEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NotificationEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationEventQueryService extends QueryService<NotificationEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventQueryService.class);

    private final NotificationEventRepository notificationEventRepository;

    private final NotificationEventMapper notificationEventMapper;

    public NotificationEventQueryService(
        NotificationEventRepository notificationEventRepository,
        NotificationEventMapper notificationEventMapper
    ) {
        this.notificationEventRepository = notificationEventRepository;
        this.notificationEventMapper = notificationEventMapper;
    }

    /**
     * Return a {@link Page} of {@link NotificationEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationEventDTO> findByCriteria(NotificationEventCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotificationEvent> specification = createSpecification(criteria);
        return notificationEventRepository.findAll(specification, page).map(notificationEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationEventCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NotificationEvent> specification = createSpecification(criteria);
        return notificationEventRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotificationEvent> createSpecification(NotificationEventCriteria criteria) {
        Specification<NotificationEvent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), NotificationEvent_.id),
                buildStringSpecification(criteria.getEventType(), NotificationEvent_.eventType),
                buildStringSpecification(criteria.getEntityType(), NotificationEvent_.entityType),
                buildStringSpecification(criteria.getEntityName(), NotificationEvent_.entityName),
                buildStringSpecification(criteria.getUserId(), NotificationEvent_.userId),
                buildRangeSpecification(criteria.getEventDate(), NotificationEvent_.eventDate),
                buildSpecification(criteria.getProcessed(), NotificationEvent_.processed),
                buildRangeSpecification(criteria.getProcessedDate(), NotificationEvent_.processedDate)
            );
        }
        return specification;
    }
}
