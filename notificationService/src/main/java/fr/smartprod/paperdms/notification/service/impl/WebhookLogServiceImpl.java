package fr.smartprod.paperdms.notification.service.impl;

import fr.smartprod.paperdms.notification.domain.WebhookLog;
import fr.smartprod.paperdms.notification.repository.WebhookLogRepository;
import fr.smartprod.paperdms.notification.service.WebhookLogService;
import fr.smartprod.paperdms.notification.service.dto.WebhookLogDTO;
import fr.smartprod.paperdms.notification.service.mapper.WebhookLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.notification.domain.WebhookLog}.
 */
@Service
@Transactional
public class WebhookLogServiceImpl implements WebhookLogService {

    private static final Logger LOG = LoggerFactory.getLogger(WebhookLogServiceImpl.class);

    private final WebhookLogRepository webhookLogRepository;

    private final WebhookLogMapper webhookLogMapper;

    public WebhookLogServiceImpl(WebhookLogRepository webhookLogRepository, WebhookLogMapper webhookLogMapper) {
        this.webhookLogRepository = webhookLogRepository;
        this.webhookLogMapper = webhookLogMapper;
    }

    @Override
    public WebhookLogDTO save(WebhookLogDTO webhookLogDTO) {
        LOG.debug("Request to save WebhookLog : {}", webhookLogDTO);
        WebhookLog webhookLog = webhookLogMapper.toEntity(webhookLogDTO);
        webhookLog = webhookLogRepository.save(webhookLog);
        return webhookLogMapper.toDto(webhookLog);
    }

    @Override
    public WebhookLogDTO update(WebhookLogDTO webhookLogDTO) {
        LOG.debug("Request to update WebhookLog : {}", webhookLogDTO);
        WebhookLog webhookLog = webhookLogMapper.toEntity(webhookLogDTO);
        webhookLog = webhookLogRepository.save(webhookLog);
        return webhookLogMapper.toDto(webhookLog);
    }

    @Override
    public Optional<WebhookLogDTO> partialUpdate(WebhookLogDTO webhookLogDTO) {
        LOG.debug("Request to partially update WebhookLog : {}", webhookLogDTO);

        return webhookLogRepository
            .findById(webhookLogDTO.getId())
            .map(existingWebhookLog -> {
                webhookLogMapper.partialUpdate(existingWebhookLog, webhookLogDTO);

                return existingWebhookLog;
            })
            .map(webhookLogRepository::save)
            .map(webhookLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WebhookLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WebhookLogs");
        return webhookLogRepository.findAll(pageable).map(webhookLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WebhookLogDTO> findOne(Long id) {
        LOG.debug("Request to get WebhookLog : {}", id);
        return webhookLogRepository.findById(id).map(webhookLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WebhookLog : {}", id);
        webhookLogRepository.deleteById(id);
    }
}
