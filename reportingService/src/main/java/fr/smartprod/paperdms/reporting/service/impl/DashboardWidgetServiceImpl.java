package fr.smartprod.paperdms.reporting.service.impl;

import fr.smartprod.paperdms.reporting.domain.DashboardWidget;
import fr.smartprod.paperdms.reporting.repository.DashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.DashboardWidgetService;
import fr.smartprod.paperdms.reporting.service.dto.DashboardWidgetDTO;
import fr.smartprod.paperdms.reporting.service.mapper.DashboardWidgetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.DashboardWidget}.
 */
@Service
@Transactional
public class DashboardWidgetServiceImpl implements DashboardWidgetService {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardWidgetServiceImpl.class);

    private final DashboardWidgetRepository dashboardWidgetRepository;

    private final DashboardWidgetMapper dashboardWidgetMapper;

    public DashboardWidgetServiceImpl(DashboardWidgetRepository dashboardWidgetRepository, DashboardWidgetMapper dashboardWidgetMapper) {
        this.dashboardWidgetRepository = dashboardWidgetRepository;
        this.dashboardWidgetMapper = dashboardWidgetMapper;
    }

    @Override
    public DashboardWidgetDTO save(DashboardWidgetDTO dashboardWidgetDTO) {
        LOG.debug("Request to save DashboardWidget : {}", dashboardWidgetDTO);
        DashboardWidget dashboardWidget = dashboardWidgetMapper.toEntity(dashboardWidgetDTO);
        dashboardWidget = dashboardWidgetRepository.save(dashboardWidget);
        return dashboardWidgetMapper.toDto(dashboardWidget);
    }

    @Override
    public DashboardWidgetDTO update(DashboardWidgetDTO dashboardWidgetDTO) {
        LOG.debug("Request to update DashboardWidget : {}", dashboardWidgetDTO);
        DashboardWidget dashboardWidget = dashboardWidgetMapper.toEntity(dashboardWidgetDTO);
        dashboardWidget = dashboardWidgetRepository.save(dashboardWidget);
        return dashboardWidgetMapper.toDto(dashboardWidget);
    }

    @Override
    public Optional<DashboardWidgetDTO> partialUpdate(DashboardWidgetDTO dashboardWidgetDTO) {
        LOG.debug("Request to partially update DashboardWidget : {}", dashboardWidgetDTO);

        return dashboardWidgetRepository
            .findById(dashboardWidgetDTO.getId())
            .map(existingDashboardWidget -> {
                dashboardWidgetMapper.partialUpdate(existingDashboardWidget, dashboardWidgetDTO);

                return existingDashboardWidget;
            })
            .map(dashboardWidgetRepository::save)
            .map(dashboardWidgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DashboardWidgetDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DashboardWidgets");
        return dashboardWidgetRepository.findAll(pageable).map(dashboardWidgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DashboardWidgetDTO> findOne(Long id) {
        LOG.debug("Request to get DashboardWidget : {}", id);
        return dashboardWidgetRepository.findById(id).map(dashboardWidgetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DashboardWidget : {}", id);
        dashboardWidgetRepository.deleteById(id);
    }
}
