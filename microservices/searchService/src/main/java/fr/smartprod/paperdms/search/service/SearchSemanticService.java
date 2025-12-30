package fr.smartprod.paperdms.search.service;

import fr.smartprod.paperdms.search.domain.SearchSemantic;
import fr.smartprod.paperdms.search.repository.SearchSemanticRepository;
import fr.smartprod.paperdms.search.repository.search.SearchSemanticSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchSemanticDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchSemanticMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.search.domain.SearchSemantic}.
 */
@Service
@Transactional
public class SearchSemanticService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchSemanticService.class);

    private final SearchSemanticRepository searchSemanticRepository;

    private final SearchSemanticMapper searchSemanticMapper;

    private final SearchSemanticSearchRepository searchSemanticSearchRepository;

    public SearchSemanticService(
        SearchSemanticRepository searchSemanticRepository,
        SearchSemanticMapper searchSemanticMapper,
        SearchSemanticSearchRepository searchSemanticSearchRepository
    ) {
        this.searchSemanticRepository = searchSemanticRepository;
        this.searchSemanticMapper = searchSemanticMapper;
        this.searchSemanticSearchRepository = searchSemanticSearchRepository;
    }

    /**
     * Save a searchSemantic.
     *
     * @param searchSemanticDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchSemanticDTO save(SearchSemanticDTO searchSemanticDTO) {
        LOG.debug("Request to save SearchSemantic : {}", searchSemanticDTO);
        SearchSemantic searchSemantic = searchSemanticMapper.toEntity(searchSemanticDTO);
        searchSemantic = searchSemanticRepository.save(searchSemantic);
        searchSemanticSearchRepository.index(searchSemantic);
        return searchSemanticMapper.toDto(searchSemantic);
    }

    /**
     * Update a searchSemantic.
     *
     * @param searchSemanticDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchSemanticDTO update(SearchSemanticDTO searchSemanticDTO) {
        LOG.debug("Request to update SearchSemantic : {}", searchSemanticDTO);
        SearchSemantic searchSemantic = searchSemanticMapper.toEntity(searchSemanticDTO);
        searchSemantic = searchSemanticRepository.save(searchSemantic);
        searchSemanticSearchRepository.index(searchSemantic);
        return searchSemanticMapper.toDto(searchSemantic);
    }

    /**
     * Partially update a searchSemantic.
     *
     * @param searchSemanticDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchSemanticDTO> partialUpdate(SearchSemanticDTO searchSemanticDTO) {
        LOG.debug("Request to partially update SearchSemantic : {}", searchSemanticDTO);

        return searchSemanticRepository
            .findById(searchSemanticDTO.getId())
            .map(existingSearchSemantic -> {
                searchSemanticMapper.partialUpdate(existingSearchSemantic, searchSemanticDTO);

                return existingSearchSemantic;
            })
            .map(searchSemanticRepository::save)
            .map(savedSearchSemantic -> {
                searchSemanticSearchRepository.index(savedSearchSemantic);
                return savedSearchSemantic;
            })
            .map(searchSemanticMapper::toDto);
    }

    /**
     * Get one searchSemantic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchSemanticDTO> findOne(Long id) {
        LOG.debug("Request to get SearchSemantic : {}", id);
        return searchSemanticRepository.findById(id).map(searchSemanticMapper::toDto);
    }

    /**
     * Delete the searchSemantic by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SearchSemantic : {}", id);
        searchSemanticRepository.deleteById(id);
        searchSemanticSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the searchSemantic corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchSemanticDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SearchSemantics for query {}", query);
        return searchSemanticSearchRepository.search(query, pageable).map(searchSemanticMapper::toDto);
    }
}
