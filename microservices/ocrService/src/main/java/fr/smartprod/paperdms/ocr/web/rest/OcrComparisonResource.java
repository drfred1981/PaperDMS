package fr.smartprod.paperdms.ocr.web.rest;

import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
import fr.smartprod.paperdms.ocr.service.OcrComparisonQueryService;
import fr.smartprod.paperdms.ocr.service.OcrComparisonService;
import fr.smartprod.paperdms.ocr.service.criteria.OcrComparisonCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ocr.domain.OcrComparison}.
 */
@RestController
@RequestMapping("/api/ocr-comparisons")
public class OcrComparisonResource {

    private static final Logger LOG = LoggerFactory.getLogger(OcrComparisonResource.class);

    private static final String ENTITY_NAME = "ocrServiceOcrComparison";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OcrComparisonService ocrComparisonService;

    private final OcrComparisonRepository ocrComparisonRepository;

    private final OcrComparisonQueryService ocrComparisonQueryService;

    public OcrComparisonResource(
        OcrComparisonService ocrComparisonService,
        OcrComparisonRepository ocrComparisonRepository,
        OcrComparisonQueryService ocrComparisonQueryService
    ) {
        this.ocrComparisonService = ocrComparisonService;
        this.ocrComparisonRepository = ocrComparisonRepository;
        this.ocrComparisonQueryService = ocrComparisonQueryService;
    }

    /**
     * {@code POST  /ocr-comparisons} : Create a new ocrComparison.
     *
     * @param ocrComparisonDTO the ocrComparisonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocrComparisonDTO, or with status {@code 400 (Bad Request)} if the ocrComparison has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OcrComparisonDTO> createOcrComparison(@Valid @RequestBody OcrComparisonDTO ocrComparisonDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OcrComparison : {}", ocrComparisonDTO);
        if (ocrComparisonDTO.getId() != null) {
            throw new BadRequestAlertException("A new ocrComparison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ocrComparisonDTO = ocrComparisonService.save(ocrComparisonDTO);
        return ResponseEntity.created(new URI("/api/ocr-comparisons/" + ocrComparisonDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ocrComparisonDTO.getId().toString()))
            .body(ocrComparisonDTO);
    }

    /**
     * {@code PUT  /ocr-comparisons/:id} : Updates an existing ocrComparison.
     *
     * @param id the id of the ocrComparisonDTO to save.
     * @param ocrComparisonDTO the ocrComparisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrComparisonDTO,
     * or with status {@code 400 (Bad Request)} if the ocrComparisonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocrComparisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcrComparisonDTO> updateOcrComparison(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OcrComparisonDTO ocrComparisonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OcrComparison : {}, {}", id, ocrComparisonDTO);
        if (ocrComparisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrComparisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrComparisonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ocrComparisonDTO = ocrComparisonService.update(ocrComparisonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrComparisonDTO.getId().toString()))
            .body(ocrComparisonDTO);
    }

    /**
     * {@code PATCH  /ocr-comparisons/:id} : Partial updates given fields of an existing ocrComparison, field will ignore if it is null
     *
     * @param id the id of the ocrComparisonDTO to save.
     * @param ocrComparisonDTO the ocrComparisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrComparisonDTO,
     * or with status {@code 400 (Bad Request)} if the ocrComparisonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ocrComparisonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ocrComparisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OcrComparisonDTO> partialUpdateOcrComparison(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OcrComparisonDTO ocrComparisonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OcrComparison partially : {}, {}", id, ocrComparisonDTO);
        if (ocrComparisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrComparisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrComparisonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OcrComparisonDTO> result = ocrComparisonService.partialUpdate(ocrComparisonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrComparisonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ocr-comparisons} : get all the ocrComparisons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocrComparisons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OcrComparisonDTO>> getAllOcrComparisons(
        OcrComparisonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OcrComparisons by criteria: {}", criteria);

        Page<OcrComparisonDTO> page = ocrComparisonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ocr-comparisons/count} : count all the ocrComparisons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOcrComparisons(OcrComparisonCriteria criteria) {
        LOG.debug("REST request to count OcrComparisons by criteria: {}", criteria);
        return ResponseEntity.ok().body(ocrComparisonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ocr-comparisons/:id} : get the "id" ocrComparison.
     *
     * @param id the id of the ocrComparisonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocrComparisonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcrComparisonDTO> getOcrComparison(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OcrComparison : {}", id);
        Optional<OcrComparisonDTO> ocrComparisonDTO = ocrComparisonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ocrComparisonDTO);
    }

    /**
     * {@code DELETE  /ocr-comparisons/:id} : delete the "id" ocrComparison.
     *
     * @param id the id of the ocrComparisonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcrComparison(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OcrComparison : {}", id);
        ocrComparisonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ocr-comparisons/_search?query=:query} : search for the ocrComparison corresponding
     * to the query.
     *
     * @param query the query of the ocrComparison search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<OcrComparisonDTO>> searchOcrComparisons(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of OcrComparisons for query {}", query);
        try {
            Page<OcrComparisonDTO> page = ocrComparisonService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
