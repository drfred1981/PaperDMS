package fr.smartprod.paperdms.notification.service;

import fr.smartprod.paperdms.notification.domain.*; // for static metamodels
import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import fr.smartprod.paperdms.notification.repository.NotificationWebhookSubscriptionRepository;
import fr.smartprod.paperdms.notification.service.criteria.NotificationWebhookSubscriptionCriteria;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookSubscriptionDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationWebhookSubscriptionMapper;
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
 * Service for executing complex queries for {@link NotificationWebhookSubscription} entities in the database.
 * The main input is a {@link NotificationWebhookSubscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NotificationWebhookSubscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationWebhookSubscriptionQueryService extends QueryService<NotificationWebhookSubscription> {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebhookSubscriptionQueryService.class);

    private final NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository;

    private final NotificationWebhookSubscriptionMapper notificationWebhookSubscriptionMapper;

    public NotificationWebhookSubscriptionQueryService(
        NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository,
        NotificationWebhookSubscriptionMapper notificationWebhookSubscriptionMapper
    ) {
        this.notificationWebhookSubscriptionRepository = notificationWebhookSubscriptionRepository;
        this.notificationWebhookSubscriptionMapper = notificationWebhookSubscriptionMapper;
    }

    /**
     * Return a {@link Page} of {@link NotificationWebhookSubscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationWebhookSubscriptionDTO> findByCriteria(NotificationWebhookSubscriptionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotificationWebhookSubscription> specification = createSpecification(criteria);
        return notificationWebhookSubscriptionRepository.findAll(specification, page).map(notificationWebhookSubscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationWebhookSubscriptionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NotificationWebhookSubscription> specification = createSpecification(criteria);
        return notificationWebhookSubscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationWebhookSubscriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotificationWebhookSubscription> createSpecification(NotificationWebhookSubscriptionCriteria criteria) {
        Specification<NotificationWebhookSubscription> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), NotificationWebhookSubscription_.id),
                buildStringSpecification(criteria.getName(), NotificationWebhookSubscription_.name),
                buildStringSpecification(criteria.getUrl(), NotificationWebhookSubscription_.url),
                buildStringSpecification(criteria.getSecret(), NotificationWebhookSubscription_.secret),
                buildSpecification(criteria.getIsActive(), NotificationWebhookSubscription_.isActive),
                buildRangeSpecification(criteria.getRetryCount(), NotificationWebhookSubscription_.retryCount),
                buildRangeSpecification(criteria.getMaxRetries(), NotificationWebhookSubscription_.maxRetries),
                buildRangeSpecification(criteria.getRetryDelay(), NotificationWebhookSubscription_.retryDelay),
                buildRangeSpecification(criteria.getLastTriggerDate(), NotificationWebhookSubscription_.lastTriggerDate),
                buildRangeSpecification(criteria.getLastSuccessDate(), NotificationWebhookSubscription_.lastSuccessDate),
                buildRangeSpecification(criteria.getFailureCount(), NotificationWebhookSubscription_.failureCount),
                buildStringSpecification(criteria.getCreatedBy(), NotificationWebhookSubscription_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), NotificationWebhookSubscription_.createdDate),
                buildRangeSpecification(criteria.getLastModifiedDate(), NotificationWebhookSubscription_.lastModifiedDate),
                buildSpecification(criteria.getNotificationWebhookLogsId(), root ->
                    root.join(NotificationWebhookSubscription_.notificationWebhookLogs, JoinType.LEFT).get(NotificationWebhookLog_.id)
                )
            );
        }
        return specification;
    }
}
