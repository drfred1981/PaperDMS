package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.repository.SearchQueryRepository;
import fr.smartprod.paperdms.search.repository.search.SearchQuerySearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchQueryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.search.domain.SearchQuery}.
 */
@Service
@Transactional
public class SearchQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryService.class);

    private final SearchQueryRepository searchQueryRepository;

    private final SearchQueryMapper searchQueryMapper;

    private final SearchQuerySearchRepository searchQuerySearchRepository;

    public SearchQueryService(
        SearchQueryRepository searchQueryRepository,
        SearchQueryMapper searchQueryMapper,
        SearchQuerySearchRepository searchQuerySearchRepository
    ) {
        this.searchQueryRepository = searchQueryRepository;
        this.searchQueryMapper = searchQueryMapper;
        this.searchQuerySearchRepository = searchQuerySearchRepository;
    }

    /**
     * Save a searchQuery.
     *
     * @param searchQueryDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchQueryDTO save(SearchQueryDTO searchQueryDTO) {
        LOG.debug("Request to save SearchQuery : {}", searchQueryDTO);
        SearchQuery searchQuery = searchQueryMapper.toEntity(searchQueryDTO);
        searchQuery = searchQueryRepository.save(searchQuery);
        searchQuerySearchRepository.index(searchQuery);
        return searchQueryMapper.toDto(searchQuery);
    }

    /**
     * Update a searchQuery.
     *
     * @param searchQueryDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchQueryDTO update(SearchQueryDTO searchQueryDTO) {
        LOG.debug("Request to update SearchQuery : {}", searchQueryDTO);
        SearchQuery searchQuery = searchQueryMapper.toEntity(searchQueryDTO);
        searchQuery = searchQueryRepository.save(searchQuery);
        searchQuerySearchRepository.index(searchQuery);
        return searchQueryMapper.toDto(searchQuery);
    }

    /**
     * Partially update a searchQuery.
     *
     * @param searchQueryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchQueryDTO> partialUpdate(SearchQueryDTO searchQueryDTO) {
        LOG.debug("Request to partially update SearchQuery : {}", searchQueryDTO);

        return searchQueryRepository
            .findById(searchQueryDTO.getId())
            .map(existingSearchQuery -> {
                searchQueryMapper.partialUpdate(existingSearchQuery, searchQueryDTO);

                return existingSearchQuery;
            })
            .map(searchQueryRepository::save)
            .map(savedSearchQuery -> {
                searchQuerySearchRepository.index(savedSearchQuery);
                return savedSearchQuery;
            })
            .map(searchQueryMapper::toDto);
    }

    /**
     * Get one searchQuery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchQueryDTO> findOne(Long id) {
        LOG.debug("Request to get SearchQuery : {}", id);
        return searchQueryRepository.findById(id).map(searchQueryMapper::toDto);
    }

    /**
     * Delete the searchQuery by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SearchQuery : {}", id);
        searchQueryRepository.deleteById(id);
        searchQuerySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the searchQuery corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchQueryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SearchQueries for query {}", query);
        return searchQuerySearchRepository.search(query, pageable).map(searchQueryMapper::toDto);
    }
}
