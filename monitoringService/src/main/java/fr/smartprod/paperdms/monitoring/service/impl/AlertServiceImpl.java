package fr.smartprod.paperdms.monitoring.service.impl;

import fr.smartprod.paperdms.monitoring.domain.Alert;
import fr.smartprod.paperdms.monitoring.repository.AlertRepository;
import fr.smartprod.paperdms.monitoring.service.AlertService;
import fr.smartprod.paperdms.monitoring.service.dto.AlertDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.AlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.Alert}.
 */
@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private static final Logger LOG = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    public AlertServiceImpl(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    @Override
    public AlertDTO save(AlertDTO alertDTO) {
        LOG.debug("Request to save Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    @Override
    public AlertDTO update(AlertDTO alertDTO) {
        LOG.debug("Request to update Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    @Override
    public Optional<AlertDTO> partialUpdate(AlertDTO alertDTO) {
        LOG.debug("Request to partially update Alert : {}", alertDTO);

        return alertRepository
            .findById(alertDTO.getId())
            .map(existingAlert -> {
                alertMapper.partialUpdate(existingAlert, alertDTO);

                return existingAlert;
            })
            .map(alertRepository::save)
            .map(alertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlertDTO> findOne(Long id) {
        LOG.debug("Request to get Alert : {}", id);
        return alertRepository.findById(id).map(alertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Alert : {}", id);
        alertRepository.deleteById(id);
    }
}
