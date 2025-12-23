package fr.smartprod.paperdms.archive.web.rest;

import fr.smartprod.paperdms.archive.repository.ArchiveJobRepository;
import fr.smartprod.paperdms.archive.service.ArchiveJobQueryService;
import fr.smartprod.paperdms.archive.service.ArchiveJobService;
import fr.smartprod.paperdms.archive.service.criteria.ArchiveJobCriteria;
import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import fr.smartprod.paperdms.archive.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveJob}.
 */
@RestController
@RequestMapping("/api/archive-jobs")
public class ArchiveJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveJobResource.class);

    private static final String ENTITY_NAME = "archiveServiceArchiveJob";

    @Value("${jhipster.clientApp.name:archiveService}")
    private String applicationName;

    private final ArchiveJobService archiveJobService;

    private final ArchiveJobRepository archiveJobRepository;

    private final ArchiveJobQueryService archiveJobQueryService;

    public ArchiveJobResource(
        ArchiveJobService archiveJobService,
        ArchiveJobRepository archiveJobRepository,
        ArchiveJobQueryService archiveJobQueryService
    ) {
        this.archiveJobService = archiveJobService;
        this.archiveJobRepository = archiveJobRepository;
        this.archiveJobQueryService = archiveJobQueryService;
    }

    /**
     * {@code POST  /archive-jobs} : Create a new archiveJob.
     *
     * @param archiveJobDTO the archiveJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new archiveJobDTO, or with status {@code 400 (Bad Request)} if the archiveJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArchiveJobDTO> createArchiveJob(@Valid @RequestBody ArchiveJobDTO archiveJobDTO) throws URISyntaxException {
        LOG.debug("REST request to save ArchiveJob : {}", archiveJobDTO);
        if (archiveJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new archiveJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        archiveJobDTO = archiveJobService.save(archiveJobDTO);
        return ResponseEntity.created(new URI("/api/archive-jobs/" + archiveJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, archiveJobDTO.getId().toString()))
            .body(archiveJobDTO);
    }

    /**
     * {@code PUT  /archive-jobs/:id} : Updates an existing archiveJob.
     *
     * @param id the id of the archiveJobDTO to save.
     * @param archiveJobDTO the archiveJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archiveJobDTO,
     * or with status {@code 400 (Bad Request)} if the archiveJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the archiveJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArchiveJobDTO> updateArchiveJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArchiveJobDTO archiveJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ArchiveJob : {}, {}", id, archiveJobDTO);
        if (archiveJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archiveJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        archiveJobDTO = archiveJobService.update(archiveJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archiveJobDTO.getId().toString()))
            .body(archiveJobDTO);
    }

    /**
     * {@code PATCH  /archive-jobs/:id} : Partial updates given fields of an existing archiveJob, field will ignore if it is null
     *
     * @param id the id of the archiveJobDTO to save.
     * @param archiveJobDTO the archiveJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archiveJobDTO,
     * or with status {@code 400 (Bad Request)} if the archiveJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the archiveJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the archiveJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArchiveJobDTO> partialUpdateArchiveJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArchiveJobDTO archiveJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ArchiveJob partially : {}, {}", id, archiveJobDTO);
        if (archiveJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archiveJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArchiveJobDTO> result = archiveJobService.partialUpdate(archiveJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archiveJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /archive-jobs} : get all the archiveJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of archiveJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArchiveJobDTO>> getAllArchiveJobs(
        ArchiveJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ArchiveJobs by criteria: {}", criteria);

        Page<ArchiveJobDTO> page = archiveJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /archive-jobs/count} : count all the archiveJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countArchiveJobs(ArchiveJobCriteria criteria) {
        LOG.debug("REST request to count ArchiveJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(archiveJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /archive-jobs/:id} : get the "id" archiveJob.
     *
     * @param id the id of the archiveJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archiveJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArchiveJobDTO> getArchiveJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ArchiveJob : {}", id);
        Optional<ArchiveJobDTO> archiveJobDTO = archiveJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(archiveJobDTO);
    }

    /**
     * {@code DELETE  /archive-jobs/:id} : delete the "id" archiveJob.
     *
     * @param id the id of the archiveJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArchiveJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ArchiveJob : {}", id);
        archiveJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
