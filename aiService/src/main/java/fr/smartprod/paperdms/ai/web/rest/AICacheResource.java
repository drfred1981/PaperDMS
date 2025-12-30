package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AICacheRepository;
import fr.smartprod.paperdms.ai.service.AICacheQueryService;
import fr.smartprod.paperdms.ai.service.AICacheService;
import fr.smartprod.paperdms.ai.service.criteria.AICacheCriteria;
import fr.smartprod.paperdms.ai.service.dto.AICacheDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AICache}.
 */
@RestController
@RequestMapping("/api/ai-caches")
public class AICacheResource {

    private static final Logger LOG = LoggerFactory.getLogger(AICacheResource.class);

    private static final String ENTITY_NAME = "aiServiceAiCache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AICacheService aICacheService;

    private final AICacheRepository aICacheRepository;

    private final AICacheQueryService aICacheQueryService;

    public AICacheResource(AICacheService aICacheService, AICacheRepository aICacheRepository, AICacheQueryService aICacheQueryService) {
        this.aICacheService = aICacheService;
        this.aICacheRepository = aICacheRepository;
        this.aICacheQueryService = aICacheQueryService;
    }

    /**
     * {@code POST  /ai-caches} : Create a new aICache.
     *
     * @param aICacheDTO the aICacheDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aICacheDTO, or with status {@code 400 (Bad Request)} if the aICache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AICacheDTO> createAICache(@Valid @RequestBody AICacheDTO aICacheDTO) throws URISyntaxException {
        LOG.debug("REST request to save AICache : {}", aICacheDTO);
        if (aICacheDTO.getId() != null) {
            throw new BadRequestAlertException("A new aICache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aICacheDTO = aICacheService.save(aICacheDTO);
        return ResponseEntity.created(new URI("/api/ai-caches/" + aICacheDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aICacheDTO.getId().toString()))
            .body(aICacheDTO);
    }

    /**
     * {@code PUT  /ai-caches/:id} : Updates an existing aICache.
     *
     * @param id the id of the aICacheDTO to save.
     * @param aICacheDTO the aICacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aICacheDTO,
     * or with status {@code 400 (Bad Request)} if the aICacheDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aICacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AICacheDTO> updateAICache(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AICacheDTO aICacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AICache : {}, {}", id, aICacheDTO);
        if (aICacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aICacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aICacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aICacheDTO = aICacheService.update(aICacheDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aICacheDTO.getId().toString()))
            .body(aICacheDTO);
    }

    /**
     * {@code PATCH  /ai-caches/:id} : Partial updates given fields of an existing aICache, field will ignore if it is null
     *
     * @param id the id of the aICacheDTO to save.
     * @param aICacheDTO the aICacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aICacheDTO,
     * or with status {@code 400 (Bad Request)} if the aICacheDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aICacheDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aICacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AICacheDTO> partialUpdateAICache(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AICacheDTO aICacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AICache partially : {}, {}", id, aICacheDTO);
        if (aICacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aICacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aICacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AICacheDTO> result = aICacheService.partialUpdate(aICacheDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aICacheDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-caches} : get all the aICaches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aICaches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AICacheDTO>> getAllAICaches(
        AICacheCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AICaches by criteria: {}", criteria);

        Page<AICacheDTO> page = aICacheQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-caches/count} : count all the aICaches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAICaches(AICacheCriteria criteria) {
        LOG.debug("REST request to count AICaches by criteria: {}", criteria);
        return ResponseEntity.ok().body(aICacheQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-caches/:id} : get the "id" aICache.
     *
     * @param id the id of the aICacheDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aICacheDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AICacheDTO> getAICache(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AICache : {}", id);
        Optional<AICacheDTO> aICacheDTO = aICacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aICacheDTO);
    }

    /**
     * {@code DELETE  /ai-caches/:id} : delete the "id" aICache.
     *
     * @param id the id of the aICacheDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAICache(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AICache : {}", id);
        aICacheService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ai-caches/_search?query=:query} : search for the aICache corresponding
     * to the query.
     *
     * @param query the query of the aICache search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AICacheDTO>> searchAICaches(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AICaches for query {}", query);
        try {
            Page<AICacheDTO> page = aICacheService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
