package fr.smartprod.paperdms.reporting.service.impl;

import fr.smartprod.paperdms.reporting.domain.ReportExecution;
import fr.smartprod.paperdms.reporting.repository.ReportExecutionRepository;
import fr.smartprod.paperdms.reporting.service.ReportExecutionService;
import fr.smartprod.paperdms.reporting.service.dto.ReportExecutionDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportExecutionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ReportExecution}.
 */
@Service
@Transactional
public class ReportExecutionServiceImpl implements ReportExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportExecutionServiceImpl.class);

    private final ReportExecutionRepository reportExecutionRepository;

    private final ReportExecutionMapper reportExecutionMapper;

    public ReportExecutionServiceImpl(ReportExecutionRepository reportExecutionRepository, ReportExecutionMapper reportExecutionMapper) {
        this.reportExecutionRepository = reportExecutionRepository;
        this.reportExecutionMapper = reportExecutionMapper;
    }

    @Override
    public ReportExecutionDTO save(ReportExecutionDTO reportExecutionDTO) {
        LOG.debug("Request to save ReportExecution : {}", reportExecutionDTO);
        ReportExecution reportExecution = reportExecutionMapper.toEntity(reportExecutionDTO);
        reportExecution = reportExecutionRepository.save(reportExecution);
        return reportExecutionMapper.toDto(reportExecution);
    }

    @Override
    public ReportExecutionDTO update(ReportExecutionDTO reportExecutionDTO) {
        LOG.debug("Request to update ReportExecution : {}", reportExecutionDTO);
        ReportExecution reportExecution = reportExecutionMapper.toEntity(reportExecutionDTO);
        reportExecution = reportExecutionRepository.save(reportExecution);
        return reportExecutionMapper.toDto(reportExecution);
    }

    @Override
    public Optional<ReportExecutionDTO> partialUpdate(ReportExecutionDTO reportExecutionDTO) {
        LOG.debug("Request to partially update ReportExecution : {}", reportExecutionDTO);

        return reportExecutionRepository
            .findById(reportExecutionDTO.getId())
            .map(existingReportExecution -> {
                reportExecutionMapper.partialUpdate(existingReportExecution, reportExecutionDTO);

                return existingReportExecution;
            })
            .map(reportExecutionRepository::save)
            .map(reportExecutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportExecutionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReportExecutions");
        return reportExecutionRepository.findAll(pageable).map(reportExecutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportExecutionDTO> findOne(Long id) {
        LOG.debug("Request to get ReportExecution : {}", id);
        return reportExecutionRepository.findById(id).map(reportExecutionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReportExecution : {}", id);
        reportExecutionRepository.deleteById(id);
    }
}
