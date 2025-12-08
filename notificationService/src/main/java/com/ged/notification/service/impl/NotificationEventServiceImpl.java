package com.ged.notification.service.impl;

import com.ged.notification.domain.NotificationEvent;
import com.ged.notification.repository.NotificationEventRepository;
import com.ged.notification.service.NotificationEventService;
import com.ged.notification.service.dto.NotificationEventDTO;
import com.ged.notification.service.mapper.NotificationEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.notification.domain.NotificationEvent}.
 */
@Service
@Transactional
public class NotificationEventServiceImpl implements NotificationEventService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventServiceImpl.class);

    private final NotificationEventRepository notificationEventRepository;

    private final NotificationEventMapper notificationEventMapper;

    public NotificationEventServiceImpl(
        NotificationEventRepository notificationEventRepository,
        NotificationEventMapper notificationEventMapper
    ) {
        this.notificationEventRepository = notificationEventRepository;
        this.notificationEventMapper = notificationEventMapper;
    }

    @Override
    public NotificationEventDTO save(NotificationEventDTO notificationEventDTO) {
        LOG.debug("Request to save NotificationEvent : {}", notificationEventDTO);
        NotificationEvent notificationEvent = notificationEventMapper.toEntity(notificationEventDTO);
        notificationEvent = notificationEventRepository.save(notificationEvent);
        return notificationEventMapper.toDto(notificationEvent);
    }

    @Override
    public NotificationEventDTO update(NotificationEventDTO notificationEventDTO) {
        LOG.debug("Request to update NotificationEvent : {}", notificationEventDTO);
        NotificationEvent notificationEvent = notificationEventMapper.toEntity(notificationEventDTO);
        notificationEvent = notificationEventRepository.save(notificationEvent);
        return notificationEventMapper.toDto(notificationEvent);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationEventDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all NotificationEvents");
        return notificationEventRepository.findAll(pageable).map(notificationEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationEventDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationEvent : {}", id);
        return notificationEventRepository.findById(id).map(notificationEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationEvent : {}", id);
        notificationEventRepository.deleteById(id);
    }
}
