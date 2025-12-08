package com.ged.notification.service;

import com.ged.notification.service.dto.NotificationEventDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ged.notification.domain.NotificationEvent}.
 */
public interface NotificationEventService {
    /**
     * Save a notificationEvent.
     *
     * @param notificationEventDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationEventDTO save(NotificationEventDTO notificationEventDTO);

    /**
     * Updates a notificationEvent.
     *
     * @param notificationEventDTO the entity to update.
     * @return the persisted entity.
     */
    NotificationEventDTO update(NotificationEventDTO notificationEventDTO);

    /**
     * Partially updates a notificationEvent.
     *
     * @param notificationEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationEventDTO> partialUpdate(NotificationEventDTO notificationEventDTO);

    /**
     * Get all the notificationEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificationEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notificationEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationEventDTO> findOne(Long id);

    /**
     * Delete the "id" notificationEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
