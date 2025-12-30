package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.TransformCompressionJob;
import fr.smartprod.paperdms.transform.repository.TransformCompressionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformCompressionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformCompressionJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.TransformCompressionJob}.
 */
@Service
@Transactional
public class TransformCompressionJobService {

    private static final Logger LOG = LoggerFactory.getLogger(TransformCompressionJobService.class);

    private final TransformCompressionJobRepository transformCompressionJobRepository;

    private final TransformCompressionJobMapper transformCompressionJobMapper;

    public TransformCompressionJobService(
        TransformCompressionJobRepository transformCompressionJobRepository,
        TransformCompressionJobMapper transformCompressionJobMapper
    ) {
        this.transformCompressionJobRepository = transformCompressionJobRepository;
        this.transformCompressionJobMapper = transformCompressionJobMapper;
    }

    /**
     * Save a transformCompressionJob.
     *
     * @param transformCompressionJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformCompressionJobDTO save(TransformCompressionJobDTO transformCompressionJobDTO) {
        LOG.debug("Request to save TransformCompressionJob : {}", transformCompressionJobDTO);
        TransformCompressionJob transformCompressionJob = transformCompressionJobMapper.toEntity(transformCompressionJobDTO);
        transformCompressionJob = transformCompressionJobRepository.save(transformCompressionJob);
        return transformCompressionJobMapper.toDto(transformCompressionJob);
    }

    /**
     * Update a transformCompressionJob.
     *
     * @param transformCompressionJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformCompressionJobDTO update(TransformCompressionJobDTO transformCompressionJobDTO) {
        LOG.debug("Request to update TransformCompressionJob : {}", transformCompressionJobDTO);
        TransformCompressionJob transformCompressionJob = transformCompressionJobMapper.toEntity(transformCompressionJobDTO);
        transformCompressionJob = transformCompressionJobRepository.save(transformCompressionJob);
        return transformCompressionJobMapper.toDto(transformCompressionJob);
    }

    /**
     * Partially update a transformCompressionJob.
     *
     * @param transformCompressionJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransformCompressionJobDTO> partialUpdate(TransformCompressionJobDTO transformCompressionJobDTO) {
        LOG.debug("Request to partially update TransformCompressionJob : {}", transformCompressionJobDTO);

        return transformCompressionJobRepository
            .findById(transformCompressionJobDTO.getId())
            .map(existingTransformCompressionJob -> {
                transformCompressionJobMapper.partialUpdate(existingTransformCompressionJob, transformCompressionJobDTO);

                return existingTransformCompressionJob;
            })
            .map(transformCompressionJobRepository::save)
            .map(transformCompressionJobMapper::toDto);
    }

    /**
     * Get one transformCompressionJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransformCompressionJobDTO> findOne(Long id) {
        LOG.debug("Request to get TransformCompressionJob : {}", id);
        return transformCompressionJobRepository.findById(id).map(transformCompressionJobMapper::toDto);
    }

    /**
     * Delete the transformCompressionJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransformCompressionJob : {}", id);
        transformCompressionJobRepository.deleteById(id);
    }
}
