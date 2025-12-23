package fr.smartprod.paperdms.search.web.rest;

import fr.smartprod.paperdms.search.repository.SearchQueryRepository;
import fr.smartprod.paperdms.search.service.SearchQueryQueryService;
import fr.smartprod.paperdms.search.service.SearchQueryService;
import fr.smartprod.paperdms.search.service.criteria.SearchQueryCriteria;
import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.search.domain.SearchQuery}.
 */
@RestController
@RequestMapping("/api/search-queries")
public class SearchQueryResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryResource.class);

    private static final String ENTITY_NAME = "searchServiceSearchQuery";

    @Value("${jhipster.clientApp.name:searchService}")
    private String applicationName;

    private final SearchQueryService searchQueryService;

    private final SearchQueryRepository searchQueryRepository;

    private final SearchQueryQueryService searchQueryQueryService;

    public SearchQueryResource(
        SearchQueryService searchQueryService,
        SearchQueryRepository searchQueryRepository,
        SearchQueryQueryService searchQueryQueryService
    ) {
        this.searchQueryService = searchQueryService;
        this.searchQueryRepository = searchQueryRepository;
        this.searchQueryQueryService = searchQueryQueryService;
    }

    /**
     * {@code POST  /search-queries} : Create a new searchQuery.
     *
     * @param searchQueryDTO the searchQueryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchQueryDTO, or with status {@code 400 (Bad Request)} if the searchQuery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchQueryDTO> createSearchQuery(@Valid @RequestBody SearchQueryDTO searchQueryDTO) throws URISyntaxException {
        LOG.debug("REST request to save SearchQuery : {}", searchQueryDTO);
        if (searchQueryDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchQuery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchQueryDTO = searchQueryService.save(searchQueryDTO);
        return ResponseEntity.created(new URI("/api/search-queries/" + searchQueryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, searchQueryDTO.getId().toString()))
            .body(searchQueryDTO);
    }

    /**
     * {@code PUT  /search-queries/:id} : Updates an existing searchQuery.
     *
     * @param id the id of the searchQueryDTO to save.
     * @param searchQueryDTO the searchQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchQueryDTO,
     * or with status {@code 400 (Bad Request)} if the searchQueryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchQueryDTO> updateSearchQuery(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchQueryDTO searchQueryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchQuery : {}, {}", id, searchQueryDTO);
        if (searchQueryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchQueryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchQueryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchQueryDTO = searchQueryService.update(searchQueryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchQueryDTO.getId().toString()))
            .body(searchQueryDTO);
    }

    /**
     * {@code PATCH  /search-queries/:id} : Partial updates given fields of an existing searchQuery, field will ignore if it is null
     *
     * @param id the id of the searchQueryDTO to save.
     * @param searchQueryDTO the searchQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchQueryDTO,
     * or with status {@code 400 (Bad Request)} if the searchQueryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchQueryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchQueryDTO> partialUpdateSearchQuery(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchQueryDTO searchQueryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchQuery partially : {}, {}", id, searchQueryDTO);
        if (searchQueryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchQueryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchQueryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchQueryDTO> result = searchQueryService.partialUpdate(searchQueryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchQueryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-queries} : get all the searchQueries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchQueries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SearchQueryDTO>> getAllSearchQueries(
        SearchQueryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SearchQueries by criteria: {}", criteria);

        Page<SearchQueryDTO> page = searchQueryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /search-queries/count} : count all the searchQueries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSearchQueries(SearchQueryCriteria criteria) {
        LOG.debug("REST request to count SearchQueries by criteria: {}", criteria);
        return ResponseEntity.ok().body(searchQueryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /search-queries/:id} : get the "id" searchQuery.
     *
     * @param id the id of the searchQueryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchQueryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchQueryDTO> getSearchQuery(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchQuery : {}", id);
        Optional<SearchQueryDTO> searchQueryDTO = searchQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchQueryDTO);
    }

    /**
     * {@code DELETE  /search-queries/:id} : delete the "id" searchQuery.
     *
     * @param id the id of the searchQueryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchQuery(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchQuery : {}", id);
        searchQueryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
