package fr.smartprod.paperdms.search.web.rest;

import fr.smartprod.paperdms.search.repository.SearchIndexRepository;
import fr.smartprod.paperdms.search.service.SearchIndexService;
import fr.smartprod.paperdms.search.service.dto.SearchIndexDTO;
import fr.smartprod.paperdms.search.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.search.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.search.domain.SearchIndex}.
 */
@RestController
@RequestMapping("/api/search-indices")
public class SearchIndexResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchIndexResource.class);

    private static final String ENTITY_NAME = "searchServiceSearchIndex";

    @Value("${jhipster.clientApp.name:searchService}")
    private String applicationName;

    private final SearchIndexService searchIndexService;

    private final SearchIndexRepository searchIndexRepository;

    public SearchIndexResource(SearchIndexService searchIndexService, SearchIndexRepository searchIndexRepository) {
        this.searchIndexService = searchIndexService;
        this.searchIndexRepository = searchIndexRepository;
    }

    /**
     * {@code POST  /search-indices} : Create a new searchIndex.
     *
     * @param searchIndexDTO the searchIndexDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchIndexDTO, or with status {@code 400 (Bad Request)} if the searchIndex has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchIndexDTO> createSearchIndex(@Valid @RequestBody SearchIndexDTO searchIndexDTO) throws URISyntaxException {
        LOG.debug("REST request to save SearchIndex : {}", searchIndexDTO);
        if (searchIndexDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchIndex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchIndexDTO = searchIndexService.save(searchIndexDTO);
        return ResponseEntity.created(new URI("/api/search-indices/" + searchIndexDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, searchIndexDTO.getId().toString()))
            .body(searchIndexDTO);
    }

    /**
     * {@code PUT  /search-indices/:id} : Updates an existing searchIndex.
     *
     * @param id the id of the searchIndexDTO to save.
     * @param searchIndexDTO the searchIndexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchIndexDTO,
     * or with status {@code 400 (Bad Request)} if the searchIndexDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchIndexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchIndexDTO> updateSearchIndex(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchIndexDTO searchIndexDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchIndex : {}, {}", id, searchIndexDTO);
        if (searchIndexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchIndexDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchIndexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchIndexDTO = searchIndexService.update(searchIndexDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchIndexDTO.getId().toString()))
            .body(searchIndexDTO);
    }

    /**
     * {@code PATCH  /search-indices/:id} : Partial updates given fields of an existing searchIndex, field will ignore if it is null
     *
     * @param id the id of the searchIndexDTO to save.
     * @param searchIndexDTO the searchIndexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchIndexDTO,
     * or with status {@code 400 (Bad Request)} if the searchIndexDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchIndexDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchIndexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchIndexDTO> partialUpdateSearchIndex(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchIndexDTO searchIndexDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchIndex partially : {}, {}", id, searchIndexDTO);
        if (searchIndexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchIndexDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchIndexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchIndexDTO> result = searchIndexService.partialUpdate(searchIndexDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchIndexDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-indices} : get all the searchIndices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchIndices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SearchIndexDTO>> getAllSearchIndices(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SearchIndices");
        Page<SearchIndexDTO> page = searchIndexService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /search-indices/:id} : get the "id" searchIndex.
     *
     * @param id the id of the searchIndexDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchIndexDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchIndexDTO> getSearchIndex(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchIndex : {}", id);
        Optional<SearchIndexDTO> searchIndexDTO = searchIndexService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchIndexDTO);
    }

    /**
     * {@code DELETE  /search-indices/:id} : delete the "id" searchIndex.
     *
     * @param id the id of the searchIndexDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchIndex(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchIndex : {}", id);
        searchIndexService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /search-indices/_search?query=:query} : search for the searchIndex corresponding
     * to the query.
     *
     * @param query the query of the searchIndex search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SearchIndexDTO>> searchSearchIndices(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SearchIndices for query {}", query);
        try {
            Page<SearchIndexDTO> page = searchIndexService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
