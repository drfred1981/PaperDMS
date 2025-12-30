package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AIAutoTagJobRepository;
import fr.smartprod.paperdms.ai.service.AIAutoTagJobQueryService;
import fr.smartprod.paperdms.ai.service.AIAutoTagJobService;
import fr.smartprod.paperdms.ai.service.criteria.AIAutoTagJobCriteria;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.ai.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AIAutoTagJob}.
 */
@RestController
@RequestMapping("/api/ai-auto-tag-jobs")
public class AIAutoTagJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(AIAutoTagJobResource.class);

    private static final String ENTITY_NAME = "aiServiceAiAutoTagJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AIAutoTagJobService aIAutoTagJobService;

    private final AIAutoTagJobRepository aIAutoTagJobRepository;

    private final AIAutoTagJobQueryService aIAutoTagJobQueryService;

    public AIAutoTagJobResource(
        AIAutoTagJobService aIAutoTagJobService,
        AIAutoTagJobRepository aIAutoTagJobRepository,
        AIAutoTagJobQueryService aIAutoTagJobQueryService
    ) {
        this.aIAutoTagJobService = aIAutoTagJobService;
        this.aIAutoTagJobRepository = aIAutoTagJobRepository;
        this.aIAutoTagJobQueryService = aIAutoTagJobQueryService;
    }

    /**
     * {@code POST  /ai-auto-tag-jobs} : Create a new aIAutoTagJob.
     *
     * @param aIAutoTagJobDTO the aIAutoTagJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aIAutoTagJobDTO, or with status {@code 400 (Bad Request)} if the aIAutoTagJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AIAutoTagJobDTO> createAIAutoTagJob(@Valid @RequestBody AIAutoTagJobDTO aIAutoTagJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AIAutoTagJob : {}", aIAutoTagJobDTO);
        if (aIAutoTagJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new aIAutoTagJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aIAutoTagJobDTO = aIAutoTagJobService.save(aIAutoTagJobDTO);
        return ResponseEntity.created(new URI("/api/ai-auto-tag-jobs/" + aIAutoTagJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aIAutoTagJobDTO.getId().toString()))
            .body(aIAutoTagJobDTO);
    }

    /**
     * {@code PUT  /ai-auto-tag-jobs/:id} : Updates an existing aIAutoTagJob.
     *
     * @param id the id of the aIAutoTagJobDTO to save.
     * @param aIAutoTagJobDTO the aIAutoTagJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIAutoTagJobDTO,
     * or with status {@code 400 (Bad Request)} if the aIAutoTagJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aIAutoTagJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AIAutoTagJobDTO> updateAIAutoTagJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AIAutoTagJobDTO aIAutoTagJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AIAutoTagJob : {}, {}", id, aIAutoTagJobDTO);
        if (aIAutoTagJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIAutoTagJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aIAutoTagJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aIAutoTagJobDTO = aIAutoTagJobService.update(aIAutoTagJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIAutoTagJobDTO.getId().toString()))
            .body(aIAutoTagJobDTO);
    }

    /**
     * {@code PATCH  /ai-auto-tag-jobs/:id} : Partial updates given fields of an existing aIAutoTagJob, field will ignore if it is null
     *
     * @param id the id of the aIAutoTagJobDTO to save.
     * @param aIAutoTagJobDTO the aIAutoTagJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIAutoTagJobDTO,
     * or with status {@code 400 (Bad Request)} if the aIAutoTagJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aIAutoTagJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aIAutoTagJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AIAutoTagJobDTO> partialUpdateAIAutoTagJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AIAutoTagJobDTO aIAutoTagJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AIAutoTagJob partially : {}, {}", id, aIAutoTagJobDTO);
        if (aIAutoTagJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIAutoTagJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aIAutoTagJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AIAutoTagJobDTO> result = aIAutoTagJobService.partialUpdate(aIAutoTagJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIAutoTagJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-auto-tag-jobs} : get all the aIAutoTagJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aIAutoTagJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AIAutoTagJobDTO>> getAllAIAutoTagJobs(
        AIAutoTagJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AIAutoTagJobs by criteria: {}", criteria);

        Page<AIAutoTagJobDTO> page = aIAutoTagJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-auto-tag-jobs/count} : count all the aIAutoTagJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAIAutoTagJobs(AIAutoTagJobCriteria criteria) {
        LOG.debug("REST request to count AIAutoTagJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(aIAutoTagJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-auto-tag-jobs/:id} : get the "id" aIAutoTagJob.
     *
     * @param id the id of the aIAutoTagJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aIAutoTagJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AIAutoTagJobDTO> getAIAutoTagJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AIAutoTagJob : {}", id);
        Optional<AIAutoTagJobDTO> aIAutoTagJobDTO = aIAutoTagJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aIAutoTagJobDTO);
    }

    /**
     * {@code DELETE  /ai-auto-tag-jobs/:id} : delete the "id" aIAutoTagJob.
     *
     * @param id the id of the aIAutoTagJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAIAutoTagJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AIAutoTagJob : {}", id);
        aIAutoTagJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ai-auto-tag-jobs/_search?query=:query} : search for the aIAutoTagJob corresponding
     * to the query.
     *
     * @param query the query of the aIAutoTagJob search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AIAutoTagJobDTO>> searchAIAutoTagJobs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AIAutoTagJobs for query {}", query);
        try {
            Page<AIAutoTagJobDTO> page = aIAutoTagJobService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
