package fr.smartprod.paperdms.ocr.web.rest;

import fr.smartprod.paperdms.ocr.repository.OcrJobRepository;
import fr.smartprod.paperdms.ocr.service.OcrJobQueryService;
import fr.smartprod.paperdms.ocr.service.OcrJobService;
import fr.smartprod.paperdms.ocr.service.criteria.OcrJobCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ocr.domain.OcrJob}.
 */
@RestController
@RequestMapping("/api/ocr-jobs")
public class OcrJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(OcrJobResource.class);

    private static final String ENTITY_NAME = "ocrServiceOcrJob";

    @Value("${jhipster.clientApp.name:ocrService}")
    private String applicationName;

    private final OcrJobService ocrJobService;

    private final OcrJobRepository ocrJobRepository;

    private final OcrJobQueryService ocrJobQueryService;

    public OcrJobResource(OcrJobService ocrJobService, OcrJobRepository ocrJobRepository, OcrJobQueryService ocrJobQueryService) {
        this.ocrJobService = ocrJobService;
        this.ocrJobRepository = ocrJobRepository;
        this.ocrJobQueryService = ocrJobQueryService;
    }

    /**
     * {@code POST  /ocr-jobs} : Create a new ocrJob.
     *
     * @param ocrJobDTO the ocrJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocrJobDTO, or with status {@code 400 (Bad Request)} if the ocrJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OcrJobDTO> createOcrJob(@Valid @RequestBody OcrJobDTO ocrJobDTO) throws URISyntaxException {
        LOG.debug("REST request to save OcrJob : {}", ocrJobDTO);
        if (ocrJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new ocrJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ocrJobDTO = ocrJobService.save(ocrJobDTO);
        return ResponseEntity.created(new URI("/api/ocr-jobs/" + ocrJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ocrJobDTO.getId().toString()))
            .body(ocrJobDTO);
    }

    /**
     * {@code PUT  /ocr-jobs/:id} : Updates an existing ocrJob.
     *
     * @param id the id of the ocrJobDTO to save.
     * @param ocrJobDTO the ocrJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrJobDTO,
     * or with status {@code 400 (Bad Request)} if the ocrJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocrJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcrJobDTO> updateOcrJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OcrJobDTO ocrJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OcrJob : {}, {}", id, ocrJobDTO);
        if (ocrJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ocrJobDTO = ocrJobService.update(ocrJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrJobDTO.getId().toString()))
            .body(ocrJobDTO);
    }

    /**
     * {@code PATCH  /ocr-jobs/:id} : Partial updates given fields of an existing ocrJob, field will ignore if it is null
     *
     * @param id the id of the ocrJobDTO to save.
     * @param ocrJobDTO the ocrJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrJobDTO,
     * or with status {@code 400 (Bad Request)} if the ocrJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ocrJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ocrJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OcrJobDTO> partialUpdateOcrJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OcrJobDTO ocrJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OcrJob partially : {}, {}", id, ocrJobDTO);
        if (ocrJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OcrJobDTO> result = ocrJobService.partialUpdate(ocrJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ocr-jobs} : get all the ocrJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocrJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OcrJobDTO>> getAllOcrJobs(
        OcrJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OcrJobs by criteria: {}", criteria);

        Page<OcrJobDTO> page = ocrJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ocr-jobs/count} : count all the ocrJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOcrJobs(OcrJobCriteria criteria) {
        LOG.debug("REST request to count OcrJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ocrJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ocr-jobs/:id} : get the "id" ocrJob.
     *
     * @param id the id of the ocrJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocrJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcrJobDTO> getOcrJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OcrJob : {}", id);
        Optional<OcrJobDTO> ocrJobDTO = ocrJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ocrJobDTO);
    }

    /**
     * {@code DELETE  /ocr-jobs/:id} : delete the "id" ocrJob.
     *
     * @param id the id of the ocrJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcrJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OcrJob : {}", id);
        ocrJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
