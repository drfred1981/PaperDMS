package fr.smartprod.paperdms.search.web.rest;

import fr.smartprod.paperdms.search.repository.SemanticSearchRepository;
import fr.smartprod.paperdms.search.service.SemanticSearchQueryService;
import fr.smartprod.paperdms.search.service.SemanticSearchService;
import fr.smartprod.paperdms.search.service.criteria.SemanticSearchCriteria;
import fr.smartprod.paperdms.search.service.dto.SemanticSearchDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.search.domain.SemanticSearch}.
 */
@RestController
@RequestMapping("/api/semantic-searches")
public class SemanticSearchResource {

    private static final Logger LOG = LoggerFactory.getLogger(SemanticSearchResource.class);

    private static final String ENTITY_NAME = "searchServiceSemanticSearch";

    @Value("${jhipster.clientApp.name:searchService}")
    private String applicationName;

    private final SemanticSearchService semanticSearchService;

    private final SemanticSearchRepository semanticSearchRepository;

    private final SemanticSearchQueryService semanticSearchQueryService;

    public SemanticSearchResource(
        SemanticSearchService semanticSearchService,
        SemanticSearchRepository semanticSearchRepository,
        SemanticSearchQueryService semanticSearchQueryService
    ) {
        this.semanticSearchService = semanticSearchService;
        this.semanticSearchRepository = semanticSearchRepository;
        this.semanticSearchQueryService = semanticSearchQueryService;
    }

    /**
     * {@code POST  /semantic-searches} : Create a new semanticSearch.
     *
     * @param semanticSearchDTO the semanticSearchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semanticSearchDTO, or with status {@code 400 (Bad Request)} if the semanticSearch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SemanticSearchDTO> createSemanticSearch(@Valid @RequestBody SemanticSearchDTO semanticSearchDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SemanticSearch : {}", semanticSearchDTO);
        if (semanticSearchDTO.getId() != null) {
            throw new BadRequestAlertException("A new semanticSearch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        semanticSearchDTO = semanticSearchService.save(semanticSearchDTO);
        return ResponseEntity.created(new URI("/api/semantic-searches/" + semanticSearchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, semanticSearchDTO.getId().toString()))
            .body(semanticSearchDTO);
    }

    /**
     * {@code PUT  /semantic-searches/:id} : Updates an existing semanticSearch.
     *
     * @param id the id of the semanticSearchDTO to save.
     * @param semanticSearchDTO the semanticSearchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semanticSearchDTO,
     * or with status {@code 400 (Bad Request)} if the semanticSearchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semanticSearchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SemanticSearchDTO> updateSemanticSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SemanticSearchDTO semanticSearchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SemanticSearch : {}, {}", id, semanticSearchDTO);
        if (semanticSearchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semanticSearchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semanticSearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        semanticSearchDTO = semanticSearchService.update(semanticSearchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semanticSearchDTO.getId().toString()))
            .body(semanticSearchDTO);
    }

    /**
     * {@code PATCH  /semantic-searches/:id} : Partial updates given fields of an existing semanticSearch, field will ignore if it is null
     *
     * @param id the id of the semanticSearchDTO to save.
     * @param semanticSearchDTO the semanticSearchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semanticSearchDTO,
     * or with status {@code 400 (Bad Request)} if the semanticSearchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the semanticSearchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the semanticSearchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SemanticSearchDTO> partialUpdateSemanticSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SemanticSearchDTO semanticSearchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SemanticSearch partially : {}, {}", id, semanticSearchDTO);
        if (semanticSearchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semanticSearchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semanticSearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SemanticSearchDTO> result = semanticSearchService.partialUpdate(semanticSearchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semanticSearchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /semantic-searches} : get all the semanticSearches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semanticSearches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SemanticSearchDTO>> getAllSemanticSearches(
        SemanticSearchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SemanticSearches by criteria: {}", criteria);

        Page<SemanticSearchDTO> page = semanticSearchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /semantic-searches/count} : count all the semanticSearches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSemanticSearches(SemanticSearchCriteria criteria) {
        LOG.debug("REST request to count SemanticSearches by criteria: {}", criteria);
        return ResponseEntity.ok().body(semanticSearchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /semantic-searches/:id} : get the "id" semanticSearch.
     *
     * @param id the id of the semanticSearchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semanticSearchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SemanticSearchDTO> getSemanticSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SemanticSearch : {}", id);
        Optional<SemanticSearchDTO> semanticSearchDTO = semanticSearchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semanticSearchDTO);
    }

    /**
     * {@code DELETE  /semantic-searches/:id} : delete the "id" semanticSearch.
     *
     * @param id the id of the semanticSearchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemanticSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SemanticSearch : {}", id);
        semanticSearchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
