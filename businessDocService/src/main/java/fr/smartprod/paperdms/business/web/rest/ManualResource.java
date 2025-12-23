package fr.smartprod.paperdms.business.web.rest;

import fr.smartprod.paperdms.business.repository.ManualRepository;
import fr.smartprod.paperdms.business.service.ManualQueryService;
import fr.smartprod.paperdms.business.service.ManualService;
import fr.smartprod.paperdms.business.service.criteria.ManualCriteria;
import fr.smartprod.paperdms.business.service.dto.ManualDTO;
import fr.smartprod.paperdms.business.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.business.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.business.domain.Manual}.
 */
@RestController
@RequestMapping("/api/manuals")
public class ManualResource {

    private static final Logger LOG = LoggerFactory.getLogger(ManualResource.class);

    private static final String ENTITY_NAME = "businessDocServiceManual";

    @Value("${jhipster.clientApp.name:businessDocService}")
    private String applicationName;

    private final ManualService manualService;

    private final ManualRepository manualRepository;

    private final ManualQueryService manualQueryService;

    public ManualResource(ManualService manualService, ManualRepository manualRepository, ManualQueryService manualQueryService) {
        this.manualService = manualService;
        this.manualRepository = manualRepository;
        this.manualQueryService = manualQueryService;
    }

    /**
     * {@code POST  /manuals} : Create a new manual.
     *
     * @param manualDTO the manualDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualDTO, or with status {@code 400 (Bad Request)} if the manual has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ManualDTO> createManual(@Valid @RequestBody ManualDTO manualDTO) throws URISyntaxException {
        LOG.debug("REST request to save Manual : {}", manualDTO);
        if (manualDTO.getId() != null) {
            throw new BadRequestAlertException("A new manual cannot already have an ID", ENTITY_NAME, "idexists");
        }
        manualDTO = manualService.save(manualDTO);
        return ResponseEntity.created(new URI("/api/manuals/" + manualDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, manualDTO.getId().toString()))
            .body(manualDTO);
    }

    /**
     * {@code PUT  /manuals/:id} : Updates an existing manual.
     *
     * @param id the id of the manualDTO to save.
     * @param manualDTO the manualDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualDTO,
     * or with status {@code 400 (Bad Request)} if the manualDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManualDTO> updateManual(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManualDTO manualDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Manual : {}, {}", id, manualDTO);
        if (manualDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        manualDTO = manualService.update(manualDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualDTO.getId().toString()))
            .body(manualDTO);
    }

    /**
     * {@code PATCH  /manuals/:id} : Partial updates given fields of an existing manual, field will ignore if it is null
     *
     * @param id the id of the manualDTO to save.
     * @param manualDTO the manualDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualDTO,
     * or with status {@code 400 (Bad Request)} if the manualDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manualDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manualDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualDTO> partialUpdateManual(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManualDTO manualDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Manual partially : {}, {}", id, manualDTO);
        if (manualDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManualDTO> result = manualService.partialUpdate(manualDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /manuals} : get all the manuals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manuals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ManualDTO>> getAllManuals(
        ManualCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Manuals by criteria: {}", criteria);

        Page<ManualDTO> page = manualQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manuals/count} : count all the manuals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countManuals(ManualCriteria criteria) {
        LOG.debug("REST request to count Manuals by criteria: {}", criteria);
        return ResponseEntity.ok().body(manualQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /manuals/:id} : get the "id" manual.
     *
     * @param id the id of the manualDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManualDTO> getManual(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Manual : {}", id);
        Optional<ManualDTO> manualDTO = manualService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualDTO);
    }

    /**
     * {@code DELETE  /manuals/:id} : delete the "id" manual.
     *
     * @param id the id of the manualDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManual(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Manual : {}", id);
        manualService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /manuals/_search?query=:query} : search for the manual corresponding
     * to the query.
     *
     * @param query the query of the manual search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ManualDTO>> searchManuals(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Manuals for query {}", query);
        try {
            Page<ManualDTO> page = manualService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
