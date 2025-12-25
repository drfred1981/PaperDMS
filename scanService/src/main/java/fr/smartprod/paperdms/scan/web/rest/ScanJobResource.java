package fr.smartprod.paperdms.scan.web.rest;

import fr.smartprod.paperdms.scan.repository.ScanJobRepository;
import fr.smartprod.paperdms.scan.service.ScanJobQueryService;
import fr.smartprod.paperdms.scan.service.ScanJobService;
import fr.smartprod.paperdms.scan.service.criteria.ScanJobCriteria;
import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import fr.smartprod.paperdms.scan.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.scan.domain.ScanJob}.
 */
@RestController
@RequestMapping("/api/scan-jobs")
public class ScanJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScanJobResource.class);

    private static final String ENTITY_NAME = "scanServiceScanJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScanJobService scanJobService;

    private final ScanJobRepository scanJobRepository;

    private final ScanJobQueryService scanJobQueryService;

    public ScanJobResource(ScanJobService scanJobService, ScanJobRepository scanJobRepository, ScanJobQueryService scanJobQueryService) {
        this.scanJobService = scanJobService;
        this.scanJobRepository = scanJobRepository;
        this.scanJobQueryService = scanJobQueryService;
    }

    /**
     * {@code POST  /scan-jobs} : Create a new scanJob.
     *
     * @param scanJobDTO the scanJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scanJobDTO, or with status {@code 400 (Bad Request)} if the scanJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScanJobDTO> createScanJob(@Valid @RequestBody ScanJobDTO scanJobDTO) throws URISyntaxException {
        LOG.debug("REST request to save ScanJob : {}", scanJobDTO);
        if (scanJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new scanJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scanJobDTO = scanJobService.save(scanJobDTO);
        return ResponseEntity.created(new URI("/api/scan-jobs/" + scanJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, scanJobDTO.getId().toString()))
            .body(scanJobDTO);
    }

    /**
     * {@code PUT  /scan-jobs/:id} : Updates an existing scanJob.
     *
     * @param id the id of the scanJobDTO to save.
     * @param scanJobDTO the scanJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scanJobDTO,
     * or with status {@code 400 (Bad Request)} if the scanJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scanJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScanJobDTO> updateScanJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScanJobDTO scanJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScanJob : {}, {}", id, scanJobDTO);
        if (scanJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scanJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scanJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scanJobDTO = scanJobService.update(scanJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scanJobDTO.getId().toString()))
            .body(scanJobDTO);
    }

    /**
     * {@code PATCH  /scan-jobs/:id} : Partial updates given fields of an existing scanJob, field will ignore if it is null
     *
     * @param id the id of the scanJobDTO to save.
     * @param scanJobDTO the scanJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scanJobDTO,
     * or with status {@code 400 (Bad Request)} if the scanJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scanJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scanJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScanJobDTO> partialUpdateScanJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScanJobDTO scanJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScanJob partially : {}, {}", id, scanJobDTO);
        if (scanJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scanJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scanJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScanJobDTO> result = scanJobService.partialUpdate(scanJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scanJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scan-jobs} : get all the scanJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scanJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScanJobDTO>> getAllScanJobs(
        ScanJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ScanJobs by criteria: {}", criteria);

        Page<ScanJobDTO> page = scanJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scan-jobs/count} : count all the scanJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countScanJobs(ScanJobCriteria criteria) {
        LOG.debug("REST request to count ScanJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(scanJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scan-jobs/:id} : get the "id" scanJob.
     *
     * @param id the id of the scanJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scanJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScanJobDTO> getScanJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScanJob : {}", id);
        Optional<ScanJobDTO> scanJobDTO = scanJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scanJobDTO);
    }

    /**
     * {@code DELETE  /scan-jobs/:id} : delete the "id" scanJob.
     *
     * @param id the id of the scanJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScanJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScanJob : {}", id);
        scanJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
