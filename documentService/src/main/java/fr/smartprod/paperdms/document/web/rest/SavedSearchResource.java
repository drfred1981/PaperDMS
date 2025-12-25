package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.SavedSearchRepository;
import fr.smartprod.paperdms.document.service.SavedSearchQueryService;
import fr.smartprod.paperdms.document.service.SavedSearchService;
import fr.smartprod.paperdms.document.service.criteria.SavedSearchCriteria;
import fr.smartprod.paperdms.document.service.dto.SavedSearchDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.SavedSearch}.
 */
@RestController
@RequestMapping("/api/saved-searches")
public class SavedSearchResource {

    private static final Logger LOG = LoggerFactory.getLogger(SavedSearchResource.class);

    private static final String ENTITY_NAME = "documentServiceSavedSearch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SavedSearchService savedSearchService;

    private final SavedSearchRepository savedSearchRepository;

    private final SavedSearchQueryService savedSearchQueryService;

    public SavedSearchResource(
        SavedSearchService savedSearchService,
        SavedSearchRepository savedSearchRepository,
        SavedSearchQueryService savedSearchQueryService
    ) {
        this.savedSearchService = savedSearchService;
        this.savedSearchRepository = savedSearchRepository;
        this.savedSearchQueryService = savedSearchQueryService;
    }

    /**
     * {@code POST  /saved-searches} : Create a new savedSearch.
     *
     * @param savedSearchDTO the savedSearchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new savedSearchDTO, or with status {@code 400 (Bad Request)} if the savedSearch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SavedSearchDTO> createSavedSearch(@Valid @RequestBody SavedSearchDTO savedSearchDTO) throws URISyntaxException {
        LOG.debug("REST request to save SavedSearch : {}", savedSearchDTO);
        if (savedSearchDTO.getId() != null) {
            throw new BadRequestAlertException("A new savedSearch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        savedSearchDTO = savedSearchService.save(savedSearchDTO);
        return ResponseEntity.created(new URI("/api/saved-searches/" + savedSearchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, savedSearchDTO.getId().toString()))
            .body(savedSearchDTO);
    }

    /**
     * {@code PUT  /saved-searches/:id} : Updates an existing savedSearch.
     *
     * @param id the id of the savedSearchDTO to save.
     * @param savedSearchDTO the savedSearchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated savedSearchDTO,
     * or with status {@code 400 (Bad Request)} if the savedSearchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the savedSearchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SavedSearchDTO> updateSavedSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SavedSearchDTO savedSearchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SavedSearch : {}, {}", id, savedSearchDTO);
        if (savedSearchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, savedSearchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!savedSearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        savedSearchDTO = savedSearchService.update(savedSearchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, savedSearchDTO.getId().toString()))
            .body(savedSearchDTO);
    }

    /**
     * {@code PATCH  /saved-searches/:id} : Partial updates given fields of an existing savedSearch, field will ignore if it is null
     *
     * @param id the id of the savedSearchDTO to save.
     * @param savedSearchDTO the savedSearchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated savedSearchDTO,
     * or with status {@code 400 (Bad Request)} if the savedSearchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the savedSearchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the savedSearchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SavedSearchDTO> partialUpdateSavedSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SavedSearchDTO savedSearchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SavedSearch partially : {}, {}", id, savedSearchDTO);
        if (savedSearchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, savedSearchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!savedSearchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SavedSearchDTO> result = savedSearchService.partialUpdate(savedSearchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, savedSearchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saved-searches} : get all the savedSearches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of savedSearches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SavedSearchDTO>> getAllSavedSearches(
        SavedSearchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SavedSearches by criteria: {}", criteria);

        Page<SavedSearchDTO> page = savedSearchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saved-searches/count} : count all the savedSearches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSavedSearches(SavedSearchCriteria criteria) {
        LOG.debug("REST request to count SavedSearches by criteria: {}", criteria);
        return ResponseEntity.ok().body(savedSearchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /saved-searches/:id} : get the "id" savedSearch.
     *
     * @param id the id of the savedSearchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the savedSearchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SavedSearchDTO> getSavedSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SavedSearch : {}", id);
        Optional<SavedSearchDTO> savedSearchDTO = savedSearchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(savedSearchDTO);
    }

    /**
     * {@code DELETE  /saved-searches/:id} : delete the "id" savedSearch.
     *
     * @param id the id of the savedSearchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SavedSearch : {}", id);
        savedSearchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
