package com.ged.notification.service;

import com.ged.notification.service.dto.NotificationPreferenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ged.notification.domain.NotificationPreference}.
 */
public interface NotificationPreferenceService {
    /**
     * Save a notificationPreference.
     *
     * @param notificationPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationPreferenceDTO save(NotificationPreferenceDTO notificationPreferenceDTO);

    /**
     * Updates a notificationPreference.
     *
     * @param notificationPreferenceDTO the entity to update.
     * @return the persisted entity.
     */
    NotificationPreferenceDTO update(NotificationPreferenceDTO notificationPreferenceDTO);

    /**
     * Partially updates a notificationPreference.
     *
     * @param notificationPreferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationPreferenceDTO> partialUpdate(NotificationPreferenceDTO notificationPreferenceDTO);

    /**
     * Get all the notificationPreferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificationPreferenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notificationPreference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationPreferenceDTO> findOne(Long id);

    /**
     * Delete the "id" notificationPreference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
