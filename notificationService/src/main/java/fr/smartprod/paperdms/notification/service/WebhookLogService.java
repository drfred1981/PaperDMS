package fr.smartprod.paperdms.notification.service;

import fr.smartprod.paperdms.notification.service.dto.WebhookLogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.notification.domain.WebhookLog}.
 */
public interface WebhookLogService {
    /**
     * Save a webhookLog.
     *
     * @param webhookLogDTO the entity to save.
     * @return the persisted entity.
     */
    WebhookLogDTO save(WebhookLogDTO webhookLogDTO);

    /**
     * Updates a webhookLog.
     *
     * @param webhookLogDTO the entity to update.
     * @return the persisted entity.
     */
    WebhookLogDTO update(WebhookLogDTO webhookLogDTO);

    /**
     * Partially updates a webhookLog.
     *
     * @param webhookLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WebhookLogDTO> partialUpdate(WebhookLogDTO webhookLogDTO);

    /**
     * Get all the webhookLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WebhookLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" webhookLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WebhookLogDTO> findOne(Long id);

    /**
     * Delete the "id" webhookLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
