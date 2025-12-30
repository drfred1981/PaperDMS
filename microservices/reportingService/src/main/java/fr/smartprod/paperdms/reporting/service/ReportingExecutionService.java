package fr.smartprod.paperdms.reporting.service;

import fr.smartprod.paperdms.reporting.domain.ReportingExecution;
import fr.smartprod.paperdms.reporting.repository.ReportingExecutionRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingExecutionDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingExecutionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingExecution}.
 */
@Service
@Transactional
public class ReportingExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingExecutionService.class);

    private final ReportingExecutionRepository reportingExecutionRepository;

    private final ReportingExecutionMapper reportingExecutionMapper;

    public ReportingExecutionService(
        ReportingExecutionRepository reportingExecutionRepository,
        ReportingExecutionMapper reportingExecutionMapper
    ) {
        this.reportingExecutionRepository = reportingExecutionRepository;
        this.reportingExecutionMapper = reportingExecutionMapper;
    }

    /**
     * Save a reportingExecution.
     *
     * @param reportingExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingExecutionDTO save(ReportingExecutionDTO reportingExecutionDTO) {
        LOG.debug("Request to save ReportingExecution : {}", reportingExecutionDTO);
        ReportingExecution reportingExecution = reportingExecutionMapper.toEntity(reportingExecutionDTO);
        reportingExecution = reportingExecutionRepository.save(reportingExecution);
        return reportingExecutionMapper.toDto(reportingExecution);
    }

    /**
     * Update a reportingExecution.
     *
     * @param reportingExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingExecutionDTO update(ReportingExecutionDTO reportingExecutionDTO) {
        LOG.debug("Request to update ReportingExecution : {}", reportingExecutionDTO);
        ReportingExecution reportingExecution = reportingExecutionMapper.toEntity(reportingExecutionDTO);
        reportingExecution = reportingExecutionRepository.save(reportingExecution);
        return reportingExecutionMapper.toDto(reportingExecution);
    }

    /**
     * Partially update a reportingExecution.
     *
     * @param reportingExecutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingExecutionDTO> partialUpdate(ReportingExecutionDTO reportingExecutionDTO) {
        LOG.debug("Request to partially update ReportingExecution : {}", reportingExecutionDTO);

        return reportingExecutionRepository
            .findById(reportingExecutionDTO.getId())
            .map(existingReportingExecution -> {
                reportingExecutionMapper.partialUpdate(existingReportingExecution, reportingExecutionDTO);

                return existingReportingExecution;
            })
            .map(reportingExecutionRepository::save)
            .map(reportingExecutionMapper::toDto);
    }

    /**
     * Get one reportingExecution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingExecutionDTO> findOne(Long id) {
        LOG.debug("Request to get ReportingExecution : {}", id);
        return reportingExecutionRepository.findById(id).map(reportingExecutionMapper::toDto);
    }

    /**
     * Delete the reportingExecution by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportingExecution : {}", id);
        reportingExecutionRepository.deleteById(id);
    }
}
