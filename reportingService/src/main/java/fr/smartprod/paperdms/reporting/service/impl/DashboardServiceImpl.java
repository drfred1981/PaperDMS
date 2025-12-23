package fr.smartprod.paperdms.reporting.service.impl;

import fr.smartprod.paperdms.reporting.domain.Dashboard;
import fr.smartprod.paperdms.reporting.repository.DashboardRepository;
import fr.smartprod.paperdms.reporting.service.DashboardService;
import fr.smartprod.paperdms.reporting.service.dto.DashboardDTO;
import fr.smartprod.paperdms.reporting.service.mapper.DashboardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.Dashboard}.
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardRepository dashboardRepository;

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardRepository dashboardRepository, DashboardMapper dashboardMapper) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public DashboardDTO save(DashboardDTO dashboardDTO) {
        LOG.debug("Request to save Dashboard : {}", dashboardDTO);
        Dashboard dashboard = dashboardMapper.toEntity(dashboardDTO);
        dashboard = dashboardRepository.save(dashboard);
        return dashboardMapper.toDto(dashboard);
    }

    @Override
    public DashboardDTO update(DashboardDTO dashboardDTO) {
        LOG.debug("Request to update Dashboard : {}", dashboardDTO);
        Dashboard dashboard = dashboardMapper.toEntity(dashboardDTO);
        dashboard = dashboardRepository.save(dashboard);
        return dashboardMapper.toDto(dashboard);
    }

    @Override
    public Optional<DashboardDTO> partialUpdate(DashboardDTO dashboardDTO) {
        LOG.debug("Request to partially update Dashboard : {}", dashboardDTO);

        return dashboardRepository
            .findById(dashboardDTO.getId())
            .map(existingDashboard -> {
                dashboardMapper.partialUpdate(existingDashboard, dashboardDTO);

                return existingDashboard;
            })
            .map(dashboardRepository::save)
            .map(dashboardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DashboardDTO> findOne(Long id) {
        LOG.debug("Request to get Dashboard : {}", id);
        return dashboardRepository.findById(id).map(dashboardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Dashboard : {}", id);
        dashboardRepository.deleteById(id);
    }
}
