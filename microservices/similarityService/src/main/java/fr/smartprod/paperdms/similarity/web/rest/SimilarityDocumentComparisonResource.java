package fr.smartprod.paperdms.similarity.web.rest;

import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentComparisonRepository;
import fr.smartprod.paperdms.similarity.service.SimilarityDocumentComparisonQueryService;
import fr.smartprod.paperdms.similarity.service.SimilarityDocumentComparisonService;
import fr.smartprod.paperdms.similarity.service.criteria.SimilarityDocumentComparisonCriteria;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentComparisonDTO;
import fr.smartprod.paperdms.similarity.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.similarity.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison}.
 */
@RestController
@RequestMapping("/api/similarity-document-comparisons")
public class SimilarityDocumentComparisonResource {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDocumentComparisonResource.class);

    private static final String ENTITY_NAME = "similarityServiceSimilarityDocumentComparison";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimilarityDocumentComparisonService similarityDocumentComparisonService;

    private final SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository;

    private final SimilarityDocumentComparisonQueryService similarityDocumentComparisonQueryService;

    public SimilarityDocumentComparisonResource(
        SimilarityDocumentComparisonService similarityDocumentComparisonService,
        SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository,
        SimilarityDocumentComparisonQueryService similarityDocumentComparisonQueryService
    ) {
        this.similarityDocumentComparisonService = similarityDocumentComparisonService;
        this.similarityDocumentComparisonRepository = similarityDocumentComparisonRepository;
        this.similarityDocumentComparisonQueryService = similarityDocumentComparisonQueryService;
    }

    /**
     * {@code POST  /similarity-document-comparisons} : Create a new similarityDocumentComparison.
     *
     * @param similarityDocumentComparisonDTO the similarityDocumentComparisonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new similarityDocumentComparisonDTO, or with status {@code 400 (Bad Request)} if the similarityDocumentComparison has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SimilarityDocumentComparisonDTO> createSimilarityDocumentComparison(
        @Valid @RequestBody SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save SimilarityDocumentComparison : {}", similarityDocumentComparisonDTO);
        if (similarityDocumentComparisonDTO.getId() != null) {
            throw new BadRequestAlertException("A new similarityDocumentComparison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        similarityDocumentComparisonDTO = similarityDocumentComparisonService.save(similarityDocumentComparisonDTO);
        return ResponseEntity.created(new URI("/api/similarity-document-comparisons/" + similarityDocumentComparisonDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, similarityDocumentComparisonDTO.getId().toString())
            )
            .body(similarityDocumentComparisonDTO);
    }

    /**
     * {@code PUT  /similarity-document-comparisons/:id} : Updates an existing similarityDocumentComparison.
     *
     * @param id the id of the similarityDocumentComparisonDTO to save.
     * @param similarityDocumentComparisonDTO the similarityDocumentComparisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityDocumentComparisonDTO,
     * or with status {@code 400 (Bad Request)} if the similarityDocumentComparisonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the similarityDocumentComparisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SimilarityDocumentComparisonDTO> updateSimilarityDocumentComparison(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SimilarityDocumentComparison : {}, {}", id, similarityDocumentComparisonDTO);
        if (similarityDocumentComparisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityDocumentComparisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityDocumentComparisonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        similarityDocumentComparisonDTO = similarityDocumentComparisonService.update(similarityDocumentComparisonDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityDocumentComparisonDTO.getId().toString())
            )
            .body(similarityDocumentComparisonDTO);
    }

    /**
     * {@code PATCH  /similarity-document-comparisons/:id} : Partial updates given fields of an existing similarityDocumentComparison, field will ignore if it is null
     *
     * @param id the id of the similarityDocumentComparisonDTO to save.
     * @param similarityDocumentComparisonDTO the similarityDocumentComparisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityDocumentComparisonDTO,
     * or with status {@code 400 (Bad Request)} if the similarityDocumentComparisonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the similarityDocumentComparisonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the similarityDocumentComparisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SimilarityDocumentComparisonDTO> partialUpdateSimilarityDocumentComparison(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SimilarityDocumentComparison partially : {}, {}", id, similarityDocumentComparisonDTO);
        if (similarityDocumentComparisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityDocumentComparisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityDocumentComparisonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SimilarityDocumentComparisonDTO> result = similarityDocumentComparisonService.partialUpdate(
            similarityDocumentComparisonDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityDocumentComparisonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /similarity-document-comparisons} : get all the similarityDocumentComparisons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of similarityDocumentComparisons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SimilarityDocumentComparisonDTO>> getAllSimilarityDocumentComparisons(
        SimilarityDocumentComparisonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SimilarityDocumentComparisons by criteria: {}", criteria);

        Page<SimilarityDocumentComparisonDTO> page = similarityDocumentComparisonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /similarity-document-comparisons/count} : count all the similarityDocumentComparisons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSimilarityDocumentComparisons(SimilarityDocumentComparisonCriteria criteria) {
        LOG.debug("REST request to count SimilarityDocumentComparisons by criteria: {}", criteria);
        return ResponseEntity.ok().body(similarityDocumentComparisonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /similarity-document-comparisons/:id} : get the "id" similarityDocumentComparison.
     *
     * @param id the id of the similarityDocumentComparisonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the similarityDocumentComparisonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SimilarityDocumentComparisonDTO> getSimilarityDocumentComparison(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SimilarityDocumentComparison : {}", id);
        Optional<SimilarityDocumentComparisonDTO> similarityDocumentComparisonDTO = similarityDocumentComparisonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(similarityDocumentComparisonDTO);
    }

    /**
     * {@code DELETE  /similarity-document-comparisons/:id} : delete the "id" similarityDocumentComparison.
     *
     * @param id the id of the similarityDocumentComparisonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSimilarityDocumentComparison(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SimilarityDocumentComparison : {}", id);
        similarityDocumentComparisonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /similarity-document-comparisons/_search?query=:query} : search for the similarityDocumentComparison corresponding
     * to the query.
     *
     * @param query the query of the similarityDocumentComparison search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SimilarityDocumentComparisonDTO>> searchSimilarityDocumentComparisons(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SimilarityDocumentComparisons for query {}", query);
        try {
            Page<SimilarityDocumentComparisonDTO> page = similarityDocumentComparisonService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
