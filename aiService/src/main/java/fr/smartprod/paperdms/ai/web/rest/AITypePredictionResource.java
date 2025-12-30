package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AITypePredictionRepository;
import fr.smartprod.paperdms.ai.service.AITypePredictionQueryService;
import fr.smartprod.paperdms.ai.service.AITypePredictionService;
import fr.smartprod.paperdms.ai.service.criteria.AITypePredictionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AITypePredictionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AITypePrediction}.
 */
@RestController
@RequestMapping("/api/ai-type-predictions")
public class AITypePredictionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AITypePredictionResource.class);

    private static final String ENTITY_NAME = "aiServiceAiTypePrediction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AITypePredictionService aITypePredictionService;

    private final AITypePredictionRepository aITypePredictionRepository;

    private final AITypePredictionQueryService aITypePredictionQueryService;

    public AITypePredictionResource(
        AITypePredictionService aITypePredictionService,
        AITypePredictionRepository aITypePredictionRepository,
        AITypePredictionQueryService aITypePredictionQueryService
    ) {
        this.aITypePredictionService = aITypePredictionService;
        this.aITypePredictionRepository = aITypePredictionRepository;
        this.aITypePredictionQueryService = aITypePredictionQueryService;
    }

    /**
     * {@code POST  /ai-type-predictions} : Create a new aITypePrediction.
     *
     * @param aITypePredictionDTO the aITypePredictionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aITypePredictionDTO, or with status {@code 400 (Bad Request)} if the aITypePrediction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AITypePredictionDTO> createAITypePrediction(@Valid @RequestBody AITypePredictionDTO aITypePredictionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AITypePrediction : {}", aITypePredictionDTO);
        if (aITypePredictionDTO.getId() != null) {
            throw new BadRequestAlertException("A new aITypePrediction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aITypePredictionDTO = aITypePredictionService.save(aITypePredictionDTO);
        return ResponseEntity.created(new URI("/api/ai-type-predictions/" + aITypePredictionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aITypePredictionDTO.getId().toString()))
            .body(aITypePredictionDTO);
    }

    /**
     * {@code PUT  /ai-type-predictions/:id} : Updates an existing aITypePrediction.
     *
     * @param id the id of the aITypePredictionDTO to save.
     * @param aITypePredictionDTO the aITypePredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aITypePredictionDTO,
     * or with status {@code 400 (Bad Request)} if the aITypePredictionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aITypePredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AITypePredictionDTO> updateAITypePrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AITypePredictionDTO aITypePredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AITypePrediction : {}, {}", id, aITypePredictionDTO);
        if (aITypePredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aITypePredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aITypePredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aITypePredictionDTO = aITypePredictionService.update(aITypePredictionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aITypePredictionDTO.getId().toString()))
            .body(aITypePredictionDTO);
    }

    /**
     * {@code PATCH  /ai-type-predictions/:id} : Partial updates given fields of an existing aITypePrediction, field will ignore if it is null
     *
     * @param id the id of the aITypePredictionDTO to save.
     * @param aITypePredictionDTO the aITypePredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aITypePredictionDTO,
     * or with status {@code 400 (Bad Request)} if the aITypePredictionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aITypePredictionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aITypePredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AITypePredictionDTO> partialUpdateAITypePrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AITypePredictionDTO aITypePredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AITypePrediction partially : {}, {}", id, aITypePredictionDTO);
        if (aITypePredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aITypePredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aITypePredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AITypePredictionDTO> result = aITypePredictionService.partialUpdate(aITypePredictionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aITypePredictionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-type-predictions} : get all the aITypePredictions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aITypePredictions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AITypePredictionDTO>> getAllAITypePredictions(
        AITypePredictionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AITypePredictions by criteria: {}", criteria);

        Page<AITypePredictionDTO> page = aITypePredictionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-type-predictions/count} : count all the aITypePredictions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAITypePredictions(AITypePredictionCriteria criteria) {
        LOG.debug("REST request to count AITypePredictions by criteria: {}", criteria);
        return ResponseEntity.ok().body(aITypePredictionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-type-predictions/:id} : get the "id" aITypePrediction.
     *
     * @param id the id of the aITypePredictionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aITypePredictionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AITypePredictionDTO> getAITypePrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AITypePrediction : {}", id);
        Optional<AITypePredictionDTO> aITypePredictionDTO = aITypePredictionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aITypePredictionDTO);
    }

    /**
     * {@code DELETE  /ai-type-predictions/:id} : delete the "id" aITypePrediction.
     *
     * @param id the id of the aITypePredictionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAITypePrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AITypePrediction : {}", id);
        aITypePredictionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ai-type-predictions/_search?query=:query} : search for the aITypePrediction corresponding
     * to the query.
     *
     * @param query the query of the aITypePrediction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AITypePredictionDTO>> searchAITypePredictions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AITypePredictions for query {}", query);
        try {
            Page<AITypePredictionDTO> page = aITypePredictionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
