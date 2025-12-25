package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AutoTagJobRepository;
import fr.smartprod.paperdms.ai.service.AutoTagJobQueryService;
import fr.smartprod.paperdms.ai.service.AutoTagJobService;
import fr.smartprod.paperdms.ai.service.criteria.AutoTagJobCriteria;
import fr.smartprod.paperdms.ai.service.dto.AutoTagJobDTO;
import fr.smartprod.paperdms.ai.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AutoTagJob}.
 */
@RestController
@RequestMapping("/api/auto-tag-jobs")
public class AutoTagJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(AutoTagJobResource.class);

    private static final String ENTITY_NAME = "aiServiceAutoTagJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutoTagJobService autoTagJobService;

    private final AutoTagJobRepository autoTagJobRepository;

    private final AutoTagJobQueryService autoTagJobQueryService;

    public AutoTagJobResource(
        AutoTagJobService autoTagJobService,
        AutoTagJobRepository autoTagJobRepository,
        AutoTagJobQueryService autoTagJobQueryService
    ) {
        this.autoTagJobService = autoTagJobService;
        this.autoTagJobRepository = autoTagJobRepository;
        this.autoTagJobQueryService = autoTagJobQueryService;
    }

    /**
     * {@code POST  /auto-tag-jobs} : Create a new autoTagJob.
     *
     * @param autoTagJobDTO the autoTagJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoTagJobDTO, or with status {@code 400 (Bad Request)} if the autoTagJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AutoTagJobDTO> createAutoTagJob(@Valid @RequestBody AutoTagJobDTO autoTagJobDTO) throws URISyntaxException {
        LOG.debug("REST request to save AutoTagJob : {}", autoTagJobDTO);
        if (autoTagJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new autoTagJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        autoTagJobDTO = autoTagJobService.save(autoTagJobDTO);
        return ResponseEntity.created(new URI("/api/auto-tag-jobs/" + autoTagJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, autoTagJobDTO.getId().toString()))
            .body(autoTagJobDTO);
    }

    /**
     * {@code PUT  /auto-tag-jobs/:id} : Updates an existing autoTagJob.
     *
     * @param id the id of the autoTagJobDTO to save.
     * @param autoTagJobDTO the autoTagJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoTagJobDTO,
     * or with status {@code 400 (Bad Request)} if the autoTagJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoTagJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoTagJobDTO> updateAutoTagJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AutoTagJobDTO autoTagJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AutoTagJob : {}, {}", id, autoTagJobDTO);
        if (autoTagJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autoTagJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autoTagJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        autoTagJobDTO = autoTagJobService.update(autoTagJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoTagJobDTO.getId().toString()))
            .body(autoTagJobDTO);
    }

    /**
     * {@code PATCH  /auto-tag-jobs/:id} : Partial updates given fields of an existing autoTagJob, field will ignore if it is null
     *
     * @param id the id of the autoTagJobDTO to save.
     * @param autoTagJobDTO the autoTagJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoTagJobDTO,
     * or with status {@code 400 (Bad Request)} if the autoTagJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the autoTagJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the autoTagJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AutoTagJobDTO> partialUpdateAutoTagJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AutoTagJobDTO autoTagJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AutoTagJob partially : {}, {}", id, autoTagJobDTO);
        if (autoTagJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autoTagJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autoTagJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AutoTagJobDTO> result = autoTagJobService.partialUpdate(autoTagJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoTagJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /auto-tag-jobs} : get all the autoTagJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autoTagJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AutoTagJobDTO>> getAllAutoTagJobs(
        AutoTagJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AutoTagJobs by criteria: {}", criteria);

        Page<AutoTagJobDTO> page = autoTagJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /auto-tag-jobs/count} : count all the autoTagJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAutoTagJobs(AutoTagJobCriteria criteria) {
        LOG.debug("REST request to count AutoTagJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(autoTagJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /auto-tag-jobs/:id} : get the "id" autoTagJob.
     *
     * @param id the id of the autoTagJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoTagJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoTagJobDTO> getAutoTagJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AutoTagJob : {}", id);
        Optional<AutoTagJobDTO> autoTagJobDTO = autoTagJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoTagJobDTO);
    }

    /**
     * {@code DELETE  /auto-tag-jobs/:id} : delete the "id" autoTagJob.
     *
     * @param id the id of the autoTagJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutoTagJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AutoTagJob : {}", id);
        autoTagJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
