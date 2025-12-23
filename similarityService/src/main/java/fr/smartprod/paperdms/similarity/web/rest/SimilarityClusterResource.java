package fr.smartprod.paperdms.similarity.web.rest;

import fr.smartprod.paperdms.similarity.repository.SimilarityClusterRepository;
import fr.smartprod.paperdms.similarity.service.SimilarityClusterService;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityClusterDTO;
import fr.smartprod.paperdms.similarity.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityCluster}.
 */
@RestController
@RequestMapping("/api/similarity-clusters")
public class SimilarityClusterResource {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityClusterResource.class);

    private static final String ENTITY_NAME = "similarityServiceSimilarityCluster";

    @Value("${jhipster.clientApp.name:similarityService}")
    private String applicationName;

    private final SimilarityClusterService similarityClusterService;

    private final SimilarityClusterRepository similarityClusterRepository;

    public SimilarityClusterResource(
        SimilarityClusterService similarityClusterService,
        SimilarityClusterRepository similarityClusterRepository
    ) {
        this.similarityClusterService = similarityClusterService;
        this.similarityClusterRepository = similarityClusterRepository;
    }

    /**
     * {@code POST  /similarity-clusters} : Create a new similarityCluster.
     *
     * @param similarityClusterDTO the similarityClusterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new similarityClusterDTO, or with status {@code 400 (Bad Request)} if the similarityCluster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SimilarityClusterDTO> createSimilarityCluster(@Valid @RequestBody SimilarityClusterDTO similarityClusterDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SimilarityCluster : {}", similarityClusterDTO);
        if (similarityClusterDTO.getId() != null) {
            throw new BadRequestAlertException("A new similarityCluster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        similarityClusterDTO = similarityClusterService.save(similarityClusterDTO);
        return ResponseEntity.created(new URI("/api/similarity-clusters/" + similarityClusterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, similarityClusterDTO.getId().toString()))
            .body(similarityClusterDTO);
    }

    /**
     * {@code PUT  /similarity-clusters/:id} : Updates an existing similarityCluster.
     *
     * @param id the id of the similarityClusterDTO to save.
     * @param similarityClusterDTO the similarityClusterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityClusterDTO,
     * or with status {@code 400 (Bad Request)} if the similarityClusterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the similarityClusterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SimilarityClusterDTO> updateSimilarityCluster(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SimilarityClusterDTO similarityClusterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SimilarityCluster : {}, {}", id, similarityClusterDTO);
        if (similarityClusterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityClusterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityClusterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        similarityClusterDTO = similarityClusterService.update(similarityClusterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityClusterDTO.getId().toString()))
            .body(similarityClusterDTO);
    }

    /**
     * {@code PATCH  /similarity-clusters/:id} : Partial updates given fields of an existing similarityCluster, field will ignore if it is null
     *
     * @param id the id of the similarityClusterDTO to save.
     * @param similarityClusterDTO the similarityClusterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated similarityClusterDTO,
     * or with status {@code 400 (Bad Request)} if the similarityClusterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the similarityClusterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the similarityClusterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SimilarityClusterDTO> partialUpdateSimilarityCluster(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SimilarityClusterDTO similarityClusterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SimilarityCluster partially : {}, {}", id, similarityClusterDTO);
        if (similarityClusterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, similarityClusterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!similarityClusterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SimilarityClusterDTO> result = similarityClusterService.partialUpdate(similarityClusterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, similarityClusterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /similarity-clusters} : get all the similarityClusters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of similarityClusters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SimilarityClusterDTO>> getAllSimilarityClusters(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SimilarityClusters");
        Page<SimilarityClusterDTO> page = similarityClusterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /similarity-clusters/:id} : get the "id" similarityCluster.
     *
     * @param id the id of the similarityClusterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the similarityClusterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SimilarityClusterDTO> getSimilarityCluster(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SimilarityCluster : {}", id);
        Optional<SimilarityClusterDTO> similarityClusterDTO = similarityClusterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(similarityClusterDTO);
    }

    /**
     * {@code DELETE  /similarity-clusters/:id} : delete the "id" similarityCluster.
     *
     * @param id the id of the similarityClusterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSimilarityCluster(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SimilarityCluster : {}", id);
        similarityClusterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
