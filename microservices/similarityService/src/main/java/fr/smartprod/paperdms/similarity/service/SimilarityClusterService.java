package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import fr.smartprod.paperdms.similarity.repository.SimilarityClusterRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityClusterSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityClusterDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityClusterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityCluster}.
 */
@Service
@Transactional
public class SimilarityClusterService {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityClusterService.class);

    private final SimilarityClusterRepository similarityClusterRepository;

    private final SimilarityClusterMapper similarityClusterMapper;

    private final SimilarityClusterSearchRepository similarityClusterSearchRepository;

    public SimilarityClusterService(
        SimilarityClusterRepository similarityClusterRepository,
        SimilarityClusterMapper similarityClusterMapper,
        SimilarityClusterSearchRepository similarityClusterSearchRepository
    ) {
        this.similarityClusterRepository = similarityClusterRepository;
        this.similarityClusterMapper = similarityClusterMapper;
        this.similarityClusterSearchRepository = similarityClusterSearchRepository;
    }

    /**
     * Save a similarityCluster.
     *
     * @param similarityClusterDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityClusterDTO save(SimilarityClusterDTO similarityClusterDTO) {
        LOG.debug("Request to save SimilarityCluster : {}", similarityClusterDTO);
        SimilarityCluster similarityCluster = similarityClusterMapper.toEntity(similarityClusterDTO);
        similarityCluster = similarityClusterRepository.save(similarityCluster);
        similarityClusterSearchRepository.index(similarityCluster);
        return similarityClusterMapper.toDto(similarityCluster);
    }

    /**
     * Update a similarityCluster.
     *
     * @param similarityClusterDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityClusterDTO update(SimilarityClusterDTO similarityClusterDTO) {
        LOG.debug("Request to update SimilarityCluster : {}", similarityClusterDTO);
        SimilarityCluster similarityCluster = similarityClusterMapper.toEntity(similarityClusterDTO);
        similarityCluster = similarityClusterRepository.save(similarityCluster);
        similarityClusterSearchRepository.index(similarityCluster);
        return similarityClusterMapper.toDto(similarityCluster);
    }

    /**
     * Partially update a similarityCluster.
     *
     * @param similarityClusterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SimilarityClusterDTO> partialUpdate(SimilarityClusterDTO similarityClusterDTO) {
        LOG.debug("Request to partially update SimilarityCluster : {}", similarityClusterDTO);

        return similarityClusterRepository
            .findById(similarityClusterDTO.getId())
            .map(existingSimilarityCluster -> {
                similarityClusterMapper.partialUpdate(existingSimilarityCluster, similarityClusterDTO);

                return existingSimilarityCluster;
            })
            .map(similarityClusterRepository::save)
            .map(savedSimilarityCluster -> {
                similarityClusterSearchRepository.index(savedSimilarityCluster);
                return savedSimilarityCluster;
            })
            .map(similarityClusterMapper::toDto);
    }

    /**
     * Get one similarityCluster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SimilarityClusterDTO> findOne(Long id) {
        LOG.debug("Request to get SimilarityCluster : {}", id);
        return similarityClusterRepository.findById(id).map(similarityClusterMapper::toDto);
    }

    /**
     * Delete the similarityCluster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SimilarityCluster : {}", id);
        similarityClusterRepository.deleteById(id);
        similarityClusterSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the similarityCluster corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityClusterDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SimilarityClusters for query {}", query);
        return similarityClusterSearchRepository.search(query, pageable).map(similarityClusterMapper::toDto);
    }
}
