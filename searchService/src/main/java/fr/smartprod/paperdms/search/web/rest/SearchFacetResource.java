package fr.smartprod.paperdms.search.web.rest;

import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
import fr.smartprod.paperdms.search.service.SearchFacetService;
import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import fr.smartprod.paperdms.search.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.smartprod.paperdms.search.domain.SearchFacet}.
 */
@RestController
@RequestMapping("/api/search-facets")
public class SearchFacetResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchFacetResource.class);

    private static final String ENTITY_NAME = "searchServiceSearchFacet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchFacetService searchFacetService;

    private final SearchFacetRepository searchFacetRepository;

    public SearchFacetResource(SearchFacetService searchFacetService, SearchFacetRepository searchFacetRepository) {
        this.searchFacetService = searchFacetService;
        this.searchFacetRepository = searchFacetRepository;
    }

    /**
     * {@code POST  /search-facets} : Create a new searchFacet.
     *
     * @param searchFacetDTO the searchFacetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchFacetDTO, or with status {@code 400 (Bad Request)} if the searchFacet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchFacetDTO> createSearchFacet(@Valid @RequestBody SearchFacetDTO searchFacetDTO) throws URISyntaxException {
        LOG.debug("REST request to save SearchFacet : {}", searchFacetDTO);
        if (searchFacetDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchFacet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchFacetDTO = searchFacetService.save(searchFacetDTO);
        return ResponseEntity.created(new URI("/api/search-facets/" + searchFacetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, searchFacetDTO.getId().toString()))
            .body(searchFacetDTO);
    }

    /**
     * {@code PUT  /search-facets/:id} : Updates an existing searchFacet.
     *
     * @param id the id of the searchFacetDTO to save.
     * @param searchFacetDTO the searchFacetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchFacetDTO,
     * or with status {@code 400 (Bad Request)} if the searchFacetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchFacetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchFacetDTO> updateSearchFacet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchFacetDTO searchFacetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchFacet : {}, {}", id, searchFacetDTO);
        if (searchFacetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchFacetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchFacetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchFacetDTO = searchFacetService.update(searchFacetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchFacetDTO.getId().toString()))
            .body(searchFacetDTO);
    }

    /**
     * {@code PATCH  /search-facets/:id} : Partial updates given fields of an existing searchFacet, field will ignore if it is null
     *
     * @param id the id of the searchFacetDTO to save.
     * @param searchFacetDTO the searchFacetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchFacetDTO,
     * or with status {@code 400 (Bad Request)} if the searchFacetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchFacetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchFacetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchFacetDTO> partialUpdateSearchFacet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchFacetDTO searchFacetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchFacet partially : {}, {}", id, searchFacetDTO);
        if (searchFacetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchFacetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchFacetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchFacetDTO> result = searchFacetService.partialUpdate(searchFacetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchFacetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-facets} : get all the searchFacets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchFacets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SearchFacetDTO>> getAllSearchFacets(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SearchFacets");
        Page<SearchFacetDTO> page = searchFacetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /search-facets/:id} : get the "id" searchFacet.
     *
     * @param id the id of the searchFacetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchFacetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchFacetDTO> getSearchFacet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchFacet : {}", id);
        Optional<SearchFacetDTO> searchFacetDTO = searchFacetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchFacetDTO);
    }

    /**
     * {@code DELETE  /search-facets/:id} : delete the "id" searchFacet.
     *
     * @param id the id of the searchFacetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchFacet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchFacet : {}", id);
        searchFacetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
