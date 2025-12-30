package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertRuleDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringAlertRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule}.
 */
@Service
@Transactional
public class MonitoringAlertRuleService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringAlertRuleService.class);

    private final MonitoringAlertRuleRepository monitoringAlertRuleRepository;

    private final MonitoringAlertRuleMapper monitoringAlertRuleMapper;

    public MonitoringAlertRuleService(
        MonitoringAlertRuleRepository monitoringAlertRuleRepository,
        MonitoringAlertRuleMapper monitoringAlertRuleMapper
    ) {
        this.monitoringAlertRuleRepository = monitoringAlertRuleRepository;
        this.monitoringAlertRuleMapper = monitoringAlertRuleMapper;
    }

    /**
     * Save a monitoringAlertRule.
     *
     * @param monitoringAlertRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringAlertRuleDTO save(MonitoringAlertRuleDTO monitoringAlertRuleDTO) {
        LOG.debug("Request to save MonitoringAlertRule : {}", monitoringAlertRuleDTO);
        MonitoringAlertRule monitoringAlertRule = monitoringAlertRuleMapper.toEntity(monitoringAlertRuleDTO);
        monitoringAlertRule = monitoringAlertRuleRepository.save(monitoringAlertRule);
        return monitoringAlertRuleMapper.toDto(monitoringAlertRule);
    }

    /**
     * Update a monitoringAlertRule.
     *
     * @param monitoringAlertRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitoringAlertRuleDTO update(MonitoringAlertRuleDTO monitoringAlertRuleDTO) {
        LOG.debug("Request to update MonitoringAlertRule : {}", monitoringAlertRuleDTO);
        MonitoringAlertRule monitoringAlertRule = monitoringAlertRuleMapper.toEntity(monitoringAlertRuleDTO);
        monitoringAlertRule = monitoringAlertRuleRepository.save(monitoringAlertRule);
        return monitoringAlertRuleMapper.toDto(monitoringAlertRule);
    }

    /**
     * Partially update a monitoringAlertRule.
     *
     * @param monitoringAlertRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitoringAlertRuleDTO> partialUpdate(MonitoringAlertRuleDTO monitoringAlertRuleDTO) {
        LOG.debug("Request to partially update MonitoringAlertRule : {}", monitoringAlertRuleDTO);

        return monitoringAlertRuleRepository
            .findById(monitoringAlertRuleDTO.getId())
            .map(existingMonitoringAlertRule -> {
                monitoringAlertRuleMapper.partialUpdate(existingMonitoringAlertRule, monitoringAlertRuleDTO);

                return existingMonitoringAlertRule;
            })
            .map(monitoringAlertRuleRepository::save)
            .map(monitoringAlertRuleMapper::toDto);
    }

    /**
     * Get one monitoringAlertRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitoringAlertRuleDTO> findOne(Long id) {
        LOG.debug("Request to get MonitoringAlertRule : {}", id);
        return monitoringAlertRuleRepository.findById(id).map(monitoringAlertRuleMapper::toDto);
    }

    /**
     * Delete the monitoringAlertRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MonitoringAlertRule : {}", id);
        monitoringAlertRuleRepository.deleteById(id);
    }
}
