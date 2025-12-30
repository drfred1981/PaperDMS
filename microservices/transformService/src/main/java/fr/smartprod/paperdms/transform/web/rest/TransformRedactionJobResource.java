package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.TransformRedactionJobRepository;
import fr.smartprod.paperdms.transform.service.TransformRedactionJobQueryService;
import fr.smartprod.paperdms.transform.service.TransformRedactionJobService;
import fr.smartprod.paperdms.transform.service.criteria.TransformRedactionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformRedactionJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.TransformRedactionJob}.
 */
@RestController
@RequestMapping("/api/transform-redaction-jobs")
public class TransformRedactionJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransformRedactionJobResource.class);

    private static final String ENTITY_NAME = "transformServiceTransformRedactionJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransformRedactionJobService transformRedactionJobService;

    private final TransformRedactionJobRepository transformRedactionJobRepository;

    private final TransformRedactionJobQueryService transformRedactionJobQueryService;

    public TransformRedactionJobResource(
        TransformRedactionJobService transformRedactionJobService,
        TransformRedactionJobRepository transformRedactionJobRepository,
        TransformRedactionJobQueryService transformRedactionJobQueryService
    ) {
        this.transformRedactionJobService = transformRedactionJobService;
        this.transformRedactionJobRepository = transformRedactionJobRepository;
        this.transformRedactionJobQueryService = transformRedactionJobQueryService;
    }

    /**
     * {@code POST  /transform-redaction-jobs} : Create a new transformRedactionJob.
     *
     * @param transformRedactionJobDTO the transformRedactionJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transformRedactionJobDTO, or with status {@code 400 (Bad Request)} if the transformRedactionJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransformRedactionJobDTO> createTransformRedactionJob(
        @Valid @RequestBody TransformRedactionJobDTO transformRedactionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TransformRedactionJob : {}", transformRedactionJobDTO);
        if (transformRedactionJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new transformRedactionJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transformRedactionJobDTO = transformRedactionJobService.save(transformRedactionJobDTO);
        return ResponseEntity.created(new URI("/api/transform-redaction-jobs/" + transformRedactionJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transformRedactionJobDTO.getId().toString()))
            .body(transformRedactionJobDTO);
    }

    /**
     * {@code PUT  /transform-redaction-jobs/:id} : Updates an existing transformRedactionJob.
     *
     * @param id the id of the transformRedactionJobDTO to save.
     * @param transformRedactionJobDTO the transformRedactionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformRedactionJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformRedactionJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transformRedactionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransformRedactionJobDTO> updateTransformRedactionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransformRedactionJobDTO transformRedactionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransformRedactionJob : {}, {}", id, transformRedactionJobDTO);
        if (transformRedactionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformRedactionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformRedactionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transformRedactionJobDTO = transformRedactionJobService.update(transformRedactionJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformRedactionJobDTO.getId().toString()))
            .body(transformRedactionJobDTO);
    }

    /**
     * {@code PATCH  /transform-redaction-jobs/:id} : Partial updates given fields of an existing transformRedactionJob, field will ignore if it is null
     *
     * @param id the id of the transformRedactionJobDTO to save.
     * @param transformRedactionJobDTO the transformRedactionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformRedactionJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformRedactionJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transformRedactionJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transformRedactionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransformRedactionJobDTO> partialUpdateTransformRedactionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransformRedactionJobDTO transformRedactionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransformRedactionJob partially : {}, {}", id, transformRedactionJobDTO);
        if (transformRedactionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformRedactionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformRedactionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransformRedactionJobDTO> result = transformRedactionJobService.partialUpdate(transformRedactionJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformRedactionJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transform-redaction-jobs} : get all the transformRedactionJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transformRedactionJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransformRedactionJobDTO>> getAllTransformRedactionJobs(
        TransformRedactionJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransformRedactionJobs by criteria: {}", criteria);

        Page<TransformRedactionJobDTO> page = transformRedactionJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transform-redaction-jobs/count} : count all the transformRedactionJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransformRedactionJobs(TransformRedactionJobCriteria criteria) {
        LOG.debug("REST request to count TransformRedactionJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(transformRedactionJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transform-redaction-jobs/:id} : get the "id" transformRedactionJob.
     *
     * @param id the id of the transformRedactionJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transformRedactionJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransformRedactionJobDTO> getTransformRedactionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransformRedactionJob : {}", id);
        Optional<TransformRedactionJobDTO> transformRedactionJobDTO = transformRedactionJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transformRedactionJobDTO);
    }

    /**
     * {@code DELETE  /transform-redaction-jobs/:id} : delete the "id" transformRedactionJob.
     *
     * @param id the id of the transformRedactionJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransformRedactionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransformRedactionJob : {}", id);
        transformRedactionJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
