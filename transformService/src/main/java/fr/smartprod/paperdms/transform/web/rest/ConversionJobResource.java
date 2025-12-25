package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.ConversionJobRepository;
import fr.smartprod.paperdms.transform.service.ConversionJobQueryService;
import fr.smartprod.paperdms.transform.service.ConversionJobService;
import fr.smartprod.paperdms.transform.service.criteria.ConversionJobCriteria;
import fr.smartprod.paperdms.transform.service.dto.ConversionJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.ConversionJob}.
 */
@RestController
@RequestMapping("/api/conversion-jobs")
public class ConversionJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConversionJobResource.class);

    private static final String ENTITY_NAME = "transformServiceConversionJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversionJobService conversionJobService;

    private final ConversionJobRepository conversionJobRepository;

    private final ConversionJobQueryService conversionJobQueryService;

    public ConversionJobResource(
        ConversionJobService conversionJobService,
        ConversionJobRepository conversionJobRepository,
        ConversionJobQueryService conversionJobQueryService
    ) {
        this.conversionJobService = conversionJobService;
        this.conversionJobRepository = conversionJobRepository;
        this.conversionJobQueryService = conversionJobQueryService;
    }

    /**
     * {@code POST  /conversion-jobs} : Create a new conversionJob.
     *
     * @param conversionJobDTO the conversionJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversionJobDTO, or with status {@code 400 (Bad Request)} if the conversionJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConversionJobDTO> createConversionJob(@Valid @RequestBody ConversionJobDTO conversionJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ConversionJob : {}", conversionJobDTO);
        if (conversionJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new conversionJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        conversionJobDTO = conversionJobService.save(conversionJobDTO);
        return ResponseEntity.created(new URI("/api/conversion-jobs/" + conversionJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, conversionJobDTO.getId().toString()))
            .body(conversionJobDTO);
    }

    /**
     * {@code PUT  /conversion-jobs/:id} : Updates an existing conversionJob.
     *
     * @param id the id of the conversionJobDTO to save.
     * @param conversionJobDTO the conversionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversionJobDTO,
     * or with status {@code 400 (Bad Request)} if the conversionJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConversionJobDTO> updateConversionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConversionJobDTO conversionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ConversionJob : {}, {}", id, conversionJobDTO);
        if (conversionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        conversionJobDTO = conversionJobService.update(conversionJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conversionJobDTO.getId().toString()))
            .body(conversionJobDTO);
    }

    /**
     * {@code PATCH  /conversion-jobs/:id} : Partial updates given fields of an existing conversionJob, field will ignore if it is null
     *
     * @param id the id of the conversionJobDTO to save.
     * @param conversionJobDTO the conversionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversionJobDTO,
     * or with status {@code 400 (Bad Request)} if the conversionJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the conversionJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConversionJobDTO> partialUpdateConversionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConversionJobDTO conversionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ConversionJob partially : {}, {}", id, conversionJobDTO);
        if (conversionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConversionJobDTO> result = conversionJobService.partialUpdate(conversionJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conversionJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conversion-jobs} : get all the conversionJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversionJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConversionJobDTO>> getAllConversionJobs(
        ConversionJobCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ConversionJobs by criteria: {}", criteria);

        Page<ConversionJobDTO> page = conversionJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conversion-jobs/count} : count all the conversionJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countConversionJobs(ConversionJobCriteria criteria) {
        LOG.debug("REST request to count ConversionJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(conversionJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conversion-jobs/:id} : get the "id" conversionJob.
     *
     * @param id the id of the conversionJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conversionJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConversionJobDTO> getConversionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ConversionJob : {}", id);
        Optional<ConversionJobDTO> conversionJobDTO = conversionJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conversionJobDTO);
    }

    /**
     * {@code DELETE  /conversion-jobs/:id} : delete the "id" conversionJob.
     *
     * @param id the id of the conversionJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ConversionJob : {}", id);
        conversionJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
