package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric;
import fr.smartprod.paperdms.reporting.repository.ReportingSystemMetricRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingSystemMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingSystemMetricMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric}.
 */
@Service
@Transactional
public class ReportingSystemMetricService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingSystemMetricService.class);

    private final ReportingSystemMetricRepository reportingSystemMetricRepository;

    private final ReportingSystemMetricMapper reportingSystemMetricMapper;

    public ReportingSystemMetricService(
        ReportingSystemMetricRepository reportingSystemMetricRepository,
        ReportingSystemMetricMapper reportingSystemMetricMapper
    ) {
        this.reportingSystemMetricRepository = reportingSystemMetricRepository;
        this.reportingSystemMetricMapper = reportingSystemMetricMapper;
    }

    /**
     * Save a reportingSystemMetric.
     *
     * @param reportingSystemMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingSystemMetricDTO save(ReportingSystemMetricDTO reportingSystemMetricDTO) {
        LOG.debug("Request to save ReportingSystemMetric : {}", reportingSystemMetricDTO);
        ReportingSystemMetric reportingSystemMetric = reportingSystemMetricMapper.toEntity(reportingSystemMetricDTO);
        reportingSystemMetric = reportingSystemMetricRepository.save(reportingSystemMetric);
        return reportingSystemMetricMapper.toDto(reportingSystemMetric);
    }

    /**
     * Update a reportingSystemMetric.
     *
     * @param reportingSystemMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingSystemMetricDTO update(ReportingSystemMetricDTO reportingSystemMetricDTO) {
        LOG.debug("Request to update ReportingSystemMetric : {}", reportingSystemMetricDTO);
        ReportingSystemMetric reportingSystemMetric = reportingSystemMetricMapper.toEntity(reportingSystemMetricDTO);
        reportingSystemMetric = reportingSystemMetricRepository.save(reportingSystemMetric);
        return reportingSystemMetricMapper.toDto(reportingSystemMetric);
    }

    /**
     * Partially update a reportingSystemMetric.
     *
     * @param reportingSystemMetricDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingSystemMetricDTO> partialUpdate(ReportingSystemMetricDTO reportingSystemMetricDTO) {
        LOG.debug("Request to partially update ReportingSystemMetric : {}", reportingSystemMetricDTO);

        return reportingSystemMetricRepository
            .findById(reportingSystemMetricDTO.getId())
            .map(existingReportingSystemMetric -> {
                reportingSystemMetricMapper.partialUpdate(existingReportingSystemMetric, reportingSystemMetricDTO);

                return existingReportingSystemMetric;
            })
            .map(reportingSystemMetricRepository::save)
            .map(reportingSystemMetricMapper::toDto);
    }

    /**
     * Get one reportingSystemMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingSystemMetricDTO> findOne(Long id) {
        LOG.debug("Request to get ReportingSystemMetric : {}", id);
        return reportingSystemMetricRepository.findById(id).map(reportingSystemMetricMapper::toDto);
    }

    /**
     * Delete the reportingSystemMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportingSystemMetric : {}", id);
        reportingSystemMetricRepository.deleteById(id);
    }
}
