package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.WatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.WatermarkJobQueryService;
import fr.smartprod.paperdms.transform.service.WatermarkJobService;
import fr.smartprod.paperdms.transform.service.criteria.WatermarkJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.WatermarkJobDTO;
import fr.smartprod.paperdms.transform.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.WatermarkJob}.
 */
@RestController
@RequestMapping("/api/watermark-jobs")
public class WatermarkJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(WatermarkJobResource.class);

    private static final String ENTITY_NAME = "transformServiceWatermarkJob";

    @Value("${jhipster.clientApp.name:transformService}")
    private String applicationName;

    private final WatermarkJobService watermarkJobService;

    private final WatermarkJobRepository watermarkJobRepository;

    private final WatermarkJobQueryService watermarkJobQueryService;

    public WatermarkJobResource(
        WatermarkJobService watermarkJobService,
        WatermarkJobRepository watermarkJobRepository,
        WatermarkJobQueryService watermarkJobQueryService
    ) {
        this.watermarkJobService = watermarkJobService;
        this.watermarkJobRepository = watermarkJobRepository;
        this.watermarkJobQueryService = watermarkJobQueryService;
    }

    /**
     * {@code POST  /watermark-jobs} : Create a new watermarkJob.
     *
     * @param watermarkJobDTO the watermarkJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new watermarkJobDTO, or with status {@code 400 (Bad Request)} if the watermarkJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WatermarkJobDTO> createWatermarkJob(@Valid @RequestBody WatermarkJobDTO watermarkJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WatermarkJob : {}", watermarkJobDTO);
        if (watermarkJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new watermarkJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        watermarkJobDTO = watermarkJobService.save(watermarkJobDTO);
        return ResponseEntity.created(new URI("/api/watermark-jobs/" + watermarkJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, watermarkJobDTO.getId().toString()))
            .body(watermarkJobDTO);
    }

    /**
     * {@code PUT  /watermark-jobs/:id} : Updates an existing watermarkJob.
     *
     * @param id the id of the watermarkJobDTO to save.
     * @param watermarkJobDTO the watermarkJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watermarkJobDTO,
     * or with status {@code 400 (Bad Request)} if the watermarkJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the watermarkJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WatermarkJobDTO> updateWatermarkJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WatermarkJobDTO watermarkJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WatermarkJob : {}, {}", id, watermarkJobDTO);
        if (watermarkJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watermarkJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watermarkJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        watermarkJobDTO = watermarkJobService.update(watermarkJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, watermarkJobDTO.getId().toString()))
            .body(watermarkJobDTO);
    }

    /**
     * {@code PATCH  /watermark-jobs/:id} : Partial updates given fields of an existing watermarkJob, field will ignore if it is null
     *
     * @param id the id of the watermarkJobDTO to save.
     * @param watermarkJobDTO the watermarkJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watermarkJobDTO,
     * or with status {@code 400 (Bad Request)} if the watermarkJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the watermarkJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the watermarkJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WatermarkJobDTO> partialUpdateWatermarkJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WatermarkJobDTO watermarkJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WatermarkJob partially : {}, {}", id, watermarkJobDTO);
        if (watermarkJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watermarkJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watermarkJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WatermarkJobDTO> result = watermarkJobService.partialUpdate(watermarkJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, watermarkJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /watermark-jobs} : get all the watermarkJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of watermarkJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WatermarkJobDTO>> getAllWatermarkJobs(
        WatermarkJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get WatermarkJobs by criteria: {}", criteria);

        Page<WatermarkJobDTO> page = watermarkJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /watermark-jobs/count} : count all the watermarkJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWatermarkJobs(WatermarkJobCriteria criteria) {
        LOG.debug("REST request to count WatermarkJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(watermarkJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /watermark-jobs/:id} : get the "id" watermarkJob.
     *
     * @param id the id of the watermarkJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watermarkJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WatermarkJobDTO> getWatermarkJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WatermarkJob : {}", id);
        Optional<WatermarkJobDTO> watermarkJobDTO = watermarkJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(watermarkJobDTO);
    }

    /**
     * {@code DELETE  /watermark-jobs/:id} : delete the "id" watermarkJob.
     *
     * @param id the id of the watermarkJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatermarkJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WatermarkJob : {}", id);
        watermarkJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
