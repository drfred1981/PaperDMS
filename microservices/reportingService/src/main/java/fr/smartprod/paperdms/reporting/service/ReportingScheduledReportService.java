package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import fr.smartprod.paperdms.reporting.repository.ReportingScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingScheduledReportDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingScheduledReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport}.
 */
@Service
@Transactional
public class ReportingScheduledReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingScheduledReportService.class);

    private final ReportingScheduledReportRepository reportingScheduledReportRepository;

    private final ReportingScheduledReportMapper reportingScheduledReportMapper;

    public ReportingScheduledReportService(
        ReportingScheduledReportRepository reportingScheduledReportRepository,
        ReportingScheduledReportMapper reportingScheduledReportMapper
    ) {
        this.reportingScheduledReportRepository = reportingScheduledReportRepository;
        this.reportingScheduledReportMapper = reportingScheduledReportMapper;
    }

    /**
     * Save a reportingScheduledReport.
     *
     * @param reportingScheduledReportDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingScheduledReportDTO save(ReportingScheduledReportDTO reportingScheduledReportDTO) {
        LOG.debug("Request to save ReportingScheduledReport : {}", reportingScheduledReportDTO);
        ReportingScheduledReport reportingScheduledReport = reportingScheduledReportMapper.toEntity(reportingScheduledReportDTO);
        reportingScheduledReport = reportingScheduledReportRepository.save(reportingScheduledReport);
        return reportingScheduledReportMapper.toDto(reportingScheduledReport);
    }

    /**
     * Update a reportingScheduledReport.
     *
     * @param reportingScheduledReportDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingScheduledReportDTO update(ReportingScheduledReportDTO reportingScheduledReportDTO) {
        LOG.debug("Request to update ReportingScheduledReport : {}", reportingScheduledReportDTO);
        ReportingScheduledReport reportingScheduledReport = reportingScheduledReportMapper.toEntity(reportingScheduledReportDTO);
        reportingScheduledReport = reportingScheduledReportRepository.save(reportingScheduledReport);
        return reportingScheduledReportMapper.toDto(reportingScheduledReport);
    }

    /**
     * Partially update a reportingScheduledReport.
     *
     * @param reportingScheduledReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingScheduledReportDTO> partialUpdate(ReportingScheduledReportDTO reportingScheduledReportDTO) {
        LOG.debug("Request to partially update ReportingScheduledReport : {}", reportingScheduledReportDTO);

        return reportingScheduledReportRepository
            .findById(reportingScheduledReportDTO.getId())
            .map(existingReportingScheduledReport -> {
                reportingScheduledReportMapper.partialUpdate(existingReportingScheduledReport, reportingScheduledReportDTO);

                return existingReportingScheduledReport;
            })
            .map(reportingScheduledReportRepository::save)
            .map(reportingScheduledReportMapper::toDto);
    }

    /**
     * Get one reportingScheduledReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingScheduledReportDTO> findOne(Long id) {
        LOG.debug("Request to get ReportingScheduledReport : {}", id);
        return reportingScheduledReportRepository.findById(id).map(reportingScheduledReportMapper::toDto);
    }

    /**
     * Delete the reportingScheduledReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportingScheduledReport : {}", id);
        reportingScheduledReportRepository.deleteById(id);
    }
}
