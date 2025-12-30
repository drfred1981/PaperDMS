package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AICorrespondentPredictionRepository;
import fr.smartprod.paperdms.ai.service.AICorrespondentPredictionQueryService;
import fr.smartprod.paperdms.ai.service.AICorrespondentPredictionService;
import fr.smartprod.paperdms.ai.service.criteria.AICorrespondentPredictionCriteria;
import fr.smartprod.paperdms.ai.service.dto.AICorrespondentPredictionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction}.
 */
@RestController
@RequestMapping("/api/ai-correspondent-predictions")
public class AICorrespondentPredictionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AICorrespondentPredictionResource.class);

    private static final String ENTITY_NAME = "aiServiceAiCorrespondentPrediction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AICorrespondentPredictionService aICorrespondentPredictionService;

    private final AICorrespondentPredictionRepository aICorrespondentPredictionRepository;

    private final AICorrespondentPredictionQueryService aICorrespondentPredictionQueryService;

    public AICorrespondentPredictionResource(
        AICorrespondentPredictionService aICorrespondentPredictionService,
        AICorrespondentPredictionRepository aICorrespondentPredictionRepository,
        AICorrespondentPredictionQueryService aICorrespondentPredictionQueryService
    ) {
        this.aICorrespondentPredictionService = aICorrespondentPredictionService;
        this.aICorrespondentPredictionRepository = aICorrespondentPredictionRepository;
        this.aICorrespondentPredictionQueryService = aICorrespondentPredictionQueryService;
    }

    /**
     * {@code POST  /ai-correspondent-predictions} : Create a new aICorrespondentPrediction.
     *
     * @param aICorrespondentPredictionDTO the aICorrespondentPredictionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aICorrespondentPredictionDTO, or with status {@code 400 (Bad Request)} if the aICorrespondentPrediction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AICorrespondentPredictionDTO> createAICorrespondentPrediction(
        @Valid @RequestBody AICorrespondentPredictionDTO aICorrespondentPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AICorrespondentPrediction : {}", aICorrespondentPredictionDTO);
        if (aICorrespondentPredictionDTO.getId() != null) {
            throw new BadRequestAlertException("A new aICorrespondentPrediction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aICorrespondentPredictionDTO = aICorrespondentPredictionService.save(aICorrespondentPredictionDTO);
        return ResponseEntity.created(new URI("/api/ai-correspondent-predictions/" + aICorrespondentPredictionDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aICorrespondentPredictionDTO.getId().toString())
            )
            .body(aICorrespondentPredictionDTO);
    }

    /**
     * {@code PUT  /ai-correspondent-predictions/:id} : Updates an existing aICorrespondentPrediction.
     *
     * @param id the id of the aICorrespondentPredictionDTO to save.
     * @param aICorrespondentPredictionDTO the aICorrespondentPredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aICorrespondentPredictionDTO,
     * or with status {@code 400 (Bad Request)} if the aICorrespondentPredictionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aICorrespondentPredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AICorrespondentPredictionDTO> updateAICorrespondentPrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AICorrespondentPredictionDTO aICorrespondentPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AICorrespondentPrediction : {}, {}", id, aICorrespondentPredictionDTO);
        if (aICorrespondentPredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aICorrespondentPredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aICorrespondentPredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aICorrespondentPredictionDTO = aICorrespondentPredictionService.update(aICorrespondentPredictionDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aICorrespondentPredictionDTO.getId().toString())
            )
            .body(aICorrespondentPredictionDTO);
    }

    /**
     * {@code PATCH  /ai-correspondent-predictions/:id} : Partial updates given fields of an existing aICorrespondentPrediction, field will ignore if it is null
     *
     * @param id the id of the aICorrespondentPredictionDTO to save.
     * @param aICorrespondentPredictionDTO the aICorrespondentPredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aICorrespondentPredictionDTO,
     * or with status {@code 400 (Bad Request)} if the aICorrespondentPredictionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aICorrespondentPredictionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aICorrespondentPredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AICorrespondentPredictionDTO> partialUpdateAICorrespondentPrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AICorrespondentPredictionDTO aICorrespondentPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AICorrespondentPrediction partially : {}, {}", id, aICorrespondentPredictionDTO);
        if (aICorrespondentPredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aICorrespondentPredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aICorrespondentPredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AICorrespondentPredictionDTO> result = aICorrespondentPredictionService.partialUpdate(aICorrespondentPredictionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aICorrespondentPredictionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-correspondent-predictions} : get all the aICorrespondentPredictions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aICorrespondentPredictions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AICorrespondentPredictionDTO>> getAllAICorrespondentPredictions(
        AICorrespondentPredictionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AICorrespondentPredictions by criteria: {}", criteria);

        Page<AICorrespondentPredictionDTO> page = aICorrespondentPredictionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-correspondent-predictions/count} : count all the aICorrespondentPredictions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAICorrespondentPredictions(AICorrespondentPredictionCriteria criteria) {
        LOG.debug("REST request to count AICorrespondentPredictions by criteria: {}", criteria);
        return ResponseEntity.ok().body(aICorrespondentPredictionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-correspondent-predictions/:id} : get the "id" aICorrespondentPrediction.
     *
     * @param id the id of the aICorrespondentPredictionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aICorrespondentPredictionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AICorrespondentPredictionDTO> getAICorrespondentPrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AICorrespondentPrediction : {}", id);
        Optional<AICorrespondentPredictionDTO> aICorrespondentPredictionDTO = aICorrespondentPredictionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aICorrespondentPredictionDTO);
    }

    /**
     * {@code DELETE  /ai-correspondent-predictions/:id} : delete the "id" aICorrespondentPrediction.
     *
     * @param id the id of the aICorrespondentPredictionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAICorrespondentPrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AICorrespondentPrediction : {}", id);
        aICorrespondentPredictionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ai-correspondent-predictions/_search?query=:query} : search for the aICorrespondentPrediction corresponding
     * to the query.
     *
     * @param query the query of the aICorrespondentPrediction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AICorrespondentPredictionDTO>> searchAICorrespondentPredictions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AICorrespondentPredictions for query {}", query);
        try {
            Page<AICorrespondentPredictionDTO> page = aICorrespondentPredictionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
