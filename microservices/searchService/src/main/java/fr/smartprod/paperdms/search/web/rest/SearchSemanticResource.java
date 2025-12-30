package fr.smartprod.paperdms.search.web.rest;

import fr.smartprod.paperdms.search.repository.SearchSemanticRepository;
import fr.smartprod.paperdms.search.service.SearchSemanticQueryService;
import fr.smartprod.paperdms.search.service.SearchSemanticService;
import fr.smartprod.paperdms.search.service.criteria.SearchSemanticCriteria;
import fr.smartprod.paperdms.search.service.dto.SearchSemanticDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.search.domain.SearchSemantic}.
 */
@RestController
@RequestMapping("/api/search-semantics")
public class SearchSemanticResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchSemanticResource.class);

    private static final String ENTITY_NAME = "searchServiceSearchSemantic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchSemanticService searchSemanticService;

    private final SearchSemanticRepository searchSemanticRepository;

    private final SearchSemanticQueryService searchSemanticQueryService;

    public SearchSemanticResource(
        SearchSemanticService searchSemanticService,
        SearchSemanticRepository searchSemanticRepository,
        SearchSemanticQueryService searchSemanticQueryService
    ) {
        this.searchSemanticService = searchSemanticService;
        this.searchSemanticRepository = searchSemanticRepository;
        this.searchSemanticQueryService = searchSemanticQueryService;
    }

    /**
     * {@code POST  /search-semantics} : Create a new searchSemantic.
     *
     * @param searchSemanticDTO the searchSemanticDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchSemanticDTO, or with status {@code 400 (Bad Request)} if the searchSemantic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchSemanticDTO> createSearchSemantic(@Valid @RequestBody SearchSemanticDTO searchSemanticDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SearchSemantic : {}", searchSemanticDTO);
        if (searchSemanticDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchSemantic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchSemanticDTO = searchSemanticService.save(searchSemanticDTO);
        return ResponseEntity.created(new URI("/api/search-semantics/" + searchSemanticDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, searchSemanticDTO.getId().toString()))
            .body(searchSemanticDTO);
    }

    /**
     * {@code PUT  /search-semantics/:id} : Updates an existing searchSemantic.
     *
     * @param id the id of the searchSemanticDTO to save.
     * @param searchSemanticDTO the searchSemanticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchSemanticDTO,
     * or with status {@code 400 (Bad Request)} if the searchSemanticDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchSemanticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchSemanticDTO> updateSearchSemantic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchSemanticDTO searchSemanticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchSemantic : {}, {}", id, searchSemanticDTO);
        if (searchSemanticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchSemanticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchSemanticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchSemanticDTO = searchSemanticService.update(searchSemanticDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchSemanticDTO.getId().toString()))
            .body(searchSemanticDTO);
    }

    /**
     * {@code PATCH  /search-semantics/:id} : Partial updates given fields of an existing searchSemantic, field will ignore if it is null
     *
     * @param id the id of the searchSemanticDTO to save.
     * @param searchSemanticDTO the searchSemanticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchSemanticDTO,
     * or with status {@code 400 (Bad Request)} if the searchSemanticDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchSemanticDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchSemanticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchSemanticDTO> partialUpdateSearchSemantic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchSemanticDTO searchSemanticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchSemantic partially : {}, {}", id, searchSemanticDTO);
        if (searchSemanticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchSemanticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchSemanticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchSemanticDTO> result = searchSemanticService.partialUpdate(searchSemanticDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchSemanticDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-semantics} : get all the searchSemantics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchSemantics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SearchSemanticDTO>> getAllSearchSemantics(
        SearchSemanticCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SearchSemantics by criteria: {}", criteria);

        Page<SearchSemanticDTO> page = searchSemanticQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /search-semantics/count} : count all the searchSemantics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSearchSemantics(SearchSemanticCriteria criteria) {
        LOG.debug("REST request to count SearchSemantics by criteria: {}", criteria);
        return ResponseEntity.ok().body(searchSemanticQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /search-semantics/:id} : get the "id" searchSemantic.
     *
     * @param id the id of the searchSemanticDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchSemanticDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchSemanticDTO> getSearchSemantic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchSemantic : {}", id);
        Optional<SearchSemanticDTO> searchSemanticDTO = searchSemanticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchSemanticDTO);
    }

    /**
     * {@code DELETE  /search-semantics/:id} : delete the "id" searchSemantic.
     *
     * @param id the id of the searchSemanticDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchSemantic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchSemantic : {}", id);
        searchSemanticService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /search-semantics/_search?query=:query} : search for the searchSemantic corresponding
     * to the query.
     *
     * @param query the query of the searchSemantic search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SearchSemanticDTO>> searchSearchSemantics(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SearchSemantics for query {}", query);
        try {
            Page<SearchSemanticDTO> page = searchSemanticService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
