package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch;
import fr.smartprod.paperdms.monitoring.repository.MonitoringDocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringDocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringDocumentWatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch}.
 */
@Service
@Transactional
public class MonitoringDocumentWatchService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringDocumentWatchService.class);

    private final MonitoringDocumentWatchRepository monitoringDocumentWatchRepository;

    private final MonitoringDocumentWatchMapper monitoringDocumentWatchMapper;

    public MonitoringDocumentWatchService(
        MonitoringDocumentWatchRepository monitoringDocumentWatchRepository,
        MonitoringDocumentWatchMapper monitoringDocumentWatchMapper
    ) {
        this.monitoringDocumentWatchRepository = monitoringDocumentWatchRepository;
        this.monitoringDocumentWatchMapper = monitoringDocumentWatchMapper;
    }

    /**
     * Save a monitoringDocumentWatch.
     *
     * @param monitoringDocumentWatchDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringDocumentWatchDTO save(MonitoringDocumentWatchDTO monitoringDocumentWatchDTO) {
        LOG.debug("Request to save MonitoringDocumentWatch : {}", monitoringDocumentWatchDTO);
        MonitoringDocumentWatch monitoringDocumentWatch = monitoringDocumentWatchMapper.toEntity(monitoringDocumentWatchDTO);
        monitoringDocumentWatch = monitoringDocumentWatchRepository.save(monitoringDocumentWatch);
        return monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);
    }

    /**
     * Update a monitoringDocumentWatch.
     *
     * @param monitoringDocumentWatchDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringDocumentWatchDTO update(MonitoringDocumentWatchDTO monitoringDocumentWatchDTO) {
        LOG.debug("Request to update MonitoringDocumentWatch : {}", monitoringDocumentWatchDTO);
        MonitoringDocumentWatch monitoringDocumentWatch = monitoringDocumentWatchMapper.toEntity(monitoringDocumentWatchDTO);
        monitoringDocumentWatch = monitoringDocumentWatchRepository.save(monitoringDocumentWatch);
        return monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);
    }

    /**
     * Partially update a monitoringDocumentWatch.
     *
     * @param monitoringDocumentWatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitoringDocumentWatchDTO> partialUpdate(MonitoringDocumentWatchDTO monitoringDocumentWatchDTO) {
        LOG.debug("Request to partially update MonitoringDocumentWatch : {}", monitoringDocumentWatchDTO);

        return monitoringDocumentWatchRepository
            .findById(monitoringDocumentWatchDTO.getId())
            .map(existingMonitoringDocumentWatch -> {
                monitoringDocumentWatchMapper.partialUpdate(existingMonitoringDocumentWatch, monitoringDocumentWatchDTO);

                return existingMonitoringDocumentWatch;
            })
            .map(monitoringDocumentWatchRepository::save)
            .map(monitoringDocumentWatchMapper::toDto);
    }

    /**
     * Get one monitoringDocumentWatch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitoringDocumentWatchDTO> findOne(Long id) {
        LOG.debug("Request to get MonitoringDocumentWatch : {}", id);
        return monitoringDocumentWatchRepository.findById(id).map(monitoringDocumentWatchMapper::toDto);
    }

    /**
     * Delete the monitoringDocumentWatch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MonitoringDocumentWatch : {}", id);
        monitoringDocumentWatchRepository.deleteById(id);
    }
}
