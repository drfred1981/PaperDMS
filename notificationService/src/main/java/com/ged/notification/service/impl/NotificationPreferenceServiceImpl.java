package com.ged.notification.service.impl;

import com.ged.notification.domain.NotificationPreference;
import com.ged.notification.repository.NotificationPreferenceRepository;
import com.ged.notification.service.NotificationPreferenceService;
import com.ged.notification.service.dto.NotificationPreferenceDTO;
import com.ged.notification.service.mapper.NotificationPreferenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.notification.domain.NotificationPreference}.
 */
@Service
@Transactional
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationPreferenceServiceImpl.class);

    private final NotificationPreferenceRepository notificationPreferenceRepository;

    private final NotificationPreferenceMapper notificationPreferenceMapper;

    public NotificationPreferenceServiceImpl(
        NotificationPreferenceRepository notificationPreferenceRepository,
        NotificationPreferenceMapper notificationPreferenceMapper
    ) {
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.notificationPreferenceMapper = notificationPreferenceMapper;
    }

    @Override
    public NotificationPreferenceDTO save(NotificationPreferenceDTO notificationPreferenceDTO) {
        LOG.debug("Request to save NotificationPreference : {}", notificationPreferenceDTO);
        NotificationPreference notificationPreference = notificationPreferenceMapper.toEntity(notificationPreferenceDTO);
        notificationPreference = notificationPreferenceRepository.save(notificationPreference);
        return notificationPreferenceMapper.toDto(notificationPreference);
    }

    @Override
    public NotificationPreferenceDTO update(NotificationPreferenceDTO notificationPreferenceDTO) {
        LOG.debug("Request to update NotificationPreference : {}", notificationPreferenceDTO);
        NotificationPreference notificationPreference = notificationPreferenceMapper.toEntity(notificationPreferenceDTO);
        notificationPreference = notificationPreferenceRepository.save(notificationPreference);
        return notificationPreferenceMapper.toDto(notificationPreference);
    }

    @Override
    public Optional<NotificationPreferenceDTO> partialUpdate(NotificationPreferenceDTO notificationPreferenceDTO) {
        LOG.debug("Request to partially update NotificationPreference : {}", notificationPreferenceDTO);

        return notificationPreferenceRepository
            .findById(notificationPreferenceDTO.getId())
            .map(existingNotificationPreference -> {
                notificationPreferenceMapper.partialUpdate(existingNotificationPreference, notificationPreferenceDTO);

                return existingNotificationPreference;
            })
            .map(notificationPreferenceRepository::save)
            .map(notificationPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationPreferenceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all NotificationPreferences");
        return notificationPreferenceRepository.findAll(pageable).map(notificationPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationPreferenceDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationPreference : {}", id);
        return notificationPreferenceRepository.findById(id).map(notificationPreferenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationPreference : {}", id);
        notificationPreferenceRepository.deleteById(id);
    }
}
