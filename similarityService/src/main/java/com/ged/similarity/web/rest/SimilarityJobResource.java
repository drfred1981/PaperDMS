package com.ged.similarity.web.rest;

import com.ged.similarity.repository.SimilarityJobRepository;
import com.ged.similarity.service.SimilarityJobQueryService;
import com.ged.similarity.service.SimilarityJobService;
import com.ged.similarity.service.criteria.SimilarityJobCriteria;
import com.ged.similarity.service.dto.SimilarityJobDTO;
import com.ged.similarity.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ged.similarity.domain.SimilarityJob}.
 */
@RestController
@RequestMapping("/api/similarity-jobs")
public class SimilarityJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityJobResource.class);

    private static final String ENTITY_NAME = "similarityServiceSimilarityJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimilarityJobService similarityJobService;

    private final SimilarityJobRepository similarityJobRepository;

    private final SimilarityJobQueryService similarityJobQueryService;

    public SimilarityJobResource(
        SimilarityJobService similarityJobService,
        SimilarityJobRepository similarityJobRepository,
        SimilarityJobQueryService similarityJobQueryService
    ) {
        this.similarityJobService = similarityJobService;
        this.similarityJobRepository = similarityJobRepository;
        this.similarityJobQueryService = similarityJobQueryService;
    }

    /**
     * {@code POST  /similarity-jobs} : Create a new similarityJob.
     *
     * @param similarityJobDTO the similarityJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new similarityJobDTO, or with status {@code 400 (Bad Request)} if the similarityJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SimilarityJobDTO> createSimilarityJob(@Valid @RequestBody SimilarityJobDTO similarityJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SimilarityJob : {}", similarityJobDTO);
        if (similarityJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new similarityJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        similarityJobDTO = similarityJobService.save(similarityJobDTO);
        return ResponseEntity.created(new URI("/api/similarity-jobs/" + similarityJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, similarityJobDTO.getId().toString()))
            .body(similarityJobDTO);
    }

    /**
     * {@code PUT  /similarity-jobs/:id} : Updates an existing similarityJob.
     *
     * @param id the id of the similarityJobDTO to save.
     * @param similarityJobDTO the similarityJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityJobDTO,
     * or with status {@code 400 (Bad Request)} if the similarityJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the similarityJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SimilarityJobDTO> updateSimilarityJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SimilarityJobDTO similarityJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SimilarityJob : {}, {}", id, similarityJobDTO);
        if (similarityJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        similarityJobDTO = similarityJobService.update(similarityJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityJobDTO.getId().toString()))
            .body(similarityJobDTO);
    }

    /**
     * {@code PATCH  /similarity-jobs/:id} : Partial updates given fields of an existing similarityJob, field will ignore if it is null
     *
     * @param id the id of the similarityJobDTO to save.
     * @param similarityJobDTO the similarityJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityJobDTO,
     * or with status {@code 400 (Bad Request)} if the similarityJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the similarityJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the similarityJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SimilarityJobDTO> partialUpdateSimilarityJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SimilarityJobDTO similarityJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SimilarityJob partially : {}, {}", id, similarityJobDTO);
        if (similarityJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SimilarityJobDTO> result = similarityJobService.partialUpdate(similarityJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /similarity-jobs} : get all the similarityJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of similarityJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SimilarityJobDTO>> getAllSimilarityJobs(
        SimilarityJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SimilarityJobs by criteria: {}", criteria);

        Page<SimilarityJobDTO> page = similarityJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /similarity-jobs/count} : count all the similarityJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSimilarityJobs(SimilarityJobCriteria criteria) {
        LOG.debug("REST request to count SimilarityJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(similarityJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /similarity-jobs/:id} : get the "id" similarityJob.
     *
     * @param id the id of the similarityJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the similarityJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SimilarityJobDTO> getSimilarityJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SimilarityJob : {}", id);
        Optional<SimilarityJobDTO> similarityJobDTO = similarityJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(similarityJobDTO);
    }

    /**
     * {@code DELETE  /similarity-jobs/:id} : delete the "id" similarityJob.
     *
     * @param id the id of the similarityJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSimilarityJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SimilarityJob : {}", id);
        similarityJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
