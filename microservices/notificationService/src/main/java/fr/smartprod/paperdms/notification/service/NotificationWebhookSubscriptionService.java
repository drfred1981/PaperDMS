package fr.smartprod.paperdms.notification.service;

import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import fr.smartprod.paperdms.notification.repository.NotificationWebhookSubscriptionRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookSubscriptionDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationWebhookSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription}.
 */
@Service
@Transactional
public class NotificationWebhookSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebhookSubscriptionService.class);

    private final NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository;

    private final NotificationWebhookSubscriptionMapper notificationWebhookSubscriptionMapper;

    public NotificationWebhookSubscriptionService(
        NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository,
        NotificationWebhookSubscriptionMapper notificationWebhookSubscriptionMapper
    ) {
        this.notificationWebhookSubscriptionRepository = notificationWebhookSubscriptionRepository;
        this.notificationWebhookSubscriptionMapper = notificationWebhookSubscriptionMapper;
    }

    /**
     * Save a notificationWebhookSubscription.
     *
     * @param notificationWebhookSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationWebhookSubscriptionDTO save(NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO) {
        LOG.debug("Request to save NotificationWebhookSubscription : {}", notificationWebhookSubscriptionDTO);
        NotificationWebhookSubscription notificationWebhookSubscription = notificationWebhookSubscriptionMapper.toEntity(
            notificationWebhookSubscriptionDTO
        );
        notificationWebhookSubscription = notificationWebhookSubscriptionRepository.save(notificationWebhookSubscription);
        return notificationWebhookSubscriptionMapper.toDto(notificationWebhookSubscription);
    }

    /**
     * Update a notificationWebhookSubscription.
     *
     * @param notificationWebhookSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationWebhookSubscriptionDTO update(NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO) {
        LOG.debug("Request to update NotificationWebhookSubscription : {}", notificationWebhookSubscriptionDTO);
        NotificationWebhookSubscription notificationWebhookSubscription = notificationWebhookSubscriptionMapper.toEntity(
            notificationWebhookSubscriptionDTO
        );
        notificationWebhookSubscription = notificationWebhookSubscriptionRepository.save(notificationWebhookSubscription);
        return notificationWebhookSubscriptionMapper.toDto(notificationWebhookSubscription);
    }

    /**
     * Partially update a notificationWebhookSubscription.
     *
     * @param notificationWebhookSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationWebhookSubscriptionDTO> partialUpdate(
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO
    ) {
        LOG.debug("Request to partially update NotificationWebhookSubscription : {}", notificationWebhookSubscriptionDTO);

        return notificationWebhookSubscriptionRepository
            .findById(notificationWebhookSubscriptionDTO.getId())
            .map(existingNotificationWebhookSubscription -> {
                notificationWebhookSubscriptionMapper.partialUpdate(
                    existingNotificationWebhookSubscription,
                    notificationWebhookSubscriptionDTO
                );

                return existingNotificationWebhookSubscription;
            })
            .map(notificationWebhookSubscriptionRepository::save)
            .map(notificationWebhookSubscriptionMapper::toDto);
    }

    /**
     * Get one notificationWebhookSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationWebhookSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationWebhookSubscription : {}", id);
        return notificationWebhookSubscriptionRepository.findById(id).map(notificationWebhookSubscriptionMapper::toDto);
    }

    /**
     * Delete the notificationWebhookSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationWebhookSubscription : {}", id);
        notificationWebhookSubscriptionRepository.deleteById(id);
    }
}
