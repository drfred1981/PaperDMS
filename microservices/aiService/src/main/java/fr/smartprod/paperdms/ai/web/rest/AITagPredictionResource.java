package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AITagPredictionRepository;
import fr.smartprod.paperdms.ai.service.AITagPredictionQueryService;
import fr.smartprod.paperdms.ai.service.AITagPredictionService;
import fr.smartprod.paperdms.ai.service.criteria.AITagPredictionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AITagPredictionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AITagPrediction}.
 */
@RestController
@RequestMapping("/api/ai-tag-predictions")
public class AITagPredictionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AITagPredictionResource.class);

    private static final String ENTITY_NAME = "aiServiceAiTagPrediction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AITagPredictionService aITagPredictionService;

    private final AITagPredictionRepository aITagPredictionRepository;

    private final AITagPredictionQueryService aITagPredictionQueryService;

    public AITagPredictionResource(
        AITagPredictionService aITagPredictionService,
        AITagPredictionRepository aITagPredictionRepository,
        AITagPredictionQueryService aITagPredictionQueryService
    ) {
        this.aITagPredictionService = aITagPredictionService;
        this.aITagPredictionRepository = aITagPredictionRepository;
        this.aITagPredictionQueryService = aITagPredictionQueryService;
    }

    /**
     * {@code POST  /ai-tag-predictions} : Create a new aITagPrediction.
     *
     * @param aITagPredictionDTO the aITagPredictionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aITagPredictionDTO, or with status {@code 400 (Bad Request)} if the aITagPrediction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AITagPredictionDTO> createAITagPrediction(@Valid @RequestBody AITagPredictionDTO aITagPredictionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AITagPrediction : {}", aITagPredictionDTO);
        if (aITagPredictionDTO.getId() != null) {
            throw new BadRequestAlertException("A new aITagPrediction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aITagPredictionDTO = aITagPredictionService.save(aITagPredictionDTO);
        return ResponseEntity.created(new URI("/api/ai-tag-predictions/" + aITagPredictionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aITagPredictionDTO.getId().toString()))
            .body(aITagPredictionDTO);
    }

    /**
     * {@code PUT  /ai-tag-predictions/:id} : Updates an existing aITagPrediction.
     *
     * @param id the id of the aITagPredictionDTO to save.
     * @param aITagPredictionDTO the aITagPredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aITagPredictionDTO,
     * or with status {@code 400 (Bad Request)} if the aITagPredictionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aITagPredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AITagPredictionDTO> updateAITagPrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AITagPredictionDTO aITagPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AITagPrediction : {}, {}", id, aITagPredictionDTO);
        if (aITagPredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aITagPredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aITagPredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aITagPredictionDTO = aITagPredictionService.update(aITagPredictionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aITagPredictionDTO.getId().toString()))
            .body(aITagPredictionDTO);
    }

    /**
     * {@code PATCH  /ai-tag-predictions/:id} : Partial updates given fields of an existing aITagPrediction, field will ignore if it is null
     *
     * @param id the id of the aITagPredictionDTO to save.
     * @param aITagPredictionDTO the aITagPredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aITagPredictionDTO,
     * or with status {@code 400 (Bad Request)} if the aITagPredictionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aITagPredictionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aITagPredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AITagPredictionDTO> partialUpdateAITagPrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AITagPredictionDTO aITagPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AITagPrediction partially : {}, {}", id, aITagPredictionDTO);
        if (aITagPredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aITagPredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aITagPredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AITagPredictionDTO> result = aITagPredictionService.partialUpdate(aITagPredictionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aITagPredictionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-tag-predictions} : get all the aITagPredictions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aITagPredictions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AITagPredictionDTO>> getAllAITagPredictions(
        AITagPredictionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AITagPredictions by criteria: {}", criteria);

        Page<AITagPredictionDTO> page = aITagPredictionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-tag-predictions/count} : count all the aITagPredictions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAITagPredictions(AITagPredictionCriteria criteria) {
        LOG.debug("REST request to count AITagPredictions by criteria: {}", criteria);
        return ResponseEntity.ok().body(aITagPredictionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-tag-predictions/:id} : get the "id" aITagPrediction.
     *
     * @param id the id of the aITagPredictionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aITagPredictionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AITagPredictionDTO> getAITagPrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AITagPrediction : {}", id);
        Optional<AITagPredictionDTO> aITagPredictionDTO = aITagPredictionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aITagPredictionDTO);
    }

    /**
     * {@code DELETE  /ai-tag-predictions/:id} : delete the "id" aITagPrediction.
     *
     * @param id the id of the aITagPredictionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAITagPrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AITagPrediction : {}", id);
        aITagPredictionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ai-tag-predictions/_search?query=:query} : search for the aITagPrediction corresponding
     * to the query.
     *
     * @param query the query of the aITagPrediction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AITagPredictionDTO>> searchAITagPredictions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AITagPredictions for query {}", query);
        try {
            Page<AITagPredictionDTO> page = aITagPredictionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
