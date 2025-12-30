package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.domain.TransformRedactionJob;
import fr.smartprod.paperdms.transform.repository.TransformRedactionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformRedactionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformRedactionJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.TransformRedactionJob}.
 */
@Service
@Transactional
public class TransformRedactionJobService {

    private static final Logger LOG = LoggerFactory.getLogger(TransformRedactionJobService.class);

    private final TransformRedactionJobRepository transformRedactionJobRepository;

    private final TransformRedactionJobMapper transformRedactionJobMapper;

    public TransformRedactionJobService(
        TransformRedactionJobRepository transformRedactionJobRepository,
        TransformRedactionJobMapper transformRedactionJobMapper
    ) {
        this.transformRedactionJobRepository = transformRedactionJobRepository;
        this.transformRedactionJobMapper = transformRedactionJobMapper;
    }

    /**
     * Save a transformRedactionJob.
     *
     * @param transformRedactionJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformRedactionJobDTO save(TransformRedactionJobDTO transformRedactionJobDTO) {
        LOG.debug("Request to save TransformRedactionJob : {}", transformRedactionJobDTO);
        TransformRedactionJob transformRedactionJob = transformRedactionJobMapper.toEntity(transformRedactionJobDTO);
        transformRedactionJob = transformRedactionJobRepository.save(transformRedactionJob);
        return transformRedactionJobMapper.toDto(transformRedactionJob);
    }

    /**
     * Update a transformRedactionJob.
     *
     * @param transformRedactionJobDTO the entity to save.
     * @return the persisted entity.
     */
    public TransformRedactionJobDTO update(TransformRedactionJobDTO transformRedactionJobDTO) {
        LOG.debug("Request to update TransformRedactionJob : {}", transformRedactionJobDTO);
        TransformRedactionJob transformRedactionJob = transformRedactionJobMapper.toEntity(transformRedactionJobDTO);
        transformRedactionJob = transformRedactionJobRepository.save(transformRedactionJob);
        return transformRedactionJobMapper.toDto(transformRedactionJob);
    }

    /**
     * Partially update a transformRedactionJob.
     *
     * @param transformRedactionJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransformRedactionJobDTO> partialUpdate(TransformRedactionJobDTO transformRedactionJobDTO) {
        LOG.debug("Request to partially update TransformRedactionJob : {}", transformRedactionJobDTO);

        return transformRedactionJobRepository
            .findById(transformRedactionJobDTO.getId())
            .map(existingTransformRedactionJob -> {
                transformRedactionJobMapper.partialUpdate(existingTransformRedactionJob, transformRedactionJobDTO);

                return existingTransformRedactionJob;
            })
            .map(transformRedactionJobRepository::save)
            .map(transformRedactionJobMapper::toDto);
    }

    /**
     * Get one transformRedactionJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransformRedactionJobDTO> findOne(Long id) {
        LOG.debug("Request to get TransformRedactionJob : {}", id);
        return transformRedactionJobRepository.findById(id).map(transformRedactionJobMapper::toDto);
    }

    /**
     * Delete the transformRedactionJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TransformRedactionJob : {}", id);
        transformRedactionJobRepository.deleteById(id);
    }
}
