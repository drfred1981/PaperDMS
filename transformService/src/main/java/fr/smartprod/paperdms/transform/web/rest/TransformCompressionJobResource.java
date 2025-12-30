package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.TransformCompressionJobRepository;
import fr.smartprod.paperdms.transform.service.TransformCompressionJobQueryService;
import fr.smartprod.paperdms.transform.service.TransformCompressionJobService;
import fr.smartprod.paperdms.transform.service.criteria.TransformCompressionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformCompressionJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.TransformCompressionJob}.
 */
@RestController
@RequestMapping("/api/transform-compression-jobs")
public class TransformCompressionJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransformCompressionJobResource.class);

    private static final String ENTITY_NAME = "transformServiceTransformCompressionJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransformCompressionJobService transformCompressionJobService;

    private final TransformCompressionJobRepository transformCompressionJobRepository;

    private final TransformCompressionJobQueryService transformCompressionJobQueryService;

    public TransformCompressionJobResource(
        TransformCompressionJobService transformCompressionJobService,
        TransformCompressionJobRepository transformCompressionJobRepository,
        TransformCompressionJobQueryService transformCompressionJobQueryService
    ) {
        this.transformCompressionJobService = transformCompressionJobService;
        this.transformCompressionJobRepository = transformCompressionJobRepository;
        this.transformCompressionJobQueryService = transformCompressionJobQueryService;
    }

    /**
     * {@code POST  /transform-compression-jobs} : Create a new transformCompressionJob.
     *
     * @param transformCompressionJobDTO the transformCompressionJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transformCompressionJobDTO, or with status {@code 400 (Bad Request)} if the transformCompressionJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransformCompressionJobDTO> createTransformCompressionJob(
        @Valid @RequestBody TransformCompressionJobDTO transformCompressionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TransformCompressionJob : {}", transformCompressionJobDTO);
        if (transformCompressionJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new transformCompressionJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transformCompressionJobDTO = transformCompressionJobService.save(transformCompressionJobDTO);
        return ResponseEntity.created(new URI("/api/transform-compression-jobs/" + transformCompressionJobDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transformCompressionJobDTO.getId().toString())
            )
            .body(transformCompressionJobDTO);
    }

    /**
     * {@code PUT  /transform-compression-jobs/:id} : Updates an existing transformCompressionJob.
     *
     * @param id the id of the transformCompressionJobDTO to save.
     * @param transformCompressionJobDTO the transformCompressionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformCompressionJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformCompressionJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transformCompressionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransformCompressionJobDTO> updateTransformCompressionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransformCompressionJobDTO transformCompressionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransformCompressionJob : {}, {}", id, transformCompressionJobDTO);
        if (transformCompressionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformCompressionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformCompressionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transformCompressionJobDTO = transformCompressionJobService.update(transformCompressionJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformCompressionJobDTO.getId().toString()))
            .body(transformCompressionJobDTO);
    }

    /**
     * {@code PATCH  /transform-compression-jobs/:id} : Partial updates given fields of an existing transformCompressionJob, field will ignore if it is null
     *
     * @param id the id of the transformCompressionJobDTO to save.
     * @param transformCompressionJobDTO the transformCompressionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformCompressionJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformCompressionJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transformCompressionJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transformCompressionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransformCompressionJobDTO> partialUpdateTransformCompressionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransformCompressionJobDTO transformCompressionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransformCompressionJob partially : {}, {}", id, transformCompressionJobDTO);
        if (transformCompressionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformCompressionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformCompressionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransformCompressionJobDTO> result = transformCompressionJobService.partialUpdate(transformCompressionJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformCompressionJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transform-compression-jobs} : get all the transformCompressionJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transformCompressionJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransformCompressionJobDTO>> getAllTransformCompressionJobs(
        TransformCompressionJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransformCompressionJobs by criteria: {}", criteria);

        Page<TransformCompressionJobDTO> page = transformCompressionJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transform-compression-jobs/count} : count all the transformCompressionJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransformCompressionJobs(TransformCompressionJobCriteria criteria) {
        LOG.debug("REST request to count TransformCompressionJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(transformCompressionJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transform-compression-jobs/:id} : get the "id" transformCompressionJob.
     *
     * @param id the id of the transformCompressionJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transformCompressionJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransformCompressionJobDTO> getTransformCompressionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransformCompressionJob : {}", id);
        Optional<TransformCompressionJobDTO> transformCompressionJobDTO = transformCompressionJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transformCompressionJobDTO);
    }

    /**
     * {@code DELETE  /transform-compression-jobs/:id} : delete the "id" transformCompressionJob.
     *
     * @param id the id of the transformCompressionJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransformCompressionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransformCompressionJob : {}", id);
        transformCompressionJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
