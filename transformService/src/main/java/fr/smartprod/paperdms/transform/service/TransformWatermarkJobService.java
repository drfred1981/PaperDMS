package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.TransformWatermarkJob;
import fr.smartprod.paperdms.transform.repository.TransformWatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformWatermarkJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformWatermarkJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.TransformWatermarkJob}.
 */
@Service
@Transactional
public class TransformWatermarkJobService {

    private static final Logger LOG = LoggerFactory.getLogger(TransformWatermarkJobService.class);

    private final TransformWatermarkJobRepository transformWatermarkJobRepository;

    private final TransformWatermarkJobMapper transformWatermarkJobMapper;

    public TransformWatermarkJobService(
        TransformWatermarkJobRepository transformWatermarkJobRepository,
        TransformWatermarkJobMapper transformWatermarkJobMapper
    ) {
        this.transformWatermarkJobRepository = transformWatermarkJobRepository;
        this.transformWatermarkJobMapper = transformWatermarkJobMapper;
    }

    /**
     * Save a transformWatermarkJob.
     *
     * @param transformWatermarkJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformWatermarkJobDTO save(TransformWatermarkJobDTO transformWatermarkJobDTO) {
        LOG.debug("Request to save TransformWatermarkJob : {}", transformWatermarkJobDTO);
        TransformWatermarkJob transformWatermarkJob = transformWatermarkJobMapper.toEntity(transformWatermarkJobDTO);
        transformWatermarkJob = transformWatermarkJobRepository.save(transformWatermarkJob);
        return transformWatermarkJobMapper.toDto(transformWatermarkJob);
    }

    /**
     * Update a transformWatermarkJob.
     *
     * @param transformWatermarkJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformWatermarkJobDTO update(TransformWatermarkJobDTO transformWatermarkJobDTO) {
        LOG.debug("Request to update TransformWatermarkJob : {}", transformWatermarkJobDTO);
        TransformWatermarkJob transformWatermarkJob = transformWatermarkJobMapper.toEntity(transformWatermarkJobDTO);
        transformWatermarkJob = transformWatermarkJobRepository.save(transformWatermarkJob);
        return transformWatermarkJobMapper.toDto(transformWatermarkJob);
    }

    /**
     * Partially update a transformWatermarkJob.
     *
     * @param transformWatermarkJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransformWatermarkJobDTO> partialUpdate(TransformWatermarkJobDTO transformWatermarkJobDTO) {
        LOG.debug("Request to partially update TransformWatermarkJob : {}", transformWatermarkJobDTO);

        return transformWatermarkJobRepository
            .findById(transformWatermarkJobDTO.getId())
            .map(existingTransformWatermarkJob -> {
                transformWatermarkJobMapper.partialUpdate(existingTransformWatermarkJob, transformWatermarkJobDTO);

                return existingTransformWatermarkJob;
            })
            .map(transformWatermarkJobRepository::save)
            .map(transformWatermarkJobMapper::toDto);
    }

    /**
     * Get one transformWatermarkJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransformWatermarkJobDTO> findOne(Long id) {
        LOG.debug("Request to get TransformWatermarkJob : {}", id);
        return transformWatermarkJobRepository.findById(id).map(transformWatermarkJobMapper::toDto);
    }

    /**
     * Delete the transformWatermarkJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransformWatermarkJob : {}", id);
        transformWatermarkJobRepository.deleteById(id);
    }
}
