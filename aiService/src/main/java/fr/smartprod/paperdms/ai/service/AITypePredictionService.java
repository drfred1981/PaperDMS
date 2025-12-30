package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.repository.AITypePredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AITypePredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AITypePredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AITypePredictionMapper;
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
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AITypePrediction}.
 */
@Service
@Transactional
public class AITypePredictionService {

    private static final Logger LOG = LoggerFactory.getLogger(AITypePredictionService.class);

    private final AITypePredictionRepository aITypePredictionRepository;

    private final AITypePredictionMapper aITypePredictionMapper;

    private final AITypePredictionSearchRepository aITypePredictionSearchRepository;

    public AITypePredictionService(
        AITypePredictionRepository aITypePredictionRepository,
        AITypePredictionMapper aITypePredictionMapper,
        AITypePredictionSearchRepository aITypePredictionSearchRepository
    ) {
        this.aITypePredictionRepository = aITypePredictionRepository;
        this.aITypePredictionMapper = aITypePredictionMapper;
        this.aITypePredictionSearchRepository = aITypePredictionSearchRepository;
    }

    /**
     * Save a aITypePrediction.
     *
     * @param aITypePredictionDTO the entity to save.
     * @return the persisted entity.
     */
    public AITypePredictionDTO save(AITypePredictionDTO aITypePredictionDTO) {
        LOG.debug("Request to save AITypePrediction : {}", aITypePredictionDTO);
        AITypePrediction aITypePrediction = aITypePredictionMapper.toEntity(aITypePredictionDTO);
        aITypePrediction = aITypePredictionRepository.save(aITypePrediction);
        aITypePredictionSearchRepository.index(aITypePrediction);
        return aITypePredictionMapper.toDto(aITypePrediction);
    }

    /**
     * Update a aITypePrediction.
     *
     * @param aITypePredictionDTO the entity to save.
     * @return the persisted entity.
     */
    public AITypePredictionDTO update(AITypePredictionDTO aITypePredictionDTO) {
        LOG.debug("Request to update AITypePrediction : {}", aITypePredictionDTO);
        AITypePrediction aITypePrediction = aITypePredictionMapper.toEntity(aITypePredictionDTO);
        aITypePrediction = aITypePredictionRepository.save(aITypePrediction);
        aITypePredictionSearchRepository.index(aITypePrediction);
        return aITypePredictionMapper.toDto(aITypePrediction);
    }

    /**
     * Partially update a aITypePrediction.
     *
     * @param aITypePredictionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AITypePredictionDTO> partialUpdate(AITypePredictionDTO aITypePredictionDTO) {
        LOG.debug("Request to partially update AITypePrediction : {}", aITypePredictionDTO);

        return aITypePredictionRepository
            .findById(aITypePredictionDTO.getId())
            .map(existingAITypePrediction -> {
                aITypePredictionMapper.partialUpdate(existingAITypePrediction, aITypePredictionDTO);

                return existingAITypePrediction;
            })
            .map(aITypePredictionRepository::save)
            .map(savedAITypePrediction -> {
                aITypePredictionSearchRepository.index(savedAITypePrediction);
                return savedAITypePrediction;
            })
            .map(aITypePredictionMapper::toDto);
    }

    /**
     *  Get all the aITypePredictions where Job is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AITypePredictionDTO> findAllWhereJobIsNull() {
        LOG.debug("Request to get all aITypePredictions where Job is null");
        return StreamSupport.stream(aITypePredictionRepository.findAll().spliterator(), false)
            .filter(aITypePrediction -> aITypePrediction.getJob() == null)
            .map(aITypePredictionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one aITypePrediction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AITypePredictionDTO> findOne(Long id) {
        LOG.debug("Request to get AITypePrediction : {}", id);
        return aITypePredictionRepository.findById(id).map(aITypePredictionMapper::toDto);
    }

    /**
     * Delete the aITypePrediction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AITypePrediction : {}", id);
        aITypePredictionRepository.deleteById(id);
        aITypePredictionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the aITypePrediction corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AITypePredictionDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AITypePredictions for query {}", query);
        return aITypePredictionSearchRepository.search(query, pageable).map(aITypePredictionMapper::toDto);
    }
}
