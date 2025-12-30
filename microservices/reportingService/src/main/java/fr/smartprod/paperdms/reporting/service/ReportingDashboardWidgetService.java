package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget;
import fr.smartprod.paperdms.reporting.repository.ReportingDashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardWidgetDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingDashboardWidgetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget}.
 */
@Service
@Transactional
public class ReportingDashboardWidgetService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingDashboardWidgetService.class);

    private final ReportingDashboardWidgetRepository reportingDashboardWidgetRepository;

    private final ReportingDashboardWidgetMapper reportingDashboardWidgetMapper;

    public ReportingDashboardWidgetService(
        ReportingDashboardWidgetRepository reportingDashboardWidgetRepository,
        ReportingDashboardWidgetMapper reportingDashboardWidgetMapper
    ) {
        this.reportingDashboardWidgetRepository = reportingDashboardWidgetRepository;
        this.reportingDashboardWidgetMapper = reportingDashboardWidgetMapper;
    }

    /**
     * Save a reportingDashboardWidget.
     *
     * @param reportingDashboardWidgetDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingDashboardWidgetDTO save(ReportingDashboardWidgetDTO reportingDashboardWidgetDTO) {
        LOG.debug("Request to save ReportingDashboardWidget : {}", reportingDashboardWidgetDTO);
        ReportingDashboardWidget reportingDashboardWidget = reportingDashboardWidgetMapper.toEntity(reportingDashboardWidgetDTO);
        reportingDashboardWidget = reportingDashboardWidgetRepository.save(reportingDashboardWidget);
        return reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);
    }

    /**
     * Update a reportingDashboardWidget.
     *
     * @param reportingDashboardWidgetDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingDashboardWidgetDTO update(ReportingDashboardWidgetDTO reportingDashboardWidgetDTO) {
        LOG.debug("Request to update ReportingDashboardWidget : {}", reportingDashboardWidgetDTO);
        ReportingDashboardWidget reportingDashboardWidget = reportingDashboardWidgetMapper.toEntity(reportingDashboardWidgetDTO);
        reportingDashboardWidget = reportingDashboardWidgetRepository.save(reportingDashboardWidget);
        return reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);
    }

    /**
     * Partially update a reportingDashboardWidget.
     *
     * @param reportingDashboardWidgetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingDashboardWidgetDTO> partialUpdate(ReportingDashboardWidgetDTO reportingDashboardWidgetDTO) {
        LOG.debug("Request to partially update ReportingDashboardWidget : {}", reportingDashboardWidgetDTO);

        return reportingDashboardWidgetRepository
            .findById(reportingDashboardWidgetDTO.getId())
            .map(existingReportingDashboardWidget -> {
                reportingDashboardWidgetMapper.partialUpdate(existingReportingDashboardWidget, reportingDashboardWidgetDTO);

                return existingReportingDashboardWidget;
            })
            .map(reportingDashboardWidgetRepository::save)
            .map(reportingDashboardWidgetMapper::toDto);
    }

    /**
     * Get one reportingDashboardWidget by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingDashboardWidgetDTO> findOne(Long id) {
        LOG.debug("Request to get ReportingDashboardWidget : {}", id);
        return reportingDashboardWidgetRepository.findById(id).map(reportingDashboardWidgetMapper::toDto);
    }

    /**
     * Delete the reportingDashboardWidget by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportingDashboardWidget : {}", id);
        reportingDashboardWidgetRepository.deleteById(id);
    }
}
