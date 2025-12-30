package fr.smartprod.paperdms.notification.service;

import fr.smartprod.paperdms.notification.domain.NotificationEvent;
import fr.smartprod.paperdms.notification.repository.NotificationEventRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationEventDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.notification.domain.NotificationEvent}.
 */
@Service
@Transactional
public class NotificationEventService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventService.class);

    private final NotificationEventRepository notificationEventRepository;

    private final NotificationEventMapper notificationEventMapper;

    public NotificationEventService(
        NotificationEventRepository notificationEventRepository,
        NotificationEventMapper notificationEventMapper
    ) {
        this.notificationEventRepository = notificationEventRepository;
        this.notificationEventMapper = notificationEventMapper;
    }

    /**
     * Save a notificationEvent.
     *
     * @param notificationEventDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationEventDTO save(NotificationEventDTO notificationEventDTO) {
        LOG.debug("Request to save NotificationEvent : {}", notificationEventDTO);
        NotificationEvent notificationEvent = notificationEventMapper.toEntity(notificationEventDTO);
        notificationEvent = notificationEventRepository.save(notificationEvent);
        return notificationEventMapper.toDto(notificationEvent);
    }

    /**
     * Update a notificationEvent.
     *
     * @param notificationEventDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationEventDTO update(NotificationEventDTO notificationEventDTO) {
        LOG.debug("Request to update NotificationEvent : {}", notificationEventDTO);
        NotificationEvent notificationEvent = notificationEventMapper.toEntity(notificationEventDTO);
        notificationEvent = notificationEventRepository.save(notificationEvent);
        return notificationEventMapper.toDto(notificationEvent);
    }

    /**
     * Partially update a notificationEvent.
     *
     * @param notificationEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationEventDTO> partialUpdate(NotificationEventDTO notificationEventDTO) {
        LOG.debug("Request to partially update NotificationEvent : {}", notificationEventDTO);

        return notificationEventRepository
            .findById(notificationEventDTO.getId())
            .map(existingNotificationEvent -> {
                notificationEventMapper.partialUpdate(existingNotificationEvent, notificationEventDTO);

                return existingNotificationEvent;
            })
            .map(notificationEventRepository::save)
            .map(notificationEventMapper::toDto);
    }

    /**
     * Get one notificationEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationEventDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationEvent : {}", id);
        return notificationEventRepository.findById(id).map(notificationEventMapper::toDto);
    }

    /**
     * Delete the notificationEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationEvent : {}", id);
        notificationEventRepository.deleteById(id);
    }
}
