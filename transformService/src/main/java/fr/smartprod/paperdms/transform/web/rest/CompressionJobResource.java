package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.CompressionJobRepository;
import fr.smartprod.paperdms.transform.service.CompressionJobService;
import fr.smartprod.paperdms.transform.service.dto.CompressionJobDTO;
import fr.smartprod.paperdms.transform.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.CompressionJob}.
 */
@RestController
@RequestMapping("/api/compression-jobs")
public class CompressionJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompressionJobResource.class);

    private static final String ENTITY_NAME = "transformServiceCompressionJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompressionJobService compressionJobService;

    private final CompressionJobRepository compressionJobRepository;

    public CompressionJobResource(CompressionJobService compressionJobService, CompressionJobRepository compressionJobRepository) {
        this.compressionJobService = compressionJobService;
        this.compressionJobRepository = compressionJobRepository;
    }

    /**
     * {@code POST  /compression-jobs} : Create a new compressionJob.
     *
     * @param compressionJobDTO the compressionJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compressionJobDTO, or with status {@code 400 (Bad Request)} if the compressionJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompressionJobDTO> createCompressionJob(@Valid @RequestBody CompressionJobDTO compressionJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CompressionJob : {}", compressionJobDTO);
        if (compressionJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new compressionJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        compressionJobDTO = compressionJobService.save(compressionJobDTO);
        return ResponseEntity.created(new URI("/api/compression-jobs/" + compressionJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, compressionJobDTO.getId().toString()))
            .body(compressionJobDTO);
    }

    /**
     * {@code PUT  /compression-jobs/:id} : Updates an existing compressionJob.
     *
     * @param id the id of the compressionJobDTO to save.
     * @param compressionJobDTO the compressionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compressionJobDTO,
     * or with status {@code 400 (Bad Request)} if the compressionJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compressionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompressionJobDTO> updateCompressionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompressionJobDTO compressionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CompressionJob : {}, {}", id, compressionJobDTO);
        if (compressionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compressionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compressionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        compressionJobDTO = compressionJobService.update(compressionJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compressionJobDTO.getId().toString()))
            .body(compressionJobDTO);
    }

    /**
     * {@code PATCH  /compression-jobs/:id} : Partial updates given fields of an existing compressionJob, field will ignore if it is null
     *
     * @param id the id of the compressionJobDTO to save.
     * @param compressionJobDTO the compressionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compressionJobDTO,
     * or with status {@code 400 (Bad Request)} if the compressionJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compressionJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compressionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompressionJobDTO> partialUpdateCompressionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompressionJobDTO compressionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CompressionJob partially : {}, {}", id, compressionJobDTO);
        if (compressionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compressionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compressionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompressionJobDTO> result = compressionJobService.partialUpdate(compressionJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compressionJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compression-jobs} : get all the compressionJobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compressionJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CompressionJobDTO>> getAllCompressionJobs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CompressionJobs");
        Page<CompressionJobDTO> page = compressionJobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compression-jobs/:id} : get the "id" compressionJob.
     *
     * @param id the id of the compressionJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compressionJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompressionJobDTO> getCompressionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CompressionJob : {}", id);
        Optional<CompressionJobDTO> compressionJobDTO = compressionJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compressionJobDTO);
    }

    /**
     * {@code DELETE  /compression-jobs/:id} : delete the "id" compressionJob.
     *
     * @param id the id of the compressionJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompressionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CompressionJob : {}", id);
        compressionJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
