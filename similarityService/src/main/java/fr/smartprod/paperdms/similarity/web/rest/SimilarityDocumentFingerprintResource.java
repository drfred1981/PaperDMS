package fr.smartprod.paperdms.similarity.web.rest;

import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentFingerprintRepository;
import fr.smartprod.paperdms.similarity.service.SimilarityDocumentFingerprintQueryService;
import fr.smartprod.paperdms.similarity.service.SimilarityDocumentFingerprintService;
import fr.smartprod.paperdms.similarity.service.criteria.SimilarityDocumentFingerprintCriteria;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentFingerprintDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint}.
 */
@RestController
@RequestMapping("/api/similarity-document-fingerprints")
public class SimilarityDocumentFingerprintResource {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDocumentFingerprintResource.class);

    private static final String ENTITY_NAME = "similarityServiceSimilarityDocumentFingerprint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimilarityDocumentFingerprintService similarityDocumentFingerprintService;

    private final SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository;

    private final SimilarityDocumentFingerprintQueryService similarityDocumentFingerprintQueryService;

    public SimilarityDocumentFingerprintResource(
        SimilarityDocumentFingerprintService similarityDocumentFingerprintService,
        SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository,
        SimilarityDocumentFingerprintQueryService similarityDocumentFingerprintQueryService
    ) {
        this.similarityDocumentFingerprintService = similarityDocumentFingerprintService;
        this.similarityDocumentFingerprintRepository = similarityDocumentFingerprintRepository;
        this.similarityDocumentFingerprintQueryService = similarityDocumentFingerprintQueryService;
    }

    /**
     * {@code POST  /similarity-document-fingerprints} : Create a new similarityDocumentFingerprint.
     *
     * @param similarityDocumentFingerprintDTO the similarityDocumentFingerprintDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new similarityDocumentFingerprintDTO, or with status {@code 400 (Bad Request)} if the similarityDocumentFingerprint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SimilarityDocumentFingerprintDTO> createSimilarityDocumentFingerprint(
        @Valid @RequestBody SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save SimilarityDocumentFingerprint : {}", similarityDocumentFingerprintDTO);
        if (similarityDocumentFingerprintDTO.getId() != null) {
            throw new BadRequestAlertException("A new similarityDocumentFingerprint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        similarityDocumentFingerprintDTO = similarityDocumentFingerprintService.save(similarityDocumentFingerprintDTO);
        return ResponseEntity.created(new URI("/api/similarity-document-fingerprints/" + similarityDocumentFingerprintDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    similarityDocumentFingerprintDTO.getId().toString()
                )
            )
            .body(similarityDocumentFingerprintDTO);
    }

    /**
     * {@code PUT  /similarity-document-fingerprints/:id} : Updates an existing similarityDocumentFingerprint.
     *
     * @param id the id of the similarityDocumentFingerprintDTO to save.
     * @param similarityDocumentFingerprintDTO the similarityDocumentFingerprintDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityDocumentFingerprintDTO,
     * or with status {@code 400 (Bad Request)} if the similarityDocumentFingerprintDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the similarityDocumentFingerprintDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SimilarityDocumentFingerprintDTO> updateSimilarityDocumentFingerprint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SimilarityDocumentFingerprint : {}, {}", id, similarityDocumentFingerprintDTO);
        if (similarityDocumentFingerprintDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityDocumentFingerprintDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityDocumentFingerprintRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        similarityDocumentFingerprintDTO = similarityDocumentFingerprintService.update(similarityDocumentFingerprintDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityDocumentFingerprintDTO.getId().toString())
            )
            .body(similarityDocumentFingerprintDTO);
    }

    /**
     * {@code PATCH  /similarity-document-fingerprints/:id} : Partial updates given fields of an existing similarityDocumentFingerprint, field will ignore if it is null
     *
     * @param id the id of the similarityDocumentFingerprintDTO to save.
     * @param similarityDocumentFingerprintDTO the similarityDocumentFingerprintDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityDocumentFingerprintDTO,
     * or with status {@code 400 (Bad Request)} if the similarityDocumentFingerprintDTO is not valid,
     * or with status {@code 404 (Not Found)} if the similarityDocumentFingerprintDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the similarityDocumentFingerprintDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SimilarityDocumentFingerprintDTO> partialUpdateSimilarityDocumentFingerprint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SimilarityDocumentFingerprint partially : {}, {}", id, similarityDocumentFingerprintDTO);
        if (similarityDocumentFingerprintDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityDocumentFingerprintDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityDocumentFingerprintRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SimilarityDocumentFingerprintDTO> result = similarityDocumentFingerprintService.partialUpdate(
            similarityDocumentFingerprintDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityDocumentFingerprintDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /similarity-document-fingerprints} : get all the similarityDocumentFingerprints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of similarityDocumentFingerprints in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SimilarityDocumentFingerprintDTO>> getAllSimilarityDocumentFingerprints(
        SimilarityDocumentFingerprintCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SimilarityDocumentFingerprints by criteria: {}", criteria);

        Page<SimilarityDocumentFingerprintDTO> page = similarityDocumentFingerprintQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /similarity-document-fingerprints/count} : count all the similarityDocumentFingerprints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSimilarityDocumentFingerprints(SimilarityDocumentFingerprintCriteria criteria) {
        LOG.debug("REST request to count SimilarityDocumentFingerprints by criteria: {}", criteria);
        return ResponseEntity.ok().body(similarityDocumentFingerprintQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /similarity-document-fingerprints/:id} : get the "id" similarityDocumentFingerprint.
     *
     * @param id the id of the similarityDocumentFingerprintDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the similarityDocumentFingerprintDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SimilarityDocumentFingerprintDTO> getSimilarityDocumentFingerprint(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SimilarityDocumentFingerprint : {}", id);
        Optional<SimilarityDocumentFingerprintDTO> similarityDocumentFingerprintDTO = similarityDocumentFingerprintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(similarityDocumentFingerprintDTO);
    }

    /**
     * {@code DELETE  /similarity-document-fingerprints/:id} : delete the "id" similarityDocumentFingerprint.
     *
     * @param id the id of the similarityDocumentFingerprintDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSimilarityDocumentFingerprint(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SimilarityDocumentFingerprint : {}", id);
        similarityDocumentFingerprintService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /similarity-document-fingerprints/_search?query=:query} : search for the similarityDocumentFingerprint corresponding
     * to the query.
     *
     * @param query the query of the similarityDocumentFingerprint search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SimilarityDocumentFingerprintDTO>> searchSimilarityDocumentFingerprints(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SimilarityDocumentFingerprints for query {}", query);
        try {
            Page<SimilarityDocumentFingerprintDTO> page = similarityDocumentFingerprintService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
