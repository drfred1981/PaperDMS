package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AILanguageDetectionRepository;
import fr.smartprod.paperdms.ai.service.AILanguageDetectionQueryService;
import fr.smartprod.paperdms.ai.service.AILanguageDetectionService;
import fr.smartprod.paperdms.ai.service.criteria.AILanguageDetectionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AILanguageDetectionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AILanguageDetection}.
 */
@RestController
@RequestMapping("/api/ai-language-detections")
public class AILanguageDetectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AILanguageDetectionResource.class);

    private static final String ENTITY_NAME = "aiServiceAiLanguageDetection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AILanguageDetectionService aILanguageDetectionService;

    private final AILanguageDetectionRepository aILanguageDetectionRepository;

    private final AILanguageDetectionQueryService aILanguageDetectionQueryService;

    public AILanguageDetectionResource(
        AILanguageDetectionService aILanguageDetectionService,
        AILanguageDetectionRepository aILanguageDetectionRepository,
        AILanguageDetectionQueryService aILanguageDetectionQueryService
    ) {
        this.aILanguageDetectionService = aILanguageDetectionService;
        this.aILanguageDetectionRepository = aILanguageDetectionRepository;
        this.aILanguageDetectionQueryService = aILanguageDetectionQueryService;
    }

    /**
     * {@code POST  /ai-language-detections} : Create a new aILanguageDetection.
     *
     * @param aILanguageDetectionDTO the aILanguageDetectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aILanguageDetectionDTO, or with status {@code 400 (Bad Request)} if the aILanguageDetection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AILanguageDetectionDTO> createAILanguageDetection(
        @Valid @RequestBody AILanguageDetectionDTO aILanguageDetectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AILanguageDetection : {}", aILanguageDetectionDTO);
        if (aILanguageDetectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new aILanguageDetection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aILanguageDetectionDTO = aILanguageDetectionService.save(aILanguageDetectionDTO);
        return ResponseEntity.created(new URI("/api/ai-language-detections/" + aILanguageDetectionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aILanguageDetectionDTO.getId().toString()))
            .body(aILanguageDetectionDTO);
    }

    /**
     * {@code PUT  /ai-language-detections/:id} : Updates an existing aILanguageDetection.
     *
     * @param id the id of the aILanguageDetectionDTO to save.
     * @param aILanguageDetectionDTO the aILanguageDetectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aILanguageDetectionDTO,
     * or with status {@code 400 (Bad Request)} if the aILanguageDetectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aILanguageDetectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AILanguageDetectionDTO> updateAILanguageDetection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AILanguageDetectionDTO aILanguageDetectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AILanguageDetection : {}, {}", id, aILanguageDetectionDTO);
        if (aILanguageDetectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aILanguageDetectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aILanguageDetectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aILanguageDetectionDTO = aILanguageDetectionService.update(aILanguageDetectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aILanguageDetectionDTO.getId().toString()))
            .body(aILanguageDetectionDTO);
    }

    /**
     * {@code PATCH  /ai-language-detections/:id} : Partial updates given fields of an existing aILanguageDetection, field will ignore if it is null
     *
     * @param id the id of the aILanguageDetectionDTO to save.
     * @param aILanguageDetectionDTO the aILanguageDetectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aILanguageDetectionDTO,
     * or with status {@code 400 (Bad Request)} if the aILanguageDetectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aILanguageDetectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aILanguageDetectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AILanguageDetectionDTO> partialUpdateAILanguageDetection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AILanguageDetectionDTO aILanguageDetectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AILanguageDetection partially : {}, {}", id, aILanguageDetectionDTO);
        if (aILanguageDetectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aILanguageDetectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aILanguageDetectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AILanguageDetectionDTO> result = aILanguageDetectionService.partialUpdate(aILanguageDetectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aILanguageDetectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-language-detections} : get all the aILanguageDetections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aILanguageDetections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AILanguageDetectionDTO>> getAllAILanguageDetections(
        AILanguageDetectionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AILanguageDetections by criteria: {}", criteria);

        Page<AILanguageDetectionDTO> page = aILanguageDetectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-language-detections/count} : count all the aILanguageDetections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAILanguageDetections(AILanguageDetectionCriteria criteria) {
        LOG.debug("REST request to count AILanguageDetections by criteria: {}", criteria);
        return ResponseEntity.ok().body(aILanguageDetectionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-language-detections/:id} : get the "id" aILanguageDetection.
     *
     * @param id the id of the aILanguageDetectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aILanguageDetectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AILanguageDetectionDTO> getAILanguageDetection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AILanguageDetection : {}", id);
        Optional<AILanguageDetectionDTO> aILanguageDetectionDTO = aILanguageDetectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aILanguageDetectionDTO);
    }

    /**
     * {@code DELETE  /ai-language-detections/:id} : delete the "id" aILanguageDetection.
     *
     * @param id the id of the aILanguageDetectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAILanguageDetection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AILanguageDetection : {}", id);
        aILanguageDetectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ai-language-detections/_search?query=:query} : search for the aILanguageDetection corresponding
     * to the query.
     *
     * @param query the query of the aILanguageDetection search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AILanguageDetectionDTO>> searchAILanguageDetections(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AILanguageDetections for query {}", query);
        try {
            Page<AILanguageDetectionDTO> page = aILanguageDetectionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
