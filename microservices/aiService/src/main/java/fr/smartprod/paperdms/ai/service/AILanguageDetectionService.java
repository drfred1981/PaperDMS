package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.repository.AILanguageDetectionRepository;
import fr.smartprod.paperdms.ai.repository.search.AILanguageDetectionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AILanguageDetectionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AILanguageDetectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AILanguageDetection}.
 */
@Service
@Transactional
public class AILanguageDetectionService {

    private static final Logger LOG = LoggerFactory.getLogger(AILanguageDetectionService.class);

    private final AILanguageDetectionRepository aILanguageDetectionRepository;

    private final AILanguageDetectionMapper aILanguageDetectionMapper;

    private final AILanguageDetectionSearchRepository aILanguageDetectionSearchRepository;

    public AILanguageDetectionService(
        AILanguageDetectionRepository aILanguageDetectionRepository,
        AILanguageDetectionMapper aILanguageDetectionMapper,
        AILanguageDetectionSearchRepository aILanguageDetectionSearchRepository
    ) {
        this.aILanguageDetectionRepository = aILanguageDetectionRepository;
        this.aILanguageDetectionMapper = aILanguageDetectionMapper;
        this.aILanguageDetectionSearchRepository = aILanguageDetectionSearchRepository;
    }

    /**
     * Save a aILanguageDetection.
     *
     * @param aILanguageDetectionDTO the entity to save.
     * @return the persisted entity.
     */
    public AILanguageDetectionDTO save(AILanguageDetectionDTO aILanguageDetectionDTO) {
        LOG.debug("Request to save AILanguageDetection : {}", aILanguageDetectionDTO);
        AILanguageDetection aILanguageDetection = aILanguageDetectionMapper.toEntity(aILanguageDetectionDTO);
        aILanguageDetection = aILanguageDetectionRepository.save(aILanguageDetection);
        aILanguageDetectionSearchRepository.index(aILanguageDetection);
        return aILanguageDetectionMapper.toDto(aILanguageDetection);
    }

    /**
     * Update a aILanguageDetection.
     *
     * @param aILanguageDetectionDTO the entity to save.
     * @return the persisted entity.
     */
    public AILanguageDetectionDTO update(AILanguageDetectionDTO aILanguageDetectionDTO) {
        LOG.debug("Request to update AILanguageDetection : {}", aILanguageDetectionDTO);
        AILanguageDetection aILanguageDetection = aILanguageDetectionMapper.toEntity(aILanguageDetectionDTO);
        aILanguageDetection = aILanguageDetectionRepository.save(aILanguageDetection);
        aILanguageDetectionSearchRepository.index(aILanguageDetection);
        return aILanguageDetectionMapper.toDto(aILanguageDetection);
    }

    /**
     * Partially update a aILanguageDetection.
     *
     * @param aILanguageDetectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AILanguageDetectionDTO> partialUpdate(AILanguageDetectionDTO aILanguageDetectionDTO) {
        LOG.debug("Request to partially update AILanguageDetection : {}", aILanguageDetectionDTO);

        return aILanguageDetectionRepository
            .findById(aILanguageDetectionDTO.getId())
            .map(existingAILanguageDetection -> {
                aILanguageDetectionMapper.partialUpdate(existingAILanguageDetection, aILanguageDetectionDTO);

                return existingAILanguageDetection;
            })
            .map(aILanguageDetectionRepository::save)
            .map(savedAILanguageDetection -> {
                aILanguageDetectionSearchRepository.index(savedAILanguageDetection);
                return savedAILanguageDetection;
            })
            .map(aILanguageDetectionMapper::toDto);
    }

    /**
     *  Get all the aILanguageDetections where Job is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AILanguageDetectionDTO> findAllWhereJobIsNull() {
        LOG.debug("Request to get all aILanguageDetections where Job is null");
        return StreamSupport.stream(aILanguageDetectionRepository.findAll().spliterator(), false)
            .filter(aILanguageDetection -> aILanguageDetection.getJob() == null)
            .map(aILanguageDetectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one aILanguageDetection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AILanguageDetectionDTO> findOne(Long id) {
        LOG.debug("Request to get AILanguageDetection : {}", id);
        return aILanguageDetectionRepository.findById(id).map(aILanguageDetectionMapper::toDto);
    }

    /**
     * Delete the aILanguageDetection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AILanguageDetection : {}", id);
        aILanguageDetectionRepository.deleteById(id);
        aILanguageDetectionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the aILanguageDetection corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AILanguageDetectionDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AILanguageDetections for query {}", query);
        return aILanguageDetectionSearchRepository.search(query, pageable).map(aILanguageDetectionMapper::toDto);
    }
}
