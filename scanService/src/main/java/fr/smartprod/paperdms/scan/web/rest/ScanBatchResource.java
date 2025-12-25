package fr.smartprod.paperdms.scan.web.rest;

import fr.smartprod.paperdms.scan.repository.ScanBatchRepository;
import fr.smartprod.paperdms.scan.service.ScanBatchQueryService;
import fr.smartprod.paperdms.scan.service.ScanBatchService;
import fr.smartprod.paperdms.scan.service.criteria.ScanBatchCriteria;
import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.scan.domain.ScanBatch}.
 */
@RestController
@RequestMapping("/api/scan-batches")
public class ScanBatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScanBatchResource.class);

    private static final String ENTITY_NAME = "scanServiceScanBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScanBatchService scanBatchService;

    private final ScanBatchRepository scanBatchRepository;

    private final ScanBatchQueryService scanBatchQueryService;

    public ScanBatchResource(
        ScanBatchService scanBatchService,
        ScanBatchRepository scanBatchRepository,
        ScanBatchQueryService scanBatchQueryService
    ) {
        this.scanBatchService = scanBatchService;
        this.scanBatchRepository = scanBatchRepository;
        this.scanBatchQueryService = scanBatchQueryService;
    }

    /**
     * {@code POST  /scan-batches} : Create a new scanBatch.
     *
     * @param scanBatchDTO the scanBatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scanBatchDTO, or with status {@code 400 (Bad Request)} if the scanBatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScanBatchDTO> createScanBatch(@Valid @RequestBody ScanBatchDTO scanBatchDTO) throws URISyntaxException {
        LOG.debug("REST request to save ScanBatch : {}", scanBatchDTO);
        if (scanBatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new scanBatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scanBatchDTO = scanBatchService.save(scanBatchDTO);
        return ResponseEntity.created(new URI("/api/scan-batches/" + scanBatchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, scanBatchDTO.getId().toString()))
            .body(scanBatchDTO);
    }

    /**
     * {@code PUT  /scan-batches/:id} : Updates an existing scanBatch.
     *
     * @param id the id of the scanBatchDTO to save.
     * @param scanBatchDTO the scanBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scanBatchDTO,
     * or with status {@code 400 (Bad Request)} if the scanBatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scanBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScanBatchDTO> updateScanBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScanBatchDTO scanBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScanBatch : {}, {}", id, scanBatchDTO);
        if (scanBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scanBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scanBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scanBatchDTO = scanBatchService.update(scanBatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scanBatchDTO.getId().toString()))
            .body(scanBatchDTO);
    }

    /**
     * {@code PATCH  /scan-batches/:id} : Partial updates given fields of an existing scanBatch, field will ignore if it is null
     *
     * @param id the id of the scanBatchDTO to save.
     * @param scanBatchDTO the scanBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scanBatchDTO,
     * or with status {@code 400 (Bad Request)} if the scanBatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scanBatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scanBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScanBatchDTO> partialUpdateScanBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScanBatchDTO scanBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScanBatch partially : {}, {}", id, scanBatchDTO);
        if (scanBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scanBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scanBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScanBatchDTO> result = scanBatchService.partialUpdate(scanBatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scanBatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scan-batches} : get all the scanBatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scanBatches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScanBatchDTO>> getAllScanBatches(
        ScanBatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ScanBatches by criteria: {}", criteria);

        Page<ScanBatchDTO> page = scanBatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scan-batches/count} : count all the scanBatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countScanBatches(ScanBatchCriteria criteria) {
        LOG.debug("REST request to count ScanBatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(scanBatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scan-batches/:id} : get the "id" scanBatch.
     *
     * @param id the id of the scanBatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scanBatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScanBatchDTO> getScanBatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScanBatch : {}", id);
        Optional<ScanBatchDTO> scanBatchDTO = scanBatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scanBatchDTO);
    }

    /**
     * {@code DELETE  /scan-batches/:id} : delete the "id" scanBatch.
     *
     * @param id the id of the scanBatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScanBatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScanBatch : {}", id);
        scanBatchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
