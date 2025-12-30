package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.MonitoringAlert;
import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringAlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlert}.
 */
@Service
@Transactional
public class MonitoringAlertService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringAlertService.class);

    private final MonitoringAlertRepository monitoringAlertRepository;

    private final MonitoringAlertMapper monitoringAlertMapper;

    public MonitoringAlertService(MonitoringAlertRepository monitoringAlertRepository, MonitoringAlertMapper monitoringAlertMapper) {
        this.monitoringAlertRepository = monitoringAlertRepository;
        this.monitoringAlertMapper = monitoringAlertMapper;
    }

    /**
     * Save a monitoringAlert.
     *
     * @param monitoringAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringAlertDTO save(MonitoringAlertDTO monitoringAlertDTO) {
        LOG.debug("Request to save MonitoringAlert : {}", monitoringAlertDTO);
        MonitoringAlert monitoringAlert = monitoringAlertMapper.toEntity(monitoringAlertDTO);
        monitoringAlert = monitoringAlertRepository.save(monitoringAlert);
        return monitoringAlertMapper.toDto(monitoringAlert);
    }

    /**
     * Update a monitoringAlert.
     *
     * @param monitoringAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringAlertDTO update(MonitoringAlertDTO monitoringAlertDTO) {
        LOG.debug("Request to update MonitoringAlert : {}", monitoringAlertDTO);
        MonitoringAlert monitoringAlert = monitoringAlertMapper.toEntity(monitoringAlertDTO);
        monitoringAlert = monitoringAlertRepository.save(monitoringAlert);
        return monitoringAlertMapper.toDto(monitoringAlert);
    }

    /**
     * Partially update a monitoringAlert.
     *
     * @param monitoringAlertDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitoringAlertDTO> partialUpdate(MonitoringAlertDTO monitoringAlertDTO) {
        LOG.debug("Request to partially update MonitoringAlert : {}", monitoringAlertDTO);

        return monitoringAlertRepository
            .findById(monitoringAlertDTO.getId())
            .map(existingMonitoringAlert -> {
                monitoringAlertMapper.partialUpdate(existingMonitoringAlert, monitoringAlertDTO);

                return existingMonitoringAlert;
            })
            .map(monitoringAlertRepository::save)
            .map(monitoringAlertMapper::toDto);
    }

    /**
     * Get one monitoringAlert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitoringAlertDTO> findOne(Long id) {
        LOG.debug("Request to get MonitoringAlert : {}", id);
        return monitoringAlertRepository.findById(id).map(monitoringAlertMapper::toDto);
    }

    /**
     * Delete the monitoringAlert by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MonitoringAlert : {}", id);
        monitoringAlertRepository.deleteById(id);
    }
}
