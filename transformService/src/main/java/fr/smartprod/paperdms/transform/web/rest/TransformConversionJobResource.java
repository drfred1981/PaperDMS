package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.TransformConversionJobRepository;
import fr.smartprod.paperdms.transform.service.TransformConversionJobQueryService;
import fr.smartprod.paperdms.transform.service.TransformConversionJobService;
import fr.smartprod.paperdms.transform.service.criteria.TransformConversionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.TransformConversionJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.TransformConversionJob}.
 */
@RestController
@RequestMapping("/api/transform-conversion-jobs")
public class TransformConversionJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransformConversionJobResource.class);

    private static final String ENTITY_NAME = "transformServiceTransformConversionJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransformConversionJobService transformConversionJobService;

    private final TransformConversionJobRepository transformConversionJobRepository;

    private final TransformConversionJobQueryService transformConversionJobQueryService;

    public TransformConversionJobResource(
        TransformConversionJobService transformConversionJobService,
        TransformConversionJobRepository transformConversionJobRepository,
        TransformConversionJobQueryService transformConversionJobQueryService
    ) {
        this.transformConversionJobService = transformConversionJobService;
        this.transformConversionJobRepository = transformConversionJobRepository;
        this.transformConversionJobQueryService = transformConversionJobQueryService;
    }

    /**
     * {@code POST  /transform-conversion-jobs} : Create a new transformConversionJob.
     *
     * @param transformConversionJobDTO the transformConversionJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transformConversionJobDTO, or with status {@code 400 (Bad Request)} if the transformConversionJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransformConversionJobDTO> createTransformConversionJob(
        @Valid @RequestBody TransformConversionJobDTO transformConversionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TransformConversionJob : {}", transformConversionJobDTO);
        if (transformConversionJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new transformConversionJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transformConversionJobDTO = transformConversionJobService.save(transformConversionJobDTO);
        return ResponseEntity.created(new URI("/api/transform-conversion-jobs/" + transformConversionJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transformConversionJobDTO.getId().toString()))
            .body(transformConversionJobDTO);
    }

    /**
     * {@code PUT  /transform-conversion-jobs/:id} : Updates an existing transformConversionJob.
     *
     * @param id the id of the transformConversionJobDTO to save.
     * @param transformConversionJobDTO the transformConversionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformConversionJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformConversionJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transformConversionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransformConversionJobDTO> updateTransformConversionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransformConversionJobDTO transformConversionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransformConversionJob : {}, {}", id, transformConversionJobDTO);
        if (transformConversionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformConversionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformConversionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transformConversionJobDTO = transformConversionJobService.update(transformConversionJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformConversionJobDTO.getId().toString()))
            .body(transformConversionJobDTO);
    }

    /**
     * {@code PATCH  /transform-conversion-jobs/:id} : Partial updates given fields of an existing transformConversionJob, field will ignore if it is null
     *
     * @param id the id of the transformConversionJobDTO to save.
     * @param transformConversionJobDTO the transformConversionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transformConversionJobDTO,
     * or with status {@code 400 (Bad Request)} if the transformConversionJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transformConversionJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transformConversionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransformConversionJobDTO> partialUpdateTransformConversionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransformConversionJobDTO transformConversionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransformConversionJob partially : {}, {}", id, transformConversionJobDTO);
        if (transformConversionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transformConversionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transformConversionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransformConversionJobDTO> result = transformConversionJobService.partialUpdate(transformConversionJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transformConversionJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transform-conversion-jobs} : get all the transformConversionJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transformConversionJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransformConversionJobDTO>> getAllTransformConversionJobs(
        TransformConversionJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransformConversionJobs by criteria: {}", criteria);

        Page<TransformConversionJobDTO> page = transformConversionJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transform-conversion-jobs/count} : count all the transformConversionJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransformConversionJobs(TransformConversionJobCriteria criteria) {
        LOG.debug("REST request to count TransformConversionJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(transformConversionJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transform-conversion-jobs/:id} : get the "id" transformConversionJob.
     *
     * @param id the id of the transformConversionJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transformConversionJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransformConversionJobDTO> getTransformConversionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransformConversionJob : {}", id);
        Optional<TransformConversionJobDTO> transformConversionJobDTO = transformConversionJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transformConversionJobDTO);
    }

    /**
     * {@code DELETE  /transform-conversion-jobs/:id} : delete the "id" transformConversionJob.
     *
     * @param id the id of the transformConversionJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransformConversionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransformConversionJob : {}", id);
        transformConversionJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
