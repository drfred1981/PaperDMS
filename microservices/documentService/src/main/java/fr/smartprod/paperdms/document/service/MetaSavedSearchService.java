package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.MetaSavedSearch;
import fr.smartprod.paperdms.document.repository.MetaSavedSearchRepository;
import fr.smartprod.paperdms.document.repository.search.MetaSavedSearchSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaSavedSearchDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaSavedSearchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.MetaSavedSearch}.
 */
@Service
@Transactional
public class MetaSavedSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaSavedSearchService.class);

    private final MetaSavedSearchRepository metaSavedSearchRepository;

    private final MetaSavedSearchMapper metaSavedSearchMapper;

    private final MetaSavedSearchSearchRepository metaSavedSearchSearchRepository;

    public MetaSavedSearchService(
        MetaSavedSearchRepository metaSavedSearchRepository,
        MetaSavedSearchMapper metaSavedSearchMapper,
        MetaSavedSearchSearchRepository metaSavedSearchSearchRepository
    ) {
        this.metaSavedSearchRepository = metaSavedSearchRepository;
        this.metaSavedSearchMapper = metaSavedSearchMapper;
        this.metaSavedSearchSearchRepository = metaSavedSearchSearchRepository;
    }

    /**
     * Save a metaSavedSearch.
     *
     * @param metaSavedSearchDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaSavedSearchDTO save(MetaSavedSearchDTO metaSavedSearchDTO) {
        LOG.debug("Request to save MetaSavedSearch : {}", metaSavedSearchDTO);
        MetaSavedSearch metaSavedSearch = metaSavedSearchMapper.toEntity(metaSavedSearchDTO);
        metaSavedSearch = metaSavedSearchRepository.save(metaSavedSearch);
        metaSavedSearchSearchRepository.index(metaSavedSearch);
        return metaSavedSearchMapper.toDto(metaSavedSearch);
    }

    /**
     * Update a metaSavedSearch.
     *
     * @param metaSavedSearchDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaSavedSearchDTO update(MetaSavedSearchDTO metaSavedSearchDTO) {
        LOG.debug("Request to update MetaSavedSearch : {}", metaSavedSearchDTO);
        MetaSavedSearch metaSavedSearch = metaSavedSearchMapper.toEntity(metaSavedSearchDTO);
        metaSavedSearch = metaSavedSearchRepository.save(metaSavedSearch);
        metaSavedSearchSearchRepository.index(metaSavedSearch);
        return metaSavedSearchMapper.toDto(metaSavedSearch);
    }

    /**
     * Partially update a metaSavedSearch.
     *
     * @param metaSavedSearchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaSavedSearchDTO> partialUpdate(MetaSavedSearchDTO metaSavedSearchDTO) {
        LOG.debug("Request to partially update MetaSavedSearch : {}", metaSavedSearchDTO);

        return metaSavedSearchRepository
            .findById(metaSavedSearchDTO.getId())
            .map(existingMetaSavedSearch -> {
                metaSavedSearchMapper.partialUpdate(existingMetaSavedSearch, metaSavedSearchDTO);

                return existingMetaSavedSearch;
            })
            .map(metaSavedSearchRepository::save)
            .map(savedMetaSavedSearch -> {
                metaSavedSearchSearchRepository.index(savedMetaSavedSearch);
                return savedMetaSavedSearch;
            })
            .map(metaSavedSearchMapper::toDto);
    }

    /**
     * Get one metaSavedSearch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaSavedSearchDTO> findOne(Long id) {
        LOG.debug("Request to get MetaSavedSearch : {}", id);
        return metaSavedSearchRepository.findById(id).map(metaSavedSearchMapper::toDto);
    }

    /**
     * Delete the metaSavedSearch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MetaSavedSearch : {}", id);
        metaSavedSearchRepository.deleteById(id);
        metaSavedSearchSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the metaSavedSearch corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaSavedSearchDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of MetaSavedSearches for query {}", query);
        return metaSavedSearchSearchRepository.search(query, pageable).map(metaSavedSearchMapper::toDto);
    }
}
