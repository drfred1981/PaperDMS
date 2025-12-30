package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.TransformWatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.TransformWatermarkJobQueryService;
import fr.smartprod.paperdms.transform.service.TransformWatermarkJobService;
import fr.smartprod.paperdms.transform.service.criteria.TransformWatermarkJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformWatermarkJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.TransformWatermarkJob}.
 */
@RestController
@RequestMapping("/api/transform-watermark-jobs")
public class TransformWatermarkJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransformWatermarkJobResource.class);

    private static final String ENTITY_NAME = "transformServiceTransformWatermarkJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransformWatermarkJobService transformWatermarkJobService;

    private final TransformWatermarkJobRepository transformWatermarkJobRepository;

    private final TransformWatermarkJobQueryService transformWatermarkJobQueryService;

    public TransformWatermarkJobResource(
        TransformWatermarkJobService transformWatermarkJobService,
        TransformWatermarkJobRepository transformWatermarkJobRepository,
        TransformWatermarkJobQueryService transformWatermarkJobQueryService
    ) {
        this.transformWatermarkJobService = transformWatermarkJobService;
        this.transformWatermarkJobRepository = transformWatermarkJobRepository;
        this.transformWatermarkJobQueryService = transformWatermarkJobQueryService;
    }

    /**
     * {@code POST  /transform-watermark-jobs} : Create a new transformWatermarkJob.
     *
     * @param transformWatermarkJobDTO the transformWatermarkJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transformWatermarkJobDTO, or with status {@code 400 (Bad Request)} if the transformWatermarkJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransformWatermarkJobDTO> createTransformWatermarkJob(
        @Valid @RequestBody TransformWatermarkJobDTO transformWatermarkJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TransformWatermarkJob : {}", transformWatermarkJobDTO);
        if (transformWatermarkJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new transformWatermarkJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transformWatermarkJobDTO = transformWatermarkJobService.save(transformWatermarkJobDTO);
        return ResponseEntity.created(new URI("/api/transform-watermark-jobs/" + transformWatermarkJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transformWatermarkJobDTO.getId().toString()))
            .body(transformWatermarkJobDTO);
    }

    /**
     * {@code PUT  /transform-watermark-jobs/:id} : Updates an existing transformWatermarkJob.
     *
     * @param id the id of the transformWatermarkJobDTO to save.
     * @param transformWatermarkJobDTO the transformWatermarkJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformWatermarkJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformWatermarkJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transformWatermarkJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransformWatermarkJobDTO> updateTransformWatermarkJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransformWatermarkJobDTO transformWatermarkJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransformWatermarkJob : {}, {}", id, transformWatermarkJobDTO);
        if (transformWatermarkJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformWatermarkJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformWatermarkJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transformWatermarkJobDTO = transformWatermarkJobService.update(transformWatermarkJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformWatermarkJobDTO.getId().toString()))
            .body(transformWatermarkJobDTO);
    }

    /**
     * {@code PATCH  /transform-watermark-jobs/:id} : Partial updates given fields of an existing transformWatermarkJob, field will ignore if it is null
     *
     * @param id the id of the transformWatermarkJobDTO to save.
     * @param transformWatermarkJobDTO the transformWatermarkJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformWatermarkJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformWatermarkJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transformWatermarkJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transformWatermarkJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransformWatermarkJobDTO> partialUpdateTransformWatermarkJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransformWatermarkJobDTO transformWatermarkJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransformWatermarkJob partially : {}, {}", id, transformWatermarkJobDTO);
        if (transformWatermarkJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformWatermarkJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformWatermarkJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransformWatermarkJobDTO> result = transformWatermarkJobService.partialUpdate(transformWatermarkJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformWatermarkJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transform-watermark-jobs} : get all the transformWatermarkJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transformWatermarkJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransformWatermarkJobDTO>> getAllTransformWatermarkJobs(
        TransformWatermarkJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransformWatermarkJobs by criteria: {}", criteria);

        Page<TransformWatermarkJobDTO> page = transformWatermarkJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transform-watermark-jobs/count} : count all the transformWatermarkJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransformWatermarkJobs(TransformWatermarkJobCriteria criteria) {
        LOG.debug("REST request to count TransformWatermarkJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(transformWatermarkJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transform-watermark-jobs/:id} : get the "id" transformWatermarkJob.
     *
     * @param id the id of the transformWatermarkJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transformWatermarkJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransformWatermarkJobDTO> getTransformWatermarkJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransformWatermarkJob : {}", id);
        Optional<TransformWatermarkJobDTO> transformWatermarkJobDTO = transformWatermarkJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transformWatermarkJobDTO);
    }

    /**
     * {@code DELETE  /transform-watermark-jobs/:id} : delete the "id" transformWatermarkJob.
     *
     * @param id the id of the transformWatermarkJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransformWatermarkJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransformWatermarkJob : {}", id);
        transformWatermarkJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
