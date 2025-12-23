package fr.smartprod.paperdms.search.service.impl;

import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
import fr.smartprod.paperdms.search.service.SearchFacetService;
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
public class SearchFacetServiceImpl implements SearchFacetService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchFacetServiceImpl.class);

    private final SearchFacetRepository searchFacetRepository;

    private final SearchFacetMapper searchFacetMapper;

    public SearchFacetServiceImpl(SearchFacetRepository searchFacetRepository, SearchFacetMapper searchFacetMapper) {
        this.searchFacetRepository = searchFacetRepository;
        this.searchFacetMapper = searchFacetMapper;
    }

    @Override
    public SearchFacetDTO save(SearchFacetDTO searchFacetDTO) {
        LOG.debug("Request to save SearchFacet : {}", searchFacetDTO);
        SearchFacet searchFacet = searchFacetMapper.toEntity(searchFacetDTO);
        searchFacet = searchFacetRepository.save(searchFacet);
        return searchFacetMapper.toDto(searchFacet);
    }

    @Override
    public SearchFacetDTO update(SearchFacetDTO searchFacetDTO) {
        LOG.debug("Request to update SearchFacet : {}", searchFacetDTO);
        SearchFacet searchFacet = searchFacetMapper.toEntity(searchFacetDTO);
        searchFacet = searchFacetRepository.save(searchFacet);
        return searchFacetMapper.toDto(searchFacet);
    }

    @Override
    public Optional<SearchFacetDTO> partialUpdate(SearchFacetDTO searchFacetDTO) {
        LOG.debug("Request to partially update SearchFacet : {}", searchFacetDTO);

        return searchFacetRepository
            .findById(searchFacetDTO.getId())
            .map(existingSearchFacet -> {
                searchFacetMapper.partialUpdate(existingSearchFacet, searchFacetDTO);

                return existingSearchFacet;
            })
            .map(searchFacetRepository::save)
            .map(searchFacetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SearchFacetDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SearchFacets");
        return searchFacetRepository.findAll(pageable).map(searchFacetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchFacetDTO> findOne(Long id) {
        LOG.debug("Request to get SearchFacet : {}", id);
        return searchFacetRepository.findById(id).map(searchFacetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SearchFacet : {}", id);
        searchFacetRepository.deleteById(id);
    }
}
