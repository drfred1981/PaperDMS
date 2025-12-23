package fr.smartprod.paperdms.monitoring.service.impl;

import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import fr.smartprod.paperdms.monitoring.repository.AlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.AlertRuleService;
import fr.smartprod.paperdms.monitoring.service.dto.AlertRuleDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.AlertRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.monitoring.domain.AlertRule}.
 */
@Service
@Transactional
public class AlertRuleServiceImpl implements AlertRuleService {

    private static final Logger LOG = LoggerFactory.getLogger(AlertRuleServiceImpl.class);

    private final AlertRuleRepository alertRuleRepository;

    private final AlertRuleMapper alertRuleMapper;

    public AlertRuleServiceImpl(AlertRuleRepository alertRuleRepository, AlertRuleMapper alertRuleMapper) {
        this.alertRuleRepository = alertRuleRepository;
        this.alertRuleMapper = alertRuleMapper;
    }

    @Override
    public AlertRuleDTO save(AlertRuleDTO alertRuleDTO) {
        LOG.debug("Request to save AlertRule : {}", alertRuleDTO);
        AlertRule alertRule = alertRuleMapper.toEntity(alertRuleDTO);
        alertRule = alertRuleRepository.save(alertRule);
        return alertRuleMapper.toDto(alertRule);
    }

    @Override
    public AlertRuleDTO update(AlertRuleDTO alertRuleDTO) {
        LOG.debug("Request to update AlertRule : {}", alertRuleDTO);
        AlertRule alertRule = alertRuleMapper.toEntity(alertRuleDTO);
        alertRule = alertRuleRepository.save(alertRule);
        return alertRuleMapper.toDto(alertRule);
    }

    @Override
    public Optional<AlertRuleDTO> partialUpdate(AlertRuleDTO alertRuleDTO) {
        LOG.debug("Request to partially update AlertRule : {}", alertRuleDTO);

        return alertRuleRepository
            .findById(alertRuleDTO.getId())
            .map(existingAlertRule -> {
                alertRuleMapper.partialUpdate(existingAlertRule, alertRuleDTO);

                return existingAlertRule;
            })
            .map(alertRuleRepository::save)
            .map(alertRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlertRuleDTO> findOne(Long id) {
        LOG.debug("Request to get AlertRule : {}", id);
        return alertRuleRepository.findById(id).map(alertRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AlertRule : {}", id);
        alertRuleRepository.deleteById(id);
    }
}
