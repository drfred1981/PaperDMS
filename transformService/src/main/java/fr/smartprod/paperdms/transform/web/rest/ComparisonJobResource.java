package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.ComparisonJobRepository;
import fr.smartprod.paperdms.transform.service.ComparisonJobService;
import fr.smartprod.paperdms.transform.service.dto.ComparisonJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.ComparisonJob}.
 */
@RestController
@RequestMapping("/api/comparison-jobs")
public class ComparisonJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(ComparisonJobResource.class);

    private static final String ENTITY_NAME = "transformServiceComparisonJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComparisonJobService comparisonJobService;

    private final ComparisonJobRepository comparisonJobRepository;

    public ComparisonJobResource(ComparisonJobService comparisonJobService, ComparisonJobRepository comparisonJobRepository) {
        this.comparisonJobService = comparisonJobService;
        this.comparisonJobRepository = comparisonJobRepository;
    }

    /**
     * {@code POST  /comparison-jobs} : Create a new comparisonJob.
     *
     * @param comparisonJobDTO the comparisonJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comparisonJobDTO, or with status {@code 400 (Bad Request)} if the comparisonJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ComparisonJobDTO> createComparisonJob(@Valid @RequestBody ComparisonJobDTO comparisonJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ComparisonJob : {}", comparisonJobDTO);
        if (comparisonJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new comparisonJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        comparisonJobDTO = comparisonJobService.save(comparisonJobDTO);
        return ResponseEntity.created(new URI("/api/comparison-jobs/" + comparisonJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, comparisonJobDTO.getId().toString()))
            .body(comparisonJobDTO);
    }

    /**
     * {@code PUT  /comparison-jobs/:id} : Updates an existing comparisonJob.
     *
     * @param id the id of the comparisonJobDTO to save.
     * @param comparisonJobDTO the comparisonJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comparisonJobDTO,
     * or with status {@code 400 (Bad Request)} if the comparisonJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comparisonJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComparisonJobDTO> updateComparisonJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComparisonJobDTO comparisonJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ComparisonJob : {}, {}", id, comparisonJobDTO);
        if (comparisonJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comparisonJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comparisonJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        comparisonJobDTO = comparisonJobService.update(comparisonJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comparisonJobDTO.getId().toString()))
            .body(comparisonJobDTO);
    }

    /**
     * {@code PATCH  /comparison-jobs/:id} : Partial updates given fields of an existing comparisonJob, field will ignore if it is null
     *
     * @param id the id of the comparisonJobDTO to save.
     * @param comparisonJobDTO the comparisonJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comparisonJobDTO,
     * or with status {@code 400 (Bad Request)} if the comparisonJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the comparisonJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the comparisonJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComparisonJobDTO> partialUpdateComparisonJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComparisonJobDTO comparisonJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ComparisonJob partially : {}, {}", id, comparisonJobDTO);
        if (comparisonJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comparisonJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comparisonJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComparisonJobDTO> result = comparisonJobService.partialUpdate(comparisonJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comparisonJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comparison-jobs} : get all the comparisonJobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comparisonJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ComparisonJobDTO>> getAllComparisonJobs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ComparisonJobs");
        Page<ComparisonJobDTO> page = comparisonJobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comparison-jobs/:id} : get the "id" comparisonJob.
     *
     * @param id the id of the comparisonJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comparisonJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComparisonJobDTO> getComparisonJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ComparisonJob : {}", id);
        Optional<ComparisonJobDTO> comparisonJobDTO = comparisonJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comparisonJobDTO);
    }

    /**
     * {@code DELETE  /comparison-jobs/:id} : delete the "id" comparisonJob.
     *
     * @param id the id of the comparisonJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComparisonJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ComparisonJob : {}", id);
        comparisonJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
