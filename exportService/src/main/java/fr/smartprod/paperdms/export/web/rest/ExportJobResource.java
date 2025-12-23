package fr.smartprod.paperdms.export.web.rest;

import fr.smartprod.paperdms.export.repository.ExportJobRepository;
import fr.smartprod.paperdms.export.service.ExportJobQueryService;
import fr.smartprod.paperdms.export.service.ExportJobService;
import fr.smartprod.paperdms.export.service.criteria.ExportJobCriteria;
import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import fr.smartprod.paperdms.export.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.export.domain.ExportJob}.
 */
@RestController
@RequestMapping("/api/export-jobs")
public class ExportJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExportJobResource.class);

    private static final String ENTITY_NAME = "exportServiceExportJob";

    @Value("${jhipster.clientApp.name:exportService}")
    private String applicationName;

    private final ExportJobService exportJobService;

    private final ExportJobRepository exportJobRepository;

    private final ExportJobQueryService exportJobQueryService;

    public ExportJobResource(
        ExportJobService exportJobService,
        ExportJobRepository exportJobRepository,
        ExportJobQueryService exportJobQueryService
    ) {
        this.exportJobService = exportJobService;
        this.exportJobRepository = exportJobRepository;
        this.exportJobQueryService = exportJobQueryService;
    }

    /**
     * {@code POST  /export-jobs} : Create a new exportJob.
     *
     * @param exportJobDTO the exportJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exportJobDTO, or with status {@code 400 (Bad Request)} if the exportJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExportJobDTO> createExportJob(@Valid @RequestBody ExportJobDTO exportJobDTO) throws URISyntaxException {
        LOG.debug("REST request to save ExportJob : {}", exportJobDTO);
        if (exportJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new exportJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        exportJobDTO = exportJobService.save(exportJobDTO);
        return ResponseEntity.created(new URI("/api/export-jobs/" + exportJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, exportJobDTO.getId().toString()))
            .body(exportJobDTO);
    }

    /**
     * {@code PUT  /export-jobs/:id} : Updates an existing exportJob.
     *
     * @param id the id of the exportJobDTO to save.
     * @param exportJobDTO the exportJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exportJobDTO,
     * or with status {@code 400 (Bad Request)} if the exportJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exportJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExportJobDTO> updateExportJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExportJobDTO exportJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExportJob : {}, {}", id, exportJobDTO);
        if (exportJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exportJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exportJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        exportJobDTO = exportJobService.update(exportJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exportJobDTO.getId().toString()))
            .body(exportJobDTO);
    }

    /**
     * {@code PATCH  /export-jobs/:id} : Partial updates given fields of an existing exportJob, field will ignore if it is null
     *
     * @param id the id of the exportJobDTO to save.
     * @param exportJobDTO the exportJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exportJobDTO,
     * or with status {@code 400 (Bad Request)} if the exportJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exportJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exportJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExportJobDTO> partialUpdateExportJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExportJobDTO exportJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExportJob partially : {}, {}", id, exportJobDTO);
        if (exportJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exportJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exportJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExportJobDTO> result = exportJobService.partialUpdate(exportJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exportJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /export-jobs} : get all the exportJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exportJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExportJobDTO>> getAllExportJobs(
        ExportJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExportJobs by criteria: {}", criteria);

        Page<ExportJobDTO> page = exportJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /export-jobs/count} : count all the exportJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExportJobs(ExportJobCriteria criteria) {
        LOG.debug("REST request to count ExportJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(exportJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /export-jobs/:id} : get the "id" exportJob.
     *
     * @param id the id of the exportJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exportJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExportJobDTO> getExportJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExportJob : {}", id);
        Optional<ExportJobDTO> exportJobDTO = exportJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exportJobDTO);
    }

    /**
     * {@code DELETE  /export-jobs/:id} : delete the "id" exportJob.
     *
     * @param id the id of the exportJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExportJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExportJob : {}", id);
        exportJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
