package fr.smartprod.paperdms.reporting.service.impl;

import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import fr.smartprod.paperdms.reporting.repository.ScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.ScheduledReportService;
import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ScheduledReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.reporting.domain.ScheduledReport}.
 */
@Service
@Transactional
public class ScheduledReportServiceImpl implements ScheduledReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledReportServiceImpl.class);

    private final ScheduledReportRepository scheduledReportRepository;

    private final ScheduledReportMapper scheduledReportMapper;

    public ScheduledReportServiceImpl(ScheduledReportRepository scheduledReportRepository, ScheduledReportMapper scheduledReportMapper) {
        this.scheduledReportRepository = scheduledReportRepository;
        this.scheduledReportMapper = scheduledReportMapper;
    }

    @Override
    public ScheduledReportDTO save(ScheduledReportDTO scheduledReportDTO) {
        LOG.debug("Request to save ScheduledReport : {}", scheduledReportDTO);
        ScheduledReport scheduledReport = scheduledReportMapper.toEntity(scheduledReportDTO);
        scheduledReport = scheduledReportRepository.save(scheduledReport);
        return scheduledReportMapper.toDto(scheduledReport);
    }

    @Override
    public ScheduledReportDTO update(ScheduledReportDTO scheduledReportDTO) {
        LOG.debug("Request to update ScheduledReport : {}", scheduledReportDTO);
        ScheduledReport scheduledReport = scheduledReportMapper.toEntity(scheduledReportDTO);
        scheduledReport = scheduledReportRepository.save(scheduledReport);
        return scheduledReportMapper.toDto(scheduledReport);
    }

    @Override
    public Optional<ScheduledReportDTO> partialUpdate(ScheduledReportDTO scheduledReportDTO) {
        LOG.debug("Request to partially update ScheduledReport : {}", scheduledReportDTO);

        return scheduledReportRepository
            .findById(scheduledReportDTO.getId())
            .map(existingScheduledReport -> {
                scheduledReportMapper.partialUpdate(existingScheduledReport, scheduledReportDTO);

                return existingScheduledReport;
            })
            .map(scheduledReportRepository::save)
            .map(scheduledReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScheduledReportDTO> findOne(Long id) {
        LOG.debug("Request to get ScheduledReport : {}", id);
        return scheduledReportRepository.findById(id).map(scheduledReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ScheduledReport : {}", id);
        scheduledReportRepository.deleteById(id);
    }
}
