package fr.smartprod.paperdms.transform.service.impl;

import fr.smartprod.paperdms.transform.domain.RedactionJob;
import fr.smartprod.paperdms.transform.repository.RedactionJobRepository;
import fr.smartprod.paperdms.transform.service.RedactionJobService;
import fr.smartprod.paperdms.transform.service.dto.RedactionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.RedactionJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.RedactionJob}.
 */
@Service
@Transactional
public class RedactionJobServiceImpl implements RedactionJobService {

    private static final Logger LOG = LoggerFactory.getLogger(RedactionJobServiceImpl.class);

    private final RedactionJobRepository redactionJobRepository;

    private final RedactionJobMapper redactionJobMapper;

    public RedactionJobServiceImpl(RedactionJobRepository redactionJobRepository, RedactionJobMapper redactionJobMapper) {
        this.redactionJobRepository = redactionJobRepository;
        this.redactionJobMapper = redactionJobMapper;
    }

    @Override
    public RedactionJobDTO save(RedactionJobDTO redactionJobDTO) {
        LOG.debug("Request to save RedactionJob : {}", redactionJobDTO);
        RedactionJob redactionJob = redactionJobMapper.toEntity(redactionJobDTO);
        redactionJob = redactionJobRepository.save(redactionJob);
        return redactionJobMapper.toDto(redactionJob);
    }

    @Override
    public RedactionJobDTO update(RedactionJobDTO redactionJobDTO) {
        LOG.debug("Request to update RedactionJob : {}", redactionJobDTO);
        RedactionJob redactionJob = redactionJobMapper.toEntity(redactionJobDTO);
        redactionJob = redactionJobRepository.save(redactionJob);
        return redactionJobMapper.toDto(redactionJob);
    }

    @Override
    public Optional<RedactionJobDTO> partialUpdate(RedactionJobDTO redactionJobDTO) {
        LOG.debug("Request to partially update RedactionJob : {}", redactionJobDTO);

        return redactionJobRepository
            .findById(redactionJobDTO.getId())
            .map(existingRedactionJob -> {
                redactionJobMapper.partialUpdate(existingRedactionJob, redactionJobDTO);

                return existingRedactionJob;
            })
            .map(redactionJobRepository::save)
            .map(redactionJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RedactionJobDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RedactionJobs");
        return redactionJobRepository.findAll(pageable).map(redactionJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RedactionJobDTO> findOne(Long id) {
        LOG.debug("Request to get RedactionJob : {}", id);
        return redactionJobRepository.findById(id).map(redactionJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete RedactionJob : {}", id);
        redactionJobRepository.deleteById(id);
    }
}
