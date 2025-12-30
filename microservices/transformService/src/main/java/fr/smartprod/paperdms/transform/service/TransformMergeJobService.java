package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.TransformMergeJob;
import fr.smartprod.paperdms.transform.repository.TransformMergeJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformMergeJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformMergeJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.TransformMergeJob}.
 */
@Service
@Transactional
public class TransformMergeJobService {

    private static final Logger LOG = LoggerFactory.getLogger(TransformMergeJobService.class);

    private final TransformMergeJobRepository transformMergeJobRepository;

    private final TransformMergeJobMapper transformMergeJobMapper;

    public TransformMergeJobService(
        TransformMergeJobRepository transformMergeJobRepository,
        TransformMergeJobMapper transformMergeJobMapper
    ) {
        this.transformMergeJobRepository = transformMergeJobRepository;
        this.transformMergeJobMapper = transformMergeJobMapper;
    }

    /**
     * Save a transformMergeJob.
     *
     * @param transformMergeJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformMergeJobDTO save(TransformMergeJobDTO transformMergeJobDTO) {
        LOG.debug("Request to save TransformMergeJob : {}", transformMergeJobDTO);
        TransformMergeJob transformMergeJob = transformMergeJobMapper.toEntity(transformMergeJobDTO);
        transformMergeJob = transformMergeJobRepository.save(transformMergeJob);
        return transformMergeJobMapper.toDto(transformMergeJob);
    }

    /**
     * Update a transformMergeJob.
     *
     * @param transformMergeJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformMergeJobDTO update(TransformMergeJobDTO transformMergeJobDTO) {
        LOG.debug("Request to update TransformMergeJob : {}", transformMergeJobDTO);
        TransformMergeJob transformMergeJob = transformMergeJobMapper.toEntity(transformMergeJobDTO);
        transformMergeJob = transformMergeJobRepository.save(transformMergeJob);
        return transformMergeJobMapper.toDto(transformMergeJob);
    }

    /**
     * Partially update a transformMergeJob.
     *
     * @param transformMergeJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransformMergeJobDTO> partialUpdate(TransformMergeJobDTO transformMergeJobDTO) {
        LOG.debug("Request to partially update TransformMergeJob : {}", transformMergeJobDTO);

        return transformMergeJobRepository
            .findById(transformMergeJobDTO.getId())
            .map(existingTransformMergeJob -> {
                transformMergeJobMapper.partialUpdate(existingTransformMergeJob, transformMergeJobDTO);

                return existingTransformMergeJob;
            })
            .map(transformMergeJobRepository::save)
            .map(transformMergeJobMapper::toDto);
    }

    /**
     * Get one transformMergeJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransformMergeJobDTO> findOne(Long id) {
        LOG.debug("Request to get TransformMergeJob : {}", id);
        return transformMergeJobRepository.findById(id).map(transformMergeJobMapper::toDto);
    }

    /**
     * Delete the transformMergeJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransformMergeJob : {}", id);
        transformMergeJobRepository.deleteById(id);
    }
}
