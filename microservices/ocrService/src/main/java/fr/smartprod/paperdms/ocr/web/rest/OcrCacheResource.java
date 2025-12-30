package fr.smartprod.paperdms.ocr.web.rest;

import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
import fr.smartprod.paperdms.ocr.service.OcrCacheQueryService;
import fr.smartprod.paperdms.ocr.service.OcrCacheService;
import fr.smartprod.paperdms.ocr.service.criteria.OcrCacheCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import fr.smartprod.paperdms.ocr.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.ocr.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ocr.domain.OcrCache}.
 */
@RestController
@RequestMapping("/api/ocr-caches")
public class OcrCacheResource {

    private static final Logger LOG = LoggerFactory.getLogger(OcrCacheResource.class);

    private static final String ENTITY_NAME = "ocrServiceOcrCache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OcrCacheService ocrCacheService;

    private final OcrCacheRepository ocrCacheRepository;

    private final OcrCacheQueryService ocrCacheQueryService;

    public OcrCacheResource(
        OcrCacheService ocrCacheService,
        OcrCacheRepository ocrCacheRepository,
        OcrCacheQueryService ocrCacheQueryService
    ) {
        this.ocrCacheService = ocrCacheService;
        this.ocrCacheRepository = ocrCacheRepository;
        this.ocrCacheQueryService = ocrCacheQueryService;
    }

    /**
     * {@code POST  /ocr-caches} : Create a new ocrCache.
     *
     * @param ocrCacheDTO the ocrCacheDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocrCacheDTO, or with status {@code 400 (Bad Request)} if the ocrCache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OcrCacheDTO> createOcrCache(@Valid @RequestBody OcrCacheDTO ocrCacheDTO) throws URISyntaxException {
        LOG.debug("REST request to save OcrCache : {}", ocrCacheDTO);
        if (ocrCacheDTO.getId() != null) {
            throw new BadRequestAlertException("A new ocrCache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ocrCacheDTO = ocrCacheService.save(ocrCacheDTO);
        return ResponseEntity.created(new URI("/api/ocr-caches/" + ocrCacheDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ocrCacheDTO.getId().toString()))
            .body(ocrCacheDTO);
    }

    /**
     * {@code PUT  /ocr-caches/:id} : Updates an existing ocrCache.
     *
     * @param id the id of the ocrCacheDTO to save.
     * @param ocrCacheDTO the ocrCacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrCacheDTO,
     * or with status {@code 400 (Bad Request)} if the ocrCacheDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocrCacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcrCacheDTO> updateOcrCache(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OcrCacheDTO ocrCacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OcrCache : {}, {}", id, ocrCacheDTO);
        if (ocrCacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrCacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrCacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ocrCacheDTO = ocrCacheService.update(ocrCacheDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrCacheDTO.getId().toString()))
            .body(ocrCacheDTO);
    }

    /**
     * {@code PATCH  /ocr-caches/:id} : Partial updates given fields of an existing ocrCache, field will ignore if it is null
     *
     * @param id the id of the ocrCacheDTO to save.
     * @param ocrCacheDTO the ocrCacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrCacheDTO,
     * or with status {@code 400 (Bad Request)} if the ocrCacheDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ocrCacheDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ocrCacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OcrCacheDTO> partialUpdateOcrCache(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OcrCacheDTO ocrCacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OcrCache partially : {}, {}", id, ocrCacheDTO);
        if (ocrCacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrCacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrCacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OcrCacheDTO> result = ocrCacheService.partialUpdate(ocrCacheDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrCacheDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ocr-caches} : get all the ocrCaches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocrCaches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OcrCacheDTO>> getAllOcrCaches(
        OcrCacheCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OcrCaches by criteria: {}", criteria);

        Page<OcrCacheDTO> page = ocrCacheQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ocr-caches/count} : count all the ocrCaches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOcrCaches(OcrCacheCriteria criteria) {
        LOG.debug("REST request to count OcrCaches by criteria: {}", criteria);
        return ResponseEntity.ok().body(ocrCacheQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ocr-caches/:id} : get the "id" ocrCache.
     *
     * @param id the id of the ocrCacheDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocrCacheDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcrCacheDTO> getOcrCache(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OcrCache : {}", id);
        Optional<OcrCacheDTO> ocrCacheDTO = ocrCacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ocrCacheDTO);
    }

    /**
     * {@code DELETE  /ocr-caches/:id} : delete the "id" ocrCache.
     *
     * @param id the id of the ocrCacheDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcrCache(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OcrCache : {}", id);
        ocrCacheService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ocr-caches/_search?query=:query} : search for the ocrCache corresponding
     * to the query.
     *
     * @param query the query of the ocrCache search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<OcrCacheDTO>> searchOcrCaches(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of OcrCaches for query {}", query);
        try {
            Page<OcrCacheDTO> page = ocrCacheService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
