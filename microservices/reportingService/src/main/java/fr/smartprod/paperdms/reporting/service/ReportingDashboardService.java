package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.ReportingDashboard;
import fr.smartprod.paperdms.reporting.repository.ReportingDashboardRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingDashboardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboard}.
 */
@Service
@Transactional
public class ReportingDashboardService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingDashboardService.class);

    private final ReportingDashboardRepository reportingDashboardRepository;

    private final ReportingDashboardMapper reportingDashboardMapper;

    public ReportingDashboardService(
        ReportingDashboardRepository reportingDashboardRepository,
        ReportingDashboardMapper reportingDashboardMapper
    ) {
        this.reportingDashboardRepository = reportingDashboardRepository;
        this.reportingDashboardMapper = reportingDashboardMapper;
    }

    /**
     * Save a reportingDashboard.
     *
     * @param reportingDashboardDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingDashboardDTO save(ReportingDashboardDTO reportingDashboardDTO) {
        LOG.debug("Request to save ReportingDashboard : {}", reportingDashboardDTO);
        ReportingDashboard reportingDashboard = reportingDashboardMapper.toEntity(reportingDashboardDTO);
        reportingDashboard = reportingDashboardRepository.save(reportingDashboard);
        return reportingDashboardMapper.toDto(reportingDashboard);
    }

    /**
     * Update a reportingDashboard.
     *
     * @param reportingDashboardDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingDashboardDTO update(ReportingDashboardDTO reportingDashboardDTO) {
        LOG.debug("Request to update ReportingDashboard : {}", reportingDashboardDTO);
        ReportingDashboard reportingDashboard = reportingDashboardMapper.toEntity(reportingDashboardDTO);
        reportingDashboard = reportingDashboardRepository.save(reportingDashboard);
        return reportingDashboardMapper.toDto(reportingDashboard);
    }

    /**
     * Partially update a reportingDashboard.
     *
     * @param reportingDashboardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingDashboardDTO> partialUpdate(ReportingDashboardDTO reportingDashboardDTO) {
        LOG.debug("Request to partially update ReportingDashboard : {}", reportingDashboardDTO);

        return reportingDashboardRepository
            .findById(reportingDashboardDTO.getId())
            .map(existingReportingDashboard -> {
                reportingDashboardMapper.partialUpdate(existingReportingDashboard, reportingDashboardDTO);

                return existingReportingDashboard;
            })
            .map(reportingDashboardRepository::save)
            .map(reportingDashboardMapper::toDto);
    }

    /**
     * Get one reportingDashboard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingDashboardDTO> findOne(Long id) {
        LOG.debug("Request to get ReportingDashboard : {}", id);
        return reportingDashboardRepository.findById(id).map(reportingDashboardMapper::toDto);
    }

    /**
     * Delete the reportingDashboard by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportingDashboard : {}", id);
        reportingDashboardRepository.deleteById(id);
    }
}
