package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
import fr.smartprod.paperdms.search.repository.search.SearchFacetSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchFacetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.search.domain.SearchFacet}.
 */
@Service
@Transactional
public class SearchFacetService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchFacetService.class);

    private final SearchFacetRepository searchFacetRepository;

    private final SearchFacetMapper searchFacetMapper;

    private final SearchFacetSearchRepository searchFacetSearchRepository;

    public SearchFacetService(
        SearchFacetRepository searchFacetRepository,
        SearchFacetMapper searchFacetMapper,
        SearchFacetSearchRepository searchFacetSearchRepository
    ) {
        this.searchFacetRepository = searchFacetRepository;
        this.searchFacetMapper = searchFacetMapper;
        this.searchFacetSearchRepository = searchFacetSearchRepository;
    }

    /**
     * Save a searchFacet.
     *
     * @param searchFacetDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchFacetDTO save(SearchFacetDTO searchFacetDTO) {
        LOG.debug("Request to save SearchFacet : {}", searchFacetDTO);
        SearchFacet searchFacet = searchFacetMapper.toEntity(searchFacetDTO);
        searchFacet = searchFacetRepository.save(searchFacet);
        searchFacetSearchRepository.index(searchFacet);
        return searchFacetMapper.toDto(searchFacet);
    }

    /**
     * Update a searchFacet.
     *
     * @param searchFacetDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchFacetDTO update(SearchFacetDTO searchFacetDTO) {
        LOG.debug("Request to update SearchFacet : {}", searchFacetDTO);
        SearchFacet searchFacet = searchFacetMapper.toEntity(searchFacetDTO);
        searchFacet = searchFacetRepository.save(searchFacet);
        searchFacetSearchRepository.index(searchFacet);
        return searchFacetMapper.toDto(searchFacet);
    }

    /**
     * Partially update a searchFacet.
     *
     * @param searchFacetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchFacetDTO> partialUpdate(SearchFacetDTO searchFacetDTO) {
        LOG.debug("Request to partially update SearchFacet : {}", searchFacetDTO);

        return searchFacetRepository
            .findById(searchFacetDTO.getId())
            .map(existingSearchFacet -> {
                searchFacetMapper.partialUpdate(existingSearchFacet, searchFacetDTO);

                return existingSearchFacet;
            })
            .map(searchFacetRepository::save)
            .map(savedSearchFacet -> {
                searchFacetSearchRepository.index(savedSearchFacet);
                return savedSearchFacet;
            })
            .map(searchFacetMapper::toDto);
    }

    /**
     * Get one searchFacet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchFacetDTO> findOne(Long id) {
        LOG.debug("Request to get SearchFacet : {}", id);
        return searchFacetRepository.findById(id).map(searchFacetMapper::toDto);
    }

    /**
     * Delete the searchFacet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SearchFacet : {}", id);
        searchFacetRepository.deleteById(id);
        searchFacetSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the searchFacet corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchFacetDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SearchFacets for query {}", query);
        return searchFacetSearchRepository.search(query, pageable).map(searchFacetMapper::toDto);
    }
}
