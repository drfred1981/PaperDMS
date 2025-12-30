package fr.smartprod.paperdms.notification.service;

import fr.smartprod.paperdms.notification.domain.NotificationWebhookLog;
import fr.smartprod.paperdms.notification.repository.NotificationWebhookLogRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookLogDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationWebhookLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.notification.domain.NotificationWebhookLog}.
 */
@Service
@Transactional
public class NotificationWebhookLogService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebhookLogService.class);

    private final NotificationWebhookLogRepository notificationWebhookLogRepository;

    private final NotificationWebhookLogMapper notificationWebhookLogMapper;

    public NotificationWebhookLogService(
        NotificationWebhookLogRepository notificationWebhookLogRepository,
        NotificationWebhookLogMapper notificationWebhookLogMapper
    ) {
        this.notificationWebhookLogRepository = notificationWebhookLogRepository;
        this.notificationWebhookLogMapper = notificationWebhookLogMapper;
    }

    /**
     * Save a notificationWebhookLog.
     *
     * @param notificationWebhookLogDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationWebhookLogDTO save(NotificationWebhookLogDTO notificationWebhookLogDTO) {
        LOG.debug("Request to save NotificationWebhookLog : {}", notificationWebhookLogDTO);
        NotificationWebhookLog notificationWebhookLog = notificationWebhookLogMapper.toEntity(notificationWebhookLogDTO);
        notificationWebhookLog = notificationWebhookLogRepository.save(notificationWebhookLog);
        return notificationWebhookLogMapper.toDto(notificationWebhookLog);
    }

    /**
     * Update a notificationWebhookLog.
     *
     * @param notificationWebhookLogDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationWebhookLogDTO update(NotificationWebhookLogDTO notificationWebhookLogDTO) {
        LOG.debug("Request to update NotificationWebhookLog : {}", notificationWebhookLogDTO);
        NotificationWebhookLog notificationWebhookLog = notificationWebhookLogMapper.toEntity(notificationWebhookLogDTO);
        notificationWebhookLog = notificationWebhookLogRepository.save(notificationWebhookLog);
        return notificationWebhookLogMapper.toDto(notificationWebhookLog);
    }

    /**
     * Partially update a notificationWebhookLog.
     *
     * @param notificationWebhookLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationWebhookLogDTO> partialUpdate(NotificationWebhookLogDTO notificationWebhookLogDTO) {
        LOG.debug("Request to partially update NotificationWebhookLog : {}", notificationWebhookLogDTO);

        return notificationWebhookLogRepository
            .findById(notificationWebhookLogDTO.getId())
            .map(existingNotificationWebhookLog -> {
                notificationWebhookLogMapper.partialUpdate(existingNotificationWebhookLog, notificationWebhookLogDTO);

                return existingNotificationWebhookLog;
            })
            .map(notificationWebhookLogRepository::save)
            .map(notificationWebhookLogMapper::toDto);
    }

    /**
     * Get one notificationWebhookLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationWebhookLogDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationWebhookLog : {}", id);
        return notificationWebhookLogRepository.findById(id).map(notificationWebhookLogMapper::toDto);
    }

    /**
     * Delete the notificationWebhookLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationWebhookLog : {}", id);
        notificationWebhookLogRepository.deleteById(id);
    }
}
