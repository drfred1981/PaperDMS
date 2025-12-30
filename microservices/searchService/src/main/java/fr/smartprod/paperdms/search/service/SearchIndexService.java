package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.SearchIndex;
import fr.smartprod.paperdms.search.repository.SearchIndexRepository;
import fr.smartprod.paperdms.search.repository.search.SearchIndexSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchIndexDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchIndexMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.search.domain.SearchIndex}.
 */
@Service
@Transactional
public class SearchIndexService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchIndexService.class);

    private final SearchIndexRepository searchIndexRepository;

    private final SearchIndexMapper searchIndexMapper;

    private final SearchIndexSearchRepository searchIndexSearchRepository;

    public SearchIndexService(
        SearchIndexRepository searchIndexRepository,
        SearchIndexMapper searchIndexMapper,
        SearchIndexSearchRepository searchIndexSearchRepository
    ) {
        this.searchIndexRepository = searchIndexRepository;
        this.searchIndexMapper = searchIndexMapper;
        this.searchIndexSearchRepository = searchIndexSearchRepository;
    }

    /**
     * Save a searchIndex.
     *
     * @param searchIndexDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchIndexDTO save(SearchIndexDTO searchIndexDTO) {
        LOG.debug("Request to save SearchIndex : {}", searchIndexDTO);
        SearchIndex searchIndex = searchIndexMapper.toEntity(searchIndexDTO);
        searchIndex = searchIndexRepository.save(searchIndex);
        searchIndexSearchRepository.index(searchIndex);
        return searchIndexMapper.toDto(searchIndex);
    }

    /**
     * Update a searchIndex.
     *
     * @param searchIndexDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchIndexDTO update(SearchIndexDTO searchIndexDTO) {
        LOG.debug("Request to update SearchIndex : {}", searchIndexDTO);
        SearchIndex searchIndex = searchIndexMapper.toEntity(searchIndexDTO);
        searchIndex = searchIndexRepository.save(searchIndex);
        searchIndexSearchRepository.index(searchIndex);
        return searchIndexMapper.toDto(searchIndex);
    }

    /**
     * Partially update a searchIndex.
     *
     * @param searchIndexDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchIndexDTO> partialUpdate(SearchIndexDTO searchIndexDTO) {
        LOG.debug("Request to partially update SearchIndex : {}", searchIndexDTO);

        return searchIndexRepository
            .findById(searchIndexDTO.getId())
            .map(existingSearchIndex -> {
                searchIndexMapper.partialUpdate(existingSearchIndex, searchIndexDTO);

                return existingSearchIndex;
            })
            .map(searchIndexRepository::save)
            .map(savedSearchIndex -> {
                searchIndexSearchRepository.index(savedSearchIndex);
                return savedSearchIndex;
            })
            .map(searchIndexMapper::toDto);
    }

    /**
     * Get one searchIndex by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchIndexDTO> findOne(Long id) {
        LOG.debug("Request to get SearchIndex : {}", id);
        return searchIndexRepository.findById(id).map(searchIndexMapper::toDto);
    }

    /**
     * Delete the searchIndex by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SearchIndex : {}", id);
        searchIndexRepository.deleteById(id);
        searchIndexSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the searchIndex corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchIndexDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SearchIndices for query {}", query);
        return searchIndexSearchRepository.search(query, pageable).map(searchIndexMapper::toDto);
    }
}
