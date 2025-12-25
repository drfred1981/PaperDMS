package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.SystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.SystemHealthService;
import fr.smartprod.paperdms.monitoring.service.dto.SystemHealthDTO;
import fr.smartprod.paperdms.monitoring.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.SystemHealth}.
 */
@RestController
@RequestMapping("/api/system-healths")
public class SystemHealthResource {

    private static final Logger LOG = LoggerFactory.getLogger(SystemHealthResource.class);

    private static final String ENTITY_NAME = "monitoringServiceSystemHealth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemHealthService systemHealthService;

    private final SystemHealthRepository systemHealthRepository;

    public SystemHealthResource(SystemHealthService systemHealthService, SystemHealthRepository systemHealthRepository) {
        this.systemHealthService = systemHealthService;
        this.systemHealthRepository = systemHealthRepository;
    }

    /**
     * {@code POST  /system-healths} : Create a new systemHealth.
     *
     * @param systemHealthDTO the systemHealthDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemHealthDTO, or with status {@code 400 (Bad Request)} if the systemHealth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemHealthDTO> createSystemHealth(@Valid @RequestBody SystemHealthDTO systemHealthDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SystemHealth : {}", systemHealthDTO);
        if (systemHealthDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemHealth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemHealthDTO = systemHealthService.save(systemHealthDTO);
        return ResponseEntity.created(new URI("/api/system-healths/" + systemHealthDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemHealthDTO.getId().toString()))
            .body(systemHealthDTO);
    }

    /**
     * {@code PUT  /system-healths/:id} : Updates an existing systemHealth.
     *
     * @param id the id of the systemHealthDTO to save.
     * @param systemHealthDTO the systemHealthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemHealthDTO,
     * or with status {@code 400 (Bad Request)} if the systemHealthDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemHealthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemHealthDTO> updateSystemHealth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemHealthDTO systemHealthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SystemHealth : {}, {}", id, systemHealthDTO);
        if (systemHealthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemHealthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemHealthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemHealthDTO = systemHealthService.update(systemHealthDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemHealthDTO.getId().toString()))
            .body(systemHealthDTO);
    }

    /**
     * {@code PATCH  /system-healths/:id} : Partial updates given fields of an existing systemHealth, field will ignore if it is null
     *
     * @param id the id of the systemHealthDTO to save.
     * @param systemHealthDTO the systemHealthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemHealthDTO,
     * or with status {@code 400 (Bad Request)} if the systemHealthDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemHealthDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemHealthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemHealthDTO> partialUpdateSystemHealth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemHealthDTO systemHealthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SystemHealth partially : {}, {}", id, systemHealthDTO);
        if (systemHealthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemHealthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemHealthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemHealthDTO> result = systemHealthService.partialUpdate(systemHealthDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemHealthDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-healths} : get all the systemHealths.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemHealths in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SystemHealthDTO>> getAllSystemHealths(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SystemHealths");
        Page<SystemHealthDTO> page = systemHealthService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-healths/:id} : get the "id" systemHealth.
     *
     * @param id the id of the systemHealthDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemHealthDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemHealthDTO> getSystemHealth(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SystemHealth : {}", id);
        Optional<SystemHealthDTO> systemHealthDTO = systemHealthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemHealthDTO);
    }

    /**
     * {@code DELETE  /system-healths/:id} : delete the "id" systemHealth.
     *
     * @param id the id of the systemHealthDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemHealth(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SystemHealth : {}", id);
        systemHealthService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
