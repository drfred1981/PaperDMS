package fr.smartprod.paperdms.transform.service.impl;

import fr.smartprod.paperdms.transform.domain.MergeJob;
import fr.smartprod.paperdms.transform.repository.MergeJobRepository;
import fr.smartprod.paperdms.transform.service.MergeJobService;
import fr.smartprod.paperdms.transform.service.dto.MergeJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.MergeJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.MergeJob}.
 */
@Service
@Transactional
public class MergeJobServiceImpl implements MergeJobService {

    private static final Logger LOG = LoggerFactory.getLogger(MergeJobServiceImpl.class);

    private final MergeJobRepository mergeJobRepository;

    private final MergeJobMapper mergeJobMapper;

    public MergeJobServiceImpl(MergeJobRepository mergeJobRepository, MergeJobMapper mergeJobMapper) {
        this.mergeJobRepository = mergeJobRepository;
        this.mergeJobMapper = mergeJobMapper;
    }

    @Override
    public MergeJobDTO save(MergeJobDTO mergeJobDTO) {
        LOG.debug("Request to save MergeJob : {}", mergeJobDTO);
        MergeJob mergeJob = mergeJobMapper.toEntity(mergeJobDTO);
        mergeJob = mergeJobRepository.save(mergeJob);
        return mergeJobMapper.toDto(mergeJob);
    }

    @Override
    public MergeJobDTO update(MergeJobDTO mergeJobDTO) {
        LOG.debug("Request to update MergeJob : {}", mergeJobDTO);
        MergeJob mergeJob = mergeJobMapper.toEntity(mergeJobDTO);
        mergeJob = mergeJobRepository.save(mergeJob);
        return mergeJobMapper.toDto(mergeJob);
    }

    @Override
    public Optional<MergeJobDTO> partialUpdate(MergeJobDTO mergeJobDTO) {
        LOG.debug("Request to partially update MergeJob : {}", mergeJobDTO);

        return mergeJobRepository
            .findById(mergeJobDTO.getId())
            .map(existingMergeJob -> {
                mergeJobMapper.partialUpdate(existingMergeJob, mergeJobDTO);

                return existingMergeJob;
            })
            .map(mergeJobRepository::save)
            .map(mergeJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MergeJobDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MergeJobs");
        return mergeJobRepository.findAll(pageable).map(mergeJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MergeJobDTO> findOne(Long id) {
        LOG.debug("Request to get MergeJob : {}", id);
        return mergeJobRepository.findById(id).map(mergeJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MergeJob : {}", id);
        mergeJobRepository.deleteById(id);
    }
}
