package fr.smartprod.paperdms.transform.service.impl;

import fr.smartprod.paperdms.transform.domain.CompressionJob;
import fr.smartprod.paperdms.transform.repository.CompressionJobRepository;
import fr.smartprod.paperdms.transform.service.CompressionJobService;
import fr.smartprod.paperdms.transform.service.dto.CompressionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.CompressionJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.CompressionJob}.
 */
@Service
@Transactional
public class CompressionJobServiceImpl implements CompressionJobService {

    private static final Logger LOG = LoggerFactory.getLogger(CompressionJobServiceImpl.class);

    private final CompressionJobRepository compressionJobRepository;

    private final CompressionJobMapper compressionJobMapper;

    public CompressionJobServiceImpl(CompressionJobRepository compressionJobRepository, CompressionJobMapper compressionJobMapper) {
        this.compressionJobRepository = compressionJobRepository;
        this.compressionJobMapper = compressionJobMapper;
    }

    @Override
    public CompressionJobDTO save(CompressionJobDTO compressionJobDTO) {
        LOG.debug("Request to save CompressionJob : {}", compressionJobDTO);
        CompressionJob compressionJob = compressionJobMapper.toEntity(compressionJobDTO);
        compressionJob = compressionJobRepository.save(compressionJob);
        return compressionJobMapper.toDto(compressionJob);
    }

    @Override
    public CompressionJobDTO update(CompressionJobDTO compressionJobDTO) {
        LOG.debug("Request to update CompressionJob : {}", compressionJobDTO);
        CompressionJob compressionJob = compressionJobMapper.toEntity(compressionJobDTO);
        compressionJob = compressionJobRepository.save(compressionJob);
        return compressionJobMapper.toDto(compressionJob);
    }

    @Override
    public Optional<CompressionJobDTO> partialUpdate(CompressionJobDTO compressionJobDTO) {
        LOG.debug("Request to partially update CompressionJob : {}", compressionJobDTO);

        return compressionJobRepository
            .findById(compressionJobDTO.getId())
            .map(existingCompressionJob -> {
                compressionJobMapper.partialUpdate(existingCompressionJob, compressionJobDTO);

                return existingCompressionJob;
            })
            .map(compressionJobRepository::save)
            .map(compressionJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompressionJobDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CompressionJobs");
        return compressionJobRepository.findAll(pageable).map(compressionJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompressionJobDTO> findOne(Long id) {
        LOG.debug("Request to get CompressionJob : {}", id);
        return compressionJobRepository.findById(id).map(compressionJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CompressionJob : {}", id);
        compressionJobRepository.deleteById(id);
    }
}
