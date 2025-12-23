package fr.smartprod.paperdms.transform.service.impl;

import fr.smartprod.paperdms.transform.domain.ComparisonJob;
import fr.smartprod.paperdms.transform.repository.ComparisonJobRepository;
import fr.smartprod.paperdms.transform.service.ComparisonJobService;
import fr.smartprod.paperdms.transform.service.dto.ComparisonJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.ComparisonJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.ComparisonJob}.
 */
@Service
@Transactional
public class ComparisonJobServiceImpl implements ComparisonJobService {

    private static final Logger LOG = LoggerFactory.getLogger(ComparisonJobServiceImpl.class);

    private final ComparisonJobRepository comparisonJobRepository;

    private final ComparisonJobMapper comparisonJobMapper;

    public ComparisonJobServiceImpl(ComparisonJobRepository comparisonJobRepository, ComparisonJobMapper comparisonJobMapper) {
        this.comparisonJobRepository = comparisonJobRepository;
        this.comparisonJobMapper = comparisonJobMapper;
    }

    @Override
    public ComparisonJobDTO save(ComparisonJobDTO comparisonJobDTO) {
        LOG.debug("Request to save ComparisonJob : {}", comparisonJobDTO);
        ComparisonJob comparisonJob = comparisonJobMapper.toEntity(comparisonJobDTO);
        comparisonJob = comparisonJobRepository.save(comparisonJob);
        return comparisonJobMapper.toDto(comparisonJob);
    }

    @Override
    public ComparisonJobDTO update(ComparisonJobDTO comparisonJobDTO) {
        LOG.debug("Request to update ComparisonJob : {}", comparisonJobDTO);
        ComparisonJob comparisonJob = comparisonJobMapper.toEntity(comparisonJobDTO);
        comparisonJob = comparisonJobRepository.save(comparisonJob);
        return comparisonJobMapper.toDto(comparisonJob);
    }

    @Override
    public Optional<ComparisonJobDTO> partialUpdate(ComparisonJobDTO comparisonJobDTO) {
        LOG.debug("Request to partially update ComparisonJob : {}", comparisonJobDTO);

        return comparisonJobRepository
            .findById(comparisonJobDTO.getId())
            .map(existingComparisonJob -> {
                comparisonJobMapper.partialUpdate(existingComparisonJob, comparisonJobDTO);

                return existingComparisonJob;
            })
            .map(comparisonJobRepository::save)
            .map(comparisonJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComparisonJobDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ComparisonJobs");
        return comparisonJobRepository.findAll(pageable).map(comparisonJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComparisonJobDTO> findOne(Long id) {
        LOG.debug("Request to get ComparisonJob : {}", id);
        return comparisonJobRepository.findById(id).map(comparisonJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ComparisonJob : {}", id);
        comparisonJobRepository.deleteById(id);
    }
}
