package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric;
import fr.smartprod.paperdms.reporting.repository.ReportingPerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingPerformanceMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingPerformanceMetricMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric}.
 */
@Service
@Transactional
public class ReportingPerformanceMetricService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingPerformanceMetricService.class);

    private final ReportingPerformanceMetricRepository reportingPerformanceMetricRepository;

    private final ReportingPerformanceMetricMapper reportingPerformanceMetricMapper;

    public ReportingPerformanceMetricService(
        ReportingPerformanceMetricRepository reportingPerformanceMetricRepository,
        ReportingPerformanceMetricMapper reportingPerformanceMetricMapper
    ) {
        this.reportingPerformanceMetricRepository = reportingPerformanceMetricRepository;
        this.reportingPerformanceMetricMapper = reportingPerformanceMetricMapper;
    }

    /**
     * Save a reportingPerformanceMetric.
     *
     * @param reportingPerformanceMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingPerformanceMetricDTO save(ReportingPerformanceMetricDTO reportingPerformanceMetricDTO) {
        LOG.debug("Request to save ReportingPerformanceMetric : {}", reportingPerformanceMetricDTO);
        ReportingPerformanceMetric reportingPerformanceMetric = reportingPerformanceMetricMapper.toEntity(reportingPerformanceMetricDTO);
        reportingPerformanceMetric = reportingPerformanceMetricRepository.save(reportingPerformanceMetric);
        return reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);
    }

    /**
     * Update a reportingPerformanceMetric.
     *
     * @param reportingPerformanceMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingPerformanceMetricDTO update(ReportingPerformanceMetricDTO reportingPerformanceMetricDTO) {
        LOG.debug("Request to update ReportingPerformanceMetric : {}", reportingPerformanceMetricDTO);
        ReportingPerformanceMetric reportingPerformanceMetric = reportingPerformanceMetricMapper.toEntity(reportingPerformanceMetricDTO);
        reportingPerformanceMetric = reportingPerformanceMetricRepository.save(reportingPerformanceMetric);
        return reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);
    }

    /**
     * Partially update a reportingPerformanceMetric.
     *
     * @param reportingPerformanceMetricDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingPerformanceMetricDTO> partialUpdate(ReportingPerformanceMetricDTO reportingPerformanceMetricDTO) {
        LOG.debug("Request to partially update ReportingPerformanceMetric : {}", reportingPerformanceMetricDTO);

        return reportingPerformanceMetricRepository
            .findById(reportingPerformanceMetricDTO.getId())
            .map(existingReportingPerformanceMetric -> {
                reportingPerformanceMetricMapper.partialUpdate(existingReportingPerformanceMetric, reportingPerformanceMetricDTO);

                return existingReportingPerformanceMetric;
            })
            .map(reportingPerformanceMetricRepository::save)
            .map(reportingPerformanceMetricMapper::toDto);
    }

    /**
     * Get one reportingPerformanceMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingPerformanceMetricDTO> findOne(Long id) {
        LOG.debug("Request to get ReportingPerformanceMetric : {}", id);
        return reportingPerformanceMetricRepository.findById(id).map(reportingPerformanceMetricMapper::toDto);
    }

    /**
     * Delete the reportingPerformanceMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportingPerformanceMetric : {}", id);
        reportingPerformanceMetricRepository.deleteById(id);
    }
}
