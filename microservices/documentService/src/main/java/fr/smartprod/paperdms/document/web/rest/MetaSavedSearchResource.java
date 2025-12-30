package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaSavedSearchRepository;
import fr.smartprod.paperdms.document.service.MetaSavedSearchQueryService;
import fr.smartprod.paperdms.document.service.MetaSavedSearchService;
import fr.smartprod.paperdms.document.service.criteria.MetaSavedSearchCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaSavedSearchDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.document.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaSavedSearch}.
 */
@RestController
@RequestMapping("/api/meta-saved-searches")
public class MetaSavedSearchResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaSavedSearchResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaSavedSearch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaSavedSearchService metaSavedSearchService;

    private final MetaSavedSearchRepository metaSavedSearchRepository;

    private final MetaSavedSearchQueryService metaSavedSearchQueryService;

    public MetaSavedSearchResource(
        MetaSavedSearchService metaSavedSearchService,
        MetaSavedSearchRepository metaSavedSearchRepository,
        MetaSavedSearchQueryService metaSavedSearchQueryService
    ) {
        this.metaSavedSearchService = metaSavedSearchService;
        this.metaSavedSearchRepository = metaSavedSearchRepository;
        this.metaSavedSearchQueryService = metaSavedSearchQueryService;
    }

    /**
     * {@code POST  /meta-saved-searches} : Create a new metaSavedSearch.
     *
     * @param metaSavedSearchDTO the metaSavedSearchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaSavedSearchDTO, or with status {@code 400 (Bad Request)} if the metaSavedSearch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaSavedSearchDTO> createMetaSavedSearch(@Valid @RequestBody MetaSavedSearchDTO metaSavedSearchDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MetaSavedSearch : {}", metaSavedSearchDTO);
        if (metaSavedSearchDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaSavedSearch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaSavedSearchDTO = metaSavedSearchService.save(metaSavedSearchDTO);
        return ResponseEntity.created(new URI("/api/meta-saved-searches/" + metaSavedSearchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaSavedSearchDTO.getId().toString()))
            .body(metaSavedSearchDTO);
    }

    /**
     * {@code PUT  /meta-saved-searches/:id} : Updates an existing metaSavedSearch.
     *
     * @param id the id of the metaSavedSearchDTO to save.
     * @param metaSavedSearchDTO the metaSavedSearchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaSavedSearchDTO,
     * or with status {@code 400 (Bad Request)} if the metaSavedSearchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaSavedSearchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaSavedSearchDTO> updateMetaSavedSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaSavedSearchDTO metaSavedSearchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaSavedSearch : {}, {}", id, metaSavedSearchDTO);
        if (metaSavedSearchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaSavedSearchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaSavedSearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaSavedSearchDTO = metaSavedSearchService.update(metaSavedSearchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaSavedSearchDTO.getId().toString()))
            .body(metaSavedSearchDTO);
    }

    /**
     * {@code PATCH  /meta-saved-searches/:id} : Partial updates given fields of an existing metaSavedSearch, field will ignore if it is null
     *
     * @param id the id of the metaSavedSearchDTO to save.
     * @param metaSavedSearchDTO the metaSavedSearchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaSavedSearchDTO,
     * or with status {@code 400 (Bad Request)} if the metaSavedSearchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaSavedSearchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaSavedSearchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaSavedSearchDTO> partialUpdateMetaSavedSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaSavedSearchDTO metaSavedSearchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaSavedSearch partially : {}, {}", id, metaSavedSearchDTO);
        if (metaSavedSearchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaSavedSearchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaSavedSearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaSavedSearchDTO> result = metaSavedSearchService.partialUpdate(metaSavedSearchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaSavedSearchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-saved-searches} : get all the metaSavedSearches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaSavedSearches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaSavedSearchDTO>> getAllMetaSavedSearches(
        MetaSavedSearchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaSavedSearches by criteria: {}", criteria);

        Page<MetaSavedSearchDTO> page = metaSavedSearchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-saved-searches/count} : count all the metaSavedSearches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaSavedSearches(MetaSavedSearchCriteria criteria) {
        LOG.debug("REST request to count MetaSavedSearches by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaSavedSearchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-saved-searches/:id} : get the "id" metaSavedSearch.
     *
     * @param id the id of the metaSavedSearchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaSavedSearchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaSavedSearchDTO> getMetaSavedSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaSavedSearch : {}", id);
        Optional<MetaSavedSearchDTO> metaSavedSearchDTO = metaSavedSearchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaSavedSearchDTO);
    }

    /**
     * {@code DELETE  /meta-saved-searches/:id} : delete the "id" metaSavedSearch.
     *
     * @param id the id of the metaSavedSearchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaSavedSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaSavedSearch : {}", id);
        metaSavedSearchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-saved-searches/_search?query=:query} : search for the metaSavedSearch corresponding
     * to the query.
     *
     * @param query the query of the metaSavedSearch search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaSavedSearchDTO>> searchMetaSavedSearches(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaSavedSearches for query {}", query);
        try {
            Page<MetaSavedSearchDTO> page = metaSavedSearchService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
