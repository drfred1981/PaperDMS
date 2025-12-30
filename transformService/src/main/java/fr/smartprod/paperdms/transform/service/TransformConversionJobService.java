package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.TransformConversionJob;
import fr.smartprod.paperdms.transform.repository.TransformConversionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformConversionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformConversionJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.TransformConversionJob}.
 */
@Service
@Transactional
public class TransformConversionJobService {

    private static final Logger LOG = LoggerFactory.getLogger(TransformConversionJobService.class);

    private final TransformConversionJobRepository transformConversionJobRepository;

    private final TransformConversionJobMapper transformConversionJobMapper;

    public TransformConversionJobService(
        TransformConversionJobRepository transformConversionJobRepository,
        TransformConversionJobMapper transformConversionJobMapper
    ) {
        this.transformConversionJobRepository = transformConversionJobRepository;
        this.transformConversionJobMapper = transformConversionJobMapper;
    }

    /**
     * Save a transformConversionJob.
     *
     * @param transformConversionJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformConversionJobDTO save(TransformConversionJobDTO transformConversionJobDTO) {
        LOG.debug("Request to save TransformConversionJob : {}", transformConversionJobDTO);
        TransformConversionJob transformConversionJob = transformConversionJobMapper.toEntity(transformConversionJobDTO);
        transformConversionJob = transformConversionJobRepository.save(transformConversionJob);
        return transformConversionJobMapper.toDto(transformConversionJob);
    }

    /**
     * Update a transformConversionJob.
     *
     * @param transformConversionJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformConversionJobDTO update(TransformConversionJobDTO transformConversionJobDTO) {
        LOG.debug("Request to update TransformConversionJob : {}", transformConversionJobDTO);
        TransformConversionJob transformConversionJob = transformConversionJobMapper.toEntity(transformConversionJobDTO);
        transformConversionJob = transformConversionJobRepository.save(transformConversionJob);
        return transformConversionJobMapper.toDto(transformConversionJob);
    }

    /**
     * Partially update a transformConversionJob.
     *
     * @param transformConversionJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransformConversionJobDTO> partialUpdate(TransformConversionJobDTO transformConversionJobDTO) {
        LOG.debug("Request to partially update TransformConversionJob : {}", transformConversionJobDTO);

        return transformConversionJobRepository
            .findById(transformConversionJobDTO.getId())
            .map(existingTransformConversionJob -> {
                transformConversionJobMapper.partialUpdate(existingTransformConversionJob, transformConversionJobDTO);

                return existingTransformConversionJob;
            })
            .map(transformConversionJobRepository::save)
            .map(transformConversionJobMapper::toDto);
    }

    /**
     * Get one transformConversionJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransformConversionJobDTO> findOne(Long id) {
        LOG.debug("Request to get TransformConversionJob : {}", id);
        return transformConversionJobRepository.findById(id).map(transformConversionJobMapper::toDto);
    }

    /**
     * Delete the transformConversionJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransformConversionJob : {}", id);
        transformConversionJobRepository.deleteById(id);
    }
}
